package com.cgc.e_commerce.service;

import com.cgc.e_commerce.dto.*;
import com.cgc.e_commerce.exception.*;
import com.cgc.e_commerce.model.*;
import com.cgc.e_commerce.model.enums.OrderStatus;
import com.cgc.e_commerce.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartService cartService;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(OrderRepository orderRepository, OrderItemRepository orderItemRepository,
                        CartService cartService, ProductRepository productRepository,
                        UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartService = cartService;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderResponse checkout(CheckoutRequest req) {
        var items = cartService.getCartItemEntities(req.userId());
        if (items.isEmpty()) throw new BadRequestException("Cart is empty");

        User user = userRepository.findById(req.userId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        BigDecimal total = items.stream()
                .map(i -> i.getProduct().getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Order order = Order.builder()
                .userId(user.getId())
                .totalAmount(total)
                .status(OrderStatus.PENDING)
                .paymentMethod(req.paymentMethod())
                .shippingAddress(req.shippingAddress())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        order = orderRepository.save(order);

        List<OrderItemResponse> orderItemResponses = new ArrayList<>();

        for (var item : items) {
            Product p = item.getProduct();
            if (p.getStockQuantity() < item.getQuantity())
                throw new BadRequestException("Insufficient stock for product: " + p.getName());

            p.setStockQuantity(p.getStockQuantity() - item.getQuantity());
            productRepository.save(p);

            OrderItem oi = OrderItem.builder()
                    .orderId(order.getId())
                    .productId(p.getId())
                    .quantity(item.getQuantity())
                    .price(p.getPrice())
                    .createdAt(LocalDateTime.now())
                    .build();
            orderItemRepository.save(oi);

            orderItemResponses.add(new OrderItemResponse(
                    oi.getId(),
                    p.getId(),
                    p.getName(),
                    p.getPrice(),
                    item.getQuantity(),
                    p.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
            ));
        }

        items.forEach(i -> cartService.removeItem(new RemoveCartItemRequest(req.userId(), i.getId())));

        return new OrderResponse(
                order.getId(),
                user.getId(),
                user.getUsername(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getPaymentMethod(),
                order.getShippingAddress(),
                orderItemResponses,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }

    public List<OrderHistoryResponse> history(Long userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);

        if (orders.isEmpty()) {
            return new ArrayList<>();
        }

        List<Long> orderIds = orders.stream().map(Order::getId).toList();
        List<OrderItem> allOrderItems = orderItemRepository.findByOrderIdIn(orderIds);

        Set<Long> productIds = allOrderItems.stream().map(OrderItem::getProductId).collect(Collectors.toSet());
        Map<Long, Product> productsMap = productRepository.findAllById(productIds)
                .stream().collect(Collectors.toMap(Product::getId, p -> p));

        Map<Long, List<OrderItem>> itemsByOrderId = allOrderItems.stream()
                .collect(Collectors.groupingBy(OrderItem::getOrderId));

        return orders.stream()
                .map(order -> convertToHistoryResponse(order, itemsByOrderId.get(order.getId()), productsMap))
                .toList();
    }

    private OrderHistoryResponse convertToHistoryResponse(Order order, List<OrderItem> orderItems, Map<Long, Product> productsMap) {
        List<OrderItemResponse> itemResponses = orderItems == null ?
                new ArrayList<>() :
                orderItems.stream()
                        .map(item -> {
                            Product product = productsMap.get(item.getProductId());
                            return new OrderItemResponse(
                                    item.getId(),
                                    product.getId(),
                                    product.getName(),
                                    item.getPrice(),
                                    item.getQuantity(),
                                    item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                            );
                        })
                        .toList();

        return new OrderHistoryResponse(
                order.getId(),
                order.getTotalAmount(),
                order.getStatus(),
                order.getPaymentMethod(),
                order.getShippingAddress(),
                itemResponses,
                order.getCreatedAt(),
                order.getUpdatedAt()
        );
    }
}