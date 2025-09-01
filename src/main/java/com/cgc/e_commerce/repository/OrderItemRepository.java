package com.cgc.e_commerce.repository;

import com.cgc.e_commerce.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
    List<OrderItem> findByOrderId(Long orderId);
    List<OrderItem> findByOrderIdIn(List<Long> orderIds);
}