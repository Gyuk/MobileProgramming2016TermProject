package com.example.hong.mylifelogger;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


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


}
