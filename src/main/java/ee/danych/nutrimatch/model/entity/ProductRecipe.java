package ee.danych.nutrimatch.model.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_recipe")
public class ProductRecipe extends BaseEntity {

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
