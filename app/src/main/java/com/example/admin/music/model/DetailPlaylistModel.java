package com.example.admin.music.model;

import android.content.Context;

import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.presenter.detail_playlist.DetailPlaylistPresenterListener;
import com.example.admin.music.view.main.MainActivity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

/**
 * Created by admin on 1/10/2019.
 */

public class DetailPlaylistModel {
    private DetailPlaylistPresenterListener callBack;
    private ArrayList<Playlist> list;

    private final String file_playlist = "playlist";

    public DetailPlaylistModel(DetailPlaylistPresenterListener callBack) {
        this.callBack = callBack;

        //init
        list = new ArrayList<>();
    }

    public void save(Context context, Playlist playlist) {
        list = MainActivity.listPlaylist;
        updatePlayList(playlist);
        writePlaylist(context);
    }

    private void writePlaylist(Context context) {
        File file = new File(context.getFilesDir(), file_playlist);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);

            //update list favorite in activity main
            MainActivity.listPlaylist = list;

            callBack.success(context);
        } catch (IOException e) {
            callBack.fail(context);
        }
    }

    private void updatePlayList(Playlist playlist) {
        for (int i = 0 ; i < list.size() ; i++) {
            Playlist pl = list.get(i);
            if (pl.getName().equals(pl.getName())) {
                list.remove(i);
                if (playlist.getListSong().size() > 0) {
                    list.add(0, playlist);
                }
                break;
            }
        }
    }
}
