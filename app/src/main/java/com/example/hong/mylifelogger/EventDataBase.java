package com.example.hong.mylifelogger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by admin on 2016-12-06.
 */
public class EventDataBase extends SQLiteOpenHelper {
    public EventDataBase(Context context) {
        super(context, "e_table", null, 1);
    }
    // 최초 실행시 Data Base 한번만 생성
    @Override


    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE e_table"
                + "(id integer primary key autoincrement, "
                + "date TEXT, "
                + "time TEXT, "
                + "address TEXT, "
                + "latitude REAL, "
                + "longitude REAL, "
                + "title TEXT, "
                + "detail TEXT, "
                + "picturekey TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS e_table");
        onCreate(db);
    }
}
