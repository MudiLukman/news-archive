package com.kontrol.newsarchive.model;

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

        if (source != null ? !source.equals(that.source) : that.source != null) return false;
        if (title != null ? !title.equals(that.title) : that.title != null) return false;
        return time != null ? time.equals(that.time) : that.time == null;
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
