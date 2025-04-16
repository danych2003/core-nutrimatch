package ee.danych.nutrimatch.service;

import ee.danych.nutrimatch.component.ProductFileSerializer;
import ee.danych.nutrimatch.entity.Product;
import ee.danych.nutrimatch.repository.ProductRepository;
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

@Service
@Slf4j
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final ProductFileSerializer rawRepository;

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
        List<Product> products = rawRepository.getProductsFromExcel();
        log.info("Serialization successfully ended started");
        productRepository.saveAll(products);
        log.info("Products successfully saved to database");
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }


}
