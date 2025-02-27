package com.example.tung_hoang_bmusic.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.tung_hoang_bmusic.R;
import com.example.tung_hoang_bmusic.activity.ActivityViewModel;
import com.example.tung_hoang_bmusic.activity.SettingsActivity;
import com.example.tung_hoang_bmusic.model.Album;
import com.example.tung_hoang_bmusic.model.Constants;
import com.example.tung_hoang_bmusic.model.PlaySong;
import com.example.tung_hoang_bmusic.model.Playlist;
import com.example.tung_hoang_bmusic.model.Song;
import com.example.tung_hoang_bmusic.ui.recycler.BaseRecyclerViewHolder;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerActionListener;
import com.example.tung_hoang_bmusic.ui.splashscreen.SplashScreenFragment;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private ActivityViewModel mActivityViewModel;
    private ValueEventListener mConnectedListener;

    RecyclerActionListener mRecyclerActionListener = new RecyclerActionListener() {
        @Override
        public void onViewClick(int position, View view, BaseRecyclerViewHolder viewHolder) {
        }

        @Override
        public void onViewLongClick(int position, View view, BaseRecyclerViewHolder viewHolder) {
        }

        @Override
        public void clickSong(Song song) {
            super.clickSong(song);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        homeViewModel =
                new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        mActivityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        homeViewModel.openDetailSong().observe(getViewLifecycleOwner(), new Observer<Song>() {
            @Override
            public void onChanged(Song song) {
                if(song != null) {
                    openDetailFragment(song);
                    homeViewModel.setDetailSong(song);
                    homeViewModel.setSongFirstClick(null);
                }
            }
        });

        openOverviewFragment();
        openOverviewSplashFragment();

        // lay thong tin tren firebase
        getData();

        return root;
    }

    private void openOverviewFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.home_fragment_container, new HomeOverviewFragment(), HomeOverviewFragment.class.getName())
                .commit();
    }

    private void openOverviewSplashFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.splash_screen, new SplashScreenFragment(), SplashScreenFragment.class.getName())
                .commit();
    }

    private void openDetailFragment(Song song) {
//        getParentFragmentManager().beginTransaction()
//                .replace(R.id.home_fragment_container, new DetailSongFragment(), DetailSongFragment.class.getName())
//                .addToBackStack(null)
//                .commit();
        mActivityViewModel.setPlaylist(new PlaySong(song, new ArrayList<>(Collections.singletonList(song))));
    }

    //Create data
    private void getData() {
        final ArrayList<Playlist>[] mData = new ArrayList[]{new ArrayList<Playlist>()};
        final Playlist[] latestRelease = {null};
        // TungDV Get list MPH sap xep theo ngay                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                 ra mat
        new Firebase(Constants.FIREBASE_REALTIME_DATABASE_URL).child(Constants.FIREBASE_REALTIME_SONG_PATH).orderByChild("releaseDate")
                .limitToLast(10).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Gson gson = new Gson();

                Object object = dataSnapshot.getValue(Object.class);
                String json = gson.toJson(object);

                try {
                    Type listType = new TypeToken<HashMap<String, Song>>() {
                    }.getType();
                    HashMap<String, Song> data = gson.fromJson(json, listType);
                    if (data != null) {
                        latestRelease[0] = new Playlist(-1, "Mới phát hành", new ArrayList<>(data.values()));
                        updatePlaylist(latestRelease[0], mData[0]);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

                    }
                });

        mConnectedListener = new Firebase(Constants.FIREBASE_REALTIME_DATABASE_URL).child(Constants.FIREBASE_REALTIME_ALBUM_PATH)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Gson gson = new Gson();

                        Object object = dataSnapshot.getValue(Object.class);
                        String json = gson.toJson(object);

                        Type listType = new TypeToken<ArrayList<Album>>() {
                        }.getType();
                        ArrayList<Album> data = gson.fromJson(json, listType);

                        if (data == null) return;
                        data.removeAll(Collections.singletonList(null));
                        mData[0] = new ArrayList<>();
                        for (Album a : data)
                            if (a.getSongList().size() > 0) {
                                mData[0].add(new Playlist(a));
                            }
                        updatePlaylist(latestRelease[0], mData[0]);

                    }

                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    private void updatePlaylist(Playlist latest, ArrayList<Playlist> data) {
        if (latest != null)
            data.add(0, latest);
        homeViewModel.setPlaylist(data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        new Firebase(Constants.FIREBASE_REALTIME_DATABASE_URL).child(Constants.FIREBASE_REALTIME_ALBUM_PATH)
                .removeEventListener(mConnectedListener);
    }

    // TungDV click setting
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.setting_menu, menu);

        MenuItem settingItem = menu.findItem(R.id.action_setting);

        settingItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
                return true;
            }
        });
    }
}