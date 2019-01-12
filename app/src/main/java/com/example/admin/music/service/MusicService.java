package com.example.admin.music.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

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
        //flag này có tác dụng khi android bị kill hoặc bộ nhớ thấp, hệ thống sẽ start lại và gửi kết quả lần nữa.
        return START_REDELIVER_INTENT;
    }
}
