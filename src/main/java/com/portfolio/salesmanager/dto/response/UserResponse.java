package com.portfolio.salesmanager.dto.response;

import com.portfolio.salesmanager.util.Role;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponse {

    private Long idUser;
    private String name;
    private String username;
    private Role role;
    private Boolean active;
    private Long branchId;
    private String branchName;
}