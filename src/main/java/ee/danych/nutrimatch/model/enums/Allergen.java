package ee.danych.nutrimatch.model.enums;

public enum Allergen {
    fish;


    public static Allergen fromString(String allergen) {
        for (Allergen allerg: Allergen.values()) {
            if(allerg.toString().equalsIgnoreCase(allergen)) {
                return allerg;
            }
        }
        throw new IllegalArgumentException("Invalid Allergen: " + allergen);
    }
}
