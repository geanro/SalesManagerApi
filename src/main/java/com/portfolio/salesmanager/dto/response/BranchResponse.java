package com.portfolio.salesmanager.dto.response;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BranchResponse {

    private Long idBranch;
    private String name;
    private String address;
    private String phone;
    private Boolean active;
}