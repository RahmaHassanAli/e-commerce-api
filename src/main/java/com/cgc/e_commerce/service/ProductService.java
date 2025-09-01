package com.cgc.e_commerce.service;

import com.cgc.e_commerce.exception.NotFoundException;
import com.cgc.e_commerce.model.Product;
import com.cgc.e_commerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    public ProductService(ProductRepository productRepository) { this.productRepository = productRepository; }


    public List<Product> list(){ return productRepository.findAll(); }
    public Product get(Long id){ return productRepository.findById(id).orElseThrow(() -> new NotFoundException("Product not found")); }


    public Product create(Product p){

        p.setCreatedAt(LocalDateTime.now());
        p.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(p);
    }
    public Product update(Long id, Product data){
        Product p = get(id);

        p.setName(data.getName());
        p.setDescription(data.getDescription());
        p.setPrice(data.getPrice());
        p.setStockQuantity(data.getStockQuantity());
        p.setCategory(data.getCategory());
        p.setImageUrl(data.getImageUrl());
        p.setUpdatedAt(LocalDateTime.now());
        return productRepository.save(p);
    }
    public void delete(Long id){ productRepository.deleteById(id); }
}