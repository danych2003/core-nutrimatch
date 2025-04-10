package ee.danych.nutrimatch.controller;

import ee.danych.nutrimatch.entity.Product;
import ee.danych.nutrimatch.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController("/api")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping(value = "/product", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        System.out.println(product.getCode());
        this.productService.save(product);
        return new ResponseEntity<>("Success", HttpStatus.OK);
    }

//    @GetMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Product> getAll() {
//        List<Product> products = this.productService.findAll();
//        return new ResponseEntity<>(products.getFirst(), HttpStatus.OK);
//    }

    @GetMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> getByKeyWord(@RequestParam(value = "key_word") String keyWord) {
        List<Product> products = this.productService.filterProductsByWord(keyWord);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping(value = "/product/name", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Product> getAll(@RequestBody String name) {
        System.out.println(name);
        List<Product> products = this.productService.getProductByName(name);
        return new ResponseEntity<>(products.getFirst(), HttpStatus.OK);
    }

    @GetMapping(value = "/serialize", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> serialize() throws IOException {
        return productService.saveProductsFromExcel();
    }
}
