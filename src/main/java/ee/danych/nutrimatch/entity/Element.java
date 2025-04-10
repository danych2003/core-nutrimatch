package ee.danych.nutrimatch.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Element {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "element_seq")
    @SequenceGenerator(name = "element_seq", sequenceName = "element_sequence", allocationSize = 1)
    private Long id;

    private String name;
    private BigDecimal quantity;

}
