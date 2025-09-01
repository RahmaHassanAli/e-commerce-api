package com.cgc.e_commerce.controller;

import com.cgc.e_commerce.dto.*;
import com.cgc.e_commerce.model.CartItem;
import com.cgc.e_commerce.model.Product;
import com.cgc.e_commerce.service.CartService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    private final CartService cartService;
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/items")
    public List<CartItemResponse> items(@RequestParam Long userId) {
        return cartService.listItems(userId);
    }

    @PostMapping("/add")
    public CartItemResponse add(@RequestBody @Valid AddToCartRequest req) {
        CartItem cartItem = cartService.addItem(req);
        return convertToResponse(cartItem, req.productId());
    }

    @PutMapping("/update")
    public CartItemResponse update(@RequestBody @Valid UpdateCartItemRequest req) {
        CartItem cartItem = cartService.updateItem(req);
        return convertToResponse(cartItem, cartItem.getProductId());
    }

    @DeleteMapping("/remove")
    public void remove(@RequestBody @Valid RemoveCartItemRequest req){
        cartService.removeItem(req);
    }

    private CartItemResponse convertToResponse(CartItem cartItem, Long productId) {
        Product product = cartService.getProduct(productId);
        return new CartItemResponse(
                cartItem.getId(),
                product.getId(),
                product.getName(),
                product.getPrice(),
                cartItem.getQuantity(),
                product.getPrice().multiply(java.math.BigDecimal.valueOf(cartItem.getQuantity()))
        );
    }
}