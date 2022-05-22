package com.kontrol.newsarchive.model;

import java.util.Objects;

public class NewsContent {

    private String source;
    private String title;
    private String time;

    public NewsContent(String source, String title, String time){
        this.source = source;
        this.title = title;
        this.time = time;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof NewsContent)) return false;

        NewsContent that = (NewsContent) o;

        if (!Objects.equals(source, that.source)) return false;
        if (!Objects.equals(title, that.title)) return false;
        return Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        int result = source.hashCode();
        result = 31 * result + title.hashCode();
        result = 31 * result + time.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "NewsContent{" +
                "source='" + source + '\'' +
                ", title='" + title + '\'' +
                ", time=" + time +
                '}';
    }
}
