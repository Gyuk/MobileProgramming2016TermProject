package com.example.hong.mylifelogger;

import android.app.Activity;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;


/**
 * Created by admin on 2016-12-06.
 */

public class Statistic extends Activity {
    static DataBaseOpen dataBaseOpen;
    static SQLiteDatabase db;
    static WalkDataBaseOpen walkDataBaseOpen;
    static SQLiteDatabase walkdb;
    ArrayList<MyData> arrayList;
    ArrayList<WalkData> walks;

    float[] count  = new float[5];
    float[] count_walk;

    ArrayList<String> labels = new ArrayList<String>();
    ArrayList<String> walk_labels = new ArrayList<String>();
    ArrayList<BarEntry> entries = new ArrayList<BarEntry>();
    ArrayList<BarEntry> walk_entries = new ArrayList<BarEntry>();
    BarChart barChart;
    BarChart barChart2;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistic);
        dataBaseOpen = new DataBaseOpen(this);
        walkDataBaseOpen = new WalkDataBaseOpen(this);
        db = dataBaseOpen.getWritableDatabase();
        walkdb = walkDataBaseOpen.getWritableDatabase();

        barChart = (BarChart) findViewById(R.id.chart);
        barChart2 = (BarChart) findViewById(R.id.chart2);
        readTable();
        readWalkTable();

        count_walk = new float[walks.size()];


        for (int i = 0; i < 5; i++)
            count[i] = 0;
        for (int i = 0; i < arrayList.size(); i++) {
            switch (arrayList.get(i).getType()) {
                case "식사":
                    count[0]++;
                    break;
                case "공부":
                    count[1]++;
                    break;
                case "운동":
                    count[2]++;
                    break;
                case "사교활동":
                    count[3]++;
                    break;
                case "기타":
                    count[4]++;
                    break;
            }
        }

        for(int i = 0; i < walks.size() ; i++){
            walk_labels.add(walks.get(i).getDateString().substring(5, 13));
            count_walk[i] = (float)walks.get(i).getCount();
            walk_entries.add(new BarEntry(count_walk[i], i));
        }
        BarDataSet walkset = new BarDataSet(walk_entries, "# of Calls");
        walkset.setColors(ColorTemplate.COLORFUL_COLORS);
        BarData data = new BarData(walk_labels, walkset);
        barChart2.setData(data);
        barChart2.setDescription("Description");

        labels.add("식사");
        labels.add("공부");
        labels.add("운동");
        labels.add("사교활동");
        labels.add("기타");

        Log.d("tag", count[0] + " " + count[1] );
        entries.add(new BarEntry(count[0], 0));
        entries.add(new BarEntry(count[1], 1));
        entries.add(new BarEntry(count[2], 2));
        entries.add(new BarEntry(count[3], 3));
        entries.add(new BarEntry(count[4], 4));




        BarDataSet dataset = new BarDataSet(entries, "# of Calls");
        dataset.setColors(ColorTemplate.COLORFUL_COLORS);
        data = new BarData(labels, dataset);
        barChart.setData(data);
        barChart.setDescription("Description");



    }

    public void readTable() {
        arrayList = new ArrayList<MyData>();
        String sql = "select * from t_table";
        Cursor results = db.rawQuery(sql, null);
        results.moveToFirst();

        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String date = results.getString(1);
            String time = results.getString(2);
            String address = results.getString(3);
            double latitude = results.getDouble(4);
            double longitude = results.getDouble(5);
            String type = results.getString(6);
            String title = results.getString(7);
            String detail = results.getString(8);
            String picturekey = results.getString(9);

            arrayList.add(new MyData(id, date, time, address, latitude, longitude,  type, title, detail, picturekey));
            results.moveToNext();
        }
        results.close();
    }
    public void readWalkTable() {
        walks = new ArrayList<WalkData>();
        String sql = "select * from walk_table";
        Cursor results = walkdb.rawQuery(sql, null);
        results.moveToFirst();

        while (!results.isAfterLast()) {
            int id = results.getInt(0);
            String date = results.getString(1);
            double count = results.getDouble(2);
            walks.add(new WalkData(date,count));
            results.moveToNext();
        }
        results.close();
    }



}
