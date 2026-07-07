package com.portfolio.salesmanager.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductResponse {

    private Long idProduct;
    private String name;
    private String description;
    private BigDecimal price;
    private Boolean active;
    private String imageUrl;
    private Long categoryId;
    private String categoryName;
}