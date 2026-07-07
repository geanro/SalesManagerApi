package com.portfolio.salesmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CategoryRequest {

    @NotBlank(message = "El nombre de la categoría es obligatorio")
    private String name;

    private String description;

    private Boolean active;
}