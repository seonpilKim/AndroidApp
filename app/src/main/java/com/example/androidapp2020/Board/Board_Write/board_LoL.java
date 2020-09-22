package com.example.androidapp2020.Board.Board_Write;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp2020.Board.Board_Item.BoardItem;
import com.example.androidapp2020.Board.LoL.League_of_Legend;
import com.example.androidapp2020.Board.LoL.lol_find;
import com.example.androidapp2020.Board.LoL.lol_free;
import com.example.androidapp2020.Board.LoL.lol_star;
import com.example.androidapp2020.Game;
import com.example.androidapp2020.MainActivity;
import com.example.androidapp2020.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class board_LoL extends AppCompatActivity {
    private Button btn_write;
    private EditText et_content;
    private EditText et_title;
    private String id;
    private String type;
    private DatabaseReference database;
    private long now;
    private Date mDate;
    private TimeZone zone = TimeZone.getTimeZone("Asia/Seoul");
    private int number = 0;
    private CountDownTimer CDT;
    private AlertDialog.Builder alert;
    private Intent intent;
    private Intent it_LoL;

   private class Board{
        private String title;
        private String content;
        private String id;
        private String time;
        private String t;
        private int views;
        private int comments;
        private int recommendations;
        private int num;

        Board(){}
        public Board(String title, String content, String id, String time, String t,
                     int views, int comments, int recommendations, int num){
            this.title = title;
            this.content = content;
            this.id = id;
            this.time = time;
            this.t = t;
            this.views = views;
            this.comments = comments;
            this.recommendations = recommendations;
            this.num = num;
        }

        public String getTitle() {return title;}
        public String getContent() {return content;}
        public String getId() {return id;}
        public String getTime() {return time;}
        public String getT() {return t;}
        public int getViews() {return views;}
        public int getComments() {return comments;}
        public int getRecommendations() { return recommendations; }
        public int getNum() {return num;}

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board__lo_l);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("글 작성");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        alert = new AlertDialog.Builder(this);
        btn_write = (Button) findViewById(R.id.btn_write);
        et_title = (EditText) findViewById(R.id.et_title);
        et_content = (EditText) findViewById(R.id.et_content);
        database = FirebaseDatabase.getInstance().getReference();
        id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        intent = getIntent();
        type = intent.getStringExtra("type");

        btn_write.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

                // 현재 시간 설정
                now = System.currentTimeMillis();
                mDate = new Date(now);
                SimpleDateFormat Time = new SimpleDateFormat("yyyy.MM.dd HH:mm");
                SimpleDateFormat t = new SimpleDateFormat("MM.dd. HH:mm");
                Time.setTimeZone(zone);
                t.setTimeZone(zone);

                // DB에 context 저장 및 화면전환
                Board board = new Board(et_title.getText().toString(), et_content.getText().toString(), id
                , Time.format(mDate), t.format(mDate), 0, 0, 0, ++number);

                switch (type) {
                    case "Notice": {
                        database.child("Board_list").child("Notice").push().setValue(board);
                        intent = new Intent(getApplicationContext(), League_of_Legend.class);
                    }break;
                    case "Find": {
                        database.child("Board_list").child("Find").push().setValue(board);
                        intent = new Intent(getApplicationContext(), lol_find.class);
                    }break;
                    case "Free": {
                        database.child("Board_list").child("Free").push().setValue(board);
                        intent = new Intent(getApplicationContext(), lol_free.class);
                    }break;
                    default:
                }
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "게시글이 작성되었습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onBackPressed(){
        alert.setTitle("");
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch(type){
                    case "Notice" :{
                        it_LoL = new Intent(getApplicationContext(), League_of_Legend.class);
                    }break;
                    case "Find" :{
                        it_LoL = new Intent(getApplicationContext(), lol_find.class);
                    }break;
                    case "Free" :{
                        it_LoL = new Intent(getApplicationContext(), lol_free.class);
                    }break;
                    case "Star" :{
                        it_LoL = new Intent(getApplicationContext(), lol_star.class);
                    }break;
                    default:
                }
                startActivity(it_LoL);
            }
        });
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.setMessage("지금 나가시면 이 글은 저장되지 않습니다.\n그래도 나가시겠습니까?");
        alert.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home: {
                alert.setTitle("");
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch(type){
                            case "Notice" :{
                                it_LoL = new Intent(getApplicationContext(), League_of_Legend.class);
                            }break;
                            case "Find" :{
                                it_LoL = new Intent(getApplicationContext(), lol_find.class);
                            }break;
                            case "Free" :{
                                it_LoL = new Intent(getApplicationContext(), lol_free.class);
                            }break;
                            case "Star" :{
                                it_LoL = new Intent(getApplicationContext(), lol_star.class);
                            }break;
                            default:
                        }
                        startActivity(it_LoL);
                    }
                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.setMessage("지금 나가시면 이 글은 저장되지 않습니다.\n그래도 나가시겠습니까?");
                alert.show();
                return true;
            }
            case R.id.btn_main:
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
                return true;
            case R.id.btn_profile:
                // 화면전환
                return true;
            case R.id.btn_friend:
                // 화면전환
                return true;
            case R.id.btn_setup:
                // 화면전환
                return true;
            case R.id.btn_game:
                Intent intent5= new Intent(getApplicationContext(), Game.class);
                startActivity(intent5);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}