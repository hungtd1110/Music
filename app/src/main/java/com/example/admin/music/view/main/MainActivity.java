package com.example.admin.music.view.main;

import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Singer;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.main.MainPresenter;
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
    public static MainViewListener callBack;

    private MainPresenter presenter;
    private FragmentTransaction fragmentTransaction;
    private LinearLayout llProgressBar;
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
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.main_search);

        //controls
        msvSearch = findViewById(R.id.materialsearchview_main_search);
        llProgressBar = findViewById(R.id.linearlayout_main_progressbar);

        //init
        callBack = this;
        action = getString(R.string.main_action_get);
        handler = new Handler();
        presenter = new MainPresenter(this);

        handler.post(runnable);

        //events
        msvSearch.setOnSearchViewListener(searchView);

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
        llProgressBar.setVisibility(View.GONE);

        //remove runnable
        handler.removeCallbacks(runnable);
    }

    @Override
    public void update() {
        action = getString(R.string.main_action_update);
        handler.post(runnable);
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
        handler.removeCallbacks(runnable);
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

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            presenter.getData(getApplicationContext(), action);
        }
    };
}
