package com.portfolio.salesmanager.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BranchInventoryRequest {

    @NotNull(message = "La sucursal es obligatoria")
    private Long branchId;

    @NotNull(message = "El producto es obligatorio")
    private Long productId;

    @NotNull(message = "El stock es obligatorio")
    @Min(value = 0, message = "El stock no puede ser negativo")
    private Integer stock;
}