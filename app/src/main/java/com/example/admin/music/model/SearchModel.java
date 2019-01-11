package com.example.admin.music.model;

import android.content.Context;

import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Singer;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.search.SearchPresenterListener;
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

public class SearchModel {
    private SearchPresenterListener callBack;
    private ArrayList<Song> listSong, listSongResult;
    private ArrayList<Singer> listSinger, listSingerResult;
    private ArrayList<Playlist> listPlaylist, listPlaylistResult;
    private ArrayList<String> listHistory;

    private final String file_history = "history";
    private final int SIZE = 3;

    public SearchModel(SearchPresenterListener callBack) {
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

    public void getHistory(Context context) {
        listHistory.clear();
        File file = new File(context.getFilesDir(), file_history);
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            listHistory = (ArrayList<String>) ois.readObject();

            callBack.showHistory(listHistory);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void search(String word) {
        getSong(word);
        getSinger(word);
        getPlaylist(word);

        callBack.show(listSongResult, listSingerResult, listPlaylistResult);
    }

    public void delete(Context context, String word) {
        //update list
        for (int i = 0 ; i < listHistory.size() ; i++) {
            String w = listHistory.get(i);
            if (w.equals(word)) {
                listHistory.remove(i);
                break;
            }
        }

        writeHistory(context);

        callBack.updateHistory(listHistory);
    }

    public void deleteAll(Context context) {
        listHistory.clear();
        writeHistory(context);
        callBack.updateHistory(listHistory);
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

    private void getPlaylist(String word) {
        listPlaylistResult.clear();
        for (Playlist playlist : listPlaylist) {
            if (playlist.getName().toLowerCase().contains(word.toLowerCase())) {
                if (listPlaylistResult.size() < SIZE) {
                    listPlaylistResult.add(playlist);
                }
            }
        }
    }

    private void getSinger(String word) {
        listSingerResult.clear();
        for (Singer singer : listSinger) {
            if (singer.getName().toLowerCase().contains(word.toLowerCase())) {
                if (listSingerResult.size() < SIZE) {
                    listSingerResult.add(singer);
                }
            }
        }
    }

    private void getSong(String word) {
        listSongResult.clear();
        for (Song song : listSong) {
            if (song.getName().toLowerCase().contains(word.toLowerCase())) {
                if (listSongResult.size() < SIZE) {
                    listSongResult.add(song);
                }
            }
        }
    }

}
