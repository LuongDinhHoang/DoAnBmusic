package com.example.tung_hoang_bmusic.ui.holder;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.tung_hoang_bmusic.R;
import com.example.tung_hoang_bmusic.model.Song;
import com.example.tung_hoang_bmusic.service.MediaPlaybackService;
import com.example.tung_hoang_bmusic.ui.recycler.BaseRecyclerViewHolder;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerActionListener;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerData;

import es.claucookie.miniequalizerlibrary.EqualizerView;

public class SongItemInPlayListHolder extends BaseRecyclerViewHolder {
    private ImageView mImageSong;
    private TextView mNameSong;
    private TextView mArtistSong;
    private ImageView mOptionSongInPlaylist;
    private EqualizerView mEqualizerView;

    private MediaPlaybackService mService;

    private RecyclerActionListener mAction;

    public SongItemInPlayListHolder(@NonNull View itemView) {
        super(itemView);
        mImageSong = itemView.findViewById(R.id.id_image_song);
        mNameSong = itemView.findViewById(R.id.id_name_song);
        mArtistSong = itemView.findViewById(R.id.artist_song);
        mOptionSongInPlaylist = itemView.findViewById(R.id.id_option_song_playlist);
        mEqualizerView = itemView.findViewById(R.id.equalizer);
    }

    @Override
    public void bindViewHolder(RecyclerData data) {
        if( data instanceof Song){
            Song song = (Song) data;
            if(mService != null){
                Song playingSong = mService.getPlayingSong();
                updateEqualizerView(playingSong != null && playingSong.getId() == song.getId() && mService.isMusicPlay() && mService.isPlaying(), song);
            }else {
                updateEqualizerView(false, song);
            }
            mNameSong.setText(song.getNameSong());
            mArtistSong.setText(song.getSinger());
            mOptionSongInPlaylist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popupMenu = new PopupMenu(itemView.getContext(), mOptionSongInPlaylist);
                    popupMenu.inflate(R.menu.menu_song_in_playlist);
                    popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.delete_song_in_playlist:
                                    updateSongFromButton(song, RecyclerActionListener.CONTROL_UPDATE.DELETE_SONG);
                                    return true;
                                case R.id.add_favorite:
                                    updateSongFromButton(song, RecyclerActionListener.CONTROL_UPDATE.ADD_FAVORITE);
                                    return true;
                                default:
                                    return false;
                            }
                        }
                    });
                    popupMenu.show();
                }
            });
        }

    }

    //update sóng khi phát 1 bài hát
    public void updateEqualizerView(boolean isPlay, Song song){
        if( isPlay ){
            mEqualizerView.animateBars();
        } else if (!mEqualizerView.isAnimating()){
            mEqualizerView.stopBars();
            if (song.loadImageFromPath(song.getPathSong()) == null) {
                mImageSong.setImageResource(R.drawable.ic_queue_music_black_24dp);
            } else {
                mImageSong.setImageBitmap(song.loadImageFromPath(song.getPathSong()));
            }
        }
        mEqualizerView.setVisibility(isPlay ? View.VISIBLE : View.INVISIBLE);
        mImageSong.setVisibility(isPlay ? View.INVISIBLE : View.VISIBLE );
    }

    @Override
    public void setupClickableViews(RecyclerActionListener actionListener) {
        mAction = actionListener;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onViewClick(getAdapterPosition(), v, SongItemInPlayListHolder.this);
            }
        });
    }

    @Override
    public void setService(MediaPlaybackService service) {
        mService = service;
    }

    private void updateSongFromButton(Song song, RecyclerActionListener.CONTROL_UPDATE state){
        mAction.updateSongFromMenuButton(song, state);
    }

}
