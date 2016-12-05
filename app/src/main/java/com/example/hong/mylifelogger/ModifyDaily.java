package com.example.hong.mylifelogger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * Created by admin on 2016-11-28.
 */
public class ModifyDaily extends Activity{
    static DataBaseOpen dataBaseOpen;
    static SQLiteDatabase db;

    int id;
    double latitude = 0;
    double longitude = 0;

    String addressString;
    int mYear, mMonth, mDay, mHour, mMinute;
    int sYear = 0;
    int sMonth= 0, sDay = 0, sHour= 0,sMinute= 0;
    private String dateString, timeString;
    private String type, title, detail;
    private String picturekey;

    private MyLocation location; // 위치사용
    Spinner dailytype;      // 일상 스피너
    CheckBox locationcheck;
    CheckBox timecheck;
    Button locationbtn;
    Button timebtn;
    Button datebtn;
    Button completebtn;
    Button detailbtn;
    Button titlebtn;
    Intent intent;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify);

        dataBaseOpen = new DataBaseOpen(this);
        db = dataBaseOpen.getWritableDatabase();

        intent = getIntent();
        id = intent.getIntExtra("ID_KEY", 0);
        dateString = intent.getStringExtra("DATE_KEY");
        timeString = intent.getStringExtra("TIME_KEY");
        addressString = intent.getStringExtra("ADDRESS_KEY");
        latitude =  intent.getDoubleExtra("LATITUDE_KEY", 0.00);
        longitude = intent.getDoubleExtra("LONGITUDE_KEY", 0.00);
        type = intent.getStringExtra("TYPE_KEY");
        title = intent.getStringExtra("TITLE_KEY");
        detail = intent.getStringExtra("DETAIL_KEY");
        picturekey = intent.getStringExtra("PICTURE_KEY");




        dailytype = (Spinner) findViewById(R.id.spinner);   //일상 스피너
        ArrayAdapter dailyadpater = ArrayAdapter.createFromResource(this, R.array.type, android.R.layout.simple_spinner_item);
        dailyadpater.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dailytype.setAdapter(dailyadpater);


        locationcheck = (CheckBox) (findViewById(R.id.LocationCheck));
        timecheck =  (CheckBox) (findViewById(R.id.TimeCheck));
        locationbtn = (Button) findViewById(R.id.LocationHandle);
        datebtn = (Button) findViewById(R.id.datebtn);
        timebtn = (Button) findViewById(R.id.timebtn);
        completebtn = (Button) findViewById(R.id.completebtn);
        detailbtn = (Button) findViewById(R.id.alert2);
        titlebtn = (Button) findViewById(R.id.alert1);

        GregorianCalendar cal = new GregorianCalendar();
        mYear = cal.get(Calendar.YEAR);
        mMonth = cal.get(Calendar.MONTH);
        mDay  = cal.get(Calendar.DAY_OF_MONTH);
        mHour = cal.get(Calendar.HOUR_OF_DAY);
        mMinute = cal.get(Calendar.MINUTE);


        /*title 입력 하는 팝업창*/
        findViewById(R.id.alert1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder alert = new AlertDialog.Builder(ModifyDaily.this);

                alert.setTitle("제목 입력");
                alert.setMessage("Title 입력");
                final EditText Title = new EditText(ModifyDaily.this);
                alert.setView(Title);

                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        title = Title.getText().toString();
                    }
                });
                alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert.show();

            }
        });

        /*detail 입력하는 팝업창*/
        findViewById(R.id.alert2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                AlertDialog.Builder alert = new AlertDialog.Builder(ModifyDaily.this);

                alert.setTitle("내용 입력");
                alert.setMessage("Detail입력");
                final EditText Detail = new EditText(ModifyDaily.this);
                alert.setView(Detail);

                alert.setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        detail = Detail.getText().toString();
                    }
                });
                alert.setNegativeButton("no", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        dialog.dismiss();
                    }
                });
                alert.show();

            }
        });

        /*확인 버튼:  db에 데이터 입력*/
        findViewById(R.id.completebtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                addressString = getAddress(latitude, longitude);
                type = dailytype.getSelectedItem().toString();
                //String msg =title+ "\n" + detail +"\n" +type+"\n"+ dateString+"\n"+timeString+"\n"+addressString+"위도: " + latitude+ "경도: "+ longitude;
                //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                modifyData(id, dateString, timeString, addressString, latitude, longitude, type, title, detail);
                setResult(RESULT_OK, intent);

                finish();
            }
        });

        findViewById(R.id.datebtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(ModifyDaily.this, dateSetListener, mYear, mMonth, mDay).show();
                dateString = Integer.toString(sYear) + "년 " + Integer.toString(sMonth) + "월 " + Integer.toString(sDay) + "일";

            }
        });

        findViewById(R.id.timebtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new TimePickerDialog(ModifyDaily.this, timeSetListener, mHour, mMinute, false).show();
                timeString = Integer.toString(sHour) + "시 " + Integer.toString(sMinute) + "분";

            }
        });

        findViewById(R.id.LocationHandle).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Intent mapIntent = new Intent(ModifyDaily.this, ManualMap.class);
                startActivityForResult(mapIntent, 2);
               //latitude = mapIntent.getDoubleExtra("LATITUDE_KEY", 2);
                //longitude = mapIntent.getDoubleExtra("LONGITUDE_KEY", 2);

            }
        });

        // 현재위치 체크박스 사용
        locationcheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    locationbtn.setEnabled(false);
                    location =new MyLocation(ModifyDaily.this);
                    if (location.isGetLocation()) {
                        //위치
                        latitude = location.getLatitude();
                        longitude = location.getLongitude();
                        addressString = getAddress(latitude, longitude);
                        String msg = addressString+ "\n"+ "위도: "+latitude + "경도: " + longitude;
                        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

                    } else {
                        latitude = 0;
                        longitude = 0;
                        location.showSettingsAlert();

                    }
                }
                else{
                    locationbtn.setEnabled(true);
                }
            }
        });

        // 현재 시간 체크박스 사용
        timecheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if (isChecked) {
                    datebtn.setEnabled(false);
                    timebtn.setEnabled(false);
                    long now = System.currentTimeMillis();
                    Date date = new Date(now);
                    SimpleDateFormat CurDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
                    SimpleDateFormat CurTimeFormat = new SimpleDateFormat("HH시 mm분");
                    dateString = CurDateFormat.format(date);
                    timeString = CurTimeFormat.format(date);

                } else {
                    datebtn.setEnabled(true);
                    timebtn.setEnabled(true);
                }

            }
        });


    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        latitude =  data.getDoubleExtra("LATITUDE_KEY", 0.00);
        longitude = data.getDoubleExtra("LONGITUDE_KEY", 0.00);

    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            sYear = year;
            sMonth = monthOfYear+1;
            sDay = dayOfMonth;
            dateString = Integer.toString(sYear) + "년 " + Integer.toString(sMonth) + "월 " + Integer.toString(sDay) + "일";
        }
    };

    private TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO Auto-generated method stub
            sHour = hourOfDay;
            sMinute = minute;
            timeString = Integer.toString(sHour) + "시 " + Integer.toString(sMinute) + "분";

        }
    };


    public void insertData(String date, String time,
                           String address,  double latitude, double longitude, String type, String title, String detail) {
        db.execSQL("INSERT INTO t_table "
                + "VALUES(NULL, '" + date
                + "', '" + time
                + "', '" + address
                + "', '" + latitude
                + "', '" + longitude
                + "', '" + type
                + "', '" + title
                + "', '" + detail
                + "', '" + picturekey
                + "');");
    }
    public void modifyData(int id, String date, String time,
                           String address,  double latitude, double longitude, String type, String title, String detail) {
        db.execSQL("UPDATE t_table SET "+
                        "date = '"+date+"', "+
                        "time = '"+ time+"', "+
                        "address = '"+ address+"', "+
                        "latitude = '"+ latitude+"', "+
                        "longitude = '"+ longitude+"', "+
                        "type = '"+ type+"', "+
                        "title = '"+ title+"', "+
                        "detail = '"+ detail+"', "+
                        "picturekey = '"+ picturekey+
                        "' WHERE id = '"+ id + "' ;"
        );
    }



    public String getAddress(double latitude, double longitude) {
        String str = "주소를 찾는 중 입니다.";

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> list;
        try {
            if (geocoder != null) {
                list = geocoder.getFromLocation(latitude, longitude, 1);
                if (list != null && list.size() > 0) {
                    str = list.get(0).getAddressLine(0).toString();
                }
                return str;
            }
        } catch (IOException e) {
            return "error";
        }
        return str;
    }

}
