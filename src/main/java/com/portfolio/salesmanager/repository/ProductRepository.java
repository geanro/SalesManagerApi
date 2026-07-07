package com.portfolio.salesmanager.repository;

import com.portfolio.salesmanager.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name);
    List<Product> findByCategory_IdCategory(Long categoryId);
    List<Product> findByPriceBetween(BigDecimal initialPrice, BigDecimal finalPrice);
    List<Product> findByActiveTrue();
    List<Product> findTop5ByOrderByPriceDesc();

}
