package com.example.admin.music.model;

import android.content.Context;

import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Singer;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.detail_search.DetailSearchPresenterListener;
import com.example.admin.music.view.detail_search.DetailSearchActivity;
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
 * Created by admin on 1/11/2019.
 */

public class DetailSearchModel {
    private DetailSearchPresenterListener callBack;
    private ArrayList<Song> listSong, listSongResult;
    private ArrayList<Singer> listSinger, listSingerResult;
    private ArrayList<Playlist> listPlaylist, listPlaylistResult;
    private ArrayList<String> listHistory;

    private final String file_history = "history";

    public DetailSearchModel(DetailSearchPresenterListener callBack) {
        this.callBack = callBack;

        //init
        listSong = MainActivity.listSong;
        listSinger = MainActivity.listSinger;
        listPlaylist = MainActivity.listPlaylist;
        listHistory = new ArrayList<>();

        listSongResult = new ArrayList<>();
        listSingerResult = new ArrayList<>();
        listPlaylistResult = new ArrayList<>();
    }

    public void search(String word) {
        getSong(word);
        getSinger(word);
        getPlaylist(word);

        //set data
        DetailSearchActivity.listSong = listSongResult;
        DetailSearchActivity.listSinger = listSingerResult;
        DetailSearchActivity.listPlaylist = listPlaylistResult;

        callBack.show();
    }

    public void save(Context context, String word) {
        readHistory(context);

        //delete word if word exits in list
        updateHistory(word);

        listHistory.add(0, word);

        writeHistory(context);
    }

    private void writeHistory(Context context) {
        File file = new File(context.getFilesDir(), file_history);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(listHistory);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateHistory(String word) {
        for (int i = 0 ; i < listHistory.size() ; i++) {
            String w = listHistory.get(i);
            if (w.equals(word)) {
                listHistory.remove(i);
                break;
            }
        }
    }

    private void readHistory(Context context) {
        listHistory.clear();
        File file = new File(context.getFilesDir(), file_history);
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            listHistory = (ArrayList<String>) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void getPlaylist(String word) {
        listPlaylistResult.clear();
        for (Playlist playlist : listPlaylist) {
            if (playlist.getName().toLowerCase().contains(word.toLowerCase())) {
                listPlaylistResult.add(playlist);
            }
        }
    }

    private void getSinger(String word) {
        listSingerResult.clear();
        for (Singer singer : listSinger) {
            if (singer.getName().toLowerCase().contains(word.toLowerCase())) {
                listSingerResult.add(singer);
            }
        }
    }

    private void getSong(String word) {
        listSongResult.clear();
        for (Song song : listSong) {
            if (song.getName().toLowerCase().contains(word.toLowerCase())) {
                listSongResult.add(song);
            }
        }
    }

}
