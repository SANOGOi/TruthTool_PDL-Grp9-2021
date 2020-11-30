package org.o7planning.javafx.model;

public class ComboExtractor {

    private String extractor;
    private String code;

    public ComboExtractor(String code, String extractor) {
        this.extractor = extractor;
        this.code = code;
    }

    public String getExtractor() {
        return this.extractor;
    }

    public void setExtractor(String extractor) {
        this.extractor = extractor;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    @Override
    public String toString()  {
        return this.extractor;
    }
}
