package com.chensoul.security.controller;

import com.chensoul.security.entity.Product;
import com.chensoul.security.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
public class ProductController {
    @Autowired
    private ProductService productService;

    @GetMapping("/products/{text}")
    public List<Product> findProductsContaining(@PathVariable String text) {
        return productService.findProducts(text);
    }

    @GetMapping("/sell")
    public List<Product> sellProduct() {
        List<Product> products = new ArrayList<>();

        products.add(new Product(1, "beer", "nikolai"));
        products.add(new Product(2, "candy", "nikolai"));
        products.add(new Product(3, "chocolate", "julien"));

        return productService.sellProducts(products);
    }
}
