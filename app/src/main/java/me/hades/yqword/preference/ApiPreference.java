package me.hades.yqword.preference;

import java.util.List;

import me.hades.yqword.model.NewsModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * Created by hades on 2018/6/12.
 *
 * Api获取
 */

public interface ApiPreference {

    @GET("/news/{page}")
    Call<List<NewsModel>> listNews(@Path("page") Integer page);

    @GET("/user/{username}")
    Call<List<>>
}
