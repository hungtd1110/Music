package com.example.admin.music.view.addlyrics;

import com.example.admin.music.model.entity.Lyrics;

import java.util.ArrayList;

/**
 * Created by admin on 1/23/2019.
 */

public interface AddLyricsViewListener {
    public void show(ArrayList<Lyrics> list);

    public void hide();
}
