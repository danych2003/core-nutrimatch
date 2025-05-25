package ee.danych.nutrimatch.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import ee.danych.nutrimatch.model.entity.Element;
import ee.danych.nutrimatch.model.entity.Product;
import ee.danych.nutrimatch.model.entity.ProductName;
import ee.danych.nutrimatch.model.enums.Language;
import ee.danych.nutrimatch.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private static final String PRODUCT_CODE = "test-code";
    private static final int PRODUCT_ENERGY = 1234;
    private static final String PRODUCT_GROUP = "test-group";
    private static final Map<Language, String> PRODUCT_NAMES = Map.of(
            Language.EN, "en-name",
            Language.LV, "lv-name",
            Language.RU, "ru-name",
            Language.ET, "et-name"
    );
    private static final Map<String, Integer> PRODUCT_ELEMENTS = Map.of(
            "test-element1", 1234,
            "test-element2", 4321
    );

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(productController).build();
    }

    @Test
    void getProductWithFilters_withoutKeyword() throws Exception {
        Page<Product> mockPage = new PageImpl<>(List.of(prepareTestProduct()));
        when(productService.findAllByPage(0)).thenReturn(mockPage);

        mockMvc.perform(get("/api/product")
                        .param("current_page", "0"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Available-Pages", "1"));
    }

    @Test
    void getProductWithFilters_withKeyword() throws Exception {
        Page<Product> mockPage = new PageImpl<>(List.of(prepareTestProduct()));
        when(productService.filterProductsByWord("milk", 0)).thenReturn(mockPage);

        mockMvc.perform(get("/api/product")
                        .param("search_query", "milk")
                        .param("current_page", "0"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Available-Pages", "1"));
    }

    @Test
    void addProduct() throws Exception {
        Product product = prepareTestProduct();
        String json = objectMapper.writeValueAsString(product);

        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());


        verify(productService, times(1)).save(product);
    }

    @Test
    void serializeProducts() throws Exception {
        when(productService.saveProductsFromExcel()).thenReturn(ResponseEntity.ok("Serialized"));

        mockMvc.perform(get("/api/serialize"))
                .andExpect(status().isOk());
    }

    @Test
    void testEndpointReturnsData() throws Exception {
        mockMvc.perform(get("/api/test"))
                .andExpect(status().isOk())
                .andExpect(content().string("Data"));
    }

    @Test
    void findRecipesWithIds() throws Exception {
        Product product = prepareTestProduct();
        when(productService.getProductById(List.of(1L, 2L))).thenReturn(List.of(product, product));

        mockMvc.perform(get("/api/products")
                        .param("ids", "1,2"))
                .andExpect(status().isOk());
    }

    private Product prepareTestProduct() {
        List<ProductName> productNames = prepareProductNames();

        return Product.builder()
                .energy(BigDecimal.valueOf(PRODUCT_ENERGY))
                .foodGroup(PRODUCT_GROUP)
                .code(PRODUCT_CODE)
                .productNames(productNames)
                .elements(prepareElements())
                .build();
    }

    private List<Element> prepareElements() {
        return PRODUCT_ELEMENTS.entrySet()
                .stream().map((entry) ->
                        Element.builder()
                                .name(entry.getKey())
                                .quantity(BigDecimal.valueOf(entry.getValue()))
                                .build())
                .toList();
    }

    private List<ProductName> prepareProductNames() {
        return PRODUCT_NAMES.entrySet().stream()
                .map((entry) ->
                        ProductName.builder()
                        .language(entry.getKey())
                        .name(entry.getValue())
                                .build()).toList();
    }
}