package ee.danych.nutrimatch.exceptions;

import lombok.Getter;

@Getter
public class XlsxFileNotFoundException extends RuntimeException {

    private final String filePath;

    public XlsxFileNotFoundException(String filePath) {
        super("File with xlsx extension not found");
        this.filePath = filePath;
    }
}
