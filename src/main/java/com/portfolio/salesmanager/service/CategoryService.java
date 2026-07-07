package com.portfolio.salesmanager.service;


import com.portfolio.salesmanager.dto.request.CategoryRequest;
import com.portfolio.salesmanager.dto.response.CategoryResponse;
import com.portfolio.salesmanager.entity.Category;
import com.portfolio.salesmanager.exception.CategoryAlreadyExistsException;
import com.portfolio.salesmanager.exception.CategoryNotFoundException;
import com.portfolio.salesmanager.mapper.CategoryMapper;
import com.portfolio.salesmanager.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class CategoryService {

    private final CategoryRepository cateRepo;
    private final CategoryMapper categoryMapper;

    public List<CategoryResponse> findAll(){

        return cateRepo.findAll()
                .stream()
                .map(categoryMapper::toResponse)
                .toList();
    }

    public CategoryResponse findCategory(Long idCategory){

        Category category= cateRepo.findById(idCategory)
                .orElseThrow(()-> new CategoryNotFoundException("Categoría no encontrada"));

        return categoryMapper.toResponse(category);
    }

    public List<CategoryResponse> findNameCategories(String name){

        List<Category> categories= cateRepo.findByNameContainingIgnoreCase(name);

        return categoryMapper.toResponseList(categories);
    }

    public List<CategoryResponse> findActiveCategories(){

        List<Category> categories= cateRepo.findByActiveTrue();

        return categoryMapper.toResponseList(categories);
    }

    @Transactional
    public CategoryResponse saveCategory(CategoryRequest categoryRequest){

        if (cateRepo.existsByNameIgnoreCase(categoryRequest.getName())){
            throw new CategoryAlreadyExistsException("Ya existe una categoria con ese nombre");
        }

        Category category= categoryMapper.toEntity(categoryRequest);

        return categoryMapper.toResponse(cateRepo.save(category));
    }

    @Transactional
    public CategoryResponse editCategory(Long idCategory,
                                         CategoryRequest categoryRequest){

        Category category= cateRepo.findById(idCategory)
                .orElseThrow(()->new CategoryNotFoundException("Categoría no encontrada"));

        if (!category.getName().equalsIgnoreCase(categoryRequest.getName())
            && cateRepo.existsByNameIgnoreCase(categoryRequest.getName())){

            throw new CategoryAlreadyExistsException("Ya existe una categoría con ese nombre");
        }

        category.setName(categoryRequest.getName());
        category.setDescription(categoryRequest.getDescription());
        category.setActive(categoryRequest.getActive() != null
                ? categoryRequest.getActive()
                :category.getActive());

        return categoryMapper.toResponse(cateRepo.save(category));
    }

    @Transactional
    public void deleteCategory(Long idCategory){

        Category category= cateRepo.findById(idCategory)
                .orElseThrow(()-> new CategoryNotFoundException("Categoría no encontrada"));

        category.setActive(false);

        cateRepo.save(category);
    }


}
