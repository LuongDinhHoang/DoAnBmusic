package com.example.tung_hoang_bmusic.model;

public interface Constants {
    String FIREBASE_STORAGE_URL = "gs://music-server-5ec92.appspot.com";
    String FIREBASE_SONG_PATH = "music";
    String FIREBASE_STORAGE_SONG_URL = FIREBASE_STORAGE_URL + "/" + FIREBASE_STORAGE_URL;

    String FIREBASE_REALTIME_DATABASE_URL = "https://music-server-5ec92-default-rtdb.firebaseio.com/";

    String FIREBASE_REALTIME_SONG_PATH = "song";

    String FIREBASE_REALTIME_ALBUM_PATH = "album";
    String FIREBASE_REALTIME_ALBUM_SONG_LIST_PATH = "songList";

    String FIREBASE_REALTIME_HOME_PATH = "home";
    String FIREBASE_REALTIME_SEARCH_PATH = "search";

    String FIREBASE_REALTIME_SEARCH_CATEGORY_PATH = "searchCategory";

    String FIREBASE_REALTIME_SEARCH_ALBUM_PATH = "search_album";
    String FIREBASE_REALTIME_SONG_URL = FIREBASE_REALTIME_DATABASE_URL + FIREBASE_REALTIME_SONG_PATH;

    String SONG_ID_NAME = "songID";
    String ALBUM_ID_NAME = "albumID";
    String PLAYLIST_ID_NAME = "playlistID";
    //HoangLD : database người dùng tạo playlist trong thư viện
    String FIREBASE_REALTIME_PLAYLIST_USER_PATH = "user_playlist";
}
