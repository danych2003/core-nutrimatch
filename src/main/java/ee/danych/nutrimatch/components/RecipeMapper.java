package ee.danych.nutrimatch.components;

import ee.danych.nutrimatch.model.dto.ProductRecipeRequest;
import ee.danych.nutrimatch.model.dto.RecipeDto;
import ee.danych.nutrimatch.model.entity.Product;
import ee.danych.nutrimatch.model.entity.ProductRecipe;
import ee.danych.nutrimatch.model.entity.Recipe;
import ee.danych.nutrimatch.model.entity.User;
import ee.danych.nutrimatch.service.ProductService;
import ee.danych.nutrimatch.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class RecipeMapper {

    private final UserService userService;
    private final ProductService productService;

    public Recipe parseDtoToEntity(RecipeDto recipeDto) {
        User user = userService.findUser(recipeDto.getUsername());
        Recipe recipe = new Recipe();
        recipe.setUser(user);
        recipe.setTitle(recipeDto.getTitle());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setAvatarBase64(recipeDto.getPhotoBase64());
        List<ProductRecipe> products = recipe.getProducts();
        recipeDto.getProducts()
                .forEach((product) ->
                        products.add(createProductRecipeFromRequest(product, recipe)
                        ));
        return recipe;
    }

    public RecipeDto parseEntityToDto(Recipe recipe) {
        RecipeDto recipeDto = new RecipeDto();
        recipeDto.setUsername(recipe.getUser().getUsername());
        recipeDto.setTitle(recipe.getTitle());
        recipeDto.setDescription(recipe.getDescription());
        recipeDto.setPhotoBase64(recipe.getAvatarBase64());
        List<ProductRecipeRequest> products = recipeDto.getProducts();
        recipe.getProducts()
                .forEach((product) ->
                        products.add(createProductRecipeFromEntity(product)
                        ));
        return recipeDto;
    }

    private ProductRecipe createProductRecipeFromRequest(ProductRecipeRequest productRecipeRequest, Recipe recipe) {
        Product product = productService.getProductById(productRecipeRequest.getProductId());

        return ProductRecipe.builder()
                .recipe(recipe)
                .quantity(BigDecimal.valueOf(productRecipeRequest.getQuantity()))
                .product(product)
                .build();
    }

    private ProductRecipeRequest createProductRecipeFromEntity(ProductRecipe productRecipe) {
        return ProductRecipeRequest.builder()
                .productId(productRecipe.getProduct().getId())
                .quantity(productRecipe.getQuantity().intValue())
                .build();
    }
}
