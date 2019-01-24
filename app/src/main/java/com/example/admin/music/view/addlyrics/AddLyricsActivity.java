package com.example.admin.music.view.addlyrics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Lyrics;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.addlyrics.AddLyricsPresenter;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class AddLyricsActivity extends AppCompatActivity implements AddLyricsViewListener {
    private AddLyricsPresenter presenter;
    private RecyclerView rvList;
    private RelativeLayout rlProgressBar;
    private MaterialSearchView msvSearch;
    private Song song;
    private ArrayList<Lyrics> listLyrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_lyrics);

        //set ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar_addlyrics_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.all_search);

        //controls
        msvSearch = findViewById(R.id.materialsearchview_addlyrics_search);
        rvList = findViewById(R.id.recyclerview_addlyrics_list);
        rlProgressBar = findViewById(R.id.relativelayout_addlyrics_progressbar);

        //init
        presenter = new AddLyricsPresenter(this);
        listLyrics = new ArrayList<>();

        getData();
        presenter.getData();

        //events
        msvSearch.setOnQueryTextListener(queryText);
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
    public void show(ArrayList<Lyrics> list) {
        listLyrics = list;

        AddLyricsAdapter adapter = new AddLyricsAdapter(this, list, song);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(adapter);

        rlProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void hide() {
        finish();
    }

    private MaterialSearchView.OnQueryTextListener queryText = new MaterialSearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            rlProgressBar.setVisibility(View.VISIBLE);
            handleSearch(query.toLowerCase());
            return false;
        }

        @Override
        public boolean onQueryTextChange(final String newText) {
            return false;
        }
    };

    private void handleSearch(String word) {
        ArrayList<Lyrics> list = new ArrayList<>();
        for (Lyrics lyrics : listLyrics) {
            if (lyrics.getName().toLowerCase().contains(word) || lyrics.getSinger().toLowerCase().contains(word)) {
                list.add(lyrics);
            }
        }

        show(list);
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        song = (Song) bundle.getSerializable(getString(R.string.key_song));
    }
}
