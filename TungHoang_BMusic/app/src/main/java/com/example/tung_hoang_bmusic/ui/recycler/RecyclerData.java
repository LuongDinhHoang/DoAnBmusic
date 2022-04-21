package com.example.tung_hoang_bmusic.ui.recycler;

public interface RecyclerData {

    public default void setViewType(int i) {
    }

    @RecyclerViewType
    int getViewType();
    boolean areItemsTheSame(RecyclerData other);
    boolean areContentsTheSame(RecyclerData other);
}
