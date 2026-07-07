package com.portfolio.salesmanager.dto.request;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class AuthenticationRequest {
    private String username;
    private String password;
}
