package com.example.tung_hoang_bmusic.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tung_hoang_bmusic.model.Playlist;
import com.example.tung_hoang_bmusic.model.Song;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<Song> mDetailSong = new MutableLiveData<>();
    private final MutableLiveData<List<Playlist>> mPlaylist = new MutableLiveData<>();
    private final MutableLiveData<Song> mSongFirstClick = new MutableLiveData<>();

    public HomeViewModel() {
        mPlaylist.setValue(new ArrayList<>());
    }

    public MutableLiveData<Song> getDetailSong() {
        return mDetailSong;
    }

    public MutableLiveData<Song> openDetailSong(){
        return mSongFirstClick;
    }
    public void setSongFirstClick(Song song){
        this.mSongFirstClick.setValue(song);
    }

    public void setDetailSong(Song mSong) {
        this.mDetailSong.setValue(mSong);
    }

    public MutableLiveData<List<Playlist>> getPlaylist() {
        return mPlaylist;
    }

    public void setPlaylist(List<Playlist> mPlaylist) {
        this.mPlaylist.setValue(mPlaylist);
    }
}