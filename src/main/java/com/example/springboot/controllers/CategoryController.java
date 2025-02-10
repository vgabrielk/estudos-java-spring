package com.example.springboot.controllers;

import com.example.springboot.dtos.CategoryDto;
import com.example.springboot.models.CategoryModel;
import com.example.springboot.repositories.CategoryRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("categories")
@CrossOrigin(origins = "*")
public class CategoryController {

    @Autowired
    CategoryRepository categoryRepository;

    @PostMapping
    public ResponseEntity<CategoryModel> createCompany(@RequestBody @Valid CategoryDto categoryDto){
        var categoryModel = new CategoryModel();
        BeanUtils.copyProperties(categoryDto, categoryModel);
        return ResponseEntity.ok().body(categoryRepository.save(categoryModel));
    }

    @GetMapping
    public ResponseEntity<List<CategoryModel>> getCompanies() {
        return ResponseEntity.ok().body(categoryRepository.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getCompany(@PathVariable UUID id){
        Optional<CategoryModel> category = categoryRepository.findById(id);

        if(category.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada!");
        }
        return ResponseEntity.ok(category.get());
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryModel> updateCategory(@PathVariable(value = "id") UUID id, @RequestBody @Valid CategoryDto categoryDto) {
        Optional<CategoryModel> category = categoryRepository.findById(id);
        if (category.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        var categoryModel = category.get();
        BeanUtils.copyProperties(categoryDto, categoryModel, "id");
        return ResponseEntity.ok(categoryRepository.save(categoryModel));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteCompany(@PathVariable UUID id){
        Optional<CategoryModel> category = categoryRepository.findById(id);
        if(category.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada!");
        }
        var categoryModel = category.get();
        categoryRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(categoryModel);
    }


}
