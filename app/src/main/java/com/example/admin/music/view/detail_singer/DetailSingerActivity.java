package com.example.admin.music.view.detail_singer;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Playlist;
import com.example.admin.music.model.entity.Singer;
import com.example.admin.music.model.entity.Song;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class DetailSingerActivity extends AppCompatActivity {
    private RecyclerView rvList;
    private Singer singer;
    private String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_singer);

        //set toolbar
        Toolbar toolbar = findViewById(R.id.toolbar_detaisinger_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.all_back_white);

        //controls
        rvList = findViewById(R.id.recyclerview_detailsinger_list);

        //init
        getData();
        show();

        if (action.equals(getString(R.string.action_see))) {
            Toast.makeText(this, getString(R.string.detailsinger_suggest), Toast.LENGTH_SHORT).show();
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

    private void show() {
        //set title
        getSupportActionBar().setTitle(singer.getName());

        //show list
        DetailSingerAdapter adapter = new DetailSingerAdapter(this, getSupportFragmentManager(), singer.getListSong(), action);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvList.setLayoutManager(layoutManager);
        rvList.setAdapter(adapter);
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        singer = (Singer) bundle.getSerializable(getString(R.string.key_singer));
        action = bundle.getString(getString(R.string.key_action));
    }
}
