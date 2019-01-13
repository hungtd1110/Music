package com.example.admin.music.view.option;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.option.OptionPresenter;
import com.example.admin.music.view.detail_song.DetailSongActivity;
import com.example.admin.music.view.favorite.FavoriteFragment;
import com.example.admin.music.view.main.MainActivity;
import com.example.admin.music.view.playlist.PlaylistFragment;

import java.util.ArrayList;

/**
 * Created by admin on 1/6/2019.
 */

public class OptionDialog extends BottomSheetDialogFragment implements OptionViewListener {
    private OptionPresenter presenter;
    private RecyclerView rvList;
    private Song song;
    private Playlist playlist;
    private ArrayList<String> listTitle;
    private ArrayList<Integer> listImage;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_option, container, false);

        //controls
        rvList = view.findViewById(R.id.recyclerview_option_list);

        //init
        presenter = new OptionPresenter(this);
        listTitle = new ArrayList<>();
        listImage = new ArrayList<>();
        song = new Song();
        playlist = new Playlist();

        getData();
        show();

        return view;
    }

    @Override
    public void hide() {
        dismiss();
    }

    @Override
    public void addFavorite(Song song) {
        presenter.addFavorite(getContext(), song);
    }

    @Override
    public void subFavorite(Song song) {
        presenter.subFavorite(getContext(), song);
    }

    @Override
    public void deleteSong(Context context, Song song) {
        presenter.deleteSong(context, song);
    }

    @Override
    public void edit(Context context, Playlist playlist, String name) {
        presenter.edit(context, playlist, name);
    }

    @Override
    public void deletePlaylist(Context context, Playlist playlist) {
        presenter.deletePlaylist(context,playlist);
    }

    @Override
    public void success(Context context, String action) {
        if (action.equals(context.getString(R.string.option_action_add))) {
            //update list favorite
            FavoriteFragment.callBack.update();

            Toast.makeText(context, context.getString(R.string.option_notify_add), Toast.LENGTH_SHORT).show();
        }
        else if (action.equals(context.getString(R.string.option_action_sub))) {
            //update list favorite
            FavoriteFragment.callBack.update();

            Toast.makeText(context, context.getString(R.string.option_notify_sub), Toast.LENGTH_SHORT).show();
        }
        else if (action.equals(context.getString(R.string.option_action_deletesong))) {
            //update data
            MainActivity.callBack.update();

            Toast.makeText(context, context.getString(R.string.option_notify_deletesong), Toast.LENGTH_SHORT).show();
        }
        else if (action.equals(context.getString(R.string.option_action_edit))) {
            //update data
            PlaylistFragment.callBack.update();

            Toast.makeText(context, context.getString(R.string.option_notify_edit), Toast.LENGTH_SHORT).show();
        }
        else if (action.equals(context.getString(R.string.option_action_deleteplaylist))) {
            //update data
            PlaylistFragment.callBack.update();

            Toast.makeText(context, context.getString(R.string.option_notify_deleteplaylist), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void fail(Context context, String action) {
        if (action.equals(context.getString(R.string.option_action_edit))) {
            Toast.makeText(context, context.getString(R.string.option_notify_conflict), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, context.getString(R.string.option_notify_errors), Toast.LENGTH_SHORT).show();
        }
    }

    private void show() {
        //set data item
        String key = getTag();
        if (key.equals(getString(R.string.key_song))) {
            listTitle.add(getString(R.string.option_item_add));
            listTitle.add(getString(R.string.option_item_addfavorite));
            listTitle.add(getString(R.string.option_item_deletesong));

            listImage.add(R.mipmap.option_add);
            listImage.add(R.mipmap.option_addfavorite);
            listImage.add(R.mipmap.option_delete);
        }
        else if (key.equals(getString(R.string.key_favorite))){
            listTitle.add(getString(R.string.option_item_add));
            listTitle.add(getString(R.string.option_item_subfavorite));
            listTitle.add(getString(R.string.option_item_deletesong));

            listImage.add(R.mipmap.option_add);
            listImage.add(R.mipmap.option_subfavorite);
            listImage.add(R.mipmap.option_delete);
        }
        else if (key.equals(getString(R.string.key_playlist))){
            listTitle.add(getString(R.string.option_item_edit));
            listTitle.add(getString(R.string.option_item_deleteplaylist));

            listImage.add(R.mipmap.option_edit);
            listImage.add(R.mipmap.option_delete);
        }

        //show
        OptionAdapter adapter = new OptionAdapter(getContext(), listTitle, listImage, song, playlist, getFragmentManager(), this);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(adapter);
    }

    private void getData() {
        Bundle bundle = getArguments();
        if (getTag().equals(getString(R.string.key_playlist))) {
            playlist = (Playlist) bundle.getSerializable(getString(R.string.key_playlist));
        }
        else {
            song = (Song) bundle.getSerializable(getString(R.string.key_song));
        }

    }
}
