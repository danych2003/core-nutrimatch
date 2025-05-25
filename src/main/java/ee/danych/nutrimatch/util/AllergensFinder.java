package ee.danych.nutrimatch.util;

import ee.danych.nutrimatch.model.entity.Product;
import ee.danych.nutrimatch.model.enums.Allergen;
import org.apache.commons.lang3.NotImplementedException;

import java.util.List;

//TODO Add allergens on backend side instead of frontend
public final class AllergensFinder {
    private AllergensFinder() {}

    public static List<Allergen> findAllergens(Product product) {
        throw new NotImplementedException();
    }

    private static boolean findFish(Product products) {
        throw new NotImplementedException();
    }

    private static boolean findByFilters(Product product) {
        throw new NotImplementedException();
    }
}
