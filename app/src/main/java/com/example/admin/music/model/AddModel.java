package com.example.admin.music.model;

import android.content.Context;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.add.AddPresenterListener;
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

public class AddModel {
    private AddPresenterListener callBack;
    private ArrayList<Playlist> list;

    private final String file_playlist = "playlist";

    public AddModel(AddPresenterListener callBack) {
        this.callBack = callBack;

        //init
        list = new ArrayList<>();
    }

    public void saveData(Context context, String name, Song song) {
        list = MainActivity.listPlaylist;
        ArrayList<Song> listSong = new ArrayList<>();

        //check playlist is exsits
        for (Playlist playList : list) {
            if (playList.getName().equals(name)) {
                listSong = playList.getListSong();
                list.remove(playList);
                break;
            }
        }

        //check song is exists
        for (Song s : listSong) {
            if (song.getName().equals(s.getName()) && song.getSinger().equals(s.getSinger())) {
                listSong.remove(s);
                break;
            }
        }

        //add song into playlist
        listSong.add(0, song);

        //add playlist
        Playlist playList = new Playlist();
        playList.setName(name);
        playList.setListSong(listSong);

        list.add(0, playList);

        writePlaylist(context);

        callBack.success(context);
    }

    private void writePlaylist(Context context) {
        File file = new File(context.getFilesDir(), file_playlist);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(list);

            //update list playlist in activity main
            MainActivity.listPlaylist = list;
        } catch (IOException e) {

        }
    }
}
