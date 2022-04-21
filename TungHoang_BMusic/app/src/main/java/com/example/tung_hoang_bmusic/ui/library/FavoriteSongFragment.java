package com.example.tung_hoang_bmusic.ui.library;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tung_hoang_bmusic.R;
import com.example.tung_hoang_bmusic.activity.ActivityViewModel;
import com.example.tung_hoang_bmusic.activity.MainActivity;
import com.example.tung_hoang_bmusic.model.PlaySong;
import com.example.tung_hoang_bmusic.model.Song;
import com.example.tung_hoang_bmusic.provider.FavoriteSongProvider;
import com.example.tung_hoang_bmusic.provider.FavoriteSongsTable;
import com.example.tung_hoang_bmusic.ui.recycler.BaseRecyclerAdapter;
import com.example.tung_hoang_bmusic.ui.recycler.BaseRecyclerViewHolder;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerActionListener;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerViewType;

import java.util.ArrayList;

public class FavoriteSongFragment extends Fragment {

    private static final int LOADER_ID = 2;
    ArrayList<Song> mAllSongList;
    ArrayList<Song> mAllFavoriteSong;

    static final String AUTHORITY = "com.android.example.provider.FavoriteSongs";
    static final String CONTENT_PATH = "backupdata";
    static final String URL = "content://" + AUTHORITY + "/" + CONTENT_PATH;
    static final Uri CONTENT_URI = Uri.parse(URL);

    private RecyclerView mRecyclerView;
    private ActivityViewModel mActivityViewModel;
    private LibraryViewModel mLibraryViewModel;
    private BaseRecyclerAdapter<Song> mAdapter;


    private RecyclerActionListener mActionListener = new RecyclerActionListener() {
        @Override
        public void onViewClick(int position, View view, BaseRecyclerViewHolder viewHolder) {
            mActivityViewModel.setPlaylist(new PlaySong(position, new ArrayList<>(mAdapter.getData())));
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void updateSongFromMenuButton(Song song, CONTROL_UPDATE state) {
            if(state == CONTROL_UPDATE.DELETE_FAVORITE_SONG){
                disLikeSong(song);
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mAllSongList = loadAllSongs();
        ArrayList<Song> favoriteListSong = getFavoriteSong(mAllSongList);

        View view = inflater.inflate(R.layout.favorite_library_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_favorite);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new BaseRecyclerAdapter<Song>( mActionListener, ((MainActivity) getActivity()).getService()) {
            @Override
            public int getItemViewType(int position) {
                return RecyclerViewType.TYPE_FAVORITE_SONG_LIBRARY;
            }
        };

        mAdapter.update(favoriteListSong);

        mRecyclerView.setAdapter(mAdapter);

        mLibraryViewModel = new ViewModelProvider(requireActivity()).get(LibraryViewModel.class);
        mLibraryViewModel.getPlaySong().observe(getViewLifecycleOwner(), new Observer<Song>() {
            @Override
            public void onChanged(Song song) {
                mAdapter.notifyDataSetChanged();
            }
        });

        mActivityViewModel = new ViewModelProvider(requireActivity()).get(ActivityViewModel.class);
        return view;
    }


    public ArrayList<Song> loadAllSongs(){
        ArrayList<Song> list = new ArrayList<>();
        Cursor c = getActivity().getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
        if (c != null && c.moveToFirst()){
            do {
                try {
                    list.add(new Song(c));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (c.moveToNext());
        }
        return list;
    }

    public ArrayList<Song> getFavoriteSong(ArrayList<Song> list) {
        ArrayList<Song> songFavorite = new ArrayList<>();
        String selecfavorite = FavoriteSongsTable.IS_FAVORITE + "=2";
        String[] proje = {
                FavoriteSongsTable.ID_PROVIDER,
                FavoriteSongsTable.IS_FAVORITE,
                FavoriteSongsTable.COUNT_OF_PLAY};
        Cursor favorite = getContext().getContentResolver().query(FavoriteSongProvider.CONTENT_URI,
                proje, selecfavorite, null, null);
        if (favorite != null) {
            while (favorite.moveToNext())
                for (int j = 0; j < list.size(); j++) {
                    Song song1 = list.get(j);
                    if (song1.getId() == favorite.getInt(0)) {
                        songFavorite.add(song1);
                    }
                }
        }
        favorite.close();
        mAllFavoriteSong = songFavorite;
        return songFavorite;
    }

    private void disLikeSong(Song song){
        ContentValues values = new ContentValues();
        values.put(FavoriteSongsTable.ID_PROVIDER, song.getId());
        values.put(FavoriteSongsTable.IS_FAVORITE, 1);
        Cursor cursor = findSongById(song.getId());
        if (cursor != null && cursor.moveToFirst()) {
            getActivity().getContentResolver().update(FavoriteSongProvider.CONTENT_URI, values,
                    "id_provider = \"" + song.getId() + "\"", null);
        } else {
            getActivity().getContentResolver().insert(FavoriteSongProvider.CONTENT_URI, values);
        }
        Toast.makeText(getActivity().getBaseContext(),
                "Đã xoá bài hát khỏi yêu thích", Toast.LENGTH_LONG).show();
        mAllFavoriteSong.remove(song);
        mAdapter.notifyDataSetChanged();
    }

    // tim kiem theo id cua bai hat
    public Cursor findSongById(int id) {
        return getActivity().getContentResolver().query(FavoriteSongProvider.CONTENT_URI, new String[]{FavoriteSongsTable.IS_FAVORITE},
                FavoriteSongsTable.ID_PROVIDER + "=?",
                new String[]{String.valueOf(id)}, null);
    }


}
