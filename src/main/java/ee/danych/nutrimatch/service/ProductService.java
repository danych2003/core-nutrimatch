package ee.danych.nutrimatch.service;

import ee.danych.nutrimatch.entity.Product;
import ee.danych.nutrimatch.repository.ProductRepository;
import ee.danych.nutrimatch.repository.RawProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final RawProductRepository rawRepository;


    public List<Product> getProductByName(String name) {
        return this.productRepository.findProductByName(name);
    }

    public void save(Product product) {
        this.productRepository.save(product);
    }

    public List<Product> findAll() {
        return this.productRepository.findAll();
    }

    public List<Product> filterProductsByWord(String word) {
        return productRepository.filterProductsByWord(word);
    }

    public ResponseEntity<String> saveProductsFromExcel() throws IOException {
        List<Product> products = rawRepository.getProductsFromExcel();
        productRepository.saveAll(products);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }


}
