package com.portfolio.salesmanager.service;

import com.portfolio.salesmanager.dto.request.BranchRequest;
import com.portfolio.salesmanager.dto.response.BranchResponse;
import com.portfolio.salesmanager.entity.Branch;
import com.portfolio.salesmanager.exception.BranchAlreadyExistsException;
import com.portfolio.salesmanager.exception.BranchNotFoundException;
import com.portfolio.salesmanager.mapper.BranchMapper;
import com.portfolio.salesmanager.repository.BranchRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class BranchService {

    private final BranchRepository branchRepo;
    private final BranchMapper branchMapper;

    public List<BranchResponse> findAll(){

        return branchRepo.findAll()
                .stream()
                .map(branchMapper::toResponse)
                .toList();
    }

    public BranchResponse findBranch(Long idBranch){

        Branch branch= branchRepo.findById(idBranch)
                .orElseThrow(()-> new BranchNotFoundException("Sucursal no encontrada"));

        return branchMapper.toResponse(branch);
    }

    @Transactional
    public BranchResponse saveBranch(BranchRequest branchRequest){
        if (branchRepo.existsByPhone(branchRequest.getPhone())){
            throw new BranchAlreadyExistsException("Ya existe una sucursal con ese número");
        }

        Branch branch = branchMapper.toEntity(branchRequest);

        return branchMapper.toResponse(branchRepo.save(branch));
    }

    @Transactional
    public BranchResponse editBranch(Long idBranch,BranchRequest branchRequest){

        Branch branch = branchRepo.findById(idBranch)
                .orElseThrow(()-> new BranchNotFoundException("Sucursal no encontrada"));

        if (!branch.getPhone().equalsIgnoreCase(branchRequest.getPhone())
                && branchRepo.existsByPhone(branchRequest.getPhone())){
            throw new BranchAlreadyExistsException("Ya existe una sucursal con ese número");
        }

        branch.setName(branchRequest.getName());
        branch.setAddress(branchRequest.getAddress());
        branch.setPhone(branchRequest.getPhone());
        branch.setActive(branchRequest.getActive() != null
                ? branchRequest.getActive()
                :branch.getActive());

        return branchMapper.toResponse(branchRepo.save(branch));
    }

    @Transactional
    public void deleteBranch(Long idBranch){

        Branch branch = branchRepo.findById(idBranch)
                .orElseThrow(() -> new BranchNotFoundException("Sucursal no encontrada"));

        branch.setActive(false);

        branchRepo.save(branch);
    }

    public List<BranchResponse> findNameBranches(String nameBranch){

        List<Branch> branches = branchRepo.findByNameContainingIgnoreCase(nameBranch);

        return branchMapper.toResponseList(branches);
    }

    public List<BranchResponse> findActiveBranches(){

        List<Branch> branches = branchRepo.findByActiveTrue();

        return branchMapper.toResponseList(branches);
    }

}
