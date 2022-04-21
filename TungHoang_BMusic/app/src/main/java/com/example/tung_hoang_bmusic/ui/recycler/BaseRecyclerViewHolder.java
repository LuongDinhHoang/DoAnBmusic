package com.example.tung_hoang_bmusic.ui.recycler;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tung_hoang_bmusic.service.MediaPlaybackService;

public abstract class BaseRecyclerViewHolder extends RecyclerView.ViewHolder {

    public BaseRecyclerViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public abstract void bindViewHolder(RecyclerData data);

    public abstract void setupClickableViews(RecyclerActionListener actionListener);

    //HoangLD: service
    public abstract void setService(MediaPlaybackService service);

}
