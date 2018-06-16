package me.hades.yqword.model;

/**
 * Created by hades on 2018/6/11.
 * 新闻
 */

public class NewsModel {

    private Integer id;
    private String title;
    private String link;
    private String brief;
    private String source;
    private String datetime;
    private String keywords;

    public NewsModel(Integer id, String title, String link, String brief, String source, String datetime, String keywords) {
        this.id = id;
        this.title = title;
        this.link = link;
        this.brief = brief;
        this.source = source;
        this.datetime = datetime;
        this.keywords = keywords;
    }

    public NewsModel() {

    }

    public NewsModel(String title, String link, String breif, String source, String datetime, String keywords) {
        this.title = title;
        this.link = link;
        this.brief = breif;
        this.source = source;
        this.datetime = datetime;
        this.keywords = keywords;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
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

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    @Override
    public String toString() {
        return "NewsModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", link='" + link + '\'' +
                ", brief='" + brief + '\'' +
                ", source='" + source + '\'' +
                ", datetime='" + datetime + '\'' +
                ", keywords='" + keywords + '\'' +
                '}';
    }
}
