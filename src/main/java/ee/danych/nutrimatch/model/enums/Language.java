package ee.danych.nutrimatch.model.enums;

public enum Language {
    RU,
    EN,
    ET,
    LV;

    public static Language fromString(String language) {
        for (Language lang: Language.values()) {
            if(lang.toString().equalsIgnoreCase(language)) {
                return lang;
            }
        }
        throw new IllegalArgumentException("Invalid Language: " + language);
    }
}
