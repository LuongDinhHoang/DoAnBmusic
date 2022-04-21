package com.example.tung_hoang_bmusic.ui.library;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tung_hoang_bmusic.R;
import com.example.tung_hoang_bmusic.activity.AddSongToPlaylist;
import com.example.tung_hoang_bmusic.activity.MainActivity;
import com.example.tung_hoang_bmusic.auth.HomeAuthActivity;
import com.example.tung_hoang_bmusic.model.Constants;
import com.example.tung_hoang_bmusic.model.Playlist;
import com.example.tung_hoang_bmusic.ui.recycler.BaseRecyclerViewHolder;
import com.example.tung_hoang_bmusic.ui.recycler.FirebaseListAdapter;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerActionListener;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerViewType;
import com.firebase.client.Firebase;
import com.firebase.client.Query;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class PlayListFragment extends Fragment {
    public static final String NAME_PLAYLIST_KEY = "namePlaylist";

    private LinearLayout mButtonNewPlayList;
    private RecyclerView mRecyclerView;

    private LibraryViewModel mLibViewModel;
    FirebaseListAdapter<Playlist> mAdapter;

    private RecyclerActionListener actionListener = new RecyclerActionListener() {
        @Override
        public void onViewClick(int position, View view, BaseRecyclerViewHolder viewHolder) {
            // click vao 1 view trong playlist fragment chuyển sang fragment detail
            Playlist playlist = mAdapter.getData().get(position);
            mLibViewModel.setPlaylistFirstClick(playlist);
            mLibViewModel.setCurrentRef(getRef().child(mAdapter.getKey(playlist)).toString());
        }

        @Override
        public void updatePlaylistFromButton(Playlist playlist, CONTROL_UPDATE state) {
            switch (state) {
                case UPDATE_NAME_PLAYLIST:
                    editNamePlaylist(playlist);
                    break;
                case ADD_SONG_TO_PLAYLIST:
                    addSongToPlaylist(playlist);
                    break;
                case DELETE_PLAYLIST:
                    deletePlaylist(playlist);
                    break;
            }
        }
    };

    public PlayListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.playlist_library_fragment, container, false);

        mButtonNewPlayList = view.findViewById(R.id.line1);
        mButtonNewPlayList.setOnClickListener(v -> {
            if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                Intent intent = new Intent(getContext(), HomeAuthActivity.class);
                startActivity(intent);
                return;
            }
            // chuyen giao dien tao playlist
            disPlayDialogCreatePlayList();
        });
        mRecyclerView = view.findViewById(R.id.recycler_playlist);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(manager);

        mLibViewModel =
                new ViewModelProvider(requireActivity()).get(LibraryViewModel.class);

        setupPlaylist();
        return view;
    }

    private void setupPlaylist() {
        Query query = getRef().orderByKey();
        mAdapter = new FirebaseListAdapter<Playlist>(actionListener, ((MainActivity) getActivity()).getService(), query, Playlist.class) {
            @Override
            public int getItemViewType(int position) {
                return RecyclerViewType.TYPE_PLAYLIST_LIBRARY;
            }
        };
        mRecyclerView.setAdapter(mAdapter);
    }

    private Firebase getRef() {
        String pathUser = "noUser";
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            pathUser = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getEmail().hashCode());
        }

        return new Firebase(Constants.FIREBASE_REALTIME_DATABASE_URL).child(Constants.FIREBASE_REALTIME_PLAYLIST_USER_PATH).child(pathUser);
    }

    private void disPlayDialogCreatePlayList() {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_create_playlist, null);
        final EditText titlePlaylist = (EditText) alertLayout.findViewById(R.id.input_name_playlist);

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Hủy", (dialog, which) -> Toast.makeText(getContext(), "Đã Hủy", Toast.LENGTH_SHORT).show());

        alert.setPositiveButton("Tạo Playlist", (dialog, which) -> {
            // code for matching password
            String user = titlePlaylist.getText().toString();
            Playlist mNewPlaylist = new Playlist();
            mNewPlaylist.setNamePlaylist(user);
            getRef().push().setValue(mNewPlaylist);
            mAdapter.notifyDataSetChanged();
            Toast.makeText(getContext(), "Đã tạo danh sách phát: " + user, Toast.LENGTH_SHORT).show();
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void editNamePlaylist(Playlist playlist) {
        LayoutInflater inflater = getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.dialog_create_playlist, null);
        final EditText titlePlaylist = (EditText) alertLayout.findViewById(R.id.input_name_playlist);
        TextView title = alertLayout.findViewById(R.id.title_dialog_create_playList);
        title.setText("Đổi tên Playlist");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            titlePlaylist.setHint(playlist.getNamePlaylist());
        }

        AlertDialog.Builder alert = new AlertDialog.Builder(getContext());
        alert.setView(alertLayout);
        alert.setCancelable(false);
        alert.setNegativeButton("Hủy", (dialog, which) -> Toast.makeText(getContext(), "Đã Hủy", Toast.LENGTH_SHORT).show());

        alert.setPositiveButton("Cập nhật", (dialog, which) -> {
            // code for matching password
            String user = titlePlaylist.getText().toString();
            playlist.setNamePlaylist(user);
            getRef().child(mAdapter.getKey(playlist)).child(NAME_PLAYLIST_KEY).setValue(user);
            Toast.makeText(getContext(), "Đã cập nhật tên thành: " + user, Toast.LENGTH_SHORT).show();
        });
        AlertDialog dialog = alert.create();
        dialog.show();
    }

    private void deletePlaylist(Playlist playlist) {
        getRef().child(mAdapter.getKey(playlist)).removeValue();
    }

    private void addSongToPlaylist(Playlist playlist) {
        Intent intent = new Intent(getActivity(), AddSongToPlaylist.class);
        intent.putExtra(AddSongToPlaylist.EXTRA_PLAYLIST, getRef().child(mAdapter.getKey(playlist)).toString());
        startActivity(intent);
    }

    private void saveData(ArrayList<Playlist> listPlaylist) {
        SharedPreferences.Editor editor = getActivity().getPreferences(Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = null;
        json = gson.toJson(listPlaylist);
        editor.putString("PLAYLIST", json);
        editor.apply();
    }

    public ArrayList<Playlist> listPlaylistPref() {
        Gson gson = new Gson();
        SharedPreferences mSharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        String json = mSharedPreferences.getString("PLAYLIST", null);
        Type type = new TypeToken<ArrayList<Playlist>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}
