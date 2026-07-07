package com.portfolio.salesmanager.dto.response;

import com.portfolio.salesmanager.util.SaleStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class SaleResponse {

    private Long idSale;
    private Long userId;
    private String userName;
    private Long branchId;
    private String branchName;
    private LocalDateTime saleDate;
    private BigDecimal total;
    private SaleStatus status;
    private List<SaleDetailResponse> details;
}