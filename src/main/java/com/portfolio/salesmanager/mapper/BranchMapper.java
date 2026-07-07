package com.portfolio.salesmanager.mapper;

import com.portfolio.salesmanager.dto.request.BranchRequest;
import com.portfolio.salesmanager.dto.response.BranchResponse;
import com.portfolio.salesmanager.entity.Branch;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BranchMapper {

    public BranchResponse toResponse(Branch branch){
        if (branch==null) return null;

        return BranchResponse.builder()
                .idBranch(branch.getIdBranch())
                .name(branch.getName())
                .address(branch.getAddress())
                .phone(branch.getPhone())
                .active(branch.getActive())
                .build();
    }

    public List<BranchResponse> toResponseList(List<Branch> branches){
        if (branches==null) return List.of();

        return branches.stream()
                .map(this::toResponse)
                .toList();
    }

    public Branch toEntity(BranchRequest branchRequest){
        if (branchRequest==null) return null;

        return Branch.builder()
                .name(branchRequest.getName())
                .address(branchRequest.getAddress())
                .phone(branchRequest.getPhone())
                .active(branchRequest.getActive() !=null ? branchRequest.getActive(): true )
                .build();
    }
}
