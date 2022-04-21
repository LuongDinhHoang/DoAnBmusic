package com.example.tung_hoang_bmusic.ui.library;

import android.content.ContentValues;
import android.database.Cursor;
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
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class OfflineSongFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    private RecyclerView mRecyclerView;
    private static final int LOADER_ID = 1;
    private BaseRecyclerAdapter<Song> mAdapter;

    private ActivityViewModel mActivityViewModel;

    private LibraryViewModel mLibraryViewModel;

    private static final SimpleDateFormat formatTimeSong = new SimpleDateFormat("mm:ss");

    private RecyclerActionListener actionListener = new RecyclerActionListener() {
        @Override
        public void onViewClick(int position, View view, BaseRecyclerViewHolder viewHolder) {
            mActivityViewModel.setPlaylist(new PlaySong(position, new ArrayList<>(mAdapter.getData())));
            mAdapter.notifyDataSetChanged();
        }

        @Override
        public void updateSongFromMenuButton(Song song, CONTROL_UPDATE state) {
            switch (state) {
                case ADD_FAVORITE:
                    likeSong(song);
                    break;
                case DELETE_SONG:
                    //xoa bai hat offline
                    deleteSongOffline(song);
                    break;
                case DELETE_FAVORITE_SONG:
                    disLikeSong(song);
                    break;
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //load bai hat o tren thiet bi
        getLoaderManager().initLoader(LOADER_ID, null, this);

//        getArguments()
        View view = inflater.inflate(R.layout.offline_library_fragment, container, false);
        mRecyclerView = view.findViewById(R.id.recycler_offline);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(layoutManager);

        mAdapter = new BaseRecyclerAdapter<Song>(actionListener, ((MainActivity) getActivity()).getService()) {
            @Override
            public int getItemViewType(int position) {
                return RecyclerViewType.TYPE_OFFLINE_SONG_LIBRARY;
            }
        };
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


    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        return new CursorLoader(getContext(), MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        ArrayList<Song> songList = new ArrayList<>();
        if (data != null && data.getCount() > 0) {
            data.moveToFirst();
            do {
                try {
                    songList.add(new Song(data));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (data.moveToNext());
        }
        mAdapter.update(songList);
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
    }

    private void likeSong(Song song) {
        ContentValues values = new ContentValues();
        values.put(FavoriteSongsTable.ID_PROVIDER, song.getId());
        values.put(FavoriteSongsTable.IS_FAVORITE, 2);
        Cursor cursor = findSongById(song.getId());
        if (cursor != null && cursor.moveToFirst()) {
            getActivity().getContentResolver().update(FavoriteSongProvider.CONTENT_URI, values,
                    "id_provider = \"" + song.getId() + "\"", null);
        } else {
            getActivity().getContentResolver().insert(FavoriteSongProvider.CONTENT_URI, values);
        }
        Toast.makeText(getActivity().getBaseContext(),
                "Đã thêm bài hát vào yêu thích", Toast.LENGTH_LONG).show();
        mAdapter.notifyDataSetChanged();
    }

    // tim kiem theo id cua bai hat
    public Cursor findSongById(int id) {
        return getActivity().getContentResolver().query(FavoriteSongProvider.CONTENT_URI, new String[]{FavoriteSongsTable.IS_FAVORITE},
                FavoriteSongsTable.ID_PROVIDER + "=?",
                new String[]{String.valueOf(id)}, null);
    }

    private void disLikeSong(Song song) {
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
        mAdapter.notifyDataSetChanged();
    }

    private void deleteSongOffline(Song song) {

    }
}
