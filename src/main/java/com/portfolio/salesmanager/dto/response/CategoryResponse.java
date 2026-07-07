package com.portfolio.salesmanager.dto.response;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryResponse {

    private Long idCategory;
    private String name;
    private String description;
    private Boolean active;
}