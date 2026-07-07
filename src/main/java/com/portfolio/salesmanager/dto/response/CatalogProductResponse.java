package com.portfolio.salesmanager.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
public class CatalogProductResponse {

    private Long idProduct;
    private String name;
    private String description;
    private String imageUrl;
    private BigDecimal price;
    private Long categoryId;
    private String categoryName;
    private Integer availableStock;
}