package com.example.admin.music.view.detail_search;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Singer;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.detail_search.DetailSearchPresenter;

import java.util.ArrayList;

public class DetailSearchActivity extends AppCompatActivity implements DetailSearchViewListener {
    public static ArrayList<Song> listSong;
    public static ArrayList<Singer> listSinger;
    public static ArrayList<Playlist> listPlaylist;

    private DetailSearchPresenter presenter;
    private RelativeLayout rlProgressBar;
    private TabLayout tlTab;
    private ViewPager vpContent;
    private TextView txtTitle;
    private String word, action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_search);

        //set ToolBar
        Toolbar toolbar = findViewById(R.id.toolbar_detailsearch_toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.all_back_black);

        //controls
        rlProgressBar = findViewById(R.id.relativelayout_detailsearch_progressbar);
        tlTab = findViewById(R.id.tablayout_detailsearch_tab);
        vpContent = findViewById(R.id.viewpager_detailsearch_content);
        txtTitle = findViewById(R.id.textview_detailsearch_title);

        //init
        presenter = new DetailSearchPresenter(this);

        getData();
        presenter.search(word);
        txtTitle.setText("Từ tìm kiếm \"" + word + "\"");


    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.save(this, word);
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
    public void show() {
        //show viewpager
        DetailSearchAdapter adapter = new DetailSearchAdapter(getSupportFragmentManager(), this);
        vpContent.setAdapter(adapter);
        vpContent.setOffscreenPageLimit(3);
        tlTab.setupWithViewPager(vpContent);

        //show fragment current
        if (action.equals(getString(R.string.action_singer))) {
            vpContent.setCurrentItem(1);
        }
        else if (action.equals(getString(R.string.action_playlist))) {
            vpContent.setCurrentItem(2);
        }
        else {
            vpContent.setCurrentItem(0);
        }

        //gone progressbar
        rlProgressBar.setVisibility(View.GONE);
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        word = bundle.getString(getString(R.string.key_word));
        action = bundle.getString(getString(R.string.key_action));
    }
}
