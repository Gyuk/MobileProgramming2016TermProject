package com.example.hong.mylifelogger;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by admin on 2016-12-06.
 */
public class EventListViewBtnAdapter extends ArrayAdapter implements View.OnClickListener {
    private static final  int  SERVER_OTHER_SEARCH = 1;

    static EventDataBase eventDataBase;
    static SQLiteDatabase db;
    Intent intent;


    public interface ListBtnClickListener {
        void onListBtnClick(int position) ;
    }
    // 생성자로부터 전달된 resource id 값을 저장.
    int resourceId ;
    // 생성자로부터 전달된 ListBtnClickListener  저장.
    private ListBtnClickListener listBtnClickListener ;


    // ListViewBtnAdapter 생성자. 마지막에 ListBtnClickListener 추가.

    EventListViewBtnAdapter(Context context, int resource, ArrayList<MyEventData> list, ListBtnClickListener clickListener) {
        super(context, resource, list) ;

        // resource id 값 복사. (super로 전달된 resource를 참조할 방법이 없음.)
        this.resourceId = resource ;
        this.listBtnClickListener = clickListener ;

    }

    public View getView(int position, View convertView, final ViewGroup parent) {
        final int pos = position ;
        final Context context = parent.getContext();

        // 생성자로부터 저장된 resourceId(listview_btn_item)에 해당하는 Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(this.resourceId/*R.layout.listview_btn_item*/, parent, false);
        }

        // 화면에 표시될 View(Layout이 inflate된)로부터 위젯에 대한 참조 획득
        final TextView textTextView = (TextView) convertView.findViewById(R.id.textView1);

        // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
        final MyEventData data = (MyEventData) getItem(position);

        // 아이템 내 각 위젯에 데이터 반영
        // textTextView.setText(listViewItem.getText());
        textTextView.setText(data.getTitle());


        Button modify = (Button) convertView.findViewById(R.id.modibtn);
        modify.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //textTextView.setText(Integer.toString(pos + 1) + "번 아이템 선택.");
                Intent intent = new Intent(parent.getContext(), ModifyEvent.class);
                // intent = new Intent(v.getContext(), ModifyDaily.class);
                intent.putExtra("ID_KEY", data.getId());
                intent.putExtra("DATE_KEY", data.getDate());
                intent.putExtra("TIME_KEY", data.getTime());
                intent.putExtra("ADDRESS_KEY", data.getAddress());
                intent.putExtra("LATITUDE_KEY", data.getLatitude());
                intent.putExtra("LONGITUDE_KEY", data.getLongitude());
                intent.putExtra("TITLE_KEY", data.getTitle());
                intent.putExtra("DETAIL_KEY", data.getDetail());
                intent.putExtra("PICTURE_KEY", data.getPicturekey());
                parent.getContext().startActivity(intent);


            }
        });

        // picturebtn 클릭 시 그림이 나오도록 수정할것
        Button picturebtn = (Button) convertView.findViewById(R.id.picturebtn);
        picturebtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                // 저장된 이미지 갤러리에서 불러오기
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_VIEW);


                // 해당 경로의 이미지파일을 부른다
                File file = new File(data.getPicturekey());

                Uri uri = Uri.fromFile(file);

                intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "image/*");
                getContext().startActivity(intent);



            }
        });

        // mapbtn 클릭시 지도에 마커되서 나오도록 수정할것
        Button mapbtn = (Button) convertView.findViewById(R.id.mapbtn);
        mapbtn.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
                //textTextView.setText(Integer.toString(pos + 1) + "번 아이템 선택.");

                // 리스트뷰 버튼에서 activity 실행시키기
                intent = new Intent(parent.getContext(), SimpleMap.class);
                intent.putExtra("LATITUDE_KEY", data.getLatitude());
                intent.putExtra("LONGITUDE_KEY", data.getLongitude());
                intent.putExtra("TITLE_KEY", data.getTitle());
                parent.getContext().startActivity(intent);

            }
        });
        //mapbtn.setTag(position);

        return convertView;
    }



    // button2가 눌려졌을 때 실행되는 onClick함수.
    public void onClick(View v) {
        // ListBtnClickListener(MainActivity)의 onListBtnClick() 함수 호출.
        if (this.listBtnClickListener != null) {
            this.listBtnClickListener.onListBtnClick((int)v.getTag()) ;

        }

    }

}
