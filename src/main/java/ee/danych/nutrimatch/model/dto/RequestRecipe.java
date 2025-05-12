package ee.danych.nutrimatch.model.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class RequestRecipe {
    private RequestRecipe() {
        products = new ArrayList<>();
    }

    @NotNull
    private String username;

    @NotNull
    private String title;

    @NotNull
    private String photoBase64;

    @NotNull
    private String description;

    @NotNull
    @Valid
    private List<ProductRecipeRequest> products;
}
