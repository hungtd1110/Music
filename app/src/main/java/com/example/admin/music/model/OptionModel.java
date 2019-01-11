package com.example.admin.music.model;

import android.content.Context;
import android.provider.MediaStore;
import android.util.Log;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.option.OptionPresenterListener;
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
 * Created by admin on 1/6/2019.
 */

public class OptionModel {
    private OptionPresenterListener callBack;
    private ArrayList<Song> listFavorite;
    private ArrayList<Playlist> listPlayList;

    private final String file_favorite = "favorite", file_playlist = "playlist";

    public OptionModel(OptionPresenterListener callBack) {
        this.callBack = callBack;

        //init
        listFavorite = new ArrayList<>();
        listPlayList = new ArrayList<>();
    }

    public void addFavorite(Context context, Song song) {
        listFavorite = MainActivity.listFavorite;

        //delete song if song exits into list
        updateFavorite(song);

        listFavorite.add(0, song);
        writeFavorite(context);

        callBack.success(context, context.getString(R.string.option_action_add));
    }

    public void subFavorite(Context context, Song song) {
        listFavorite = MainActivity.listFavorite;

        //delete song if song exits into list
        updateFavorite(song);

        writeFavorite(context);

        callBack.success(context, context.getString(R.string.option_action_sub));
    }

    public void deleteSong(Context context, Song song) {
        //delete file in media store
        context.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MediaStore.MediaColumns.DATA +
                "='" + song.getPathAudio() + "'", null);

        //delete file infact
        File file = new File(song.getPathAudio());
        file.delete();

        //update favorite
        listFavorite = MainActivity.listFavorite;
        updateFavorite(song);   //delete song if song exits into list
        writeFavorite(context);

        //update playlist
        listPlayList = MainActivity.listPlaylist;
        updatePlaylist(song);   //delete song if song exits into playlist
        writePlaylist(context);

        callBack.success(context, context.getString(R.string.option_action_deletesong));
    }

    public void edit(Context context, Playlist playlist, String name) {
        boolean check = false;
        listPlayList = MainActivity.listPlaylist;

        //check name
        for (Playlist pl : listPlayList) {
            if (pl.getName().equals(name)) {
                callBack.fail(context, context.getString(R.string.option_action_edit));
                check = true;
                break;
            }
        }

        if (!check) {
            handleEdit(playlist, name);
            writePlaylist(context);
            callBack.success(context, context.getString(R.string.option_action_edit));
        }
    }

    public void deletePlaylist(Context context, Playlist playlist) {
        listPlayList = MainActivity.listPlaylist;
        handleDelete(playlist);
        writePlaylist(context);

        callBack.success(context, context.getString(R.string.option_action_deleteplaylist));
    }

    private void handleDelete(Playlist playlist) {
        for (int i = 0 ; i < listPlayList.size() ; i++) {
            Playlist pl = listPlayList.get(i);
            if (playlist.getName().equals(pl.getName())) {
                listPlayList.remove(i);
                break;
            }
        }
    }

    private void handleEdit(Playlist playlist, String name) {
        for (int i = 0 ; i < listPlayList.size() ; i++) {
            Playlist pl = listPlayList.get(i);
            if (playlist.getName().equals(pl.getName())) {
                listPlayList.remove(i);
                playlist.setName(name);
                listPlayList.add(0, playlist);
                break;
            }
        }
    }

    private void writePlaylist(Context context) {
        File file = new File(context.getFilesDir(), file_playlist);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(listPlayList);

            //update list favorite in activity main
            MainActivity.listPlaylist = listPlayList;
        } catch (IOException e) {

        }
    }

    private void updatePlaylist(Song song) {
        for (int i = 0 ; i < listPlayList.size() ; i++) {
            ArrayList<Song> list = listPlayList.get(i).getListSong();
            for (int j = 0 ; j < list.size() ; j++) {
                Song s = list.get(j);
                if (song.getName().equals(s.getName()) && song.getSinger().equals(s.getSinger())) {
                    list.remove(j);

                    if (list.size() == 0) {
                        //delete playlist if size = 0
                        listPlayList.remove(i);
                    }
                    else {
                        //update playlist
                        listPlayList.get(i).setListSong(list);
                    }
                    break;
                }
            }
        }
    }

    private void writeFavorite(Context context) {
        File file = new File(context.getFilesDir(), file_favorite);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(listFavorite);

            //update list favorite in activity main
            MainActivity.listFavorite = listFavorite;
        } catch (IOException e) {

        }
    }

    private void updateFavorite(Song song) {
        for (int i = 0 ; i < listFavorite.size() ; i++) {
            Song s = listFavorite.get(i);
            if (song.getName().equals(s.getName()) && song.getSinger().equals(s.getSinger())) {
                listFavorite.remove(i);
                break;
            }
        }
    }
}
