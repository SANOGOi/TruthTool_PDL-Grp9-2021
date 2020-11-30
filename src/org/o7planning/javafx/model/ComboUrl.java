package org.o7planning.javafx.model;

public class ComboUrl {
    private String url;
    //private String code;

    public ComboUrl(String url) {
        this.url = url;
        //this.code = code;
    }

    public String getExtractor() {
        return this.url;
    }

    public void setExtractor(String extractor) {
        this.url = url;
    }

    /*public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }*/

    @Override
    public String toString()  {
        return this.url;
    }
}
