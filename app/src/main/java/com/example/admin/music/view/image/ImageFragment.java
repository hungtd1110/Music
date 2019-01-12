package com.example.admin.music.view.image;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.admin.music.R;
import com.example.admin.music.model.entity.Song;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

/**
 * Created by admin on 1/10/2019.
 */

public class ImageFragment extends Fragment implements ImageViewListener {
    public static ImageViewListener callBack;

    private ImageView imvImage;
    private Song song;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_image, container, false);

        //controls
        imvImage = view.findViewById(R.id.imageview_image_image);

        //init
        callBack = this;

        getData();
        show();

        return view;
    }

    @Override
    public void update(Song song) {
        this.song = song;
        show();
    }

    private void show() {
        try {
            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            mmr.setDataSource(song.getPathAudio());
            byte[] artBytes = mmr.getEmbeddedPicture();
            if (artBytes != null) {
                InputStream is = new ByteArrayInputStream(mmr.getEmbeddedPicture());
                Bitmap bm = BitmapFactory.decodeStream(is);
                imvImage.setImageBitmap(bm);
            }
            else {
                imvImage.setImageResource(R.drawable.all_imagesong);
            }
        } catch (Exception e) {

        }
    }

    private void getData() {
        Bundle bundle = getArguments();
        song = (Song) bundle.getSerializable(getString(R.string.key_song));
    }

}
