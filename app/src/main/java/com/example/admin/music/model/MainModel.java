package com.example.admin.music.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Singer;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.main.MainPresenterListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

/**
 * Created by admin on 1/5/2019.
 */

public class MainModel {
    private MainPresenterListener callBack;
    private ArrayList<Song> listSong;
    private ArrayList<Song> listFavorite;
    private ArrayList<Singer> listSinger;
    private ArrayList<Playlist> listPlaylist;
    private String file_playlist = "playlist", file_favorite = "favorite";

    public MainModel(MainPresenterListener callBack) {
        this.callBack = callBack;

        //init
        listSong = new ArrayList<>();
        listFavorite = new ArrayList<>();
        listSinger = new ArrayList<>();
        listPlaylist = new ArrayList<>();
    }

    public void getData(Context context) {
        getListSong(context);
        getListFavorite(context);
        getListSinger();
        getListPlayList(context);

        callBack.show(listSong, listFavorite, listSinger, listPlaylist);
    }

    private void getListPlayList(Context context) {
        listPlaylist.clear();
        File file = new File(context.getFilesDir(), file_playlist);
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            listPlaylist = (ArrayList<Playlist>) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getListSinger() {
        //get list name singer
        ArrayList<String> listNameSinger = new ArrayList<>();
        for (Song song : listSong) {
            String nameSinger = song.getSinger();
            boolean check = false;

            //check name singer
            for (String name : listNameSinger) {
                if (nameSinger.equals(name)) {
                    check = true;
                    break;
                }
            }

            if (!check) {
                listNameSinger.add(nameSinger);
            }
        }

        //get list singer
        for (String name : listNameSinger) {
            Singer singer = new Singer();
            singer.setName(name);

            ArrayList<Song> list = new ArrayList<>();
            for (Song song : listSong) {
                if (song.getSinger().equals(name)) {
                    list.add(song);
                }
            }

            singer.setListSong(list);
            listSinger.add(singer);
        }
    }

    private void getListFavorite(Context context) {
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

    private void getListSong(Context context) {
        listSong.clear();
        ContentResolver contentResolver = context.getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri, null, null, null, null);
        while (cursor != null && cursor.moveToNext()) {
            int name = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int singer = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int pathAudio = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);

            Song song = new Song();
            song.setName(cursor.getString(name));
            song.setSinger(cursor.getString(singer));
            song.setPathAudio(cursor.getString(pathAudio));

            listSong.add(song);
        }
    }
}
