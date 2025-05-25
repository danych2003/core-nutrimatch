package ee.danych.nutrimatch.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.danych.nutrimatch.components.RecipeMapper;
import ee.danych.nutrimatch.model.dto.RecipeDto;
import ee.danych.nutrimatch.model.entity.Recipe;
import ee.danych.nutrimatch.service.JWTService;
import ee.danych.nutrimatch.service.RecipeService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeController.class)
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private RecipeService recipeService;

    @MockitoBean
    private RecipeMapper recipeMapper;

    @MockitoBean
    private JWTService jwtService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser
    @DisplayName("POST /api/recipe - should create recipe")
    void addRecipe() throws Exception {
        RecipeDto dto = new RecipeDto();
        dto.setTitle("Test Recipe");
        dto.setUsername("testUser");
        dto.setDescription("Test description");
        dto.setPhotoBase64("data:image/png;base64,...");

        Recipe recipe = new Recipe();
        recipe.setTitle("Test Recipe");

        when(recipeMapper.parseDtoToEntity(dto)).thenReturn(recipe);

        mockMvc.perform(post("/api/recipe")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(content().string("Success"));

        verify(recipeService).save(recipe);
    }

    @Test
    @WithMockUser
    @DisplayName("GET /api/recipes - should return paginated recipes")
    void getRecipeWithFilters() throws Exception {
        Recipe recipe = new Recipe();
        recipe.setTitle("Test Recipe");

        RecipeDto dto = new RecipeDto();
        dto.setTitle("Test Recipe");

        Page<Recipe> page = new PageImpl<>(List.of(recipe));
        when(recipeService.findAllByPage(0)).thenReturn(page);
        when(recipeMapper.parseEntityToDto(recipe)).thenReturn(dto);

        mockMvc.perform(get("/api/recipes")
                        .param("current_page", "0"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Available-Pages", "1"))
                .andExpect(jsonPath("$[0].title").value("Test Recipe"));
    }

    @Test
    @WithMockUser
    @DisplayName("DELETE /api/recipe?name=Test - should delete recipe")
    void deleteRecipe() throws Exception {
        mockMvc.perform(delete("/api/recipe")
                        .param("name", "Test")
                        .with(csrf()))
                .andExpect(status().isAccepted())
                .andExpect(content().string("Success"));

        verify(recipeService).deleteRecipeByTitle("Test");
    }
}
