package com.example.tung_hoang_bmusic.ui.holder;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.example.tung_hoang_bmusic.R;
import com.example.tung_hoang_bmusic.model.ImageSearchModel;
import com.example.tung_hoang_bmusic.service.MediaPlaybackService;
import com.example.tung_hoang_bmusic.ui.recycler.BaseRecyclerViewHolder;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerActionListener;
import com.example.tung_hoang_bmusic.ui.recycler.RecyclerData;

public class ImageSearchItemHolder extends BaseRecyclerViewHolder {

    private ImageView mImageSearchButton;

    public ImageSearchItemHolder(@NonNull View itemView) {
        super(itemView);
        mImageSearchButton = itemView.findViewById(R.id.image_search);
    }

    @Override
    public void bindViewHolder(RecyclerData data) {
        if (data instanceof ImageSearchModel){
            ImageSearchModel image = (ImageSearchModel) data;
            mImageSearchButton.setImageResource(image.getImageSearchUrl());
        }
    }

    @Override
    public void setupClickableViews(RecyclerActionListener actionListener) {
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actionListener.onViewClick(getAdapterPosition(), v, ImageSearchItemHolder.this);
            }
        });
    }

    @Override
    public void setService(MediaPlaybackService service) {

    }

}
