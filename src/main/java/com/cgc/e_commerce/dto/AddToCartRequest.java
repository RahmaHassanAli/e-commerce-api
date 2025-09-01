package com.cgc.e_commerce.dto;

import jakarta.validation.constraints.*;

public record AddToCartRequest(
        @NotNull Long userId,
        @NotNull Long productId,
        @Min(1) int quantity
) {}

