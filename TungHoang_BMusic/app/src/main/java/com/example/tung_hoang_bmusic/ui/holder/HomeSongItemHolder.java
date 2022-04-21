package com.example.tung_hoang_bmusic.ui.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.GenericTransitionOptions;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.tung_hoang_bmusic.R;
import com.example.tung_hoang_bmusic.model.Song;
import com.example.tung_hoang_bmusic.service.MediaPlaybackService;
import com.example.tung_hoang_bmusic.ui.recycler.BaseRecyclerViewHolder;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerActionListener;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerData;

public class HomeSongItemHolder extends BaseRecyclerViewHolder {
    private ImageView mImageView;
    private TextView mTextSongName;
    private TextView mTextArtistsName;

    public HomeSongItemHolder(@NonNull View itemView) {
        super(itemView);
        mImageView = itemView.findViewById(R.id.image_song_home);
        mTextSongName = itemView.findViewById(R.id.name_song);
        mTextArtistsName = itemView.findViewById(R.id.artists);
    }

    @Override
    public void bindViewHolder(RecyclerData data) {
        if (data instanceof Song) {
            Song song = (Song) data;
            //Glide.... de load anh :|
            Glide.with(mImageView)
                    .load(song.getImageUrl())
                    .transition(GenericTransitionOptions.with(android.R.anim.fade_in))
                    .apply(RequestOptions.
                            placeholderOf(R.drawable.placeholder_music))
                    .into(mImageView);

            mTextSongName.setText(song.getNameSong());
            mTextArtistsName.setText(song.getSinger());
        }
    }

    @Override
    public void setupClickableViews(RecyclerActionListener actionListener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onViewClick(getAdapterPosition(), v, HomeSongItemHolder.this);
            }
        });
    }

    @Override
    public void setService(MediaPlaybackService service) {

    }

}
