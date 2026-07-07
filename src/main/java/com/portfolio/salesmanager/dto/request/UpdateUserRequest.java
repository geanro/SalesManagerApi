package com.portfolio.salesmanager.dto.request;

import com.portfolio.salesmanager.util.Role;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateUserRequest {

    private String name;

    private Role role;

    private Boolean active;

    private Long branchId;
}
