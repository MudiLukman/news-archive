package com.kontrol.newsarchive.model;

public class OldUrl {

    private String url;
    private String date;

    public OldUrl(String url, String date){
        this.url = url;
        this.date = date;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "OldUrl{" +
                "url='" + url + '\'' +
                ", date=" + date +
                '}';
    }
}
