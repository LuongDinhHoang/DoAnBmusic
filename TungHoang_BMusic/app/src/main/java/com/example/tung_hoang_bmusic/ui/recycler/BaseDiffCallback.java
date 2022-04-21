package com.example.tung_hoang_bmusic.ui.recycler;


import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

public class BaseDiffCallback<T extends RecyclerData> extends DiffUtil.Callback {

    private List<T> oldData, newData;

    BaseDiffCallback(List<T> oldData, List<T> newData) {
        this.oldData = oldData;
        this.newData = newData;
    }

    @Override
    public int getOldListSize() {
        return oldData == null ? 0 : oldData.size();
    }

    @Override
    public int getNewListSize() {
        return newData == null ? 0 : newData.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldData.get(oldItemPosition).areItemsTheSame(newData.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldData.get(oldItemPosition).areContentsTheSame(newData.get(newItemPosition));
    }
}
