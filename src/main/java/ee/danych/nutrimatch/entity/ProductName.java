package ee.danych.nutrimatch.entity;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "product_name")
public class ProductName {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "p_name_seq")
    @SequenceGenerator(name = "p_name_seq", sequenceName = "p_name_seq", allocationSize = 1)
    private Long id;
    private String language;
    private String name;
}
