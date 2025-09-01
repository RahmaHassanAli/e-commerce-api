package com.cgc.e_commerce.service;

import com.cgc.e_commerce.model.CartItem;
import com.cgc.e_commerce.model.Product;

public class CartItemWithProduct {
    private final CartItem cartItem;
    private final Product product;

    public CartItemWithProduct(CartItem cartItem, Product product) {
        this.cartItem = cartItem;
        this.product = product;
    }

    public CartItem getCartItem() {
        return cartItem;
    }

    public Product getProduct() {
        return product;
    }

    public Long getId() {
        return cartItem.getId();
    }

    public Integer getQuantity() {
        return cartItem.getQuantity();
    }
}