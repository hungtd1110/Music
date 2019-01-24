package com.example.admin.music.config;

import com.example.admin.music.model.entity.Lyrics;
import com.example.admin.music.model.entity.Test;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

/**
 * Created by admin on 1/23/2019.
 */

public interface Data {
    @FormUrlEncoded
    @POST("lyrics")
    Call<Void> upload(@Field("idUser") String idUser, @Field("name") String name, @Field("singer") String singer,
                      @Field("content") String content, @Field("download") int download);

    @GET("lyrics/{id}/edit")
    Call<Void> update(@Path("id") int id);

    @GET("lyrics")
    Call<ArrayList<Lyrics>> getList();
}
