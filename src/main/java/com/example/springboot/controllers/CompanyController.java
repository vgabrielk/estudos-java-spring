package com.example.springboot.controllers;

import com.example.springboot.dtos.CompanyDto;
import com.example.springboot.models.CompanyModel;
import com.example.springboot.repositories.CompanyRepository;
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
@RequestMapping("/company")
public class CompanyController {

    @Autowired
    CompanyRepository companyRepository;

    @PostMapping
    public ResponseEntity createCompany(@RequestBody @Valid CompanyDto companyDto) {
        var companyModel = new CompanyModel();
        BeanUtils.copyProperties(companyDto, companyModel);
        return ResponseEntity.status(HttpStatus.CREATED).body(companyRepository.save(companyModel));
    }

    @GetMapping
    public ResponseEntity<List<CompanyModel>> getAllCompanies() {
        return ResponseEntity.ok(companyRepository.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyModel> getCompany(@PathVariable(value = "id") UUID id) {
        Optional<CompanyModel> company = companyRepository.findById(id);
        if (company.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.ok(company.get());
    }

    @PutMapping("{id}")
    public ResponseEntity<CompanyModel> updateCompany(@PathVariable(value = "id") UUID id, @RequestBody @Valid CompanyDto companyDto) {
        Optional<CompanyModel> company = companyRepository.findById(id);
        if (company.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        var companyModel = company.get();
        BeanUtils.copyProperties(companyDto, companyModel, "id");
        return ResponseEntity.ok(companyRepository.save(companyModel));
    }

    @DeleteMapping("{id}")
    public ResponseEntity deleteCompany(@PathVariable(value = "id") UUID id) {
        Optional<CompanyModel> company = companyRepository.findById(id);
        if (company.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        var companyModel = company.get();
        companyRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body(companyModel);

    }
}


