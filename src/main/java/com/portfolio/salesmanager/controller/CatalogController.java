package com.portfolio.salesmanager.controller;

import com.portfolio.salesmanager.dto.response.BranchResponse;
import com.portfolio.salesmanager.dto.response.CatalogProductResponse;
import com.portfolio.salesmanager.dto.response.CategoryResponse;
import com.portfolio.salesmanager.service.CatalogService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/catalog")
public class CatalogController {

    private final CatalogService catalogService;

    @GetMapping("/products")
    public ResponseEntity<List<CatalogProductResponse>> findActiveProducts(){

        return ResponseEntity.ok(catalogService.findActiveProducts());
    }
    
    @GetMapping("/branches")
    public ResponseEntity<List<BranchResponse>> findActiveBranches(){

        return ResponseEntity.ok(catalogService.findActiveBranches());
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<CatalogProductResponse>> searchProducts(@RequestParam String name){

        return ResponseEntity.ok(catalogService.searchProducts(name));
    }

    @GetMapping("/products/{idProduct}")
    public ResponseEntity<CatalogProductResponse> findProduct(@PathVariable Long idProduct){

        return ResponseEntity.ok(catalogService.findProduct(idProduct));
    }

    @GetMapping("/products/{idProduct}/stock")
    public ResponseEntity<Integer> findAvailableStock(@PathVariable Long idProduct){

        return ResponseEntity.ok(catalogService.findAvailableStock(idProduct));
    }

    @GetMapping("/categories")
    public ResponseEntity<List<CategoryResponse>> findActiveCategories(){

        return ResponseEntity.ok(catalogService.findActiveCategories());
    }
}