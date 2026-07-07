package com.portfolio.salesmanager.mapper;

import com.portfolio.salesmanager.dto.response.CatalogProductResponse;
import com.portfolio.salesmanager.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class CatalogMapper {

    public CatalogProductResponse toResponse(Product product, Integer availableStock){

        if (product == null) return null;

        return CatalogProductResponse.builder()
                .idProduct(product.getIdProduct())
                .name(product.getName())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .price(product.getPrice())
                .categoryId(product.getCategory().getIdCategory())
                .categoryName(product.getCategory().getName())
                .availableStock(availableStock)
                .build();
    }
}