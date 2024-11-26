package com.chensoul.security.service;

import com.chensoul.security.entity.Product;
import com.chensoul.security.repository.ProductRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreFilter;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ProductService {
    private final ProductRepository productRepository;

    @PreFilter("filterObject.owner == authentication.name")
    public List<Product> sellProducts(List<Product> products) {
        return products;
    }

    //    @PostFilter("filterObject.owner == authentication.principal.username")
    public List<Product> findProducts(String text) {
        return productRepository.findProductByNameContains(text);
    }
}
