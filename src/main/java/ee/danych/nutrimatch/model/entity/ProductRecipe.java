package ee.danych.nutrimatch.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "product_recipe")
public class ProductRecipe extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "p_recipe_seq")
    @SequenceGenerator(name = "p_recipe_seq", sequenceName = "p_recipe_sequence", allocationSize = 1)
    private Long id;

    @NotNull
    private BigDecimal quantity;

    @OneToOne
    @NotNull
    private Product product;

    @ManyToOne
    @JoinColumn(name = "recipe_id")
    @NotNull
    private Recipe recipe;
}
