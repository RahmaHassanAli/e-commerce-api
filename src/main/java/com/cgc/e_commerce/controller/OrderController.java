package com.cgc.e_commerce.controller;

import com.cgc.e_commerce.dto.CheckoutRequest;
import com.cgc.e_commerce.dto.OrderHistoryResponse;
import com.cgc.e_commerce.dto.OrderResponse;
import com.cgc.e_commerce.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    public OrderController(OrderService orderService) { this.orderService = orderService; }

    @PostMapping("/checkout")
    public OrderResponse checkout(@RequestBody @Valid CheckoutRequest req){
        return orderService.checkout(req);
    }

    @GetMapping("/history")
    public List<OrderHistoryResponse> history(@RequestParam Long userId){
        return orderService.history(userId);
    }
}