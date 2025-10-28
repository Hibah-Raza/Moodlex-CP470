package com.example.moodlex;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

/**
 * SQLite helper: tables moods and journals
 */
public class MoodDatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "moodlex.db";
    private static final int DB_VERSION = 1;

    private static final String TABLE_MOODS = "moods";
    private static final String COL_M_ID = "id";
    private static final String COL_M_MOOD = "mood";
    private static final String COL_M_NOTE = "note";
    private static final String COL_M_TIMESTAMP = "timestamp";

    private static final String TABLE_JOURNALS = "journals";
    private static final String COL_J_ID = "id";
    private static final String COL_J_TITLE = "title";
    private static final String COL_J_CONTENT = "content";
    private static final String COL_J_TIMESTAMP = "timestamp";

    public MoodDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createMoods = "CREATE TABLE " + TABLE_MOODS + " (" +
                COL_M_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_M_MOOD + " INTEGER, " +
                COL_M_NOTE + " TEXT, " +
                COL_M_TIMESTAMP + " INTEGER DEFAULT (strftime('%s','now'))" +
                ");";
        String createJournals = "CREATE TABLE " + TABLE_JOURNALS + " (" +
                COL_J_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_J_TITLE + " TEXT, " +
                COL_J_CONTENT + " TEXT, " +
                COL_J_TIMESTAMP + " INTEGER DEFAULT (strftime('%s','now'))" +
                ");";
        db.execSQL(createMoods);
        db.execSQL(createJournals);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOODS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOURNALS);
        onCreate(db);
    }

    public long insertMood(int mood, String note) {
        ContentValues cv = new ContentValues();
        cv.put(COL_M_MOOD, mood);
        cv.put(COL_M_NOTE, note);
        return getWritableDatabase().insert(TABLE_MOODS, null, cv);
    }

    public List<MoodEntry> getAllMoods() {
        List<MoodEntry> list = new ArrayList<>();
        String q = "SELECT * FROM " + TABLE_MOODS + " ORDER BY " + COL_M_TIMESTAMP + " DESC";
        Cursor c = getReadableDatabase().rawQuery(q, null);
        while (c.moveToNext()) {
            MoodEntry m = new MoodEntry(
                    c.getInt(c.getColumnIndexOrThrow(COL_M_ID)),
                    c.getInt(c.getColumnIndexOrThrow(COL_M_MOOD)),
                    c.getString(c.getColumnIndexOrThrow(COL_M_NOTE)),
                    c.getLong(c.getColumnIndexOrThrow(COL_M_TIMESTAMP))
            );
            list.add(m);
        }
        c.close();
        return list;
    }

    public MoodEntry getLastMood() {
        String q = "SELECT * FROM " + TABLE_MOODS + " ORDER BY " + COL_M_TIMESTAMP + " DESC LIMIT 1";
        Cursor c = getReadableDatabase().rawQuery(q, null);
        if (c.moveToFirst()) {
            MoodEntry m = new MoodEntry(
                    c.getInt(c.getColumnIndexOrThrow(COL_M_ID)),
                    c.getInt(c.getColumnIndexOrThrow(COL_M_MOOD)),
                    c.getString(c.getColumnIndexOrThrow(COL_M_NOTE)),
                    c.getLong(c.getColumnIndexOrThrow(COL_M_TIMESTAMP))
            );
            c.close();
            return m;
        }
        c.close();
        return null;
    }

    public long insertJournal(String title, String content) {
        ContentValues cv = new ContentValues();
        cv.put(COL_J_TITLE, title);
        cv.put(COL_J_CONTENT, content);
        return getWritableDatabase().insert(TABLE_JOURNALS, null, cv);
    }
}
