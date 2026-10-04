package com.MAF.ecommerce.service;



import com.MAF.ecommerce.model.Product;
import org.springframework.cache.annotation.Cacheable;
import com.MAF.ecommerce.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }
    @Cacheable("products")
    public List<Product> getProducts() {
        System.out.println("Getting products from MySQL...");
        return productRepository.findAll();
    }
}