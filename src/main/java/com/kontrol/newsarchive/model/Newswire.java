package com.kontrol.newsarchive.model;


public class Newswire {

    private String url;

    public Newswire(String url){
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "Newswire{" +
                "url='" + url + '\'' +
                '}';
    }
}
