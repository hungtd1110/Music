package com.example.admin.music.presenter.detail_playlist;

import android.content.Context;

import com.example.admin.music.model.entity.Playlist;

/**
 * Created by admin on 1/10/2019.
 */

public interface DetailPlaylistPresenterListener {
    public void save(Context context, Playlist playlist);

    public void success(Context context);

    public void fail(Context context);
}
