package ee.danych.nutrimatch.service;

import ee.danych.nutrimatch.model.entity.Recipe;
import ee.danych.nutrimatch.repository.RecipeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RecipeService {
    private static final int PAGE_SIZE = 30;

    private final RecipeRepository recipeRepository;

    public void save(Recipe recipe) {
        recipeRepository.save(recipe);
    }

    public Page<Recipe> filterRecipesByWord(String word, int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return recipeRepository.filterRecipesByWord(word, pageable);
    }

    public Page<Recipe> findAllByPage(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return recipeRepository.findAll(pageable);
    }

    public void deleteRecipeByTitle(String title) {
        Optional<Recipe> recipeOpt = recipeRepository.findByTitleIgnoreCase(title);
        recipeOpt.ifPresent(recipeRepository::delete);
    }
}
