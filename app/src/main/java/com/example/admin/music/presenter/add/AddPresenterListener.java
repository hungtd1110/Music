package com.example.admin.music.presenter.add;

import android.content.Context;

import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Song;

import java.util.ArrayList;

/**
 * Created by admin on 1/6/2019.
 */

public interface AddPresenterListener {
    public void getData(Context context);

    public void show(ArrayList<Playlist> list);

    public void saveData(Context context, String name, Song song);
}
