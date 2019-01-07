package com.example.admin.music.presenter.option;

import android.content.Context;

import com.example.admin.music.model.entity.Song;

/**
 * Created by admin on 1/6/2019.
 */

public interface OptionPresenterListener {
    public void addFavorite(Context context, Song song);

    public void subFavorite(Context context, Song song);

    public void delete(Context context, Song song);

    public void success(Context context, String action);

    public void fail(String action);
}
