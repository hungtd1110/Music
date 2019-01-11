package com.example.admin.music.view.detail_playlist;

import android.content.Context;

import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Song;

import java.util.ArrayList;

/**
 * Created by admin on 1/10/2019.
 */

public interface DetailPlaylistViewListener {
    public void save(Context context, ArrayList<Song> list);

    public void success(Context context);

    public void fail(Context context);
}
