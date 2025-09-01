package com.cgc.e_commerce.dto;

import jakarta.validation.constraints.NotNull;

public record RemoveCartItemRequest(
        @NotNull Long userId,
        @NotNull Long cartItemId
) {}
