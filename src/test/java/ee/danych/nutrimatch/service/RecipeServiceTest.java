package ee.danych.nutrimatch.service;

import ee.danych.nutrimatch.model.entity.Recipe;
import ee.danych.nutrimatch.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class RecipeServiceTest {

    private RecipeRepository recipeRepository;
    private RecipeService recipeService;

    @BeforeEach
    void setUp() {
        recipeRepository = mock(RecipeRepository.class);
        recipeService = new RecipeService(recipeRepository);
    }

    @Test
    void save_callsRepositorySave() {
        Recipe recipe = new Recipe();
        recipeService.save(recipe);
        verify(recipeRepository, times(1)).save(recipe);
    }

    @Test
    void filterRecipesByWord_returnsFilteredRecipes() {
        List<Recipe> content = List.of(new Recipe(), new Recipe());
        Page<Recipe> page = new PageImpl<>(content);
        when(recipeRepository.filterRecipesByWord(eq("salad"), any(Pageable.class)))
                .thenReturn(page);

        Page<Recipe> result = recipeService.filterRecipesByWord("salad", 1);

        assertEquals(2, result.getContent().size());
        verify(recipeRepository).filterRecipesByWord(eq("salad"), any(Pageable.class));
    }

    @Test
    void findAllByPage_returnsCorrectPage() {
        List<Recipe> content = List.of(new Recipe());
        Page<Recipe> page = new PageImpl<>(content);
        when(recipeRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Recipe> result = recipeService.findAllByPage(0);

        assertEquals(1, result.getContent().size());
        verify(recipeRepository).findAll(any(Pageable.class));
    }

    @Test
    void deleteRecipeByTitle_whenRecipeExists_deletesRecipe() {
        Recipe recipe = new Recipe();
        when(recipeRepository.findByTitleIgnoreCase("Soup")).thenReturn(Optional.of(recipe));

        recipeService.deleteRecipeByTitle("Soup");

        verify(recipeRepository).delete(recipe);
    }

    @Test
    void deleteRecipeByTitle_whenRecipeNotFound_doesNothing() {
        when(recipeRepository.findByTitleIgnoreCase("Nonexistent")).thenReturn(Optional.empty());

        recipeService.deleteRecipeByTitle("Nonexistent");

        verify(recipeRepository, never()).delete(any());
    }
}
