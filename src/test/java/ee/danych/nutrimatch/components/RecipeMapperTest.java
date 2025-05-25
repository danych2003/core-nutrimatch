package ee.danych.nutrimatch.components;

import ee.danych.nutrimatch.model.dto.ProductRecipeRequest;
import ee.danych.nutrimatch.model.dto.RecipeDto;
import ee.danych.nutrimatch.model.entity.Product;
import ee.danych.nutrimatch.model.entity.ProductRecipe;
import ee.danych.nutrimatch.model.entity.Recipe;
import ee.danych.nutrimatch.model.entity.User;
import ee.danych.nutrimatch.service.ProductService;
import ee.danych.nutrimatch.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RecipeMapperTest {

    private UserService userService;
    private ProductService productService;
    private RecipeMapper recipeMapper;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        productService = mock(ProductService.class);
        recipeMapper = new RecipeMapper(userService, productService);
    }

    @Test
    void parseDtoToEntity() {
        String username = "john";
        User user = new User();
        user.setUsername(username);

        Product product = new Product();
        product.setId(1L);

        RecipeDto dto = new RecipeDto();
        dto.setUsername(username);
        dto.setTitle("Salad");
        dto.setDescription("Healthy");
        dto.setPhotoBase64("base64image");

        ProductRecipeRequest request = ProductRecipeRequest.builder()
                .productId(1L)
                .quantity(2)
                .build();
        dto.getProducts().add(request);

        when(userService.findUser(username)).thenReturn(user);
        when(productService.getProductById(1L)).thenReturn(product);

        Recipe entity = recipeMapper.parseDtoToEntity(dto);

        assertEquals(username, entity.getUser().getUsername());
        assertEquals("Salad", entity.getTitle());
        assertEquals("Healthy", entity.getDescription());
        assertEquals("base64image", entity.getAvatarBase64());
        assertEquals(1, entity.getProducts().size());

        ProductRecipe pr = entity.getProducts().get(0);
        assertEquals(product, pr.getProduct());
        assertEquals(new BigDecimal(2), pr.getQuantity());
        assertEquals(entity, pr.getRecipe());
    }

    @Test
    void parseEntityToDto() {
        User user = new User();
        user.setUsername("john");

        Product product = new Product();
        product.setId(1L);

        ProductRecipe pr = ProductRecipe.builder()
                .product(product)
                .quantity(new BigDecimal(2))
                .build();

        Recipe entity = new Recipe();
        entity.setUser(user);
        entity.setTitle("Salad");
        entity.setDescription("Healthy");
        entity.setAvatarBase64("base64image");
        entity.getProducts().add(pr);

        RecipeDto dto = recipeMapper.parseEntityToDto(entity);

        assertEquals("john", dto.getUsername());
        assertEquals("Salad", dto.getTitle());
        assertEquals("Healthy", dto.getDescription());
        assertEquals("base64image", dto.getPhotoBase64());
        assertEquals(1, dto.getProducts().size());

        ProductRecipeRequest request = dto.getProducts().get(0);
        assertEquals(1L, request.getProductId());
        assertEquals(2, request.getQuantity());
    }
}
