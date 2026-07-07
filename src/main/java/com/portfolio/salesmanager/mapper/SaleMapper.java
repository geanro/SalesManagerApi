package com.portfolio.salesmanager.mapper;

import com.portfolio.salesmanager.dto.response.SaleDetailResponse;
import com.portfolio.salesmanager.dto.response.SaleResponse;
import com.portfolio.salesmanager.entity.Sale;
import com.portfolio.salesmanager.entity.SaleDetail;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class SaleMapper {
    public SaleResponse toResponse(Sale sale){
        if (sale==null) return null;

        return SaleResponse.builder()
                .idSale(sale.getIdSale())
                .userId(sale.getUser().getIdUser())
                .userName(sale.getUser().getName())
                .branchId(sale.getBranch().getIdBranch())
                .branchName(sale.getBranch().getName())
                .saleDate(sale.getSaleDate())
                .total(sale.getTotal())
                .status(sale.getStatus())
                .details(toDetailResponseList(sale.getDetails()))
                .build();
    }

    public List<SaleResponse> toResponseList(List<Sale> sales){
        if (sales==null) return List.of();

        return sales.stream()
                .map(this::toResponse)
                .toList();
    }

    public SaleDetailResponse toDetailResponse(SaleDetail detail){
        if (detail==null) return null;

        return SaleDetailResponse.builder()
                .productId(detail.getProduct().getIdProduct())
                .productName(detail.getProduct().getName())
                .quantity(detail.getQuantity())
                .unitPrice(detail.getUnitPrice())
                .subtotal(detail.getSubtotal())
                .build();
    }

    public List<SaleDetailResponse> toDetailResponseList(List<SaleDetail> details){
        if (details==null) return List.of();

        return details.stream()
                .map(this::toDetailResponse)
                .toList();
    }
}
