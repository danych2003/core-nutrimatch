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
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
    void addProduct() throws Exception {
        Product product = prepareTestProduct();
        String json = objectMapper.writeValueAsString(product);

        mockMvc.perform(post("/api/product")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());


        verify(productService, times(1)).save(product);
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