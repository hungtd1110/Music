package com.example.admin.music.view.main;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Singer;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.main.MainPresenter;
import com.example.admin.music.service.MusicService;
import com.example.admin.music.view.detail_song.DetailSongActivity;
import com.example.admin.music.view.favorite.FavoriteFragment;
import com.example.admin.music.view.music.MusicFragment;
import com.example.admin.music.view.playlist.PlaylistFragment;
import com.example.admin.music.view.search.SearchFragment;
import com.example.admin.music.view.singer.SingerFragment;
import com.example.admin.music.view.song.SongFragment;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainViewListener {
    public static MaterialSearchView msvSearch;
    public static ArrayList<Song> listSong;
    public static ArrayList<Song> listFavorite;
    public static ArrayList<Singer> listSinger;
    public static ArrayList<Playlist> listPlaylist;
    public static ArrayList<Song> listLyrics;
    public static String idUser;
    public static MainViewListener callBack;

    private MainPresenter presenter;
    private FragmentTransaction fragmentTransaction;
    private RelativeLayout rlProgressBar;
    private Handler handler;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar_main_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.all_search);

        //controls
        msvSearch = findViewById(R.id.materialsearchview_main_search);
        rlProgressBar = findViewById(R.id.relativelayout_main_progressbar);

        //init
        callBack = this;
        action = getString(R.string.main_action_get);
        handler = new Handler();
        presenter = new MainPresenter(this);

        handler.post(runnableData);
        handler.post(runnableUser);
        initService();  //init service

        //events
        msvSearch.setOnSearchViewListener(searchView);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DetailSongActivity.callBack.cancel();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (msvSearch.isSearchOpen()) {
                    msvSearch.closeSearch();
                } else {
                    msvSearch.showSearch(false);
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void show() {
        //show music
        fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.framlayout_main_content, new MusicFragment());
        fragmentTransaction.commit();

        //gone progressbar
        rlProgressBar.setVisibility(View.GONE);

        //remove runnable
        handler.removeCallbacks(runnableData);
    }

    @Override
    public void update() {
        action = getString(R.string.main_action_update);
        handler.post(runnableData);
    }

    @Override
    public void showUpdate() {
        //update list song
        SongFragment.callBack.update();

        //update list favorite
        FavoriteFragment.callBack.update();

        //update list singer
        SingerFragment.callBack.update();

        //update list playlist
        PlaylistFragment.callBack.update();

        //remove runnable
        handler.removeCallbacks(runnableData);
    }

    private void initService() {
        Intent intent = new Intent(this,MusicService.class);
        intent.setAction("");
        startService(intent);
    }

    private MaterialSearchView.SearchViewListener searchView = new MaterialSearchView.SearchViewListener() {
        SearchFragment fragment = new SearchFragment();
        @Override
        public void onSearchViewShown() {
            //show content
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.framlayout_main_content, fragment);
            fragmentTransaction.commit();
        }

        @Override
        public void onSearchViewClosed() {
            //close content, show music
            fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.remove(fragment);
            fragmentTransaction.commit();
        }
    };

    private Runnable runnableData = new Runnable() {
        @Override
        public void run() {
            presenter.getData(getApplicationContext(), action);
        }
    };

    private Runnable runnableUser = new Runnable() {
        @Override
        public void run() {
            presenter.getUser(getApplicationContext());
        }
    };

}
