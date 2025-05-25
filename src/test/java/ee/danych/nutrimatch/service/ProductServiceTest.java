package ee.danych.nutrimatch.service;

import ee.danych.nutrimatch.components.ProductFileSerializer;
import ee.danych.nutrimatch.exceptions.ProductNotFoundException;
import ee.danych.nutrimatch.model.entity.Product;
import ee.danych.nutrimatch.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    private ProductRepository productRepository;
    private ProductFileSerializer productFileSerializer;
    private ProductService productService;

    @BeforeEach
    void setUp() {
        productRepository = mock(ProductRepository.class);
        productFileSerializer = mock(ProductFileSerializer.class);
        productService = new ProductService(productFileSerializer, productRepository);
    }

    @Test
    void getProductByName_returnsCorrectProducts() {
        List<Product> expected = List.of(new Product(), new Product());
        when(productRepository.findProductByName("milk")).thenReturn(expected);

        List<Product> actual = productService.getProductByName("milk");

        assertEquals(expected, actual);
    }

    @Test
    void save_callsRepositorySave() {
        Product product = new Product();
        productService.save(product);

        verify(productRepository, times(1)).save(product);
    }

    @Test
    void findAll_returnsAllProducts() {
        List<Product> expected = List.of(new Product(), new Product());
        when(productRepository.findAll()).thenReturn(expected);

        List<Product> actual = productService.findAll();

        assertEquals(expected, actual);
    }

    @Test
    void findAllByPage_returnsCorrectPage() {
        List<Product> content = List.of(new Product(), new Product());
        Page<Product> page = new PageImpl<>(content);
        when(productRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Product> result = productService.findAllByPage(0);

        assertEquals(2, result.getContent().size());
        assertEquals(content, result.getContent());
    }

    @Test
    void filterProductsByWord_returnsFilteredProducts() {
        List<Product> content = List.of(new Product());
        Page<Product> page = new PageImpl<>(content);
        when(productRepository.filterProductsByWord(eq("cheese"), any(Pageable.class))).thenReturn(page);

        Page<Product> result = productService.filterProductsByWord("cheese", 1);

        assertEquals(1, result.getContent().size());
    }

    @Test
    void saveProductsFromExcel_savesProductsCorrectly() throws IOException {
        List<Product> products = List.of(new Product(), new Product());
        when(productFileSerializer.getProductsFromExcel()).thenReturn(products);

        var response = productService.saveProductsFromExcel();

        verify(productFileSerializer).getProductsFromExcel();
        verify(productRepository).saveAll(products);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Success", response.getBody());
    }

    @Test
    void getProductById_existingId_returnsProduct() {
        Product product = new Product();
        when(productRepository.getProductById(1L)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);

        assertEquals(product, result);
    }

    @Test
    void getProductById_nonExistingId_throwsException() {
        when(productRepository.getProductById(999L)).thenReturn(Optional.empty());

        assertThrows(ProductNotFoundException.class, () -> productService.getProductById(999L));
    }

    @Test
    void getProductById_list_returnsListWithNullsForMissing() {
        Product product1 = new Product();
        product1.setId(1L);
        when(productRepository.findById(1L)).thenReturn(Optional.of(product1));
        when(productRepository.findById(2L)).thenReturn(Optional.empty());

        List<Product> result = productService.getProductById(List.of(1L, 2L));

        assertEquals(2, result.size());
        assertEquals(product1, result.get(0));
        assertNull(result.get(1));
    }
}
