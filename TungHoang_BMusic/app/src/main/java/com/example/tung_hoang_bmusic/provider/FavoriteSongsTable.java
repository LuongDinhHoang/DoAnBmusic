package com.example.tung_hoang_bmusic.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class FavoriteSongsTable extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "songManager";
    private static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "songFavorite";

    public static final String KEY_ID = "id";
    public static final String ID_PROVIDER = "id_provider";
    public static final String IS_FAVORITE = "is_favorite";
    public static final String COUNT_OF_PLAY = "count_of_play";


    public FavoriteSongsTable(Context context, String name,
                              SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String create_songs_table = String.
                format("CREATE TABLE %s(%s INTEGER PRIMARY KEY AUTOINCREMENT," +
                                "%s INTEGER,%s INTEGER, %s INTEGER )",
                        TABLE_NAME, KEY_ID, ID_PROVIDER,
                        IS_FAVORITE, COUNT_OF_PLAY);
        db.execSQL(create_songs_table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
}
