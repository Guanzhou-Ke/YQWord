package me.hades.yqword.model;

/**
 * Created by hades on 2018/6/11.
 * 新闻
 */

public class NewsModel {

    private String title;
    private String link;
    private String brief;
    private String source;
    private String detetime;
    private String keywords;

    public NewsModel() {

    }

    public NewsModel(String title, String link, String breif, String source, String detetime, String keywords) {
        this.title = title;
        this.link = link;
        this.brief = breif;
        this.source = source;
        this.detetime = detetime;
        this.keywords = keywords;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getBrief() {
        return brief;
    }

    public void setBrief(String breif) {
        this.brief = breif;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDetetime() {
        return detetime;
    }

    public void setDetetime(String detetime) {
        this.detetime = detetime;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }
}
