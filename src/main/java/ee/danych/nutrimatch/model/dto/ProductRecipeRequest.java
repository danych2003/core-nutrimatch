package ee.danych.nutrimatch.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductRecipeRequest {
    @NotNull
    private Long productId;
    @NotNull
    private int quantity;
    @NotNull
    private String unit;
}
