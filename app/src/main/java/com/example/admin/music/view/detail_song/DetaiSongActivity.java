package com.example.admin.music.view.detail_song;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.detail_song.DetailSongPresenter;
import com.example.admin.music.view.add.AddDialog;
import com.example.admin.music.view.favorite.FavoriteFragment;
import com.example.admin.music.view.list.ListDialog;
import com.example.admin.music.view.speed.SpeedDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;

public class DetaiSongActivity extends AppCompatActivity implements View.OnClickListener, DetailSongViewListener {
    private ArrayList<Song> list;
    private int index;
    private Song song;
    private ArrayList<Integer> listRandom;
    private MediaPlayer mediaPlayer;
    private ProgressBar pbLoad;
    private ImageView imvRun, imvNext, imvPrevious, imvLoop, imvList, imvFavorite, imvSpeed, imvAdd, imvBack;
    private SeekBar sbAudio;
    private TextView txtTime, txtDuration, txtName, txtSinger;
    private ViewPager vpContent;
    private Runnable runnableTime;
    private Handler handler;
    private String typeLoop = "repeat", speed = "1";
    private DetailSongPresenter presenter;
    private boolean favorite, autoRun;

    private final int TIME_UPDATE = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detai_song);

        //controls
        imvRun = findViewById(R.id.imageview_detailsong_run);
        imvNext = findViewById(R.id.imageview_detailsong_next);
        imvPrevious = findViewById(R.id.imageview_detailsong_previous);
        imvLoop = findViewById(R.id.imageview_detailsong_loop);
        imvList = findViewById(R.id.imageview_detailsong_list);
        imvFavorite = findViewById(R.id.imageview_detailsong_favorite);
        imvSpeed = findViewById(R.id.imageview_detailsong_speed);
        imvAdd = findViewById(R.id.imageview_detailsong_add);
        imvBack = findViewById(R.id.imageview_detailsong_back);
        pbLoad = findViewById(R.id.progressbar_detailsong_load);
        sbAudio = findViewById(R.id.seekbar_detailsong_audio);
        txtTime = findViewById(R.id.textview_detailsong_time);
        txtDuration = findViewById(R.id.textview_detailsong_duration);
        txtName = findViewById(R.id.textview_detailsong_name);
        txtSinger = findViewById(R.id.textview_detailsong_singer);
        vpContent = findViewById(R.id.viewpager_detailsong_content);

        //init
        presenter = new DetailSongPresenter(this);
        listRandom = new ArrayList<>();
        handler = new Handler();
        autoRun = false;

        getData();
        loadAudio();
        showViewPager();
        txtName.setText(song.getName());
        txtSinger.setText(song.getSinger());

        //events
        imvRun.setOnClickListener(this);
        imvNext.setOnClickListener(this);
        imvPrevious.setOnClickListener(this);
        imvLoop.setOnClickListener(this);
        imvList.setOnClickListener(this);
        imvFavorite.setOnClickListener(this);
        imvSpeed.setOnClickListener(this);
        imvAdd.setOnClickListener(this);
        imvBack.setOnClickListener(this);
        sbAudio.setOnSeekBarChangeListener(changeListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.saveData(this, song, favorite);
    }

    @Override
    public void updateSong(int index) {
        this.index = index;
        song = list.get(index);
        updateSong();
    }

    @Override
    public void showFavorite(boolean favorite) {
        this.favorite = favorite;
        if (favorite) {
            imvFavorite.setImageResource(R.mipmap.detailsong_favorite);
        }
        else {
            imvFavorite.setImageResource(R.mipmap.detailsong_notfavorite);
        }
    }

    @Override
    public void updateSpeed(float speed) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            mediaPlayer.setPlaybackParams(mediaPlayer.getPlaybackParams().setSpeed(speed));

            this.speed = (int) (speed/0.5f) - 1 + "";
        }
        else {
            Toast.makeText(this, getString(R.string.detailsong_speed), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void success() {
        FavoriteFragment.callBack.update();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageview_detailsong_run:
                handleRun();
                break;
            case R.id.imageview_detailsong_next:
                handleNext();
                break;
            case R.id.imageview_detailsong_previous:
                handlePrevious();
                break;
            case R.id.imageview_detailsong_loop:
                handleLoop();
                break;
            case R.id.imageview_detailsong_list:
                handleList();
                break;
            case R.id.imageview_detailsong_favorite:
                handleFavorite();
                break;
            case R.id.imageview_detailsong_speed:
                handleSpeed();
                break;
            case R.id.imageview_detailsong_add:
                handleAdd();
                break;
            case R.id.imageview_detailsong_back:
                finish();
                break;
        }
    }

    private void handleAdd() {
        AddDialog dialog = new AddDialog();
        Bundle bundle = new Bundle();
        bundle.putSerializable(getString(R.string.key_song), song);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "");
    }

    private void handleSpeed() {
        SpeedDialog dialog = new SpeedDialog();
        dialog.show(getSupportFragmentManager(), speed);
    }

    private void handleFavorite() {
        if (favorite) {
            imvFavorite.setImageResource(R.mipmap.detailsong_notfavorite);
            favorite = false;
        }
        else {
            imvFavorite.setImageResource(R.mipmap.detailsong_favorite);
            favorite = true;
        }
    }

    private void handleList() {
        ListDialog dialog = new ListDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("index", index);
        bundle.putSerializable("list", list);
        dialog.setArguments(bundle);
        dialog.show(getSupportFragmentManager(), "");
    }

    private void handleLoop() {
        switch (typeLoop) {
            case "repeat":
                imvLoop.setImageResource(R.mipmap.detaisong_repeatone);
                typeLoop = "repeat_one";
                break;
            case "repeat_one":
                imvLoop.setImageResource(R.mipmap.detailsong_shuffle);
                typeLoop = "shuffle";
                break;
            case "shuffle":
                imvLoop.setImageResource(R.mipmap.detailsong_repeat);
                typeLoop = "repeat";
                break;
        }
    }

    private void handlePrevious() {
        if (index == 0) {
            index = list.size() - 1;
        }
        else {
            index --;
        }
        song = list.get(index);
        updateSong();
    }

    private void handleNext() {
        if (index == list.size() - 1) {
            index = 0;
        }
        else {
            index ++;
        }
        song = list.get(index);
        updateSong();
    }

    private void updateSong() {
        resetMediaPlayer();

        //update infomation
        txtName.setText(song.getName());
        txtSinger.setText(song.getSinger());
        pbLoad.setVisibility(View.VISIBLE);
        imvRun.setVisibility(View.INVISIBLE);
        loadAudio();
    }

    private void resetMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
        }
    }

    private void handleRun() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            imvRun.setImageResource(R.mipmap.detailsong_play);
        }
        else {
            mediaPlayer.start();
            imvRun.setImageResource(R.mipmap.detailsong_pause);
            setTime();
            autoRun = true;

            //get data list random
            if (listRandom.size() == 0) {
                getListRandom();
            }
        }
    }

    private void setTime() {
        runnableTime = new Runnable() {
            @Override
            public void run() {
                try {
                    int position = mediaPlayer.getCurrentPosition();
                    txtTime.setText(convertTime(position));
                    sbAudio.setProgress(position);
                } catch (Exception e) {

                }
                handler.postDelayed(this, TIME_UPDATE);
            }
        };
        handler.postDelayed(runnableTime, TIME_UPDATE);
    }

    private void showViewPager() {
        DetailSongAdapter adapter = new DetailSongAdapter(getSupportFragmentManager(), this, song);
        vpContent.setAdapter(adapter);
    }

    private void loadAudio() {
        String pathAudio = song.getPathAudio();
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mediaPlayer.setDataSource(pathAudio);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mediaPlayer) {
                    //set init data
                    pbLoad.setVisibility(View.INVISIBLE);
                    imvRun.setVisibility(View.VISIBLE);
                    int duration = mediaPlayer.getDuration();
                    sbAudio.setMax(duration);
                    txtDuration.setText(convertTime(duration));
                    if (autoRun) {
                        mediaPlayer.start();
                        imvRun.setImageResource(R.mipmap.detailsong_pause);
                    }

                    //event when mediaplayer complete
                    mediaPlayer.setOnCompletionListener(completionListener);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String convertTime(int duration) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        return sdf.format(duration);
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        list = (ArrayList<Song>) bundle.getSerializable(getString(R.string.key_list));
        index = bundle.getInt(getString(R.string.key_index));
        song = list.get(index);

        //show favorite
        presenter.getData(this, song);
    }

    private void getListRandom() {
        for (int i = 0 ; i < list.size() ; i++) {
            if (i != index) {
                listRandom.add(i);
            }
        }
    }

    private MediaPlayer.OnCompletionListener completionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mediaPlayer) {
            imvRun.setImageResource(R.mipmap.detailsong_play);
            switch (typeLoop) {
                case "repeat":
                    handleNext();
                    break;
                case "repeat_one":
                    mediaPlayer.start();
                    imvRun.setImageResource(R.mipmap.detailsong_pause);
                    break;
                case "shuffle":
                    if (listRandom.size() > 0) {
                        Random random = new Random();
                        int current = random.nextInt(listRandom.size());
                        index = listRandom.get(current);
                        listRandom.remove(current);
                        song = list.get(index);
                        updateSong();
                    }
                    break;
            }
        }
    };

    private SeekBar.OnSeekBarChangeListener changeListener = new SeekBar.OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(final SeekBar seekBar, int i, boolean b) {

        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            mediaPlayer.seekTo(seekBar.getProgress());
        }
    };

}
