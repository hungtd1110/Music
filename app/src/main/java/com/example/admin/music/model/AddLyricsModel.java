package com.example.admin.music.model;

import com.example.admin.music.config.Data;
import com.example.admin.music.model.entity.Lyrics;
import com.example.admin.music.presenter.addlyrics.AddLyricsPresenterListener;
import com.example.admin.music.utils.APIUtils;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 1/23/2019.
 */

public class AddLyricsModel {
    private AddLyricsPresenterListener callBack;
    private ArrayList<Lyrics> list;

    public AddLyricsModel(AddLyricsPresenterListener callBack) {
        this.callBack = callBack;

        //init
        list = new ArrayList<>();
    }

    public void getData() {
        Data data = APIUtils.getData();
        Call<ArrayList<Lyrics>> call = data.getList();
        call.enqueue(new Callback<ArrayList<Lyrics>>() {
            @Override
            public void onResponse(Call<ArrayList<Lyrics>> call, Response<ArrayList<Lyrics>> response) {
                if (response.body() != null) {
                    list = response.body();
                }
                callBack.show(list);
            }

            @Override
            public void onFailure(Call<ArrayList<Lyrics>> call, Throwable t) {

            }
        });
    }
}
