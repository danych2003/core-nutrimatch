package ee.danych.nutrimatch.repository;

import ee.danych.nutrimatch.model.entity.Recipe;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT r FROM Recipe r WHERE LOWER(r.title) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Recipe> filterRecipesByWord(@Param("name") String name, Pageable page);

    @Modifying
    @Transactional
    @Query("DELETE FROM Recipe r WHERE LOWER(r.title) = LOWER(:title)")
    void deleteByTitle(@Param("title") String title);

    Optional<Recipe> findByTitleIgnoreCase(String title);
}
