package ee.danych.nutrimatch.repository;

import ee.danych.nutrimatch.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    @Query("SELECT p FROM Product p JOIN p.productNames pn WHERE LOWER(pn.name) = LOWER(:name)")
    List<Product> findProductByName(@Param("name") String name);

    @Query("SELECT p FROM Product p JOIN p.productNames pn WHERE LOWER(pn.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Product> filterProductsByWord(@Param("name") String name, Pageable page);
}
