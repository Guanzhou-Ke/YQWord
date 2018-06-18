package me.hades.yqword.preference;

import java.util.List;

import me.hades.yqword.model.Message;
import me.hades.yqword.model.NewsModel;
import me.hades.yqword.model.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by hades on 2018/6/12.
 *
 * Api获取
 */

public interface ApiPreference {

    @GET("/news/{page}")
    Call<List<NewsModel>> listNews(@Path("page") Integer page);

    @GET("/user/{username}")
    Call<User> getUserInfo(@Path("username") String username, @Query("token") String token);

    @POST("/user")
    @FormUrlEncoded
    Call<Message> addUser(@Field("username") String username, @Field("password") String password);

    @POST("/login/{username}")
    @FormUrlEncoded
    Call<Message> login(@Path("username") String username, @Field("password") String password);

    @POST("/logout/{username}")
    @FormUrlEncoded
    Call<Message> logout(@Path("username") String username, @Field("token") String token);
}



