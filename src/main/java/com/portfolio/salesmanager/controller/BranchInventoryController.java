package com.portfolio.salesmanager.controller;

import com.portfolio.salesmanager.dto.request.BranchInventoryRequest;
import com.portfolio.salesmanager.dto.request.UpdateStockRequest;
import com.portfolio.salesmanager.dto.response.BranchInventoryResponse;
import com.portfolio.salesmanager.service.BranchInventoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/inventory")
public class BranchInventoryController {

    private final BranchInventoryService inventoryService;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_INVENTORY')")
    public ResponseEntity<List<BranchInventoryResponse>> findAll() {
        return ResponseEntity.ok(inventoryService.findAll());
    }

    @GetMapping("/my-branch")
    @PreAuthorize("hasAuthority('READ_BRANCH_INVENTORY')")
    public ResponseEntity<List<BranchInventoryResponse>> findInventoryByMyBranch() {
        return ResponseEntity.ok(inventoryService.findInventoryByMyBranch());
    }

    @GetMapping("/branch/{idBranch}")
    @PreAuthorize("hasAuthority('READ_INVENTORY')")
    public ResponseEntity<List<BranchInventoryResponse>> findByBranch(@PathVariable Long idBranch) {
        return ResponseEntity.ok(inventoryService.findByBranch(idBranch));
    }

    @GetMapping("/product/{idProduct}")
    @PreAuthorize("hasAuthority('READ_INVENTORY')")
    public ResponseEntity<List<BranchInventoryResponse>> findByProduct(@PathVariable Long idProduct) {
        return ResponseEntity.ok(inventoryService.findByProduct(idProduct));
    }

    @GetMapping("/branch/{idBranch}/product/{idProduct}")
    @PreAuthorize("hasAuthority('READ_INVENTORY')")
    public ResponseEntity<BranchInventoryResponse> findByBranchAndProduct(
            @PathVariable Long idBranch,
            @PathVariable Long idProduct) {
        return ResponseEntity.ok(inventoryService.findByBranchAndProduct(idBranch, idProduct));
    }

    @GetMapping("/low-stock")
    @PreAuthorize("hasAuthority('READ_INVENTORY')")
    public ResponseEntity<List<BranchInventoryResponse>> findLowStock(@RequestParam Integer stock) {
        return ResponseEntity.ok(inventoryService.findLowStock(stock));
    }

    @GetMapping("/{idInventory}")
    @PreAuthorize("hasAuthority('READ_INVENTORY')")
    public ResponseEntity<BranchInventoryResponse> findInventory(@PathVariable Long idInventory) {
        return ResponseEntity.ok(inventoryService.findInventory(idInventory));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SAVE_INVENTORY')")
    public ResponseEntity<BranchInventoryResponse> saveInventory(
            @Valid @RequestBody BranchInventoryRequest request) {
        BranchInventoryResponse creado = inventoryService.saveInventory(request);
        return ResponseEntity
                .created(URI.create("/inventory/" + creado.getIdInventory()))
                .body(creado);
    }

    @PutMapping("/{idInventory}/stock")
    @PreAuthorize("hasAuthority('UPDATE_INVENTORY')")
    public ResponseEntity<BranchInventoryResponse> updateStock(
            @PathVariable Long idInventory,
            @Valid @RequestBody UpdateStockRequest request) {
        return ResponseEntity.ok(inventoryService.updateStock(idInventory, request));
    }

    @DeleteMapping("/{idInventory}")
    @PreAuthorize("hasAuthority('DELETE_INVENTORY')")
    public ResponseEntity<Void> deleteInventory(@PathVariable Long idInventory) {
        inventoryService.deleteInventory(idInventory);
        return ResponseEntity.noContent().build();
    }
}
