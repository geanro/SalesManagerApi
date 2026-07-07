package com.portfolio.salesmanager.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProductRequest {

    @NotBlank(message = "El nombre del producto es obligatorio")
    private String name;

    private String description;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMin(value = "0.01", message = "El precio debe ser mayor a 0")
    private BigDecimal price;

    private Boolean active;

    private String imageUrl;
    
    @NotNull(message = "La categoría es obligatoria")
    private Long categoryId;
}