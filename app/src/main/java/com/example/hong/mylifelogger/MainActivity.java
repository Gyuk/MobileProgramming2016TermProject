package com.example.hong.mylifelogger;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Stack;

public class MainActivity extends AppCompatActivity {
    String dateString;
    Stack<WalkData> walks;
    static WalkDataBaseOpen walkDataDataBaseOpen;
    static SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        walkDataDataBaseOpen = new WalkDataBaseOpen(this);
        db = walkDataDataBaseOpen.getWritableDatabase();
        Log.d("메세지", "MainCreate()");
        Intent intent = new Intent(this, WalkService.class);
        startService(intent);
    }
    public void onClickWriteDaily(View v){
        Intent intent = new Intent(this, WriteDaily.class);
        startActivity(intent);
    }
    public void onClickWriteEvent(View v){
        Intent intent = new Intent(this, WriteEvent.class);
        startActivity(intent);
    }
    public void onClickStatistic(View v){
        Intent intent = new Intent(this, Statistic.class);
        startActivity(intent);
    }

    public void onClickShare(View v){
       startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://34.193.103.121:8080/")));
    }

    @Override
    protected void onResume() {
        super.onPostResume();
        dateString = getDateString();
        readWalkTable();
        if(walks.size() == 0) {
            insertWalkData(dateString, WalkService.walk);
            Log.d("메세지", "insert when size = 0");
        }
        else{
            if (!walks.peek().getDateString().equals(dateString)) {
                modifyData(walks.peek().getDateString(), WalkService.walk);
                WalkService.walk = 0;
                insertWalkData(dateString, WalkService.walk);
                Log.d("메세지", "insert differ date" + walks.size());
                Log.i("메세지", walks.peek().getDateString() + " / " + dateString );
            }
            else {
                if(WalkService.walk > walks.peek().getCount())
                    modifyData(dateString, WalkService.walk);
                else
                    WalkService.walk =  (int)walks.peek().getCount();

                Log.d("메세지", "modify date " + dateString + Double.toString(WalkService.walk));
            }
        }
    }
    protected String getDateString(){
        long now = System.currentTimeMillis();
        Date date = new Date(now);
        SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        return CurDateFormat.format(date);
    }
    public void readWalkTable() {
        walks = new Stack<WalkData>();
        String sql = "select * from walk_table";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();

        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String date = results.getString(1);
            double count = results.getDouble(2);
            walks.push(new WalkData(date,count));
            results.moveToNext();
        }
        results.close();
    }
    public void insertWalkData(String date, double count) {
        db.execSQL("INSERT INTO walk_table "
                + "VALUES(NULL, '" + date
                + "', '" + count
                + "');");
    }
    public void modifyData(String date, double count) {
        db.execSQL("UPDATE walk_table SET "+
                "date = '"+date+"', "+
                "count = '"+ count +
                "' WHERE date = '"+ date + "' ;"
        );
    }


}
