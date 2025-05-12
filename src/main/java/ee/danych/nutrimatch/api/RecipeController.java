package ee.danych.nutrimatch.api;

import ee.danych.nutrimatch.components.RecipeMapper;
import ee.danych.nutrimatch.model.dto.RequestRecipe;
import ee.danych.nutrimatch.model.entity.Recipe;
import ee.danych.nutrimatch.repository.RecipeRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeMapper recipeMapper;
    private final RecipeRepository recipeRepository;

    @PostMapping(value = "/recipe", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addRecipe(@Valid @RequestBody RequestRecipe requestRecipe) {
        Recipe recipe = recipeMapper.parseRequestToEntity(requestRecipe);
        recipeRepository.save(recipe);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }


}
