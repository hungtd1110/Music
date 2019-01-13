package com.example.admin.music.view.lyrics;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.lyrics.LyricsPresenter;
import com.example.lyrics_lib.LyricsView;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.filter.entity.NormalFile;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by admin on 1/10/2019.
 */

public class LyricsFragment extends Fragment implements LyricsViewListener {
    public static LyricsViewListener callBack;

    private LyricsPresenter presenter;
    private LyricsView lrvContent;
    private Song song;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lyrics, container, false);

        //controls
        lrvContent = view.findViewById(R.id.lyricsview_lyrics_content);

        //init
        callBack = this;
        presenter = new LyricsPresenter(this);

        getData();
        presenter.getLyrics(song);

        return view;
    }

    @Override
    public void update(Song song) {
        presenter.getLyrics(song);
        show(song);
    }

    @Override
    public void show(Song song) {
        File file = new File(song.getPathLyrics());
        lrvContent.loadFile(file);
    }

    @Override
    public void run(int time, String type) {
        if (type.equals(getString(R.string.key_karaoke))) {
            lrvContent.runKaraoke(time);
        }
        else if (type.equals(getString(R.string.key_hightlight))) {
            lrvContent.runHightLight(time);
        }
    }

    @Override
    public void updateLyrics(int time, String type) {
        if (type.equals(getString(R.string.key_karaoke))) {
            lrvContent.updateKaraoke(time);
        }
        else if (type.equals(getString(R.string.key_hightlight))) {
            lrvContent.updateHightLight(time);
        }
    }

    private void getData() {
        Bundle bundle = getArguments();
        song = (Song) bundle.getSerializable(getString(R.string.key_song));
    }

}
