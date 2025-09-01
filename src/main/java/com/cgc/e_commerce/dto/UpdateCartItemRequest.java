package com.cgc.e_commerce.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record UpdateCartItemRequest(
        @NotNull Long userId,
        @NotNull Long cartItemId,
        @Min(1) int quantity) {}
