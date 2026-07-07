package com.portfolio.salesmanager.mapper;

import com.portfolio.salesmanager.dto.response.ProductResponse;
import com.portfolio.salesmanager.entity.Product;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProductMapper {

    public ProductResponse toResponse(Product product){
        if (product==null) return null;

        return ProductResponse.builder()
                .idProduct(product.getIdProduct())
                .name(product.getName())
                .description(product.getDescription())
                .imageUrl(product.getImageUrl())
                .price(product.getPrice())
                .active(product.getActive())
                .categoryId(product.getCategory().getIdCategory())
                .categoryName(product.getCategory().getName())
                .build();
    }

    public List<ProductResponse> toResponseList(List<Product> products){
        if (products==null) return List.of();

        return products.stream()
                .map(this::toResponse)
                .toList();
    }
}
