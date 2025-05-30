package com.chensoul.security.repository;

import com.chensoul.security.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.prepost.PostFilter;

import java.util.List;

public interface ProductRepository
        extends JpaRepository<Product, Integer> {

    @PostFilter("filterObject.owner == authentication.principal.username")
//    @Query("SELECT p FROM Product p WHERE p.name LIKE %:text% AND p.owner=?#{authentication.principal.username}")
    List<Product> findProductByNameContains(String text);
}
