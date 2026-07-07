package com.portfolio.salesmanager.dto.response;

import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BranchInventoryResponse {

    private Long idInventory;
    private Long branchId;
    private String branchName;
    private Long productId;
    private String productName;
    private Integer stock;
}