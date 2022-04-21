package com.example.tung_hoang_bmusic.model;

import java.util.ArrayList;
import java.util.List;

public class PlaylistSearch {
    private String nameCategory;
    private List<Song> listSong = new ArrayList<>();

    public PlaylistSearch(List<Song> listSong, String nameCategory) {
        this.nameCategory = nameCategory;
        this.listSong = listSong;
    }

    public String getNameCategory() {
        return nameCategory;
    }

    public void setNameCategory(String nameCategory) {
        this.nameCategory = nameCategory;
    }

    public List<Song> getListSong() {
        return listSong;
    }

    public void setListSong(List<Song> listSong) {
        this.listSong = listSong;
    }
}
