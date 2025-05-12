package ee.danych.nutrimatch.components;

import ee.danych.nutrimatch.model.dto.ProductRecipeRequest;
import ee.danych.nutrimatch.model.dto.RequestRecipe;
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

    public Recipe parseRequestToEntity(RequestRecipe requestRecipe) {
        User user = userService.findUser(requestRecipe.getUsername());
        Recipe recipe = new Recipe();
        recipe.setUser(user);
        recipe.setTitle(requestRecipe.getTitle());
        recipe.setDescription(requestRecipe.getDescription());
        List<ProductRecipe> products = recipe.getProducts();
        requestRecipe.getProducts()
                .forEach((product) ->
                        products.add(createProductRecipeFromRequest(product, recipe)
                        ));
        return recipe;
    }

    private ProductRecipe createProductRecipeFromRequest(ProductRecipeRequest productRecipeRequest, Recipe recipe) {
        Product product = productService.getProductById(productRecipeRequest.getProductId());

        return ProductRecipe.builder()
                .recipe(recipe)
                .quantity(BigDecimal.valueOf(productRecipeRequest.getQuantity()))
                .product(product)
                .build();
    }
}
