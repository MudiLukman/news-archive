package com.kontrol.newsarchive.model;

public class Keyword {

    private String value;

    public Keyword(){
        value = "NNPC";
    }

    public Keyword(String value){
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "Keyword{" +
                "value='" + value + '\'' +
                '}';
    }
}
