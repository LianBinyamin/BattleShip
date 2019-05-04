package com.example.lian.betterbattleship;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "HighScores.db";
    private static final int VERSION = 1;
    public static final String TABLE_NAME_EASY_LEVEL = "highScores_table_easy";
    public static final String TABLE_NAME_NORMAL_LEVEL = "highScores_table_normal";
    public static final String TABLE_NAME_HARD_LEVEL = "highScores_table_hard";
    public static final String KEY_ID = "ID";
    public static final String KEY_NAME = "Name";
    public static final String KEY_SCORE = "Score";
    private final int COL_NUM_NAME = 1;
    private final int COL_NUM_SCORE = 2;
    private List<Score> listAllScoresEasy;
    private List<Score> listAllScoresNormal;
    private List<Score> listAllScoresHard;
//    private String[] EASY_ROW = {KEY_NAME, KEY_SCORE};
//    private String[] NORMAL_ROW = {KEY_NAME, KEY_SCORE};
//    private String[] HARD_ROW = {KEY_NAME, KEY_SCORE};


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        listAllScoresEasy = new ArrayList<Score>();
        listAllScoresNormal = new ArrayList<Score>();
        listAllScoresHard = new ArrayList<Score>();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d("DATA BASE", "ON CREATE");
        createEasyTable(db);
        createNormalTable(db);
        createHardTable(db);
    }

    public void createEasyTable(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_NAME_EASY_LEVEL + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                  + KEY_NAME + " TEXT," + KEY_SCORE + " INTEGER)");

        Log.d("created table", "EASY");
    }

    public void createNormalTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_NORMAL_LEVEL + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME + " TEXT," + KEY_SCORE + " INTEGER)");

        Log.d("created table", "NORMAL");
    }

    public void createHardTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME_HARD_LEVEL + " (" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + KEY_NAME + " TEXT," + KEY_SCORE + " INTEGER)");

        Log.d("created table", "HARD");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        wipeDb();
        onCreate(db);
    }

    public void wipeDb() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_EASY_LEVEL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_NORMAL_LEVEL);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_HARD_LEVEL);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insertDataToEasyTable(String name, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.remove(KEY_ID);
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_SCORE, score);
        long res = db.insert(TABLE_NAME_EASY_LEVEL, null, contentValues);
        if (res == -1) {
            return false;
        }
        return true;
    }

    public boolean insertDataToNormalTable(String name, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.remove(KEY_ID);
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_SCORE, score);
        long res = db.insert(TABLE_NAME_NORMAL_LEVEL, null, contentValues);
        if (res == -1) {
            return false;
        }
        return true;
    }

    public boolean insertDataToHardTable(String name, int score) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.remove(KEY_ID);
        contentValues.put(KEY_NAME, name);
        contentValues.put(KEY_SCORE, score);
        long res = db.insert(TABLE_NAME_HARD_LEVEL, null, contentValues);
        if (res == -1) {
            return false;
        }
        return true;
    }

    public List<Score> getAllDataFromEasyTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_EASY_LEVEL + " order by " + KEY_SCORE +
                " DESC " + " limit 10 ", null);

        listAllScoresEasy.clear();
        res.moveToFirst();

        while(!res.isAfterLast()) {
            String name = res.getString(COL_NUM_NAME);
            int score = res.getInt(COL_NUM_SCORE);
            listAllScoresEasy.add(new Score(name, score));
            res.moveToNext();
        }
        return listAllScoresEasy;
    }

    public List<Score> getAllDataFromNormalTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_NORMAL_LEVEL + " order by " + KEY_SCORE +
                " DESC " + " limit 10 ", null);

        listAllScoresNormal.clear();
        res.moveToFirst();

        while(!res.isAfterLast()) {
            String name = res.getString(COL_NUM_NAME);
            int score = res.getInt(COL_NUM_SCORE);
            listAllScoresNormal.add(new Score(name, score));
            res.moveToNext();
        }
        return listAllScoresNormal;
    }

    public List<Score> getAllDataFromHardTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME_HARD_LEVEL + " order by " + KEY_SCORE +
                " DESC " + " limit 10 ", null);

        listAllScoresHard.clear();
        res.moveToFirst();

        while(!res.isAfterLast()) {
            String name = res.getString(COL_NUM_NAME);
            int score = res.getInt(COL_NUM_SCORE);
            listAllScoresHard.add(new Score(name, score));
            res.moveToNext();
        }
        return listAllScoresHard;
    }

}
