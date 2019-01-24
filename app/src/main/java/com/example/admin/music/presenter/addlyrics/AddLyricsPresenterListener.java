package com.example.admin.music.presenter.addlyrics;

import com.example.admin.music.model.entity.Lyrics;

import java.util.ArrayList;

/**
 * Created by admin on 1/23/2019.
 */

public interface AddLyricsPresenterListener {
    public void getData();

    public void show(ArrayList<Lyrics> list);
}
