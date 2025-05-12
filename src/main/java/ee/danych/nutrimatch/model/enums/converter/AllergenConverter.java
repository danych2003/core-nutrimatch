package ee.danych.nutrimatch.model.enums.converter;

import ee.danych.nutrimatch.model.enums.Allergen;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AllergenConverter implements AttributeConverter<Allergen, String> {

    @Override
    public String convertToDatabaseColumn(Allergen attribute) {
        return attribute == null ? null : attribute.name();
    }

    @Override
    public Allergen convertToEntityAttribute(String dbData) {
        return dbData == null ? null : Allergen.fromString(dbData);
    }
}
