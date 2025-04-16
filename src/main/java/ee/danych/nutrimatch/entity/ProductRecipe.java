package ee.danych.nutrimatch.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
@Table(name = "product_recipe")
public class ProductRecipe {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "p_recipe_seq")
    @SequenceGenerator(name = "p_recipe_seq", sequenceName = "p_recipe_sequence", allocationSize = 1)
    private String id;

    private BigDecimal quantity;

    @OneToOne
    private Product product;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    private Recipe recipe;
}
