package com.example.springboot.controllers;

import com.example.springboot.dtos.ProductRecordDto;
import com.example.springboot.models.CategoryModel;
import com.example.springboot.models.CompanyModel;
import com.example.springboot.models.ProductModel;
import com.example.springboot.repositories.CategoryRepository;
import com.example.springboot.repositories.CompanyRepository;
import com.example.springboot.repositories.ProductRepository;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    CompanyRepository companyRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @PostMapping("")
    public ResponseEntity<Object> saveProduct(@RequestBody @Valid ProductRecordDto productRecordDto) {
        Optional<CompanyModel> companyOptional = companyRepository.findById(productRecordDto.companyId());
        Optional<CategoryModel> categoryOptional = categoryRepository.findById(productRecordDto.categoryId());


            if (companyOptional.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada");
            }
        if (categoryOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Categoria não encontrada");
        }

        var productModel = new ProductModel();
        BeanUtils.copyProperties(productRecordDto, productModel);
        productModel.setCompany(companyOptional.get());
        productModel.setCategory(categoryOptional.get());

        return ResponseEntity.status(HttpStatus.CREATED).body(productRepository.save(productModel));
    }

    @GetMapping("")
    public ResponseEntity<List<ProductModel>> getAllProducts() {
        return ResponseEntity.status(HttpStatus.OK).body(productRepository.findAll());
    }

    @GetMapping("{id}")
    public ResponseEntity<Object> getProduct(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> product = productRepository.findById(id);
        if(product.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
        return ResponseEntity.status(HttpStatus.OK).body(product.get());
    }

    @PutMapping("{id}")
    public ResponseEntity<Object> updateProduct(@PathVariable(value = "id") UUID id,
                                                @RequestBody @Valid ProductRecordDto productRecordDto) {
        Optional<ProductModel> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }

        Optional<CompanyModel> companyOptional = companyRepository.findById(productRecordDto.companyId());
        if (companyOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empresa não encontrada");
        }

        var productModel = productOptional.get();

        productModel.setName(productRecordDto.name());
        productModel.setDescription(productRecordDto.description());
        productModel.setValue(productRecordDto.value());
        productModel.setCompany(companyOptional.get());

        return ResponseEntity.status(HttpStatus.OK).body(productRepository.save(productModel));
    }


    @DeleteMapping("{id}")
    public ResponseEntity<Object> deleteProduct(@PathVariable(value = "id") UUID id) {
        Optional<ProductModel> productOptional = productRepository.findById(id);
        if (productOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Produto não encontrado");
        }
        productRepository.delete(productOptional.get());
        return ResponseEntity.status(HttpStatus.OK).body("Produto removido com sucesso");
    }
}
