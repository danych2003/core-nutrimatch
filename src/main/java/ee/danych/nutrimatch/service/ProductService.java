package ee.danych.nutrimatch.service;

import ee.danych.nutrimatch.exceptions.ProductNotFoundException;
import ee.danych.nutrimatch.model.entity.Product;
import ee.danych.nutrimatch.repository.ProductRepository;
import ee.danych.nutrimatch.util.ProductFileSerializer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    private static final int PAGE_SIZE = 30;


    public List<Product> getProductByName(String name) {
        return this.productRepository.findProductByName(name);
    }

    public void save(Product product) {
        this.productRepository.save(product);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Page<Product> findAllByPage(int page) {
        Pageable pageable = PageRequest.of(page, PAGE_SIZE);
        return productRepository.findAll(pageable);
    }

    public Page<Product> filterProductsByWord(String word, int page) {
        Pageable pageable = PageRequest.of(page, 30);
        return productRepository.filterProductsByWord(word, pageable);
    }

    public ResponseEntity<String> saveProductsFromExcel() throws IOException {
        log.info("Serialization process started");
        List<Product> products = ProductFileSerializer.getProductsFromExcel();
        log.info("Serialization successfully ended started");
        productRepository.saveAll(products);
        log.info("Products successfully saved to database");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }


    public Product getProductById(Long id) {
        return productRepository.getProductById(id)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }

    public List<Product> getProductById(List<Long> ids) {
        return ids.stream()
                .map(id -> productRepository.findById(id).orElse(null))
                .collect(Collectors.toList());
    }
}
