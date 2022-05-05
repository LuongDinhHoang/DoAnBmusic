package com.example.tung_hoang_bmusic.ui.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tung_hoang_bmusic.R;
import com.example.tung_hoang_bmusic.activity.MainActivity;
import com.example.tung_hoang_bmusic.model.Playlist;
import com.example.tung_hoang_bmusic.model.Song;
import com.example.tung_hoang_bmusic.ui.recycler.BaseRecyclerAdapter;
import com.example.tung_hoang_bmusic.ui.recycler.BaseRecyclerViewHolder;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerActionListener;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerViewType;

import java.util.List;

public class HomeOverviewFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLinearLayout;
    private BaseRecyclerAdapter<Playlist> adapter;

    RecyclerActionListener mRecyclerActionListener = new RecyclerActionListener() {
        @Override
        public void onViewClick(int position, View view, BaseRecyclerViewHolder viewHolder) {
        }

        @Override
        public void onViewLongClick(int position, View view, BaseRecyclerViewHolder viewHolder) {
        }

        @Override
        public void clickSong(Song song) {
            if (TextUtils.isEmpty(song.getNameSong())
                    && TextUtils.isEmpty(song.getSinger())
                    && TextUtils.isEmpty(song.getDuration())
                    && TextUtils.isEmpty(song.getLinkUrl())
                    && TextUtils.isEmpty(song.getImageUrl())) {
                Toast.makeText(getContext(), "Không thể mở bài hát do lỗi dữ liệu!!!", Toast.LENGTH_SHORT).show();
                return;
            }
            homeViewModel.setSongFirstClick(song);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home_overview, container, false);
        mRecyclerView = root.findViewById(R.id.list_block_playlist_home);
//        mRecyclerView.setHasFixedSize(true);

        mLinearLayout = new LinearLayoutManager(getContext());
        mRecyclerView.setLayoutManager(mLinearLayout);

        adapter = new BaseRecyclerAdapter<Playlist>(mRecyclerActionListener, ((MainActivity) getActivity()).getService()) {
            @Override
            public int getItemViewType(int position) {
                return RecyclerViewType.TYPE_BLOCK_HOME_CATEGORY;
            }
        };
        mRecyclerView.setAdapter(adapter);

        homeViewModel =
                new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        homeViewModel.getPlaylist().observe(getViewLifecycleOwner(), new Observer<List<Playlist>>() {
            @Override
            public void onChanged(List<Playlist> playlists) {
                adapter.update(playlists);
            }
        });

        return root;
    }
}