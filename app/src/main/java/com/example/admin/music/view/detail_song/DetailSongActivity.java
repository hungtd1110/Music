package com.example.admin.music.view.detail_song;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RemoteViews;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.presenter.detail_song.DetailSongPresenter;
import com.example.admin.music.service.MusicService;
import com.example.admin.music.view.add.AddDialog;
import com.example.admin.music.view.establish.EstablishDialog;
import com.example.admin.music.view.favorite.FavoriteFragment;
import com.example.admin.music.view.image.ImageFragment;
import com.example.admin.music.view.list.ListDialog;
import com.example.admin.music.view.lyrics.LyricsFragment;
import com.example.admin.music.view.speed.SpeedDialog;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.activity.NormalFilePickActivity;
import com.vincent.filepicker.filter.entity.NormalFile;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimeZone;

public class DetailSongActivity extends AppCompatActivity implements View.OnClickListener, DetailSongViewListener {
    public static DetailSongViewListener callBack;

    private static Activity activity;
    private static ArrayList<Song> list;
    private static int index, duration;
    private static Song song;
    private static ArrayList<Integer> listRandom;
    private static MediaPlayer mediaPlayer;
    private static String typeLoop, speed, typeLyrics;
    private static boolean autoRun, close, clock;
    private static ImageView imvRun;
    private static NotificationManager notificationManager;

    private ProgressBar pbLoad;
    private ImageView imvNext, imvPrevious, imvLoop, imvList, imvFavorite, imvSpeed, imvAdd, imvBack, imvEstablish, imvClock;
    private SeekBar sbAudio;
    private TextView txtTime, txtDuration, txtName, txtSinger;
    private ViewPager vpContent;
    private Handler handler;
    private Runnable runnableTime, runnableLyrics;
    private boolean favorite, notification;
    private DetailSongPresenter presenter;

    private final int TIME_UPDATE = 500, NOTIFICATION_ID = 0;

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
        imvEstablish = findViewById(R.id.imageview_detailsong_establish);
        imvClock = findViewById(R.id.imageview_detailsong_clock);
        pbLoad = findViewById(R.id.progressbar_detailsong_load);
        sbAudio = findViewById(R.id.seekbar_detailsong_audio);
        txtTime = findViewById(R.id.textview_detailsong_time);
        txtDuration = findViewById(R.id.textview_detailsong_duration);
        txtName = findViewById(R.id.textview_detailsong_name);
        txtSinger = findViewById(R.id.textview_detailsong_singer);
        vpContent = findViewById(R.id.viewpager_detailsong_content);

        //init
        activity = this;
        presenter = new DetailSongPresenter(this);
        handler = new Handler();
        close = false;
        typeLyrics = getString(R.string.key_hightlight);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        getNotification();
        if (notification) {
            handleContinue();
        }
        else {
            callBack = this;
            autoRun = false;
            clock = false;
            typeLoop = "repeat";
            speed = "1";
            listRandom = new ArrayList<>();

            resetMediaPlayer();
            getData();
            loadAudio();
        }

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
        imvEstablish.setOnClickListener(this);
        imvClock.setOnClickListener(this);
        sbAudio.setOnSeekBarChangeListener(changeListener);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        //save data
        presenter.saveFavorite(this, song, favorite);
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
    public void success(String action) {
        if (action.equals(getString(R.string.action_favorite))) {
            FavoriteFragment.callBack.update();
        }
        else if (action.equals(getString(R.string.action_lyrics))) {
            Toast.makeText(this, getString(R.string.detailsong_lyrics), Toast.LENGTH_SHORT).show();
            LyricsFragment.callBack.update(song);
        }
    }

    @Override
    public void updateRun() {
        handleRun();
        showNotification();
    }

    @Override
    public void updatePrevious() {
        handlePrevious();
    }

    @Override
    public void updateNext() {
        handleNext();
    }

    @Override
    public void updateClose() {
        notificationManager.cancel(NOTIFICATION_ID);
        autoRun = false;
        close = true;
        updateSong();
    }

    @Override
    public void updateShow() {
        DetailSongActivity.activity.finish();

        //show new activity
        Intent intent = new Intent(this, DetailSongActivity.class);
        intent.putExtra(getString(R.string.detailsong_notification), true);
        startActivity(intent);
    }

    @Override
    public void cancel() {
        notificationManager.cancel(NOTIFICATION_ID);
    }

    @Override
    public void addLyrics() {
        Intent intent = new Intent(this, NormalFilePickActivity.class);
        intent.putExtra(Constant.MAX_NUMBER, 1);
        intent.putExtra(NormalFilePickActivity.SUFFIX, new String[] {"txt"});
        startActivityForResult(intent, 0);
    }

    @Override
    public void updateType(String type) {
        typeLyrics = type;

        //update lyrics
        LyricsFragment.callBack.updateLyrics(this, mediaPlayer.getCurrentPosition(), typeLyrics);

        Toast.makeText(this, typeLyrics, Toast.LENGTH_SHORT).show();
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
            case R.id.imageview_detailsong_establish:
                handleEstablish();
                break;
            case R.id.imageview_detailsong_clock:
                handleClock();
                break;
        }
    }

    private void handleClock() {
        if (!clock) {
            showClock();
        }
        else {
            showTime();
        }
    }

    private void showTime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //get view
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.view_detailsong_time, null, false);

        //controls
        final TextView txtTime = view.findViewById(R.id.textview_time_time);

        //init
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (duration > 0) {
                    String time;
                    if (duration > 60 * 60 * 1000) {
                        time = convertTimeLong(duration);
                    }
                    else {
                        time = convertTime(duration);
                    }

                    txtTime.setText(time);
                    Log.i("time_show", time);
                    handler.postDelayed(this, 0);
                }
                else {
                    txtTime.setText(convertTime(0));
                }
            }
        };
        handler.postDelayed(runnable, 0);

        builder.setTitle(getString(R.string.clock_title))
                .setView(view)
                .setPositiveButton("Tắt hẹn giờ", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        duration = 0;
                        clock = false;

                        Toast.makeText(DetailSongActivity.this, getString(R.string.detailsong_time), Toast.LENGTH_SHORT).show();
                    }
                })
                .create();

        builder.show();
    }

    private void showClock() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //get view
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.view_detailsong_clock, null, false);

        //controls
        final LinearLayout llDefault = view.findViewById(R.id.linearlayout_clock_default);
        final LinearLayout llTime = view.findViewById(R.id.linearlayout_clock_time);
        final EditText edtTime = view.findViewById(R.id.editext_clock_time);
        final RadioButton rb30 = view.findViewById(R.id.radiobutton_clock_30);
        final RadioButton rb45 = view.findViewById(R.id.radiobutton_clock_45);
        final RadioButton rb60 = view.findViewById(R.id.radiobutton_clock_60);
        final RadioButton rb90 = view.findViewById(R.id.radiobutton_clock_90);


        //events
        llTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                llDefault.setVisibility(View.GONE);
                edtTime.setVisibility(View.VISIBLE);
            }
        });

        builder.setTitle(getString(R.string.clock_title))
                .setView(view)
                .setPositiveButton("Đồng ý", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String time = edtTime.getText().toString();
                        if (time.equals("") || time.equals(null)) {
                            if (rb30.isChecked()) {
                                handleTime(30);
                            }
                            else if (rb45.isChecked()) {
                                handleTime(45);
                            }
                            else if (rb60.isChecked()){
                                handleTime(60);
                            }
                            else if (rb90.isChecked()) {
                                handleTime(90);
                            }
                        }
                        else {
                            handleTime(Integer.parseInt(time));
                        }
                    }
                }).create();
        
        builder.show();
    }

    private void handleTime(int t) {
        duration = t * 60 * 1000;      //mili seconds

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (duration > 0) {
                    duration -= 1000;
                    Log.i("time_set", convertTime(duration));
                    handler.postDelayed(this, 1000);
                }
                else if (clock){
                    updateClose();
                    clock = false;
                }
            }
        }, 1000);

        clock = true;
        Toast.makeText(this, getString(R.string.detailsong_clock) + " " + t + " phút", Toast.LENGTH_SHORT).show();
    }

    private void handleEstablish() {
        EstablishDialog dialog = new EstablishDialog();
        dialog.show(getSupportFragmentManager(), "");
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

        //save data
        presenter.saveFavorite(this, song, favorite);
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

        //update viewpager
        ImageFragment.callBack.update(song);
        LyricsFragment.callBack.update(song);

        //update favorite
        presenter.getFavorite(this, song);
    }

    private void resetMediaPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();

            imvRun.setImageResource(R.mipmap.detailsong_play);
            txtTime.setText("00:00");
            sbAudio.setProgress(0);
        }
    }

    private void handleRun() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            imvRun.setImageResource(R.mipmap.detailsong_play);
            autoRun = false;

            //update animation
            ImageFragment.callBack.animation(false);
        }
        else {
            mediaPlayer.start();
            imvRun.setImageResource(R.mipmap.detailsong_pause);
            setTime();
            setLyrics();
            autoRun = true;

            //update animation
            ImageFragment.callBack.animation(true);

            //get data list random
            if (listRandom.size() == 0) {
                getListRandom();
            }
        }

        showNotification();
        close = false;
    }

    private void handleContinue() {
        pbLoad.setVisibility(View.INVISIBLE);
        imvRun.setVisibility(View.VISIBLE);

        if (mediaPlayer.isPlaying()) {
            imvRun.setImageResource(R.mipmap.detailsong_pause);
        }
        else {
            imvRun.setImageResource(R.mipmap.detailsong_play);
        }

        int duration = mediaPlayer.getDuration();
        sbAudio.setMax(duration);
        sbAudio.setProgress(mediaPlayer.getCurrentPosition());
        txtDuration.setText(convertTime(duration));

        setTime();
        setLyrics();

        //show favorite
        presenter.getFavorite(this, song);

        //update lyrics
        LyricsFragment.callBack.updateLyrics(this, mediaPlayer.getCurrentPosition(), typeLyrics);
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

    private void setLyrics() {
        runnableLyrics = new Runnable() {
            @Override
            public void run() {
                try {
                    int time = mediaPlayer.getCurrentPosition();
                    LyricsFragment.callBack.run(getApplicationContext(), time, typeLyrics);
                } catch (Exception e) {

                }
                handler.postDelayed(this, 0);
            }
        };
        handler.postDelayed(runnableLyrics, 0);
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

                    if (!close) {
                        //update notification
                        showNotification();
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

    private String convertTimeLong(int duration) {
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mm:ss");
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(duration);
    }

    private void getData() {
        Bundle bundle = getIntent().getExtras();
        list = (ArrayList<Song>) bundle.getSerializable(getString(R.string.key_list));
        index = bundle.getInt(getString(R.string.key_index));
        song = list.get(index);

        //show favorite
        presenter.getFavorite(this, song);
    }

    private void getNotification() {
        Bundle bundle = getIntent().getExtras();
        notification = bundle.getBoolean(getString(R.string.detailsong_notification), false);
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
            int time = seekBar.getProgress();

            //update lyrics
            LyricsFragment.callBack.updateLyrics(getApplicationContext(), time, typeLyrics);

            mediaPlayer.seekTo(time);
        }
    };

    private void showNotification() {
        //get view
        RemoteViews viewSmall = new RemoteViews(getPackageName(), R.layout.view_notification_small);
        RemoteViews viewBig = new RemoteViews(getPackageName(), R.layout.view_notification_big);

        //set data
        viewSmall.setTextViewText(R.id.textview_notification_name, song.getName());
        viewSmall.setTextViewText(R.id.textview_notification_singer, song.getSinger());

        viewBig.setTextViewText(R.id.textview_notification_name, song.getName());
        viewBig.setTextViewText(R.id.textview_notification_singer, song.getSinger());

        if (mediaPlayer.isPlaying()) {
            viewSmall.setImageViewResource(R.id.imageview_notification_run, R.drawable.notification_pause);
            viewBig.setImageViewResource(R.id.imageview_notification_run, R.drawable.notification_pause);
        }
        else {
            viewSmall.setImageViewResource(R.id.imageview_notification_run, R.drawable.notification_play);
            viewBig.setImageViewResource(R.id.imageview_notification_run, R.drawable.notification_play);
        }

        //get image from file mp3
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(song.getPathAudio());
            byte[] artBytes = mmr.getEmbeddedPicture();
            if (artBytes != null) {
                InputStream is = new ByteArrayInputStream(mmr.getEmbeddedPicture());
                Bitmap bm = BitmapFactory.decodeStream(is);

                viewSmall.setImageViewBitmap(R.id.imageview_notification_image, bm);
                viewBig.setImageViewBitmap(R.id.imageview_notification_image, bm);
            }
            else {
                viewSmall.setImageViewResource(R.id.imageview_notification_image, R.drawable.all_imagesong);
                viewBig.setImageViewResource(R.id.imageview_notification_image, R.drawable.all_imagesong);
            }
        } catch (Exception e) {

        }

        //set intent
        Intent intentRun = new Intent(this, MusicService.class);
        intentRun.setAction(MusicService.ACTION_RUN);

        Intent intentNext = new Intent(this, MusicService.class);
        intentNext.setAction(MusicService.ACTION_NEXT);

        Intent intentPrevious = new Intent(this, MusicService.class);
        intentPrevious.setAction(MusicService.ACTION_PREVIOUS);

        Intent intentClose = new Intent(this, MusicService.class);
        intentClose.setAction(MusicService.ACTION_CLOSE);

        Intent intentShow = new Intent(this, MusicService.class);
        intentShow.setAction(MusicService.ACTION_SHOW);

        PendingIntent pendingRun = PendingIntent.getService(this, 0, intentRun, 0);
        PendingIntent pendingNext = PendingIntent.getService(this, 0, intentNext, 0);
        PendingIntent pendingPrevious = PendingIntent.getService(this, 0, intentPrevious, 0);
        PendingIntent pendingClose = PendingIntent.getService(this, 0, intentClose, 0);
        PendingIntent pendingShow = PendingIntent.getService(this, 0, intentShow, 0);

        //events
        viewSmall.setOnClickPendingIntent(R.id.imageview_notification_run, pendingRun);
        viewSmall.setOnClickPendingIntent(R.id.imageview_notification_next, pendingNext);
        viewSmall.setOnClickPendingIntent(R.id.imageview_notification_previous, pendingPrevious);

        viewBig.setOnClickPendingIntent(R.id.imageview_notification_run, pendingRun);
        viewBig.setOnClickPendingIntent(R.id.imageview_notification_next, pendingNext);
        viewBig.setOnClickPendingIntent(R.id.imageview_notification_previous, pendingPrevious);
        viewBig.setOnClickPendingIntent(R.id.imageview_notification_close, pendingClose);

        //show notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "");
        builder.setSmallIcon(R.drawable.ic_app)
//                .setAutoCancel(true)    //auto cancel notification when click
                .setOngoing(true)   //disable clear
                .setContentIntent(pendingShow)
                .setCustomContentView(viewSmall)
                .setCustomBigContentView(viewBig);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Constant.RESULT_PICK_FILE);
            if (list.size() > 0) {
                String path = list.get(0).getPath();
                song.setPathLyrics(path);
                presenter.addLyrics(this, song);
            }
        }
    }
}
