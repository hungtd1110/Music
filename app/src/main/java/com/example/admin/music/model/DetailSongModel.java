package com.example.admin.music.model;

import android.content.Context;
import android.util.Log;

import com.example.admin.music.R;
import com.example.admin.music.config.Data;
import com.example.admin.music.model.entity.Lyrics;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.model.entity.Test;
import com.example.admin.music.presenter.detail_song.DetailSongPresenterListener;
import com.example.admin.music.utils.APIUtils;
import com.example.admin.music.view.main.MainActivity;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by admin on 1/6/2019.
 */

public class DetailSongModel {
    private DetailSongPresenterListener callBack;
    private ArrayList<Song> listFavorite;
    private ArrayList<Song> listLyrics;

    private final String file_favorite = "favorite", file_lyrics = "lyrics";

    public DetailSongModel(DetailSongPresenterListener callBack) {
        this.callBack = callBack;

        //init
        listFavorite = new ArrayList<>();
        listLyrics = new ArrayList<>();
    }

    public void getFavorite(Context context, Song song) {
        listFavorite = MainActivity.listFavorite;

        //check favorite
        int count = 0;
        for (Song s : listFavorite) {
            if (song.getName().equals(s.getName()) && song.getSinger().equals(s.getSinger())) {
                count ++;
                break;
            }
        }
        if (count == 0) {
            callBack.showFavorite(false);
        }
        else {
            callBack.showFavorite(true);
        }
    }

    public void saveFavorite(Context context, Song song, boolean favorite) {
        //delete song if song exits in list
        updateFavorite(song);
        
        if (favorite) {
            listFavorite.add(0, song);
        }
        
        writeFavorite(context);

        callBack.success(context.getString(R.string.action_favorite));
    }

    public void addLyrics(Context context, Song song) {
        listLyrics = MainActivity.listLyrics;

        //delete song if song exits in list
        updateLyrics(song);

        listLyrics.add(song);

        writeLyrics(context);

        callBack.success(context.getString(R.string.action_lyrics));

        uploadLyrics(song);
    }

    private void uploadLyrics(Song song) {
        String content = getContent(song);

        if (!content.equals("")) {
            //get data
            Lyrics lyrics = new Lyrics();
            lyrics.setIdUser(MainActivity.idUser);
            lyrics.setName(song.getName());
            lyrics.setSinger(song.getSinger());
            lyrics.setContent(content);

            //upload
            Data data = APIUtils.getData();
            Call<Void> call = data.upload(lyrics.getIdUser(), lyrics.getName(), lyrics.getSinger(),
                    lyrics.getContent(), lyrics.getDownload());
            call.enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    Log.i("call_upload", "ok");
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.i("call_upload", t.toString());
                }
            });
        }
    }

    private String getContent(Song song) {
        String content = "";
        File file = new File(song.getPathLyrics());

        try {
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader reader = new BufferedReader(isr);
            String line = "";
            while ((line = reader.readLine()) != null) {
                //check format
                int begin = line.indexOf("[");
                int end = line.indexOf("]");
                String time = line.substring(begin + 1,end);
                String lyrics = line.substring(end + 1);

                //add content
                content += line + "\n";
            }
            reader.close();
        }
        catch (Exception e) {

        }

        return content;
    }

    private void writeLyrics(Context context) {
        File file = new File(context.getFilesDir(), file_lyrics);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(listLyrics);

            //update list favorite in activity main
            MainActivity.listLyrics = listLyrics;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void updateLyrics(Song song) {
        for (int i = 0 ; i < listLyrics.size() ; i++) {
            Song s = listLyrics.get(i);
            if (song.getName().equals(s.getName()) && song.getSinger().equals(s.getSinger())) {
                listLyrics.remove(i);
                break;
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
            e.printStackTrace();
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
