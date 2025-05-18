package ee.danych.nutrimatch.model.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductRecipeRequest {
    @NotNull
    private Long productId;
    @NotNull
    private int quantity;
    @NotNull
    private String unit;
}
