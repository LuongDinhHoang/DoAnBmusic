package com.example.tung_hoang_bmusic.model;

import com.example.tung_hoang_bmusic.ui.recycler.RecyclerData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.firebase.database.Exclude;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Album implements RecyclerData, Serializable {
    private int idAlbum;
    private String nameAlbum;
    private List<Integer> songList = new ArrayList<>();

    public Album() {
    }

    public Album(Playlist playlist) {
        this(playlist.getIdCategory(), playlist.getNamePlaylist());
        this.songList = new ArrayList<>(songList.size());
        for (Song i : playlist.getSongList()) {
            this.songList.add(i.getId());
        }
    }

    public Album(int idAlbum, String nameAlbum) {
        this.idAlbum = idAlbum;
        this.nameAlbum = nameAlbum;
    }

    public Album(int idAlbum, String nameAlbum, List<Integer> songList) {
        this.idAlbum = idAlbum;
        this.nameAlbum = nameAlbum;
        this.songList = songList;
    }

    public List<Integer> getSongList() {
        return songList;
    }

    public void setSongList(List<Integer> mSongList) {
        this.songList = mSongList;
    }

    public int getIdAlbum() {
        return idAlbum;
    }

    public void setIdAlbum(int mIdAlbum) {
        this.idAlbum = mIdAlbum;
    }

    public String getNameAlbum() {
        return nameAlbum;
    }

    public void setNameAlbum(String mNameCategory) {
        this.nameAlbum = mNameCategory;
    }

    @Exclude
    @JsonIgnore
    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public boolean areItemsTheSame(RecyclerData other) {
        if (other instanceof Album) {
            Album obj = (Album) other;
            return idAlbum == obj.idAlbum
                    && songList.equals(obj.songList)
                    && nameAlbum.equals(obj.nameAlbum);
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(RecyclerData other) {
        if (other instanceof Album) {
            Album obj = (Album) other;
            return idAlbum == obj.idAlbum
                    && songList.equals(obj.songList)
                    && nameAlbum.equals(obj.nameAlbum);
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album obj = (Album) o;
        return idAlbum == obj.idAlbum
                && songList.equals(obj.songList)
                && nameAlbum.equals(obj.nameAlbum);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idAlbum, nameAlbum, songList);
    }
}
