package com.example.tung_hoang_bmusic.ui.recycler;

import android.view.View;

import androidx.annotation.NonNull;

import com.example.tung_hoang_bmusic.service.MediaPlaybackService;

public class InvalidViewHolder extends BaseRecyclerViewHolder {

    InvalidViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    @Override
    public void bindViewHolder(RecyclerData data) {
        throw new IllegalArgumentException("You are using an invalid view holder. You need to create a viewType value in CCRecyclerViewType.java, then check CCViewHolderFactory.java and do the same job as TYPE_INVALID.");
    }

    @Override
    public void setupClickableViews(RecyclerActionListener actionListener) {
        throw new IllegalArgumentException("You are using an invalid view holder. You need to create a viewType value in CCRecyclerViewType.java, then check CCViewHolderFactory.java and do the same job as TYPE_INVALID.");
    }

    @Override
    public void setService(MediaPlaybackService service) {
        throw new IllegalArgumentException("You are using an invalid view holder. You need to create a viewType value in CCRecyclerViewType.java, then check CCViewHolderFactory.java and do the same job as TYPE_INVALID.");
    }

}
