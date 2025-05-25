package ee.danych.nutrimatch.exceptions;

public class RecipeNotFoundException extends RuntimeException {
    public RecipeNotFoundException(String recipeTitle) {
        super("Recipe with title " + recipeTitle + " not found");
    }
}
