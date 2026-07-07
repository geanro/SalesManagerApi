package com.portfolio.salesmanager.controller;

import com.portfolio.salesmanager.dto.request.SaleRequest;
import com.portfolio.salesmanager.dto.response.SaleResponse;
import com.portfolio.salesmanager.service.SaleService;
import com.portfolio.salesmanager.util.SaleStatus;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/sales")
public class SaleController {

    private final SaleService saleService;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ALL_SALES')")
    public ResponseEntity<List<SaleResponse>> findAll(){
        return ResponseEntity.ok(saleService.findAll());
    }

    @GetMapping("/me")
    @PreAuthorize("hasAuthority('READ_MY_SALES')")
    public ResponseEntity<List<SaleResponse>> findMySales(){
        return ResponseEntity.ok(saleService.findMySales());
    }

    @GetMapping("/my-branch")
    @PreAuthorize("hasAuthority('READ_BRANCH_SALES')")
    public ResponseEntity<List<SaleResponse>> findSalesByMyBranch(){
        return ResponseEntity.ok(saleService.findSalesByMyBranch());
    }

    @GetMapping("/user/{idUser}")
    @PreAuthorize("hasAuthority('READ_USER_SALES')")
    public ResponseEntity<List<SaleResponse>> findSalesByUser(@PathVariable Long idUser){
        return ResponseEntity.ok(saleService.findSalesByUser(idUser));
    }

    @GetMapping("/branch/{idBranch}")
    @PreAuthorize("hasAuthority('READ_ANY_BRANCH_SALES')")
    public ResponseEntity<List<SaleResponse>> findSalesByBranch(@PathVariable Long idBranch){
        return ResponseEntity.ok(saleService.findSalesByBranch(idBranch));
    }

    @GetMapping("/status")
    @PreAuthorize("hasAuthority('READ_ALL_SALES')")
    public ResponseEntity<List<SaleResponse>> findSalesByStatus(@RequestParam SaleStatus status){
        return ResponseEntity.ok(saleService.findSalesByStatus(status));
    }

    @GetMapping("/date-between")
    @PreAuthorize("hasAuthority('READ_ALL_SALES')")
    public ResponseEntity<List<SaleResponse>> findSalesByDateBetween(
            @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime start,
            @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime end){
        return ResponseEntity.ok(saleService.findSalesByDateBetween(start, end));
    }

    @GetMapping("/top-sale")
    @PreAuthorize("hasAuthority('READ_ALL_SALES')")
    public ResponseEntity<List<SaleResponse>> findTopSales(){
        return ResponseEntity.ok(saleService.findTopSales());
    }

    @GetMapping("/{idSale}")
    @PreAuthorize("hasAuthority('READ_ALL_SALES')")
    public ResponseEntity<SaleResponse> findSale(@PathVariable Long idSale){
        return ResponseEntity.ok(saleService.findSale(idSale));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('CREATE_ONE_SALE')")
    public ResponseEntity<SaleResponse> saveSale(@Valid @RequestBody SaleRequest saleRequest){
        SaleResponse creado= saleService.saveSale(saleRequest);
        return ResponseEntity.created(URI.create("/sales/" + creado.getIdSale())).body(creado);
    }

    @PutMapping("/{idSale}/cancel")
    @PreAuthorize("hasAuthority('CANCEL_ONE_SALE')")
    public ResponseEntity<SaleResponse> cancelSale(@PathVariable Long idSale){
        return ResponseEntity.ok(saleService.cancelSale(idSale));
    }
}
