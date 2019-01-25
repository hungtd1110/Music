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
import java.util.Random;

public class DetailSingerActivity extends AppCompatActivity {
    private static ArrayList<Integer> list;

    private RecyclerView rvList;
    private ImageView imvImage;
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
        imvImage = findViewById(R.id.imageview_detailsinger_imagebig);

        //init
        getData();
        setImage();
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

    private void setImage() {
        //get data
        if (list == null || list.size() == 0) {
            list = new ArrayList<>();
            list.add(R.drawable.all_imagelist_1);
            list.add(R.drawable.all_imagelist_2);
            list.add(R.drawable.all_imagelist_3);
            list.add(R.drawable.all_imagelist_4);
            list.add(R.drawable.all_imagelist_5);
            list.add(R.drawable.all_imagelist_6);
            list.add(R.drawable.all_imagelist_7);
            list.add(R.drawable.all_imagelist_8);
            list.add(R.drawable.all_imagelist_9);
            list.add(R.drawable.all_imagelist_10);
        }

        //get random
        Random random = new Random();
        int index = random.nextInt(list.size());

        //set image
        imvImage.setImageResource(list.get(index));

        list.remove(index);
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        singer = (Singer) bundle.getSerializable(getString(R.string.key_singer));
        action = bundle.getString(getString(R.string.key_action));
    }
}
