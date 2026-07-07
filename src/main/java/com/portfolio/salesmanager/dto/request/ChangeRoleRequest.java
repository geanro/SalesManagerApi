package com.portfolio.salesmanager.dto.request;

import com.portfolio.salesmanager.util.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChangeRoleRequest {

    @NotNull(message = "El rol es obligatorio")
    private Role role;

    private Long branchId;
}