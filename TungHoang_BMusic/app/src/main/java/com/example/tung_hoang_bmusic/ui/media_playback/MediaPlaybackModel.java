package com.example.tung_hoang_bmusic.ui.media_playback;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tung_hoang_bmusic.model.Playlist;
import com.example.tung_hoang_bmusic.model.Song;

public class MediaPlaybackModel extends ViewModel {

    private MutableLiveData<String> mPathImage;
    private final MutableLiveData<Playlist> mDetailPlaylist = new MutableLiveData<>();
    private final MutableLiveData<Song> mSong = new MutableLiveData<>();
    // nhan su kien khi click vao mot bai hat
    private final MutableLiveData<Song> mClickSong = new MutableLiveData<>();

    private final MutableLiveData<Playlist> mPlaylistFirstClick = new MutableLiveData<>();

    public MediaPlaybackModel() {
        mPathImage = new MutableLiveData<>();
        mPathImage.setValue("This is notifications fragment");
    }

    public LiveData<String> getPathImage() {
        return mPathImage;
    }

    public void setPathImage(String mPathImage) {
        this.mPathImage.setValue(mPathImage);
    }

    public MutableLiveData<Playlist> getDetailPlayList() {
        return mDetailPlaylist;
    }

    public void setDetailPlaylist(Playlist mPLayList) {
        this.mDetailPlaylist.setValue(mPLayList);
    }
//
    public MutableLiveData<Song> getSong() {
        return mSong;
    }

    public void setSong(Song song) {
        this.mSong.setValue(song);
    }

    public MutableLiveData<Playlist> openDetailPlaylist(){
        return mPlaylistFirstClick;
    }
    public void setPlaylistFirstClick( Playlist playlist){
        this.mPlaylistFirstClick.setValue(playlist);
    }
}