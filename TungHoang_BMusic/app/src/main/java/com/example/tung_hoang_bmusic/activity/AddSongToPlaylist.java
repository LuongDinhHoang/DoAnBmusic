package com.example.tung_hoang_bmusic.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tung_hoang_bmusic.R;
import com.example.tung_hoang_bmusic.model.Constants;
import com.example.tung_hoang_bmusic.model.Playlist;
import com.example.tung_hoang_bmusic.model.Song;
import com.example.tung_hoang_bmusic.ui.recycler.BaseRecyclerAdapter;
import com.example.tung_hoang_bmusic.ui.recycler.BaseRecyclerViewHolder;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerActionListener;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerData;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerViewType;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddSongToPlaylist extends AppCompatActivity {
    public static final String EXTRA_PLAYLIST = "extra_playlist";

    public RecyclerView mRecyclerView;
    private BaseRecyclerAdapter<CheckboxSong> mAdapter;
    Firebase firebase;
    Playlist mPlaylist;

    private RecyclerActionListener actionListener = new RecyclerActionListener() {
        @Override
        public void onViewClick(int position, View view, BaseRecyclerViewHolder viewHolder) {
            mAdapter.notifyDataSetChanged();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_song_playlist_list);

        String playlistPath = getIntent().getStringExtra(EXTRA_PLAYLIST);
        if (!TextUtils.isEmpty(playlistPath)) {
            try {
                firebase = new Firebase(playlistPath);
                firebase.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mPlaylist = dataSnapshot.getValue(Playlist.class);
                        if (mAdapter.getItemCount() > 0) {
                            for (CheckboxSong song : mAdapter.getData())
                                for (Song s : mPlaylist.getSongList())
                                    if (song.mSong.getId() == s.getId()) {
                                        song.mChecked = true;
                                        break;
                                    }
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        mRecyclerView = findViewById(R.id.recycler_add_song);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new BaseRecyclerAdapter<CheckboxSong>(getData()) {
            @Override
            public int getItemViewType(int position) {
                return RecyclerViewType.TYPE_ADD_SONG_IN_PLAYLIST;
            }
        };
        mRecyclerView.setAdapter(mAdapter);

        findViewById(R.id.cancel_playlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        findViewById(R.id.update_playlist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlaylist.getSongList().clear();
                for (CheckboxSong song : mAdapter.getData())
                    if (song.mChecked) {
                        mPlaylist.getSongList().add(song.mSong);
                    }
                firebase.setValue(mPlaylist);
                finish();
            }
        });
    }

    private List<CheckboxSong> getData() {
        List<CheckboxSong> dataSong = new ArrayList<>();
        new Firebase(Constants.FIREBASE_REALTIME_DATABASE_URL).child(Constants.FIREBASE_REALTIME_SONG_PATH)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Gson gson = new Gson();

                        Object object = dataSnapshot.getValue(Object.class);
                        String json = gson.toJson(object);

                        try {
                            Type listType = new TypeToken<ArrayList<Song>>() {
                            }.getType();
                            ArrayList<Song> data = gson.fromJson(json, listType);
                            if (data != null) {
                                for (Song s : data) {
                                    CheckboxSong checkboxSong = new CheckboxSong(s);
                                    if (mPlaylist != null)
                                        for (Song i : mPlaylist.getSongList())
                                            if (i.equals(s)) {
                                                checkboxSong.mChecked = true;
                                                break;
                                            }
                                    dataSong.add(checkboxSong);
                                }
                            }
                            mAdapter.update(dataSong);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });

        return dataSong;
    }

    public static class CheckboxSong implements RecyclerData {
        public Song mSong;
        public boolean mChecked;

        public CheckboxSong(Song mSong) {
            this.mSong = mSong;
        }

        @Override
        public int getViewType() {
            return 0;
        }

        @Override
        public boolean areItemsTheSame(RecyclerData other) {
            return false;
        }

        @Override
        public boolean areContentsTheSame(RecyclerData other) {
            return false;
        }
    }
}
