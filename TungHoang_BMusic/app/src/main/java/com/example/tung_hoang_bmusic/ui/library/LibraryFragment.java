package com.example.tung_hoang_bmusic.ui.library;

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
import com.example.tung_hoang_bmusic.model.Playlist;

public class LibraryFragment extends Fragment {

    private LibraryViewModel mLibraryViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        mLibraryViewModel =
                new ViewModelProvider(requireActivity()).get(LibraryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_library, container, false);
        mLibraryViewModel.openDetailPlaylist().observe(getViewLifecycleOwner(), new Observer<Playlist>() {
            @Override
            public void onChanged(Playlist playlist) {
                if (playlist != null){
                    LibraryFragment.this.openDetailFragment();
                    mLibraryViewModel.setDetailPlaylist(playlist);
                    mLibraryViewModel.setPlaylistFirstClick(null);
                }
            }
        });

        openOverviewFragment();
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void openOverviewFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.container_fragment_library, new LibraryOverViewFragment(),
                        LibraryOverViewFragment.class.getName())
                .addToBackStack(null)
                .commit();
    }

    private void openDetailFragment() {
        getParentFragmentManager().beginTransaction()
                .replace(R.id.container_fragment_library, new DetailPlayListFragment(), DetailPlayListFragment.class.getName())
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