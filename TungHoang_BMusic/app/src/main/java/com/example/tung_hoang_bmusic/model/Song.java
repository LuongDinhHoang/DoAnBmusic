package com.example.tung_hoang_bmusic.model;

import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;

import com.example.tung_hoang_bmusic.ui.recycler.RecyclerData;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Objects;

@IgnoreExtraProperties
public class Song implements RecyclerData, Serializable {
    private static final SimpleDateFormat formatTimeSong = new SimpleDateFormat("mm:ss");

    private int id;
    private String nameSong;
    private String pathSong;
    private String singer;
    private String albumID;
    private String duration;
    private int idCategory;
    private String imageUrl;
    private String linkUrl;
    private String releaseDate;

    @Exclude
    private boolean isOffline = false;

    public Song() {
        this(0, "", "", "", "", "", 0, "", "", "");
    }

    public Song(int id) {
        this(id, "", "", "", "", "", 0, "", "", "");
    }

    public Song(int id, String nameSong, String pathSong, String singer, String albumID, String duration, int idCategory, String mImageUrl) {
        this(id, nameSong, pathSong, singer, albumID, duration, idCategory, mImageUrl, "", "");
    }

    public Song(int id, String nameSong, String pathSong, String singer, String albumID,
                String duration, int idCategory, String mImageUrl, String mLinkUrl, String releaseDate) {
        this.id = id;
        this.nameSong = nameSong;
        this.pathSong = pathSong;
        this.singer = singer;
        this.albumID = albumID;
        this.duration = duration;
        this.idCategory = idCategory;
        this.imageUrl = mImageUrl;
        this.linkUrl = mLinkUrl;
        this.releaseDate = releaseDate;
    }

    //song offline
    public Song(int id, String nameSong, String imageUrl, String artist, String albumID, String timeSong) {
        this(id, nameSong, imageUrl, artist, albumID, timeSong, 0, "", "", "");
        isOffline = true;
    }

    public Song(Cursor data) {
        this();
        this.id = Integer.parseInt(data.getString(data.getColumnIndex(MediaStore.Audio.Media._ID)));
        this.nameSong = data.getString(data.getColumnIndex(MediaStore.Audio.Media.TITLE));
        this.pathSong = data.getString(data.getColumnIndex(MediaStore.Audio.Media.DATA));
        this.singer = data.getString(data.getColumnIndex(MediaStore.Audio.Media.ARTIST));
        this.albumID = data.getString(data.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
        try {
            int duration = Integer.parseInt(data.getString(data.getColumnIndex(MediaStore.Audio.Media.DURATION)));
            this.duration = formatTimeSong.format(duration);
        } catch (NumberFormatException e) {
            this.duration = "N/A";
        }
        isOffline = true;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getId() {
        return id;
    }

    public String getNameSong() {
        return nameSong;
    }

    public String getPathSong() {
        return pathSong;
    }

    public String getSinger() {
        return singer;
    }

    public String getDuration() {
        return duration;
    }

    public String getAlbumID() {
        return albumID;
    }

    public int getIdCategory() {
        return idCategory;
    }

    public String getLinkUrl() {
        return linkUrl;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCategoryID(int id) {
        this.idCategory = id;
    }

    public void setNameSong(String nameSong) {
        this.nameSong = nameSong;
    }

    public void setSinger(String singer) {
        this.singer = singer;
    }

    public void setAlbumID(String albumID) {
        this.albumID = albumID;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    //lay anh theo bitmap neu co path
    public Bitmap loadImageFromPath(String path) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        try {
            mediaMetadataRetriever.setDataSource(path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] data = mediaMetadataRetriever.getEmbeddedPicture();
        return data == null ? null : BitmapFactory.decodeByteArray(data, 0, data.length);
    }

    public void updateData(Song song) {
        this.id = song.id;
        this.nameSong = song.nameSong;
        this.pathSong = song.pathSong;
        this.singer = song.singer;
        this.albumID = song.albumID;
        this.duration = song.duration;
        this.idCategory = song.idCategory;
        this.imageUrl = song.imageUrl;
        this.linkUrl = song.linkUrl;
    }

    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public boolean areItemsTheSame(RecyclerData other) {
        if (other instanceof Song) {
            Song obj = (Song) other;
            return id == obj.id
                    && nameSong.equals(obj.nameSong)
                    && pathSong.equals(obj.pathSong)
                    && singer.equals(obj.singer)
                    && albumID.equals(obj.albumID)
                    && idCategory == obj.idCategory
                    && imageUrl.equals(obj.imageUrl)
                    && linkUrl.equals(obj.linkUrl);
        }
        return false;
    }

    @Override
    public boolean areContentsTheSame(RecyclerData other) {
        if (other instanceof Song) {
            Song obj = (Song) other;
            return id == obj.id
                    && nameSong.equals(obj.nameSong)
                    && pathSong.equals(obj.pathSong)
                    && singer.equals(obj.singer)
                    && albumID.equals(obj.albumID)
                    && idCategory == obj.idCategory
                    && imageUrl.equals(obj.imageUrl)
                    && linkUrl.equals(obj.linkUrl);
        }
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Song song = (Song) o;
        return id == song.id &&
                idCategory == song.idCategory &&
                Objects.equals(nameSong, song.nameSong) &&
                Objects.equals(pathSong, song.pathSong) &&
                Objects.equals(singer, song.singer) &&
                Objects.equals(albumID, song.albumID) &&
                Objects.equals(duration, song.duration) &&
                Objects.equals(imageUrl, song.imageUrl) &&
                Objects.equals(linkUrl, song.linkUrl);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nameSong, pathSong, singer, albumID, duration, idCategory, imageUrl, linkUrl);
    }

    @JsonIgnore
    @Exclude
    public boolean isOffline() {
        return isOffline;
    }

    public void setOffline() {
    }
}
