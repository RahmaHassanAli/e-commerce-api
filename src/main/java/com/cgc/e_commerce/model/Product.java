package com.cgc.e_commerce.model;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;


@Entity
@Table(name = "products")
@Data @NoArgsConstructor
@AllArgsConstructor @Builder
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String name;


    @Column(columnDefinition = "TEXT")
    private String description;


    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;


    @Column(name = "stock_quantity", nullable = false)
    private Integer stockQuantity;


    private String category;


    @Column(name = "image_url")
    private String imageUrl;


    @Column(name = "created_at")
    private LocalDateTime createdAt;


    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
