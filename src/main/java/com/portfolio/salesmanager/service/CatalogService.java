package com.portfolio.salesmanager.service;

import com.portfolio.salesmanager.dto.response.BranchResponse;
import com.portfolio.salesmanager.dto.response.CatalogProductResponse;
import com.portfolio.salesmanager.dto.response.CategoryResponse;
import com.portfolio.salesmanager.entity.Product;
import com.portfolio.salesmanager.exception.ProductNotFoundException;
import com.portfolio.salesmanager.mapper.BranchMapper;
import com.portfolio.salesmanager.mapper.CatalogMapper;
import com.portfolio.salesmanager.mapper.CategoryMapper;
import com.portfolio.salesmanager.repository.BranchInventoryRepository;
import com.portfolio.salesmanager.repository.BranchRepository;
import com.portfolio.salesmanager.repository.CategoryRepository;
import com.portfolio.salesmanager.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CatalogService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BranchInventoryRepository inventoryRepository;
    private final CategoryMapper categoryMapper;
    private final CatalogMapper catalogMapper;
    private final BranchRepository branchRepository;
    private final BranchMapper branchMapper;


    public List<CatalogProductResponse> findActiveProducts(){

        return productRepository.findByActiveTrue()
                .stream()
                .map(this::mapProductWithStock)
                .toList();
    }

    public CatalogProductResponse findProduct(Long idProduct){

        Product product = productRepository.findById(idProduct)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado"));

        if (!product.getActive()) {
            throw new ProductNotFoundException("Producto no disponible");
        }

        return mapProductWithStock(product);
    }

    public List<BranchResponse> findActiveBranches(){

        return branchMapper.toResponseList(branchRepository.findByActiveTrue());
    }

    public List<CatalogProductResponse> searchProducts(String name){

        return productRepository.findByNameContainingIgnoreCaseAndActiveTrue(name)
                .stream()
                .map(this::mapProductWithStock)
                .toList();
    }

    public List<CategoryResponse> findActiveCategories(){

        return categoryMapper.toResponseList(categoryRepository.findByActiveTrue());
    }

    public Integer findAvailableStock(Long idProduct){

        Product product = productRepository.findById(idProduct)
                .orElseThrow(() -> new ProductNotFoundException("Producto no encontrado"));

        if (!product.getActive()) {
            throw new ProductNotFoundException("Producto no disponible");
        }

        return inventoryRepository.sumStockByProductId(idProduct);
    }

    private CatalogProductResponse mapProductWithStock(Product product){

        Integer availableStock = inventoryRepository.sumStockByProductId(product.getIdProduct());

        return catalogMapper.toResponse(product, availableStock);
    }
}