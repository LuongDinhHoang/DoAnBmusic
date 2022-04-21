package com.example.tung_hoang_bmusic.activity;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tung_hoang_bmusic.model.PlaySong;
import com.example.tung_hoang_bmusic.model.Playlist;
import com.example.tung_hoang_bmusic.model.Song;

public class ActivityViewModel extends ViewModel {

    private final MutableLiveData<Playlist> mDetailPlaylist = new MutableLiveData<>();
    private final MutableLiveData<PlaySong> mPlaySong = new MutableLiveData<>();
    // nhan su kien khi click vao mot bai hat
    private final MutableLiveData<Song> mClickSong = new MutableLiveData<>();

    public ActivityViewModel() {
    }

    public MutableLiveData<Playlist> getDetailPlayList() {
        return mDetailPlaylist;
    }

    public void setDetailPlaylist(Playlist mPLayList) {
        this.mDetailPlaylist.setValue(mPLayList);
    }
    //
    public MutableLiveData<PlaySong> getPlaylist() {
        return mPlaySong;
    }

    public void setPlaylist(PlaySong mPlaylist) {
        this.mPlaySong.setValue(mPlaylist);
    }

}
