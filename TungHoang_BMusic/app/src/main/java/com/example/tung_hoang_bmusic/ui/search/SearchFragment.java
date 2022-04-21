package com.example.tung_hoang_bmusic.ui.search;

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
import com.example.tung_hoang_bmusic.activity.SettingsActivity;
import com.example.tung_hoang_bmusic.model.ImageSearchModel;
import com.example.tung_hoang_bmusic.model.Song;
import com.example.tung_hoang_bmusic.ui.home.DetailSongFragment;

public class SearchFragment extends Fragment {

    private SearchViewModel mSearchViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        mSearchViewModel =
                new ViewModelProvider(requireActivity()).get(SearchViewModel.class);
        View root = inflater.inflate(R.layout.fragment_search, container, false);
        mSearchViewModel.openDetailSearch().observe(getViewLifecycleOwner(), new Observer<ImageSearchModel>() {
            @Override
            public void onChanged(ImageSearchModel image) {
                if (image != null) {
                    SearchFragment.this.openDetailFragment();
                    mSearchViewModel.setDetailImageSearch(image);
                    mSearchViewModel.setImageSearchFirstClick(null);
                }
            }
        });

        mSearchViewModel.openDetailHomeFragment().observe(getViewLifecycleOwner(), new Observer<Song>() {
            @Override
            public void onChanged(Song song) {
                System.out.println("HoangLD song "+song);
                if(song != null) {
                    openDetailHomeFragment();
                    mSearchViewModel.setClickSong(song);
                    mSearchViewModel.setItemSearchFirstClick(null);
                }
            }
        });

        openOverviewFragment();
        return root;
    }


    private void openOverviewFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.container_fragment_search, new SearchOverViewFragment(),
                        SearchOverViewFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

    private void openDetailFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.container_fragment_search, new DetailSearchFragment(), DetailSearchFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

    private void openDetailHomeFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.container_fragment_search, new DetailSongFragment(), DetailSongFragment.class.getName())
                .addToBackStack(null)
                .commit();
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