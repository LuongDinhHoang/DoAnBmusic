package com.example.tung_hoang_bmusic.ui.recycler;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tung_hoang_bmusic.service.MediaPlaybackService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BaseRecyclerAdapter<T extends RecyclerData> extends RecyclerView.Adapter<BaseRecyclerViewHolder> {

    private List<T> data;
    private RecyclerActionListener actionListener;
    private MediaPlaybackService mService;

    public BaseRecyclerAdapter() {
        this.data = new ArrayList<>();
    }

    public BaseRecyclerAdapter(List<T> data) {
        this.data = new ArrayList<>();
    }

    public BaseRecyclerAdapter(RecyclerActionListener actionListener, MediaPlaybackService service) {
        this.data = new ArrayList<>();
        this.actionListener = actionListener;
        this.mService = service;
    }

    public BaseRecyclerAdapter(List<T> data, RecyclerActionListener actionListener) {
        this.data = data;//new ArrayList<>();
        this.actionListener = actionListener;
    }
    //HoangLD set service
    public void setService(MediaPlaybackService service){
        mService = service;
    }

    @NonNull
    @Override
    public BaseRecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, @RecyclerViewType int viewType) {
        BaseRecyclerViewHolder viewHolder = ViewHolderFactory.createViewHolder(viewType, parent);
        viewHolder.setupClickableViews(actionListener);
        viewHolder.setService(mService);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewHolder vh, int position) {
        RecyclerData item = data.get(position);
        vh.bindViewHolder(item);
    }

    @Override
    public int getItemCount() {
        return data == null ? 0 : data.size();
    }

    @Override
    public int getItemViewType(int position) {
        return data.get(position).getViewType();
    }

    public RecyclerData getItemAt(int position) {
        return data != null && position < data.size() ? data.get(position) : null;
    }

    public void update(List<T> newData) {
        final DiffUtil.Callback diffCallback = new BaseDiffCallback<T>(data, newData);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
        this.data = newData;
        diffResult.dispatchUpdatesTo(this);
    }

    public void setActionListener(RecyclerActionListener actionListener) {
        this.actionListener = actionListener;
    }

    public void removeItem(int position) {
        this.data.remove(position);
        notifyItemRemoved(position);
    }

    public void addItem(int position, T item) {
        data.add(position, item);
        notifyItemInserted(position);
    }

    public void addItem(T item) {
        Collections.addAll(data, item);
        notifyItemInserted(data.size() - 1);
    }

    public List<T> getData() {
        return data;
    }
}
