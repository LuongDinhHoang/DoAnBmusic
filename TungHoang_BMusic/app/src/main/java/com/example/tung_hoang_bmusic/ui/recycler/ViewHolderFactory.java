package com.example.tung_hoang_bmusic.ui.recycler;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;

import com.example.tung_hoang_bmusic.R;
import com.example.tung_hoang_bmusic.ui.holder.AddSongInPlayListItemHolder;
import com.example.tung_hoang_bmusic.ui.holder.BlockHomeCategoryHolder;
import com.example.tung_hoang_bmusic.ui.holder.DetailSearchItemHolder;
import com.example.tung_hoang_bmusic.ui.holder.FavoriteSongItemLibraryHolder;
import com.example.tung_hoang_bmusic.ui.holder.HomeSongItemHolder;
import com.example.tung_hoang_bmusic.ui.holder.ImageSearchItemHolder;
import com.example.tung_hoang_bmusic.ui.holder.OfflineSongItemLibraryHolder;
import com.example.tung_hoang_bmusic.ui.holder.PlayListItemLibraryHolder;
import com.example.tung_hoang_bmusic.ui.holder.SongItemInPlayListHolder;
import com.example.tung_hoang_bmusic.ui.holder.SongSearchItemHolder;

public class ViewHolderFactory {

    public static BaseRecyclerViewHolder createViewHolder(@RecyclerViewType int viewType, ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(getLayoutId(viewType), parent, false);
        switch (viewType) {
            case RecyclerViewType.TYPE_INVALID:
                return new InvalidViewHolder(view);
            case RecyclerViewType.TYPE_BLOCK_HOME_CATEGORY:
                return new BlockHomeCategoryHolder(view);
            case RecyclerViewType.TYPE_ITEM_SONG_IN_HOME:
                return new HomeSongItemHolder(view);
            case RecyclerViewType.TYPE_IMAGE_SEARCH:
                return new ImageSearchItemHolder(view);
            case RecyclerViewType.TYPE_SONG_SEARCH:
                return new SongSearchItemHolder(view);
            case RecyclerViewType.TYPE_OFFLINE_SONG_LIBRARY:
                return new OfflineSongItemLibraryHolder(view);
            case RecyclerViewType.TYPE_PLAYLIST_LIBRARY:
                return new PlayListItemLibraryHolder(view);
            case RecyclerViewType.TYPE_FAVORITE_SONG_LIBRARY:
                return new FavoriteSongItemLibraryHolder(view);
            case RecyclerViewType.TYPE_SONG_IN_PLAYLIST:
                return new SongItemInPlayListHolder(view);
            case RecyclerViewType.TYPE_DETAIL_SEARCH:
                return new DetailSearchItemHolder(view);
            case RecyclerViewType.TYPE_ADD_SONG_IN_PLAYLIST:
                return new AddSongInPlayListItemHolder(view);
        }
        return new InvalidViewHolder(view);
    }

    @LayoutRes
    private static int getLayoutId(int viewType) {
        switch (viewType) {
            case RecyclerViewType.TYPE_INVALID:
                return 0;
            case RecyclerViewType.TYPE_BLOCK_HOME_CATEGORY:
                return R.layout.block_song_category;
            case RecyclerViewType.TYPE_ITEM_SONG_IN_HOME:
                return R.layout.item_list_in_block;

            case RecyclerViewType.TYPE_IMAGE_SEARCH:
                return R.layout.item_grid_search_layout;
            case RecyclerViewType.TYPE_SONG_SEARCH:
                return R.layout.item_search_layout;

            case RecyclerViewType.TYPE_OFFLINE_SONG_LIBRARY:
                return R.layout.item_list_offline;
            case RecyclerViewType.TYPE_PLAYLIST_LIBRARY:
                return R.layout.item_list_playlist;
            case RecyclerViewType.TYPE_FAVORITE_SONG_LIBRARY:
                return R.layout.item_list_favorite;
            case RecyclerViewType.TYPE_SONG_IN_PLAYLIST:
                return R.layout.item_song_in_playlist;
            case RecyclerViewType.TYPE_DETAIL_SEARCH:
                return R.layout.item_list_search;
            case RecyclerViewType.TYPE_ADD_SONG_IN_PLAYLIST:
                return R.layout.add_song_playlist_item;
        }
        return 0;
    }

}
