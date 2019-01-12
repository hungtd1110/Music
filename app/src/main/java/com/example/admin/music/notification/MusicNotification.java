package com.example.admin.music.notification;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.support.v4.app.NotificationCompat;
import android.widget.RemoteViews;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Song;
import com.example.admin.music.service.MusicService;
import com.example.admin.music.view.detail_song.DetailSongActivity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by admin on 1/12/2019.
 */

public class MusicNotification {
    private Context context;
    private RemoteViews viewSmall, viewBig;

    public MusicNotification(Context context) {
        this.context = context;
    }

    public void update(Song song) {
        viewSmall.setTextViewText(R.id.textview_notification_name, song.getName());
        viewSmall.setTextViewText(R.id.textview_notification_singer, song.getSinger());

        viewBig.setTextViewText(R.id.textview_notification_name, song.getName());
        viewBig.setTextViewText(R.id.textview_notification_singer, song.getSinger());

        viewSmall.setImageViewResource(R.id.imageview_notification_image, R.drawable.all_imagesong);
        viewBig.setImageViewResource(R.id.imageview_notification_image, R.drawable.all_imagesong);
    }

    public void show(Song song) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        //get view
        viewSmall = new RemoteViews(context.getPackageName(), R.layout.view_notification_small);
        viewBig = new RemoteViews(context.getPackageName(), R.layout.view_notification_big);

        //set data
        viewSmall.setTextViewText(R.id.textview_notification_name, song.getName());
        viewSmall.setTextViewText(R.id.textview_notification_singer, song.getSinger());

        viewBig.setTextViewText(R.id.textview_notification_name, song.getName());
        viewBig.setTextViewText(R.id.textview_notification_singer, song.getSinger());

//            if (mediaPlayer.isPlaying()) {
//                viewSmall.setImageViewResource(R.id.imageview_notification_run, R.drawable.notification_pause);
//                viewBig.setImageViewResource(R.id.imageview_notification_run, R.drawable.notification_pause);
//            }
//            else {
//                viewSmall.setImageViewResource(R.id.imageview_notification_run, R.drawable.notification_play);
//                viewBig.setImageViewResource(R.id.imageview_notification_run, R.drawable.notification_play);
//            }

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
        Intent intentRun = new Intent(context, MusicService.class);
        intentRun.setAction(MusicService.ACTION_RUN);

        Intent intentNext = new Intent(context, MusicService.class);
        intentNext.setAction(MusicService.ACTION_NEXT);

        Intent intentPrevious = new Intent(context, MusicService.class);
        intentPrevious.setAction(MusicService.ACTION_PREVIOUS);

        Intent intentShow = new Intent(context, DetailSongActivity.class);
        intentShow.putExtra(context.getString(R.string.detailsong_continue), true);

        PendingIntent pendingRun = PendingIntent.getService(context, 0, intentRun, 0);
        PendingIntent pendingNext = PendingIntent.getService(context, 0, intentNext, 0);
        PendingIntent pendingPrevious = PendingIntent.getService(context, 0, intentPrevious, 0);
        PendingIntent pendingShow = PendingIntent.getActivity(context, 0, intentShow, 0);

        //events
        viewSmall.setOnClickPendingIntent(R.id.imageview_notification_run, pendingRun);
        viewSmall.setOnClickPendingIntent(R.id.imageview_notification_next, pendingNext);
        viewSmall.setOnClickPendingIntent(R.id.imageview_notification_previous, pendingPrevious);

        viewBig.setOnClickPendingIntent(R.id.imageview_notification_run, pendingRun);
        viewBig.setOnClickPendingIntent(R.id.imageview_notification_next, pendingNext);
        viewBig.setOnClickPendingIntent(R.id.imageview_notification_previous, pendingPrevious);

        //show notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "");
        builder.setSmallIcon(R.drawable.detailsong_clock)
                .setAutoCancel(true)
                .setContentIntent(pendingShow)
                .setCustomContentView(viewSmall)
                .setCustomBigContentView(viewBig);

        notificationManager.notify(0, builder.build());
    }
}
