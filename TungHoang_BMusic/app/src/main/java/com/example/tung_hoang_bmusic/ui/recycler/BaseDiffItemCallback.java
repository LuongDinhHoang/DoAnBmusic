package com.example.tung_hoang_bmusic.ui.recycler;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;

public class BaseDiffItemCallback<T extends RecyclerData> extends DiffUtil.ItemCallback<T> {

    public BaseDiffItemCallback() {

    }

    @Override
    public boolean areItemsTheSame(@NonNull T t, @NonNull T t1) {
        return t.areItemsTheSame(t1);
    }

    @Override
    public boolean areContentsTheSame(@NonNull T t, @NonNull T t1) {
        return t.areContentsTheSame(t1);
    }
}
