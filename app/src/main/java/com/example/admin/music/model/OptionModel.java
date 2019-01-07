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
        readFavorite(context);

        //delete song if song exits into list
        updateFavorite(song);

        listFavorite.add(0, song);
        writeFavorite(context, context.getString(R.string.option_action_add));
    }

    public void subFavorite(Context context, Song song) {
        readFavorite(context);

        //delete song if song exits into list
        updateFavorite(song);

        writeFavorite(context, context.getString(R.string.option_action_sub));
    }

    public void delete(Context context, Song song) {
        String action = context.getString(R.string.option_action_delete);

        try {
            //delete file in media store
            context.getContentResolver().delete(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, MediaStore.MediaColumns.DATA +
                    "='" + song.getPathAudio() + "'", null);

            //delete file infact
            File file = new File(song.getPathAudio());
            file.delete();

            //update favorite
            readFavorite(context);  //get data list
            updateFavorite(song);   //delete song if song exits into list
            writeFavorite(context, "");

            //update playlist
            readPlaylist(context);  //get data playlist
            updatePlaylist(song);   //delete song if song exits into playlist
            writePlaylist(context);

            callBack.success(context, action);
        }
        catch (Exception e) {
            callBack.fail(action);
            Log.e("errors_option_delete", e.toString());
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

    private void readPlaylist(Context context) {
        listPlayList.clear();
        File file = new File(context.getFilesDir(), file_playlist);
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            listPlayList = (ArrayList<Playlist>) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void writeFavorite(Context context, String action) {
        File file = new File(context.getFilesDir(), file_favorite);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(listFavorite);

            //update list favorite in activity main
            MainActivity.listFavorite = listFavorite;

            if (!action.equals("")) {
                callBack.success(context, action);
            }
        } catch (IOException e) {
            if (!action.equals("")) {
                callBack.fail(action);
            }
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

    private void readFavorite(Context context) {
        listFavorite.clear();
        File file = new File(context.getFilesDir(), file_favorite);
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            listFavorite = (ArrayList<Song>) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
