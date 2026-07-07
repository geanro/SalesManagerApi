package com.portfolio.salesmanager.controller;

import com.portfolio.salesmanager.dto.request.BranchRequest;
import com.portfolio.salesmanager.dto.response.BranchResponse;
import com.portfolio.salesmanager.service.BranchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/branches")
public class BranchController {

    private final BranchService branchService;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ALL_BRANCHES')")
    public ResponseEntity<List<BranchResponse>> getAllBranches(){
        return ResponseEntity.ok(branchService.findAll());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('READ_ALL_BRANCHES')")
    public ResponseEntity<List<BranchResponse>> findNameBranches(@RequestParam String name){
        return ResponseEntity.ok(branchService.findNameBranches(name));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('READ_ALL_BRANCHES')")
    public ResponseEntity<List<BranchResponse>> findActiveBranches(){
        return ResponseEntity.ok(branchService.findActiveBranches());
    }

    @GetMapping("/{idBranch}")
    @PreAuthorize("hasAuthority('READ_ALL_BRANCHES')")
    public ResponseEntity<BranchResponse> findBranch(@PathVariable Long idBranch) {
        return ResponseEntity.ok(branchService.findBranch(idBranch));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SAVE_ONE_BRANCH')")
    public ResponseEntity<BranchResponse> saveBranch(@Valid @RequestBody BranchRequest branchRequest){
        BranchResponse creado= branchService.saveBranch(branchRequest);
        return ResponseEntity.created(URI.create("/branches/" + creado.getIdBranch())).body(creado);
    }

    @PutMapping("/{idBranch}")
    @PreAuthorize("hasAuthority('UPDATE_ONE_BRANCH')")
    public ResponseEntity<BranchResponse> editBranch(@PathVariable Long idBranch,
                                                     @Valid @RequestBody BranchRequest branchRequest){
        return ResponseEntity.ok(branchService.editBranch(idBranch,branchRequest));
    }

    @DeleteMapping("/{idBranch}")
    @PreAuthorize("hasAuthority('DELETE_ONE_BRANCH')")
    public ResponseEntity<Void> deleteBranch(@PathVariable Long idBranch){
        branchService.deleteBranch(idBranch);
        return ResponseEntity.noContent().build();
    }
}
