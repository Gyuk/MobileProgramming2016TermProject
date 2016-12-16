package com.example.hong.mylifelogger;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by admin on 2016-11-26.
 */
public class WriteDaily extends Activity implements ListViewBtnAdapter.ListBtnClickListener{
    static DataBaseOpen dataBaseOpen;
    static SQLiteDatabase db;
    ArrayList<MyData> items = new ArrayList<MyData>();

    String picturekey = "";

    ListView listView;
    ListViewBtnAdapter adapter;

    // 삭제할 데이터의 id 선언
    int ID;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily);


        //DB 생성
        dataBaseOpen = new DataBaseOpen(this);
        db = dataBaseOpen.getWritableDatabase();

        // DB읽어오기
        readTable();

        //리스트뷰 생성
        adapter = new ListViewBtnAdapter(this, R.layout.listview_btn_item, items,  this);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {     // 아이템 클릭 이벤트 처리
            @Override
            public void onItemClick(AdapterView parent, View v, int position, long id) {
                SetList();
                String msg = items.get((int) id).getPrint();
                AlertDialog.Builder alert = new AlertDialog.Builder(WriteDaily.this);
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                alert.setMessage(msg);
                alert.show();
                // TODO : item click
            }
        }) ;

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                AlertDialog.Builder alert2 = new AlertDialog.Builder(WriteDaily.this);
                alert2.setTitle("삭제");
                alert2.setMessage("삭제 할까요??");
                ID = (int) id;

                alert2.setPositiveButton("예", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        int n = items.get(ID).getId();
                        db.execSQL("DELETE FROM t_table WHERE id = " + n + ";");
                        adapter.notifyDataSetChanged();
                        SetList();

                        dialog.dismiss();
                    }
                });
                alert2.setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert2.show();

                // 그냥 클릭과 구분해주기 위해서 true로 바꿔줌
                return true;
            }
        });

        adapter.notifyDataSetChanged();
        SetList();

    }



    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        adapter.notifyDataSetChanged();
        SetList();
    }


    public void SetList(){
        readTable();
        ListView listView;
        ListViewBtnAdapter adapter;
        adapter = new ListViewBtnAdapter(this, R.layout.listview_btn_item, items,  this);
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(adapter);

    }
    public void readTable() {
        items = new ArrayList<MyData>();
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

            items.add(new MyData(id, date, time, address, latitude, longitude,  type, title, detail, picturekey));
            results.moveToNext();
        }
        results.close();
    }

    public void onListBtnClick(int position){
        Toast.makeText(this, Integer.toString(position + 1) + "번 아이템이 선택되었습니다.", Toast.LENGTH_SHORT).show() ;
        adapter.notifyDataSetChanged();
        SetList();

    }

    public void onClickBack(View v){
        finish();
    }

    public void onClickaddDaily(View v){
        Intent intent = new Intent(this, AddDaily.class);
        startActivityForResult(intent, 0);

    }

    protected void onResume() {
        super.onResume();
        SetList();
    }


}
