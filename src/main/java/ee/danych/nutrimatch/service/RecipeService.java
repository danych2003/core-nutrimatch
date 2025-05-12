package ee.danych.nutrimatch.service;

import ee.danych.nutrimatch.model.entity.Recipe;
import ee.danych.nutrimatch.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private final RecipeRepository recipeRepository;

    public void saveRecipe(Recipe recipe) {
        recipeRepository.save(recipe);
    }
}
