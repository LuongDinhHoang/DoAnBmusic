package com.example.tung_hoang_bmusic.ui.media_playback;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tung_hoang_bmusic.R;
import com.example.tung_hoang_bmusic.auth.HomeAuthActivity;
import com.example.tung_hoang_bmusic.model.Song;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import de.hdodenhof.circleimageview.CircleImageView;

public class ImageFragment extends Fragment implements View.OnClickListener{

    private static final int STORAGE_PERMISSION_REQUEST_CODE = 1001;
    private CircleImageView mImageSongMedia;
    private ObjectAnimator mObjectAnimator;
    private ImageView mImgDownloadSong;
    private Song mSong;

    public ImageFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.image_song_media, container, false);

        mImageSongMedia = view.findViewById(R.id.image_song_media_play);
        mImgDownloadSong = view.findViewById(R.id.img_download_song);
        mObjectAnimator = ObjectAnimator.ofFloat(mImageSongMedia, "rotation", 0f, 360f);
        mObjectAnimator.setDuration(10000);
        mObjectAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimator.setRepeatMode(ValueAnimator.RESTART);
        mObjectAnimator.setInterpolator(new LinearInterpolator());

        mImgDownloadSong.setOnClickListener(this);
        
        MediaPlaybackModel mediaPlaybackModel =
                new ViewModelProvider(requireActivity()).get(MediaPlaybackModel.class);
        mediaPlaybackModel.getPathImage().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String pathImage) {
               updateImageSong(pathImage);
            }
        });

        mediaPlaybackModel.getSong().observe(getViewLifecycleOwner(), new Observer<Song>() {
            @Override
            public void onChanged(Song song) {
                mSong = song;
                if (mSong == null){
                    mImgDownloadSong.setVisibility(View.INVISIBLE);
                }
            }
        });
        

        return view;
    }

    public void updateImageSong(String imagePath){
        Bitmap bitmap = loadImageFromPath(imagePath);
        if (bitmap == null) {
            Glide.with(mImageSongMedia)
                    .load(imagePath)
                    .apply(RequestOptions.
                            placeholderOf(R.drawable.music))
                    .into(mImageSongMedia);
        } else {
            mImageSongMedia.setImageBitmap(bitmap);
        }
    }

    //lay anh theo bitmap neu co path
    public Bitmap loadImageFromPath(String path) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] data = mediaMetadataRetriever.getEmbeddedPicture();
        return data == null ? null : BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    @Override
    public void onClick(View v) {
        if (v == mImgDownloadSong){
            downloadSongFromFragment();
        }
    }

    private void downloadSongFromFragment(){
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if (user == null) {
            //neu chua dang nhap can dang nhap truoc can dang nhap truoc
            Intent intent = new Intent(getActivity(), HomeAuthActivity.class);
            startActivity(intent);
        } else {
            // neu dang dang nhap thi tai luon
            if (ContextCompat.checkSelfPermission(requireActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        STORAGE_PERMISSION_REQUEST_CODE);
            } else {
                // Permission has already been granted
                downloadSong();
            }

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case STORAGE_PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0 && permissions[0].equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // check whether storage permission granted or not.
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // do what you want;
                        downloadSong();
                    }
                }
                break;
            default:
                break;
        }
    }

    private void downloadSong() {
        if (mSong == null)
            return;
        try {
            String result = java.net.URLDecoder.decode(mSong.getLinkUrl(), StandardCharsets.UTF_8.name());
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(result);
            storageReference.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                Toast.makeText(getContext(), " Tải bài hát ", Toast.LENGTH_LONG).show();
                new ImageFragment.DownloadFileFromURL().execute(downloadUrl.toString(), mSong.getNameSong() + ".mp3");
            });
        } catch (UnsupportedEncodingException e) {
            Log.i("TungDV", "downloadSong: 1");
            // not going to happen - value came from JDK's own StandardCharsets
        }
    }

    private static class DownloadFileFromURL extends AsyncTask<String, String, Void> {

        /**
         * Before starting background thread Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected Void doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // this will be useful so that you can show a tipical 0-100%
                // progress bar
                int lenghtOfFile = connection.getContentLength();

                // download the file
                InputStream input = new BufferedInputStream(url.openStream(),
                        8192);

                File file = new File(Environment
                        .getExternalStorageDirectory().toString()
                        + "/Download/" + f_url[1]);
                // Output stream
                OutputStream output = new FileOutputStream(file);

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
        }

        /**
         * After completing background task Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(Void file_url) {
            // dismiss the dialog after the file was downloaded
        }

    }
}
