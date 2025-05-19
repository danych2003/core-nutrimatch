package ee.danych.nutrimatch.api;

import ee.danych.nutrimatch.model.entity.Product;
import ee.danych.nutrimatch.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @PostMapping(value = "/product", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> addProduct(@RequestBody Product product) {
        this.productService.save(product);
        return new ResponseEntity<>("Success", HttpStatus.CREATED);
    }

//    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<Product> getAll() {
//        List<Product> products = this.productService.findAll();
//        return new ResponseEntity<>(products.getFirst(), HttpStatus.OK);
//    }

    @GetMapping(value = "/product", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> getProductWithFilters(
            @RequestParam(value = "search_query", required = false, defaultValue = "") String keyWord,
            @RequestParam(value = "current_page", required = false, defaultValue = "0") int page
    ) {
        Page<Product> products;
        if(keyWord.isEmpty()) {
            products = this.productService.findAllByPage(page);
        } else {
            products = this.productService.filterProductsByWord(keyWord, page);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("X-Available-Pages", String.valueOf(products.getTotalPages()));
        return new ResponseEntity<>(products.getContent(), headers, HttpStatus.OK);
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

    @GetMapping(value = "/test", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> test() throws IOException {
        return new ResponseEntity<>("Data", HttpStatus.OK);
    }

    @GetMapping(value = "/products", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<Product>> findRecipesWithIds(@RequestParam("ids") String ids) throws IOException {
        List<Long> productIds = Arrays.stream(ids.split(","))
                .map(Long::parseLong)
                .toList();
        List<Product> products = productService.getProductById(productIds);
        return new ResponseEntity<>(products, HttpStatus.OK);
    }
}
