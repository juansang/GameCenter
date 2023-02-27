package com.example.gamecenter;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class ListOpenHelper extends SQLiteOpenHelper {

    public ListOpenHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static final String TAG = ListOpenHelper.class.getSimpleName();

    private static final int DATABASE_VERSION = 1;
    private static final String SCORES_LIST_TABLE = "score_entries";
    private static final String DATABASE_NAME = "scorelist";

    public static final String KEY_ID = "_id";
    public static final String KEY_GAME = "game";
    public static final String KEY_BOARD = "board";
    public static final String KEY_PLAYER = "player";
    public static final String KEY_SCORE = "score";
    public static final String KEY_TIME = "time";

    private static final String[] COLUMNS = { KEY_ID,KEY_PLAYER,KEY_GAME,KEY_BOARD,KEY_SCORE,KEY_TIME };

    private static final String SCORES_LIST_TABLE_CREATE =
            "CREATE TABLE " + SCORES_LIST_TABLE + " (" + KEY_ID + " INTEGER PRIMARY KEY, " +
                    KEY_PLAYER + " TEXT, " +
                    KEY_GAME + " TEXT, " +
                    KEY_BOARD + " TEXT , " +
                    KEY_SCORE + " TEXT , " +
                    KEY_TIME + " TEXT );";


    private SQLiteDatabase mWritableDB;
    private SQLiteDatabase mReadableDB;


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SCORES_LIST_TABLE_CREATE);
    }

    @SuppressLint("Range")
    public Stats query2048(int position) {

        String query = "SELECT  * FROM " + SCORES_LIST_TABLE + " WHERE " +
                KEY_GAME + " = '2048'" + " AND " + KEY_GAME + " IS NOT NULL" +
                " ORDER BY " + KEY_SCORE + " ASC " +
                "LIMIT " + position + ",1";


        Cursor cursor = null;
        Stats entry = new Stats();

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();
            entry.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            entry.setName(cursor.getString(cursor.getColumnIndex(KEY_PLAYER)));
            entry.setGame(cursor.getString(cursor.getColumnIndex(KEY_GAME)));
            entry.setBoard(cursor.getString(cursor.getColumnIndex(KEY_BOARD)));
            entry.setScore(cursor.getInt(cursor.getColumnIndex(KEY_SCORE)));
            entry.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME)));


        } catch (Exception e) {
            Log.d(TAG, "QUERY EXCEPTION! " + e.getMessage());
        } finally {
            cursor.close();
            return entry;
        }
    }

    @SuppressLint("Range")
    public Stats queryLO(int position) {
        String query = "SELECT  * FROM " + SCORES_LIST_TABLE + " WHERE "
                + KEY_GAME + "='LIGHTSOUT'" + " AND " + KEY_PLAYER + " IS NOT NULL" +
                " ORDER BY " + KEY_PLAYER + " ASC " +
                "LIMIT " + position + ",1";

        Cursor cursor = null;
        Stats entry = new Stats();

        try {
            if (mReadableDB == null) {
                mReadableDB = getReadableDatabase();
            }
            cursor = mReadableDB.rawQuery(query, null);
            cursor.moveToFirst();
            entry.setId(cursor.getInt(cursor.getColumnIndex(KEY_ID)));
            entry.setName(cursor.getString(cursor.getColumnIndex(KEY_PLAYER)));
            entry.setGame(cursor.getString(cursor.getColumnIndex(KEY_GAME)));
            entry.setBoard(cursor.getString(cursor.getColumnIndex(KEY_BOARD)));
            entry.setScore(cursor.getInt(cursor.getColumnIndex(KEY_SCORE)));
            entry.setTime(cursor.getString(cursor.getColumnIndex(KEY_TIME)));


        } catch (Exception e) {
            Log.d(TAG, "QUERY EXCEPTION! " + e.getMessage());
        } finally {
            cursor.close();
            return entry;
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(ListOpenHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + SCORES_LIST_TABLE);
        onCreate(db);
    }

    public long insert(String player,String game,String board,String score,String time){
        long newId = 0;
        ContentValues values = new ContentValues();
        values.put(KEY_PLAYER, player);
        values.put(KEY_GAME, game);
        values.put(KEY_BOARD, board);
        values.put(KEY_SCORE, score);
        values.put(KEY_TIME, time);
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            newId = mWritableDB.insert(SCORES_LIST_TABLE, null, values);
        } catch (Exception e) {
            Log.d(TAG, "INSERT EXCEPTION! " + e.getMessage());
        }
        return newId;
    }

    public long count(){
        if (mReadableDB == null) {
            mReadableDB = getReadableDatabase();
        }
        return DatabaseUtils.queryNumEntries(mReadableDB, SCORES_LIST_TABLE);
    }

    public int delete2048(String name) {
        int deleted = 0;
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            deleted = mWritableDB.delete(SCORES_LIST_TABLE,
                    KEY_GAME + " = '2048'" + " AND " + KEY_PLAYER + "= ?" , new String[]{String.valueOf(name)});
        } catch (Exception e) {
            Log.d (TAG, "DELETE EXCEPTION! " + e.getMessage());
        }
        return deleted;
    }



    public int deleteLO(String name) {
        int deleted = 0;
        try {
            if (mWritableDB == null) {
                mWritableDB = getWritableDatabase();
            }
            deleted = mWritableDB.delete(SCORES_LIST_TABLE,
                    KEY_GAME + " = 'LIGHTSOUT'" + " AND " + KEY_PLAYER + "= ?", new String[]{String.valueOf(name)});
        } catch (Exception e) {
            Log.d (TAG, "DELETE EXCEPTION! " + e.getMessage());
        }
        return deleted;
    }
}
