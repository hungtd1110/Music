package com.example.admin.music.model;

import android.content.Context;
import android.os.Environment;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.detail_song.DetailSongPresenterListener;
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

public class DetailSongModel {
    private DetailSongPresenterListener callBack;
    private ArrayList<Song> list;

    private final String file_favorite = "favorite";

    public DetailSongModel(DetailSongPresenterListener callBack) {
        this.callBack = callBack;

        //init
        list = new ArrayList<>();
    }

    public void getData(Context context, Song song) {
        readFavorite(context);

        //check favorite
        int count = 0;
        for (Song s : list) {
            if (song.getName().equals(s.getName()) && song.getSinger().equals(s.getSinger())) {
                count ++;
                break;
            }
        }
        if (count == 0) {
            callBack.show(false);
        }
        else {
            callBack.show(true);
        }
    }

    public void saveData(Context context, Song song, boolean favorite) {
        //delete song if song exits in list
        updateList(song);
        
        if (favorite) {
            list.add(0, song);
        }
        
        writeFavorite(context);
    }

    private void writeFavorite(Context context) {
        File file = new File(context.getFilesDir(), file_favorite);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);

            //update list favorite in activity main
            MainActivity.listFavorite = list;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateList(Song song) {
        for (int i = 0 ; i < list.size() ; i++) {
            Song s = list.get(i);
            if (song.getName().equals(s.getName()) && song.getSinger().equals(s.getSinger())) {
                list.remove(i);
                break;
            }
        }
    }

    private void readFavorite(Context context) {
        list.clear();
        File file = new File(context.getFilesDir(), file_favorite);
        try {
            FileInputStream fis = new FileInputStream(file);
            ObjectInputStream ois = new ObjectInputStream(fis);
            list = (ArrayList<Song>) ois.readObject();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
