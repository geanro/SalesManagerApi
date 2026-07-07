package com.portfolio.salesmanager.dto.request;

import jakarta.persistence.Column;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BranchRequest {

    @NotBlank(message = "El nombre de la sucursal es obligatorio")
    private String name;

    @NotBlank(message = "La dirección es obligatoria")
    private String address;

    @NotBlank(message = "El teléfono es obligatorio")
    @Column(unique = true)
    private String phone;

    private Boolean active;
}