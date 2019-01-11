package com.example.admin.music.presenter.search;

import android.content.Context;

import com.example.admin.music.model.SearchModel;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Singer;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.view.search.SearchViewListener;

import java.util.ArrayList;

/**
 * Created by admin on 1/10/2019.
 */

public class SearchPresenter implements SearchPresenterListener {
    private SearchViewListener callBack;
    private SearchModel model;

    public SearchPresenter(SearchViewListener callBack) {
        this.callBack = callBack;

        //init
        model = new SearchModel(this);
    }

    @Override
    public void getHistory(Context context) {
        model.getHistory(context);
    }

    @Override
    public void showHistory(ArrayList<String> list) {
        callBack.showHistory(list);
    }

    @Override
    public void search(String word) {
        model.search(word);
    }

    @Override
    public void show(ArrayList<Song> listSong, ArrayList<Singer> listSinger, ArrayList<Playlist> listPlaylist) {
        callBack.show(listSong, listSinger, listPlaylist);
    }

    @Override
    public void delete(Context context, String word) {
        model.delete(context, word);
    }

    @Override
    public void updateHistory(ArrayList<String> list) {
        callBack.updateHistory(list);
    }

    @Override
    public void deleteALl(Context context) {
        model.deleteAll(context);
    }
}
