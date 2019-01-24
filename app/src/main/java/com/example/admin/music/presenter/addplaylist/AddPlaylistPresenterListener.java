package com.example.admin.music.presenter.addplaylist;

import android.content.Context;

import com.example.admin.music.model.entity.Song;

/**
 * Created by admin on 1/6/2019.
 */

public interface AddPlaylistPresenterListener {
    public void show();

    public void saveData(Context context, String name, Song song);

    public void success(Context context);

    public void fail(Context context);
}
