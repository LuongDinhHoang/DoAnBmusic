package com.example.tung_hoang_bmusic.ui.home;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tung_hoang_bmusic.R;
import com.example.tung_hoang_bmusic.activity.ActivityViewModel;
import com.example.tung_hoang_bmusic.activity.MainActivity;
import com.example.tung_hoang_bmusic.auth.HomeAuthActivity;
import com.example.tung_hoang_bmusic.model.PlaySong;
import com.example.tung_hoang_bmusic.model.Song;
import com.example.tung_hoang_bmusic.service.MediaPlaybackService;
import com.example.tung_hoang_bmusic.ui.search.SearchViewModel;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

public class DetailSongFragment extends Fragment implements View.OnClickListener {
    private static final int STORAGE_PERMISSION_REQUEST_CODE = 1001;
    private ImageView mImageView;
    private TextView mTextSong;
    private TextView mArtistSong;
    private TextView mDurationSong;
    private LinearLayout mDownloadSong;
    private ImageView mPlaySong;

    private Song song;

    private HomeViewModel homeViewModel;
    private ActivityViewModel mActivityViewModel;
    MediaPlaybackService mService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_song_home, container, false);
        mImageView = view.findViewById(R.id.image_view_detail);
        mTextSong = view.findViewById(R.id.name_song_detail);
        mArtistSong = view.findViewById(R.id.artist_song_detail);
        mDurationSong = view.findViewById(R.id.duration_detail);
        mDownloadSong = view.findViewById(R.id.download_song);
        mPlaySong = view.findViewById(R.id.play_song_detail);

        mDownloadSong.setOnClickListener(this);
        mPlaySong.setOnClickListener(this);

        mService = ((MainActivity) getActivity()).getService();
        if (mService != null)
            mService.listenChangeDetailFragment(() -> updateSong(song));

        homeViewModel =
                new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeViewModel.getDetailSong().observe(getViewLifecycleOwner(), song -> {
            if (song != null) {
                updateSong(song);
            }
        });

        SearchViewModel mSearchViewModel =
                new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        mSearchViewModel.getClickSong().observe(getViewLifecycleOwner(), song -> {
            if (song != null) {
                updateSong(song);
                mSearchViewModel.setClickSong(null);
            }
        });

        mActivityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);
        return view;
    }

    @Override
    public void onClick(View v) {
        if (v == mDownloadSong) {
            //download song
            //check xem login chưa, nếu chưa login thì yêu cầu logic hoặc sign up
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

        } else if (v == mPlaySong) {
            //play music
            if (mService != null) {
                if (mService.isMusicPlay() && mService.isPlaying()) {
                    mPlaySong.setImageResource(R.drawable.ic_play_circle_filled_orange_24dp);
                    mService.pause();
                } else {
                    mPlaySong.setImageResource(R.drawable.ic_pause_circle_filled_orange_24dp);
                    mActivityViewModel.setPlaylist(new PlaySong(song, new ArrayList<>(Collections.singletonList(song))));
                }
            }
        }
    }

    public void updateSong(Song song) {
        if (song != null) {
            this.song = song;
            Glide.with(mImageView)
                    .load(song.getImageUrl())
                    .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                    .apply(RequestOptions.
                            placeholderOf(R.drawable.placeholder_music))
                    .into(mImageView);
            mTextSong.setText(song.getNameSong());
            mArtistSong.setText(song.getSinger());
            mDurationSong.setText(new SimpleDateFormat("mm:ss").format(Integer.parseInt(song.getDuration())));
            if (mService != null) {
                Song playingSong = ((MainActivity) getActivity()).getService().getPlayingSong();
                mPlaySong.setImageResource(playingSong != null && playingSong.getId() == song.getId() && mService.isMusicPlay() && mService.isPlaying()
                        ? R.drawable.ic_pause_circle_filled_orange_24dp : R.drawable.ic_play_circle_filled_orange_24dp);
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

    public Context mContext;

    private void downloadSong() {
        if (song == null) return;
        try {
            String result = java.net.URLDecoder.decode(song.getLinkUrl(), StandardCharsets.UTF_8.name());
            StorageReference storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(result);
            storageReference.getDownloadUrl().addOnSuccessListener(downloadUrl -> {
                Toast.makeText(getContext(), " Tải bài hát ", Toast.LENGTH_LONG).show();
                mContext = getContext();
                new DownloadFileFromURL().execute(downloadUrl.toString(), song.getNameSong() + ".mp3");
            });
        } catch (UnsupportedEncodingException e) {
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
