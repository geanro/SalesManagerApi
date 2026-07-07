package com.portfolio.salesmanager.dto.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.*;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SaleRequest {

    private Long branchId;

    @Valid
    @NotEmpty(message = "La venta debe tener al menos un detalle")
    private List<SaleDetailRequest> details;
}