package com.portfolio.salesmanager.service;

import com.portfolio.salesmanager.dto.request.ProductRequest;
import com.portfolio.salesmanager.dto.response.ProductResponse;
import com.portfolio.salesmanager.entity.Category;
import com.portfolio.salesmanager.entity.Product;
import com.portfolio.salesmanager.exception.CategoryNotFoundException;
import com.portfolio.salesmanager.exception.ProductNotFoundException;
import com.portfolio.salesmanager.mapper.ProductMapper;
import com.portfolio.salesmanager.repository.CategoryRepository;
import com.portfolio.salesmanager.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository prodRepo;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepo;

    public List<ProductResponse> findAll(){
        return prodRepo.findAll()
                .stream()
                .map(productMapper::toResponse)
                .toList();
    }

    public ProductResponse findProduct(Long idProduct){

        Product product= prodRepo.findById(idProduct)
                .orElseThrow(()-> new ProductNotFoundException("Producto no encontrado"));

        return productMapper.toResponse(product);
    }

    public List<ProductResponse> findNameProducts(String name){

        List<Product> products= prodRepo.findByNameContainingIgnoreCase(name);

        return productMapper.toResponseList(products);
    }

    public List<ProductResponse> findProductsByIdCategory(Long idCategory){

        categoryRepo.findById(idCategory)
                .orElseThrow(()-> new CategoryNotFoundException("Categoría no encontrada"));

        List<Product> products= prodRepo.findByCategory_IdCategory(idCategory);

        return productMapper.toResponseList(products);
    }

    public List<ProductResponse> findProductByPriceBetween(BigDecimal initialPrice,
                                                           BigDecimal finalPrice){

        List<Product> products = prodRepo.findByPriceBetween(initialPrice,finalPrice);

        return productMapper.toResponseList(products);
    }

    public List<ProductResponse> findActiveProducts(){

        List<Product> products = prodRepo.findByActiveTrue();

        return productMapper.toResponseList(products);
    }

    public List<ProductResponse> findTop5ExpensiveProducts(){

        List<Product> products = prodRepo.findTop5ByOrderByPriceDesc();

        return productMapper.toResponseList(products);
    }

    @Transactional
    public ProductResponse saveProduct(ProductRequest productRequest){

        Category category= categoryRepo.findById(productRequest.getCategoryId())
                .orElseThrow(()->new CategoryNotFoundException("Categoría no encontrada"));

        Product product= Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .imageUrl(productRequest.getImageUrl())
                .price(productRequest.getPrice())
                .active(productRequest.getActive() != null
                        ? productRequest.getActive()
                        : true)
                .category(category)
                .build();

        return productMapper.toResponse(prodRepo.save(product));
    }

    @Transactional
    public ProductResponse editProduct(Long idProduct,
                                       ProductRequest productRequest){

        Product product= prodRepo.findById(idProduct)
                .orElseThrow(()->new ProductNotFoundException("Producto no encontrado"));

        Category category= categoryRepo.findById(productRequest.getCategoryId())
                        .orElseThrow(()->new CategoryNotFoundException("Categoría no encontrada"));

        product.setName(productRequest.getName());
        product.setDescription(productRequest.getDescription());
        product.setImageUrl(productRequest.getImageUrl());
        product.setPrice(productRequest.getPrice());
        product.setActive(productRequest.getActive() != null
                          ? productRequest.getActive()
                          : product.getActive());
        product.setCategory(category);

        return productMapper.toResponse(prodRepo.save(product));
    }

    @Transactional
    public void deleteProduct(Long idProduct){

        Product product= prodRepo.findById(idProduct)
                .orElseThrow(()-> new ProductNotFoundException("Producto no encontrado"));

        product.setActive(false);

        prodRepo.save(product);
    }
}
