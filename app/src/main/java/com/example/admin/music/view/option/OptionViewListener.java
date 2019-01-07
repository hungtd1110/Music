package com.example.admin.music.view.option;

import android.content.Context;

import com.example.admin.music.model.entity.Song;

/**
 * Created by admin on 1/6/2019.
 */

public interface OptionViewListener {
    public void hide();

    public void addFavorite(Song song);

    public void subFavorite(Song song);

    public void delete(Context context, Song song);

    public void success(Context context, String action);

    public void fail(String action);
}
