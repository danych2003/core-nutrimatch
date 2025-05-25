package ee.danych.nutrimatch.api;

import ee.danych.nutrimatch.components.RecipeMapper;
import ee.danych.nutrimatch.model.dto.RecipeDto;
import ee.danych.nutrimatch.model.entity.Recipe;
import ee.danych.nutrimatch.service.RecipeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class RecipeController {

    private final RecipeMapper recipeMapper;
    private final RecipeService recipeService;

    @PostMapping(value = "/recipe", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addRecipe(@Valid @RequestBody RecipeDto recipeDto) {
        Recipe recipe = recipeMapper.parseDtoToEntity(recipeDto);
        recipeService.save(recipe);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

    @GetMapping(value = "/recipes", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<RecipeDto>> getRecipeWithFilters(
            @RequestParam(value = "search_query", required = false, defaultValue = "") String keyWord,
            @RequestParam(value = "current_page", required = false, defaultValue = "0") int page
    ) {
        Page<Recipe> recipes;
        if(keyWord.isEmpty()) {
            recipes = this.recipeService.findAllByPage(page);
        } else {
            recipes = this.recipeService.filterRecipesByWord(keyWord, page);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Available-Pages", String.valueOf(recipes.getTotalPages()));

        List<RecipeDto> recipesResponse = recipes.getContent()
                .stream().map(recipeMapper::parseEntityToDto)
                .toList();
        return new ResponseEntity<>(recipesResponse, headers, HttpStatus.OK);
    }

    @DeleteMapping(value = "/recipe", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> deleteRecipe(@RequestParam("name") String recipeName) {
        recipeService.deleteRecipeByTitle(recipeName);
        return new ResponseEntity<>("Success", HttpStatus.ACCEPTED);
    }
}
