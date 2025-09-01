package com.cgc.e_commerce.controller;


import com.cgc.e_commerce.dto.ProductRequest;
import com.cgc.e_commerce.model.Product;
import com.cgc.e_commerce.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;
import java.util.List;


@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;
    public ProductController(ProductService productService) { this.productService = productService; }


    @GetMapping
    public List<Product> list(){ return productService.list(); }


    @GetMapping("/{id}")
    public Product get(@PathVariable Long id){ return productService.get(id); }


    @PostMapping
    public Product create(@RequestBody @Valid ProductRequest req){
        Product p = Product.builder()
                .name(req.name())
                .description(req.description())
                .price(req.price())
                .stockQuantity(req.stockQuantity())
                .category(req.category())
                .imageUrl(req.imageUrl())
                .build();
        return productService.create(p);
    }


    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody @Valid ProductRequest req){
        Product p = Product.builder()
                .name(req.name())
                .description(req.description())
                .price(req.price())
                .stockQuantity(req.stockQuantity())
                .category(req.category())
                .imageUrl(req.imageUrl())
                .build();
        return productService.update(id, p);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){ productService.delete(id); }
}