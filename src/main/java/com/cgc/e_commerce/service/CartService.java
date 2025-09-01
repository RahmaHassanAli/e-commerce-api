package com.cgc.e_commerce.service;

import com.cgc.e_commerce.dto.*;
import com.cgc.e_commerce.exception.*;
import com.cgc.e_commerce.model.*;
import com.cgc.e_commerce.repository.*;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository,
                       ProductRepository productRepository, UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    public Cart getOrCreateCart(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User not found");
        }
        return cartRepository.findByUserId(userId).orElseGet(() -> {
            Cart c = Cart.builder()
                    .userId(userId)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            return cartRepository.save(c);
        });
    }

    public List<CartItemResponse> listItems(Long userId) {
        Cart cart = getOrCreateCart(userId);
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            return List.of();
        }

        List<Long> productIds = cartItems.stream().map(CartItem::getProductId).toList();
        Map<Long, Product> productsMap = productRepository.findAllById(productIds)
                .stream().collect(Collectors.toMap(Product::getId, p -> p));

        return cartItems.stream()
                .map(item -> {
                    Product product = productsMap.get(item.getProductId());
                    return new CartItemResponse(
                            item.getId(),
                            product.getId(),
                            product.getName(),
                            product.getPrice(),
                            item.getQuantity(),
                            product.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
                    );
                })
                .toList();
    }

    public List<CartItemWithProduct> getCartItemEntities(Long userId) {
        Cart cart = getOrCreateCart(userId);
        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            return List.of();
        }

        List<Long> productIds = cartItems.stream().map(CartItem::getProductId).toList();
        Map<Long, Product> productsMap = productRepository.findAllById(productIds)
                .stream().collect(Collectors.toMap(Product::getId, p -> p));

        return cartItems.stream()
                .map(item -> new CartItemWithProduct(item, productsMap.get(item.getProductId())))
                .toList();
    }

    @Transactional
    public CartItem addItem(AddToCartRequest req) {
        Cart cart = getOrCreateCart(req.userId());
        Product product = productRepository.findById(req.productId())
                .orElseThrow(() -> new NotFoundException("Product not found"));
        if (req.quantity() < 1) throw new BadRequestException("Quantity must be >= 1");
        if (product.getStockQuantity() < req.quantity())
            throw new BadRequestException("Not enough stock. Available: " + product.getStockQuantity());

        CartItem item = cartItemRepository.findByCartIdAndProductId(cart.getId(), product.getId())
                .orElseGet(() -> CartItem.builder()
                        .cartId(cart.getId())
                        .productId(product.getId())
                        .quantity(0)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build());

        int newQty = item.getQuantity() + req.quantity();
        if (newQty > product.getStockQuantity())
            throw new BadRequestException("Quantity exceeds available stock");
        item.setQuantity(newQty);
        item.setUpdatedAt(LocalDateTime.now());
        return cartItemRepository.save(item);
    }

    @Transactional
    public CartItem updateItem(UpdateCartItemRequest req) {
        Cart cart = getOrCreateCart(req.userId());
        CartItem item = cartItemRepository.findById(req.cartItemId())
                .orElseThrow(() -> new NotFoundException("Cart item not found"));
        if (!item.getCartId().equals(cart.getId()))
            throw new BadRequestException("Item does not belong to user's cart");
        if (req.quantity() < 1) throw new BadRequestException("Quantity must be >= 1");

        Product product = productRepository.findById(item.getProductId())
                .orElseThrow(() -> new NotFoundException("Product not found"));
        if (req.quantity() > product.getStockQuantity())
            throw new BadRequestException("Quantity exceeds available stock");
        item.setQuantity(req.quantity());
        item.setUpdatedAt(LocalDateTime.now());
        return cartItemRepository.save(item);
    }

    @Transactional
    public void removeItem(RemoveCartItemRequest req) {
        Cart cart = getOrCreateCart(req.userId());
        CartItem item = cartItemRepository.findById(req.cartItemId())
                .orElseThrow(() -> new NotFoundException("Cart item not found"));
        if (!item.getCartId().equals(cart.getId()))
            throw new BadRequestException("Item does not belong to user's cart");
        cartItemRepository.delete(item);
    }

    public Product getProduct(Long productId) {
        return productRepository.findById(productId)
                .orElseThrow(() -> new NotFoundException("Product not found"));
    }
}