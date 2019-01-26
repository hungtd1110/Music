package com.example.admin.music.service;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.admin.music.R;
import com.example.admin.music.view.detail_song.DetailSongActivity;

/**
 * Created by admin on 1/11/2019.
 */

public class MusicService extends Service {
    public static final String ACTION_RUN = "RUN";
    public static final String ACTION_PREVIOUS = "PREVIOUS";
    public static final String ACTION_NEXT = "NEXT";
    public static final String ACTION_CLOSE = "CLOSE";
    public static final String ACTION_SHOW = "SHOW";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        switch (intent.getAction()) {
            case ACTION_RUN:
                DetailSongActivity.callBack.updateRun();
                break;
            case ACTION_PREVIOUS:
                DetailSongActivity.callBack.updatePrevious();
                break;
            case ACTION_NEXT:
                DetailSongActivity.callBack.updateNext();
                break;
            case ACTION_CLOSE:
                DetailSongActivity.callBack.updateClose();
                break;
            case ACTION_SHOW:
                DetailSongActivity.callBack.updateShow();
                break;
        }
        return START_REDELIVER_INTENT;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("service_destroy", "destroy");
        try {
            DetailSongActivity.callBack.cancel();
        }
        catch (Exception e) {

        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        Log.i("service_kill", "kill");
        stopSelf();     //hủy service, app sẽ chắc chắn chạy vào onDestroy
    }
}
