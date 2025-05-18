package ee.danych.nutrimatch.util;

import ee.danych.nutrimatch.model.entity.Product;
import ee.danych.nutrimatch.model.enums.Allergen;

import java.util.ArrayList;
import java.util.List;

//TODO Add allergens on backend side instead of frontend
public final class AllergensFinder {
    private AllergensFinder() {}

    public static List<Allergen> findAllergens(Product product) {
        List<Allergen> allergens = new ArrayList<>();

        return allergens;
    }

    private static boolean findFish(Product products) {

        return true;
    }

    private static boolean findByFilters(Product product) {

        return true;
    }
}
