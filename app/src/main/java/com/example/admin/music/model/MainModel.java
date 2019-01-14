package com.example.admin.music.model;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Singer;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.main.MainPresenterListener;
import com.example.admin.music.view.main.MainActivity;

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
    private ArrayList<Song> listLyrics;

    private final String file_playlist = "playlist", file_favorite = "favorite", file_lyrics = "lyrics";

    public MainModel(MainPresenterListener callBack) {
        this.callBack = callBack;

        //init
        listSong = new ArrayList<>();
        listFavorite = new ArrayList<>();
        listSinger = new ArrayList<>();
        listPlaylist = new ArrayList<>();
        listLyrics = new ArrayList<>();
    }

    public void getData(Context context, String action) {
        getListSong(context);
        getListFavorite(context);
        getListSinger();
        getListPlaylist(context);
        getListLyrics(context);

        //check list
        checkListFavorite();
        checkListPlaylist();

        //set data
        MainActivity.listSong = listSong;
        MainActivity.listFavorite = listFavorite;
        MainActivity.listSinger = listSinger;
        MainActivity.listPlaylist = listPlaylist;
        MainActivity.listLyrics = listLyrics;

        if (action.equals(context.getString(R.string.main_action_get))) {
            callBack.show();
        }
        else if (action.equals(context.getString(R.string.main_action_update))) {
            callBack.showUpdate();
        }
    }

    private void checkListPlaylist() {
        for (int i = 0 ; i < listPlaylist.size() ; i++) {
            Playlist playlist = listPlaylist.get(i);
            ArrayList<Song> list = playlist.getListSong();

            for (int j = 0 ; j < list.size() ; j++) {
                int check = 0;

                Song song = list.get(j);
                for (Song s : listSong) {
                    if (song.getName().equals(s.getName()) && song.getSinger().equals(s.getSinger())) {
                        check ++;
                        break;
                    }
                }

                if (check == 0) {
                    list.remove(j);
                }
            }

            listPlaylist.remove(i);

            if (list.size() > 0) {
                playlist.setListSong(list);
                listPlaylist.add(playlist);
            }
        }
    }

    private void checkListFavorite() {
        for (int i = 0 ; i < listFavorite.size() ; i++) {
            int check = 0;

            Song song = listFavorite.get(i);
            for (Song s : listSong) {
                if (song.getName().equals(s.getName()) && song.getSinger().equals(s.getSinger())) {
                    check ++;
                    break;
                }
            }

            if (check == 0) {
                listFavorite.remove(i);
            }
        }
    }

    private void getListLyrics(Context context) {
        listLyrics.clear();
        File file = new File(context.getFilesDir(), file_lyrics);
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            listLyrics = (ArrayList<Song>) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getListPlaylist(Context context) {
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
        listSinger.clear();

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
