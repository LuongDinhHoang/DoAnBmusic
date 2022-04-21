package com.example.tung_hoang_bmusic.ui.bottomsheet;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.tung_hoang_bmusic.R;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class PlaybackDetailFragment extends BottomSheetDialogFragment {

    @Override
    public int getTheme() {
        return R.style.BottomSheetDialogTheme;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle args = getArguments();

        return inflater.inflate(R.layout.ccnews_fragment_articles, container, false);
    }


}
