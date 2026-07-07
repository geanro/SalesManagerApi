package com.portfolio.salesmanager.mapper;

import com.portfolio.salesmanager.dto.response.BranchInventoryResponse;
import com.portfolio.salesmanager.entity.BranchInventory;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BranchInventoryMapper {

    public BranchInventoryResponse toResponse(BranchInventory branchInventory){
        if (branchInventory==null) return null;

        return BranchInventoryResponse.builder()
                .idInventory(branchInventory.getIdInventory())
                .branchId(branchInventory.getBranch().getIdBranch())
                .branchName(branchInventory.getBranch().getName())
                .productId(branchInventory.getProduct().getIdProduct())
                .productName(branchInventory.getProduct().getName())
                .stock(branchInventory.getStock())
                .build();
    }

    public List<BranchInventoryResponse> toResponseList(List<BranchInventory> branchInventories){
        if (branchInventories==null) return List.of();

        return branchInventories.stream()
                .map(this::toResponse)
                .toList();
    }
}
