package com.portfolio.salesmanager.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateUserProfileRequest {

    @NotBlank(message = "El nombre es obligatorio")
    private String name;
}