package com.example.hong.mylifelogger;

/**
 * Created by admin on 2016-12-06.
 */
public class MyEventData  {
    private int id;
    private String date;
    private String time;
    private String address;
    private String etitle;
    private String edetail;
    private double latitude;
    private double longitude;
    private String picturekey;

    public MyEventData(int i, String d, String t, String ad, double latitude, double longitude, String ti,
                  String de, String pk){
        id = i;
        date = d;
        time = t;
        address = ad;
        etitle = ti;
        edetail = de;
        this.latitude = latitude;
        this.longitude = longitude;
        picturekey = pk;
    }
    public String getDate() {
        return date;
    }
    public int getId(){return id;}
    public String getTime() {
        return time;
    }
    public String getAddress() {
        return address;
    }

    public void setText(String text) {
        this.etitle = text ;
    }

    public String getTitle() {return this.etitle;}
    public String getDetail() { return edetail;}
    public double getLatitude(){  return latitude;    }
    public double getLongitude(){
        return longitude;
    }
    public String getPicturekey(){return picturekey; }

    public String getPrint() {
        return "ID: "+ id+ "\n"+
                date + " " + time +"\n" +
                "주소: "+getAddress() + "\n" +
                "이벤트내용:" +  edetail;
    }


}
