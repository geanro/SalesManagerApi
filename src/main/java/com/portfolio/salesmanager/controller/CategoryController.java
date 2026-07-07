package com.portfolio.salesmanager.controller;

import com.portfolio.salesmanager.dto.request.CategoryRequest;
import com.portfolio.salesmanager.dto.response.CategoryResponse;
import com.portfolio.salesmanager.service.CategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/categories")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping
    @PreAuthorize("hasAuthority('READ_ALL_CATEGORIES')")
    public ResponseEntity<List<CategoryResponse>> findAll(){
        return ResponseEntity.ok(categoryService.findAll());
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('READ_ALL_CATEGORIES')")
    public ResponseEntity<List<CategoryResponse>> findNameCategories(@RequestParam String name){
        return ResponseEntity.ok(categoryService.findNameCategories(name));
    }

    @GetMapping("/active")
    @PreAuthorize("hasAuthority('READ_ALL_CATEGORIES')")
    public ResponseEntity<List<CategoryResponse>> findActiveCategories(){
        return ResponseEntity.ok(categoryService.findActiveCategories());
    }

    @GetMapping("/{idCategory}")
    @PreAuthorize("hasAuthority('READ_ALL_CATEGORIES')")
    public ResponseEntity<CategoryResponse> findCategory(@PathVariable Long idCategory){
        return ResponseEntity.ok(categoryService.findCategory(idCategory));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('SAVE_ONE_CATEGORY')")
    public ResponseEntity<CategoryResponse> saveCategory(@Valid @RequestBody CategoryRequest categoryRequest){
        CategoryResponse creado= categoryService.saveCategory(categoryRequest);
        return ResponseEntity.created(URI.create("/categories/" + creado.getIdCategory())).body(creado);
    }

    @PutMapping("/{idCategory}")
    @PreAuthorize("hasAuthority('UPDATE_ONE_CATEGORY')")
    public ResponseEntity<CategoryResponse> editCategory(@PathVariable Long idCategory,
                                                         @Valid @RequestBody CategoryRequest categoryRequest){
        return ResponseEntity.ok(categoryService.editCategory(idCategory,categoryRequest));
    }

    @DeleteMapping("/{idCategory}")
    @PreAuthorize("hasAuthority('DELETE_ONE_CATEGORY')")
    public ResponseEntity<Void> deleteCategory(@PathVariable Long idCategory){
        categoryService.deleteCategory(idCategory);
        return ResponseEntity.noContent().build();
    }
}
