package ee.danych.nutrimatch.entity;


import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@Table(name = "product")
public class Product {
    public Product() {
        this.elements = new ArrayList<>();
        this.productNames = new ArrayList<>();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "product_seq")
    @SequenceGenerator(name = "product_seq", sequenceName = "product_sequence", allocationSize = 1)
    private Long id;
    private String code;
    private String synonyms;
    private String foodGroup;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductName> productNames;
    private BigDecimal energy;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Element> elements;
}

