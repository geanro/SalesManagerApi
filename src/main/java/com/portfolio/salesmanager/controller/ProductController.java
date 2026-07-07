package com.portfolio.salesmanager.controller;

import com.portfolio.salesmanager.dto.request.ProductRequest;
import com.portfolio.salesmanager.dto.response.ProductResponse;
import com.portfolio.salesmanager.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ALL_PRODUCTS')")
    public ResponseEntity<List<ProductResponse>> findAll(){
        return ResponseEntity.ok(productService.findAll());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('READ_ALL_PRODUCTS')")
    public ResponseEntity<List<ProductResponse>> findNameProducts(@RequestParam String name){
        return ResponseEntity.ok(productService.findNameProducts(name));
    }

    @GetMapping("/category/{idCategory}")
    @PreAuthorize("hasAuthority('READ_ALL_PRODUCTS')")
    public ResponseEntity<List<ProductResponse>> findProductsByIdCategory(@PathVariable Long idCategory){
        return ResponseEntity.ok(productService.findProductsByIdCategory(idCategory));
    }

    @GetMapping("/price-between")
    @PreAuthorize("hasAuthority('READ_ALL_PRODUCTS')")
    public ResponseEntity<List<ProductResponse>> findProductByPriceBetween(@RequestParam BigDecimal initialPrice,
                                                                           @RequestParam BigDecimal finalPrice){
        return ResponseEntity.ok(productService.findProductByPriceBetween(initialPrice, finalPrice));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('READ_ALL_PRODUCTS')")
    public ResponseEntity<List<ProductResponse>> findActiveProducts(){
        return ResponseEntity.ok(productService.findActiveProducts());
    }

    @GetMapping("/top-expensive")
    @PreAuthorize("hasAuthority('READ_ALL_PRODUCTS')")
    public ResponseEntity<List<ProductResponse>> findTop5ExpensiveProducts(){
        return ResponseEntity.ok(productService.findTop5ExpensiveProducts());
    }

    @GetMapping("/{idProduct}")
    @PreAuthorize("hasAuthority('READ_ALL_PRODUCTS')")
    public ResponseEntity<ProductResponse> findProduct(@PathVariable Long idProduct){
        return ResponseEntity.ok(productService.findProduct(idProduct));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SAVE_ONE_PRODUCT')")
    public ResponseEntity<ProductResponse> saveProduct(@Valid @RequestBody ProductRequest productRequest){
        ProductResponse creado= productService.saveProduct(productRequest);
        return ResponseEntity.created(URI.create("/products/" + creado.getIdProduct())).body(creado);
    }

    @PutMapping("/{idProduct}")
    @PreAuthorize("hasAuthority('UPDATE_ONE_PRODUCT')")
    public ResponseEntity<ProductResponse> editProduct(@PathVariable Long idProduct,
                                                       @Valid @RequestBody ProductRequest productRequest){
        return ResponseEntity.ok(productService.editProduct(idProduct,productRequest));
    }

    @DeleteMapping("/{idProduct}")
    @PreAuthorize("hasAuthority('DELETE_ONE_PRODUCT')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long idProduct){
        productService.deleteProduct(idProduct);
        return ResponseEntity.noContent().build();
    }
}
