package com.cgc.e_commerce.dto;

import com.cgc.e_commerce.model.enums.PaymentMethod;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record CheckoutRequest(
        @NotNull Long userId,
        @NotNull PaymentMethod paymentMethod,
        @NotBlank String shippingAddress
) {}