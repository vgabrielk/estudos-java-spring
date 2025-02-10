package com.example.springboot.dtos;

import jakarta.validation.constraints.NotBlank;

public record CategoryDto(@NotBlank String name) {
}
