package com.example.tung_hoang_bmusic.model;

import com.example.tung_hoang_bmusic.ui.recycler.RecyclerData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class Playlist implements RecyclerData {
    private int idCategory;
    private String namePlaylist;
    private List<Song> songList = new ArrayList<>();

    public Playlist() {
        songList = new ArrayList<>();
    }

    public Playlist(int mIdPlaylist, String mNamePlaylist) {
        this.idCategory = mIdPlaylist;
        this.namePlaylist = mNamePlaylist;
        songList = new ArrayList<>();
    }

    public Playlist(int mIdPlaylist, String mNamePlaylist, List<Song> songList) {
        this.idCategory = mIdPlaylist;
        this.namePlaylist = mNamePlaylist;
        this.songList = songList;
    }

    public List<Song> getSongList() {
        return songList;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public String getNamePlaylist() {
        return namePlaylist;
    }

    public void setIdCategory(int mIdCategory) {
        this.idCategory = mIdCategory;
    }

    public void setNamePlaylist(String mNameCategory) {
        this.namePlaylist = mNameCategory;
    }

    public void setSongList(List<Song> songList) {
        this.songList = songList;
    }

    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public boolean areItemsTheSame(RecyclerData other) {
        if (areContentsTheSame(other)) {
            Playlist obj = (Playlist) other;
            return idCategory == obj.idCategory
                    && namePlaylist.equals(obj.namePlaylist);
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(RecyclerData other) {
        if (other instanceof Playlist) {
            Playlist obj = (Playlist) other;
            return idCategory == obj.idCategory
                    && namePlaylist.equals(obj.namePlaylist)
                    && new HashSet<>(songList).equals(new HashSet<>(obj.songList));
        }
        return false;
    }
}
