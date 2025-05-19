package ee.danych.nutrimatch.model.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import ee.danych.nutrimatch.model.enums.Language;
import ee.danych.nutrimatch.model.enums.converter.LanguageConverter;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = "product")
@Builder
@Table(name = "product_name")
public class ProductName {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "p_name_seq")
    @SequenceGenerator(name = "p_name_seq", sequenceName = "p_name_seq", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id")
    @JsonBackReference
    private Product product;

    @Convert(converter = LanguageConverter.class)
    private Language language;
    private String name;
}
