package com.example.tung_hoang_bmusic.ui.holder;

import android.annotation.SuppressLint;
import android.view.Menu;
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

public class OfflineSongItemLibraryHolder extends BaseRecyclerViewHolder {
    private ImageView mImageOffline;
    private TextView mNameSongOffline;
    private TextView mDuration;
    private ImageView mOptionOffline;
    private EqualizerView mEqualizerView;

    //Xu ly su kien popup menu
    private PopupMenu mPopupMenu;
    private MediaPlaybackService mService;

    private RecyclerActionListener mAction;

    public OfflineSongItemLibraryHolder(@NonNull View itemView) {
        super(itemView);
        mImageOffline = itemView.findViewById(R.id.id_image_offline);
        mNameSongOffline = itemView.findViewById(R.id.id_item_name_song);
        mDuration = itemView.findViewById(R.id.id_item_duration);
        mOptionOffline = itemView.findViewById(R.id.id_option_menu);
        mEqualizerView = itemView.findViewById(R.id.equalizer);
    }

    @Override
    public void bindViewHolder(RecyclerData data) {
        if (data instanceof Song) {
            Song song = (Song) data;
            if(mService != null){
                Song playingSong = mService.getPlayingSong();
                updateEqualizerView(playingSong != null && playingSong.getId() == song.getId() && mService.isMusicPlay() && mService.isPlaying(), song);
            }else {
                updateEqualizerView(false, song);
            }
            mNameSongOffline.setText(song.getNameSong());
            mDuration.setText(song.getDuration());
            mOptionOffline.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mPopupMenu = new PopupMenu(itemView.getContext(), mOptionOffline);
                    mPopupMenu.inflate(R.menu.menu_offline_song);
                    Menu menu = mPopupMenu.getMenu();
                    MenuItem itemLike = menu.findItem(R.id.like_song);
                    MenuItem itemDisLike = menu.findItem(R.id.dis_like_song);
                    if ( mService.loadFavoriteStatus(song.getId()) == 2){
                        itemLike.setVisible(false);
                    } else {
                        itemDisLike.setVisible(false);
                    }
                    mPopupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {
                            switch (item.getItemId()) {
                                case R.id.like_song:
                                    updateSongFromMenu(song, RecyclerActionListener.CONTROL_UPDATE.ADD_FAVORITE);
                                    return true;
                                case R.id.delete_song:
                                    updateSongFromMenu(song, RecyclerActionListener.CONTROL_UPDATE.DELETE_SONG);
                                    return true;
                                case R.id.add_playlist_song:
                                    return true;
                                case R.id.dis_like_song:
                                    updateSongFromMenu(song, RecyclerActionListener.CONTROL_UPDATE.DELETE_FAVORITE_SONG);
                                default:
                                    return false;
                            }
                        }
                    });
                    mPopupMenu.show();
                }
            });
        }
    }
    //update sóng khi phát 1 bài hát
    @SuppressLint("SetTextI18n")
    public void updateEqualizerView(boolean isPlay, Song song){
        if( isPlay ){
            mEqualizerView.animateBars();
        } else if (!mEqualizerView.isAnimating()){
            mEqualizerView.stopBars();
            if (song.loadImageFromPath(song.getPathSong()) == null) {
                mImageOffline.setImageResource(R.drawable.music);
            } else {
                mImageOffline.setImageBitmap(song.loadImageFromPath(song.getPathSong()));
            }
        }
        mEqualizerView.getLayoutParams().height = 65;
        mEqualizerView.getLayoutParams().width = 65;
        mEqualizerView.requestLayout();
        mEqualizerView.setVisibility(isPlay ? View.VISIBLE : View.INVISIBLE);
        mImageOffline.setVisibility(isPlay ? View.INVISIBLE : View.VISIBLE );
    }

    @Override
    public void setupClickableViews(RecyclerActionListener actionListener) {
        mAction = actionListener;
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onViewClick(getAdapterPosition(), v, OfflineSongItemLibraryHolder.this);
            }
        });
    }

    @Override
    public void setService(MediaPlaybackService service) {
        mService = service;
    }

    private void updateSongFromMenu(Song song, RecyclerActionListener.CONTROL_UPDATE state){
        mAction.updateSongFromMenuButton(song, state);
    }
}
