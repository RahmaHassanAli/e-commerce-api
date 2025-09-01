package com.cgc.e_commerce.dto;

import com.cgc.e_commerce.model.enums.OrderStatus;
import com.cgc.e_commerce.model.enums.PaymentMethod;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderHistoryResponse(
        Long id,
        BigDecimal totalAmount,
        OrderStatus status,
        PaymentMethod paymentMethod,
        String shippingAddress,
        List<OrderItemResponse> items,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
