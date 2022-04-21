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
        ArrayList<Playlist> mData = new ArrayList<>();
        // TungDV Get list MPH sap xep theo ngay ra mat
        new Firebase(Constants.FIREBASE_REALTIME_DATABASE_URL).child(Constants.FIREBASE_REALTIME_SONG_PATH).orderByChild("releaseDate")
                .limitToLast(6).addListenerForSingleValueEvent(new ValueEventListener() {
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
                        Playlist playlist = new Playlist(1,"Mới phát hành", new ArrayList<>(data.values()));
                        mData.add(playlist);
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        new Firebase(Constants.FIREBASE_REALTIME_DATABASE_URL).child(Constants.FIREBASE_REALTIME_HOME_PATH).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Gson gson = new Gson();

                Object object = dataSnapshot.getValue(Object.class);
                String json = gson.toJson(object);

                Type listType = new TypeToken<ArrayList<Playlist>>() {}.getType();
                ArrayList<Playlist> data = gson.fromJson(json, listType);

                for (int i =0; i < data.size() ; i++)
                    mData.add(data.get(i));
                homeViewModel.setPlaylist(mData);

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
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