package me.hades.yqword.model;


/**
 * Created by hades on 2018/6/17.
 */

public class User {

    private Integer id;

    private String username;

    private String password;

    private String nickname;

    private String headicon;

    private Integer knowlage_word;

    private String rank_level;

    public User() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getHeadicon() {
        return headicon;
    }

    public void setHeadicon(String headicon) {
        this.headicon = headicon;
    }

    public Integer getKnowlage_word() {
        return knowlage_word;
    }

    public void setKnowlage_word(Integer knowlage_word) {
        this.knowlage_word = knowlage_word;
    }

    public String getRank_level() {
        return rank_level;
    }

    public void setRank_level(String rank_level) {
        this.rank_level = rank_level;
    }
}
