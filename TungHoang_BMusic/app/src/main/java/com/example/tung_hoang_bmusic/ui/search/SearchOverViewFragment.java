package com.example.tung_hoang_bmusic.ui.search;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tung_hoang_bmusic.R;
import com.example.tung_hoang_bmusic.model.Constants;
import com.example.tung_hoang_bmusic.model.ImageSearchModel;
import com.example.tung_hoang_bmusic.model.Song;
import com.example.tung_hoang_bmusic.ui.recycler.BaseRecyclerAdapter;
import com.example.tung_hoang_bmusic.ui.recycler.BaseRecyclerViewHolder;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerActionListener;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerViewType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchOverViewFragment extends Fragment {

    private SearchViewModel searchViewModel;
    private SearchView mSearchView;
    private RecyclerView mRecyclerViewPodCasts;
    private RecyclerView mRecyclerViewSearch;
    private GridLayoutManager mGridLayout;
    BaseRecyclerAdapter<Song> mViewSearchAdapter;
    BaseRecyclerAdapter<ImageSearchModel> mAdapter;

    RecyclerActionListener mRecyclerViewAction = new RecyclerActionListener() {
        @Override
        public void onViewClick(int position, View view, BaseRecyclerViewHolder viewHolder) {
            searchViewModel.setImageSearchFirstClick(mAdapter.getData().get(position));
        }

        @Override
        public void onViewLongClick(int position, View view, BaseRecyclerViewHolder viewHolder) {
            //thưc hien goi detail fragment
            searchViewModel.setItemSearchFirstClick(mViewSearchAdapter.getData().get(position));
        }

        @Override
        public void clickSong(Song song) {
            super.clickSong(song);
        }
    };

    private ValueEventListener mValueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            System.out.println(snapshot);
            Gson gson = new Gson();

            Object object = snapshot.getValue(Object.class);
            String json = gson.toJson(object);

            try {
                Type listType = new TypeToken<HashMap<String, Song>>() {
                }.getType();
                HashMap<String, Song> data = gson.fromJson(json, listType);
                if (data != null) {
                    mViewSearchAdapter.update(new ArrayList<>(data.values()));
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                Type listType = new TypeToken<ArrayList<Song>>() {
                }.getType();
                ArrayList<Song> data = gson.fromJson(json, listType);
                if (data != null) {
                    mViewSearchAdapter.update(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        searchViewModel =
                new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search_overview, container, false);

        mSearchView = root.findViewById(R.id.search_view);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(
                Context.SEARCH_SERVICE);
        mSearchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextChange(String newText) {
                doFilterAsync(newText);
                return true;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                doFilterAsync(query);

                return true;
            }

            void doFilterAsync(String queryText) {
                boolean isSearchViewVisible = !TextUtils.isEmpty(queryText);
                mRecyclerViewSearch.setVisibility(
                        isSearchViewVisible ? View.VISIBLE : View.INVISIBLE);
                mRecyclerViewPodCasts.setVisibility(
                        isSearchViewVisible ? View.INVISIBLE : View.VISIBLE);

                if (!isSearchViewVisible) return;

                Query queryRef = FirebaseDatabase.getInstance().getReference(
                        Constants.FIREBASE_REALTIME_SONG_PATH)
                        .orderByChild("nameSong")
                        .startAt(queryText)
                        .endAt(queryText + "\uf8ff")
                        .limitToFirst(10);
                queryRef.addValueEventListener(mValueEventListener);
            }
        });
        mRecyclerViewPodCasts = root.findViewById(R.id.recycler_view_podcasts);
        mRecyclerViewPodCasts.setHasFixedSize(true);

        mGridLayout = new GridLayoutManager(getContext(), 2);
        mRecyclerViewPodCasts.setLayoutManager(mGridLayout);

        mAdapter = new BaseRecyclerAdapter<ImageSearchModel>(getData(), mRecyclerViewAction) {
            @Override
            public int getItemViewType(int position) {
                return RecyclerViewType.TYPE_IMAGE_SEARCH;
            }
        };

        mRecyclerViewPodCasts.setAdapter(mAdapter);

        mRecyclerViewSearch = root.findViewById(R.id.recycler_view_search);
        mRecyclerViewSearch.setHasFixedSize(true);
        mRecyclerViewSearch.setLayoutManager(new LinearLayoutManager(getContext()));
        mViewSearchAdapter = new BaseRecyclerAdapter<Song>(new ArrayList<>(), mRecyclerViewAction) {
            @Override
            public int getItemViewType(int position) {
                return RecyclerViewType.TYPE_SONG_SEARCH;
            }
        };
        mRecyclerViewSearch.setAdapter(mViewSearchAdapter);

        return root;
    }

    private List<ImageSearchModel> getData() {
        List<ImageSearchModel> data = new ArrayList<ImageSearchModel>();
        data.add(new ImageSearchModel("Nhạc Việt", R.drawable.nhac_viet));
        data.add(new ImageSearchModel("Hip-Hop", R.drawable.hip_hop));
        data.add(new ImageSearchModel("Pop", R.drawable.pop));
        data.add(new ImageSearchModel("Lãng mạn", R.drawable.lang_man));
        data.add(new ImageSearchModel("K-Pop", R.drawable.k_pop));
        data.add(new ImageSearchModel("Thư giãn", R.drawable.thu_gian));
        data.add(new ImageSearchModel("Mới phát hành", R.drawable.moi_phat_hanh));
        data.add(new ImageSearchModel("Thịnh hành", R.drawable.thinh_hanh));
        return data;
    }
}
