package com.example.androidapp2020.Board.Board_Item;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.androidapp2020.Board.Adapter.CommViewAdapter;
import com.example.androidapp2020.Board.LoL.League_of_Legend;
import com.example.androidapp2020.Board.Board_Write.board_LoL_edit;
import com.example.androidapp2020.Board.LoL.lol_find;
import com.example.androidapp2020.Board.LoL.lol_free;
import com.example.androidapp2020.Board.LoL.lol_star;
import com.example.androidapp2020.Game;
import com.example.androidapp2020.Board.ListVO.CommVO;
import com.example.androidapp2020.MainActivity;
import com.example.androidapp2020.MenuActivity;
import com.example.androidapp2020.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class BoardItem extends AppCompatActivity {
    private Intent intent;

    private String key;
    private String id;
    private String recommended;
    private String type;

    private DatabaseReference database;

    private ArrayList<String> recommendeds = new ArrayList<>();
    private ArrayList<CommVO> commVO = new ArrayList<>();

    private CommViewAdapter adapter;

    private ListView listView;
    private ScrollView scrollView;

    private Map<String, Object> taskMap = new HashMap<String, Object>();

    private Date mDate;

    private TimeZone zone = TimeZone.getTimeZone("Asia/Seoul");

    private EditText et_comment;

    private Button btn_register;
    private Button btn_recommendation;
    private Button btn_edit;
    private Button btn_delete;

    private TextView tv_comments;
    private TextView tv_recommendations;
    private TextView tv_recommendations2;
    private TextView tv_views;
    private TextView tv_time;
    private TextView tv_id;
    private TextView tv_content;
    private TextView tv_title;

    private int comments;
    private int recommendations;
    private int number;

    private long now;

    private AlertDialog.Builder alert;

    private class Comment {
        private String id;
        private String content;
        private String time;

        Comment() { }
        public Comment(String id, String content, String time) {
            this.id = id;
            this.content = content;
            this.time = time;
        }

        public String getId() {
            return id;
        }

        public String getContent() {
            return content;
        }

        public String getTime() {
            return time;
        }
    }

    private boolean IsExist(String item){
        return recommendeds.contains(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_item);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("롤");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        intent = getIntent();
        key = intent.getStringExtra("key");
        btn_register = (Button) findViewById(R.id.btn_lol_comment_Register);
        btn_recommendation = (Button) findViewById(R.id.btn_lol_board_Recommendations);
        btn_edit = (Button) findViewById(R.id.btn_lol_board_edit);
        btn_delete = (Button) findViewById(R.id.btn_lol_board_delete);
        et_comment = (EditText) findViewById(R.id.et_lol_Comment);
        database = FirebaseDatabase.getInstance().getReference();
        listView = (ListView) findViewById(R.id.lv_board_view);
        scrollView = (ScrollView) findViewById(R.id.sv_lol_board_item);
        adapter = new CommViewAdapter(commVO);
        listView.setAdapter(adapter);
        number = intent.getIntExtra("number",0);
        alert = new AlertDialog.Builder(this);
        type = intent.getStringExtra("type");

        tv_title = (TextView) findViewById(R.id.tv_lol_board_Title);
        tv_title.setText(intent.getStringExtra("title"));
        tv_content = (TextView) findViewById(R.id.tv_lol_board_Content);
        tv_content.setText(intent.getStringExtra("content"));
        tv_id = (TextView) findViewById(R.id.tv_lol_board_Id);
        tv_id.setText(intent.getStringExtra("id"));
        tv_time = (TextView) findViewById(R.id.tv_lol_board_Time);
        tv_time.setText(intent.getStringExtra("time"));
        tv_views = (TextView) findViewById((R.id.tv_lol_board_Views));
        tv_views.setText(""+(intent.getIntExtra("views", 0)+1));
        tv_comments = (TextView) findViewById(R.id.tv_lol_board_Comments);
        tv_comments.setText(""+(intent.getIntExtra("comments",0)));
        tv_recommendations = (TextView) findViewById(R.id.tv_lol_board_Recommendations);
        tv_recommendations.setText(""+(intent.getIntExtra("recommendations", 0)));
        tv_recommendations2 = (TextView) findViewById(R.id.tv_lol_board_Recommendations2);

        listView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        if(type.equals("Find")){
            btn_recommendation.setVisibility(View.GONE);
            tv_recommendations.setVisibility(View.GONE);
            tv_recommendations2.setVisibility(View.GONE);
        }
        if(intent.getStringExtra("id").equals(id)){
            btn_delete.setVisibility(View.VISIBLE);
            btn_edit.setVisibility(View.VISIBLE);
        }
        if(id.equals("228bc064cfc41a98")){
            btn_delete.setVisibility(View.VISIBLE);
        }
        else{
            btn_delete.setVisibility(View.INVISIBLE);
            btn_edit.setVisibility(View.INVISIBLE);
        }

//수정
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), board_LoL_edit.class);
                intent.putExtra("title", tv_title.getText().toString());
                intent.putExtra("content", tv_content.getText().toString());
                intent.putExtra("key", key);
                intent.putExtra("type", type);
                finish();
                startActivity(intent);
            }
        });

// 삭제
        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alert.setTitle("");
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        database.child("Board_list").child(type).child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "게시글이 삭제되었습니다.", Toast.LENGTH_LONG).show();
                                switch(type){
                                    case "Notice" :{
                                        intent = new Intent(getApplicationContext(), League_of_Legend.class);
                                    }break;
                                    case "Find" :{
                                        intent = new Intent(getApplicationContext(), lol_find.class);
                                    }break;
                                    case "Free" :{
                                        intent = new Intent(getApplicationContext(), lol_free.class);
                                    }break;
                                    case "Star" :{
                                        intent = new Intent(getApplicationContext(), lol_star.class);
                                    }break;
                                    default:
                                }
                                startActivity(intent);
                            }
                        });
                    }
                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
               alert.setMessage("정말 삭제하시겠습니까?");
               alert.show();
            }
        });
// 추천
        database.child("Board_list").child(type).child(key).child("recommended").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recommendations = 0;
                for (DataSnapshot recSnapshot : dataSnapshot.getChildren()) {
                   recommendations++;
                   recommended = recSnapshot.getValue(String.class);
                   recommendeds.add(recommended);
                }
                taskMap.put("/Board_list/" + type + "/" + key + "/recommendations", recommendations);
                database.updateChildren(taskMap);
                tv_recommendations.setText("" + recommendations);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

        btn_recommendation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!IsExist(id)) {
                    database.child("Board_list").child(type).child(key).child("recommended").push().setValue(id);
                    Toast.makeText(getApplicationContext(), "이 게시글을 추천하였습니다.", Toast.LENGTH_LONG).show();
                    // 화면 새로고침
                    intent = getIntent();
                    finish();
                    startActivity(intent);
                }
                else {
                    Toast.makeText(getApplicationContext(), "이미 추천하셨습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        database.child("Board_list").child(type).child(key).child("commented").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments = 0;
                for(DataSnapshot comSnapshot : dataSnapshot.getChildren()){
                    comments++;
                }
                taskMap.put("/Board_list/" + type + "/" + key + "/comments", comments);
                database.updateChildren(taskMap);
                tv_comments.setText("" + comments);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

// 댓글
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                now = System.currentTimeMillis();
                mDate = new Date(now);
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy.MM.dd\nHH:mm:ss");
                simpleDate.setTimeZone(zone);
                Comment com = new Comment(id, et_comment.getText().toString(), simpleDate.format(mDate));
                database.child("Board_list").child(type).child(key).child("commented").push().setValue(com);
                Toast.makeText(getApplicationContext(), "댓글이 작성되었습니다.", Toast.LENGTH_LONG).show();
                intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        database.child("Board_list").child(type).child(key).child("commented").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                CommVO commVO = dataSnapshot.getValue(CommVO.class);
                adapter.addVO(commVO.getID(), commVO.getContent(), commVO.getTime());
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private long time= 0;
    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() - time >= 2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
        else if(System.currentTimeMillis() - time < 2000 ){
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        }
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
                switch(type){
                    case "Notice" :{
                        intent = new Intent(getApplicationContext(), League_of_Legend.class);
                    }break;
                    case "Find" :{
                        intent = new Intent(getApplicationContext(), lol_find.class);
                    }break;
                    case "Free" :{
                        intent = new Intent(getApplicationContext(), lol_free.class);
                    }break;
                    case "Star" :{
                        intent = new Intent(getApplicationContext(), lol_star.class);
                    }break;
                    default:
                }
                startActivity(intent);
                return true;
            }
            case R.id.btn_main:
                intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
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
                intent= new Intent(getApplicationContext(), Game.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}