package com.example.admin.music.presenter.detail_search;

import android.content.Context;

import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Singer;
import com.example.admin.music.model.entity.Song;

import java.util.ArrayList;

/**
 * Created by admin on 1/11/2019.
 */

public interface DetailSearchPresenterListener {
    public void search(String word);

    public void show();

    public void save(Context context, String word);
}
