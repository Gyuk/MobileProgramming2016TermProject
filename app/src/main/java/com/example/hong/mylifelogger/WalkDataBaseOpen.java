package com.example.hong.mylifelogger;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by user on 2016-12-06.
 */

public class WalkDataBaseOpen extends SQLiteOpenHelper {
    public WalkDataBaseOpen(Context context) {
        super(context, "walk_table", null, 1);
    }
    // 최초 실행시 Data Base 한번만 생성
    @Override


    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE walk_table"
                + "(id integer primary key  autoincrement, "
                + "date TEXT, "
                + "count REAL);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS walk_table");
        onCreate(db);
    }
}
