package com.example.tung_hoang_bmusic.ui.recycler;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface RecyclerViewType {
    int TYPE_INVALID = 0;

    int TYPE_HOME_BASE = 1989;
    int TYPE_BLOCK_HOME_CATEGORY = TYPE_HOME_BASE + 1;
    int TYPE_ITEM_SONG_IN_HOME = TYPE_HOME_BASE + 3;

    int TYPE_IMAGE_SEARCH = TYPE_HOME_BASE + 4;
    int TYPE_SONG_SEARCH = TYPE_HOME_BASE + 5;

    int TYPE_OFFLINE_SONG_LIBRARY = TYPE_HOME_BASE + 6;
    int TYPE_PLAYLIST_LIBRARY = TYPE_HOME_BASE + 7;
    int TYPE_FAVORITE_SONG_LIBRARY = TYPE_HOME_BASE + 8;
    int TYPE_SONG_IN_PLAYLIST = TYPE_HOME_BASE + 9;
    int TYPE_DETAIL_SEARCH = TYPE_HOME_BASE + 10;

    int TYPE_ADD_SONG_IN_PLAYLIST = TYPE_HOME_BASE +11;
}
