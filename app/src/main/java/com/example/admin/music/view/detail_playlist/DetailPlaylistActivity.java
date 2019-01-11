package com.example.admin.music.view.detail_playlist;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.detail_playlist.DetailPlaylistPresenter;
import com.example.admin.music.view.playlist.PlaylistFragment;

import java.util.ArrayList;

public class DetailPlaylistActivity extends AppCompatActivity implements DetailPlaylistViewListener {
    private DetailPlaylistPresenter presenter;
    private RecyclerView rvList;
    private Playlist playlist;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_playlist);

        //set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_detailplaylist_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.all_back_white);

        //controls
        rvList = findViewById(R.id.recyclerview_detailplaylist_list);

        //init
        presenter = new DetailPlaylistPresenter(this);

        getData();
        show();

        if (action.equals(getString(R.string.action_see))) {
            Toast.makeText(this, getString(R.string.detailplaylist_suggest), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    @Override
    public void save(Context context,  ArrayList<Song> list) {
        playlist.setListSong(list);
        presenter.save(context, playlist);
    }

    @Override
    public void success(Context context) {
        //update playlist detail
        show();

        //update playlist
        PlaylistFragment.callBack.update();

        Toast.makeText(context, context.getString(R.string.detailplaylist_notify_success), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void fail(Context context) {
        Toast.makeText(context, context.getString(R.string.detailplaylist_notify_fail), Toast.LENGTH_SHORT).show();
    }

    private void show() {
        //set title
        getSupportActionBar().setTitle(playlist.getName());

        //show list
        DetailPlaylistAdapter adapter = new DetailPlaylistAdapter(this, playlist.getListSong(), action);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(adapter);
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        playlist = (Playlist) bundle.getSerializable(getString(R.string.key_playlist));
        action = bundle.getString(getString(R.string.key_action));
    }

}
