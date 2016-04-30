package com.firekost.app.VKPlayer;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context, String databaseName) {
        super(context, databaseName, null, DATABASE_VERSION);
        Log.wtf(databaseName, DATABASE_CREATE_SCRIPT);
    }

    public static final String DATABASE_TABLE = "audios";
    public static final String _ID = "_id";
    public static final String TITLE = "title";
    public static final String ARTIST = "artist";
    public static final String DURATION = "duration";
    public static final String URL = "url";
    public static final String AUDIO_ID = "audio_id";
    public static final String LYRICS_ID = "lyrics_id";

    private static final String DATABASE_CREATE_SCRIPT = "create table " + DATABASE_TABLE + " ("
            + _ID + " integer primary key autoincrement, "
            + TITLE + " text, "
            + ARTIST + " text, "
            + DURATION + " integer, "
            + URL + " text, "
            + AUDIO_ID + " integer, "
            + LYRICS_ID + " integer);";

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DATABASE_CREATE_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
