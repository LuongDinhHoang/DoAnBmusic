package com.example.tung_hoang_bmusic.ui.media_playback;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.tung_hoang_bmusic.R;

public class LyricsFragment extends Fragment {
    private TextView mLyricsView;

    public LyricsFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lyrics_song_fragment, container, false);
        mLyricsView = view.findViewById(R.id.lyrics_song);
        mLyricsView.setMovementMethod(new ScrollingMovementMethod());
        mLyricsView.setText(/*R.string.lyrics_default*/"Updating...");
        return view;
    }
}
