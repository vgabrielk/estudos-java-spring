package com.example.springboot.dtos;


import jakarta.validation.constraints.NotBlank;

public record CompanyDto(@NotBlank String name, @NotBlank String cnpj) {
}
