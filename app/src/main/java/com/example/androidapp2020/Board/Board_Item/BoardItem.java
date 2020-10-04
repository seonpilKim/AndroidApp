package com.example.androidapp2020.Board.Board_Item;

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
import android.widget.PopupMenu;
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
import com.example.androidapp2020.Board.Board_List.AmUs.AmongUs;
import com.example.androidapp2020.Board.Board_List.AmUs.au_find;
import com.example.androidapp2020.Board.Board_List.AmUs.au_free;
import com.example.androidapp2020.Board.Board_List.BG.BG_find;
import com.example.androidapp2020.Board.Board_List.BG.BG_free;
import com.example.androidapp2020.Board.Board_List.BG.BattleGrounds;
import com.example.androidapp2020.Board.Board_List.ETC.ET_Cetera;
import com.example.androidapp2020.Board.Board_List.ETC.etc_find;
import com.example.androidapp2020.Board.Board_List.ETC.etc_free;
import com.example.androidapp2020.Board.Board_List.FIFA.FifaOnline4;
import com.example.androidapp2020.Board.Board_List.FIFA.ff_find;
import com.example.androidapp2020.Board.Board_List.FIFA.ff_free;
import com.example.androidapp2020.Board.Board_List.HS.HearthStone;
import com.example.androidapp2020.Board.Board_List.HS.hs_find;
import com.example.androidapp2020.Board.Board_List.HS.hs_free;
import com.example.androidapp2020.Board.Board_List.KR.KartRider;
import com.example.androidapp2020.Board.Board_List.KR.kr_find;
import com.example.androidapp2020.Board.Board_List.KR.kr_free;
import com.example.androidapp2020.Board.Board_List.LoL.League_of_Legend;
import com.example.androidapp2020.Board.Board_List.LoL.lol_find;
import com.example.androidapp2020.Board.Board_List.LoL.lol_free;
import com.example.androidapp2020.Board.Board_List.OW.OverWatch;
import com.example.androidapp2020.Board.Board_List.OW.ow_find;
import com.example.androidapp2020.Board.Board_List.OW.ow_free;
import com.example.androidapp2020.Board.Board_List.SC2.StarCraft2;
import com.example.androidapp2020.Board.Board_List.SC2.sc_find;
import com.example.androidapp2020.Board.Board_List.SC2.sc_free;
import com.example.androidapp2020.Board.Board_Write.board_edit;
import com.example.androidapp2020.Chat.ChattingChannel;
import com.example.androidapp2020.Chat.ChattingRoom;
import com.example.androidapp2020.FriendAddActivity;
import com.example.androidapp2020.Game;
import com.example.androidapp2020.Board.ListVO.CommVO;
import com.example.androidapp2020.MenuActivity;
import com.example.androidapp2020.ProfileActivity;
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
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

public class BoardItem extends AppCompatActivity {
    private Intent intent;

    private String key;
    private String id;
    private String recommended;
    private String board_type;
    private String game_type;
    private String userID;
    private String cm_userID;

    private DatabaseReference database;

    private ArrayList<String> recommendeds = new ArrayList<>();
    private ArrayList<CommVO> commVO = new ArrayList<>();
    private List<String> Friends;

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
    private Button mn_pf_addFriend;

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
        private String userID;
        private String content;
        private String time;


        Comment() { }
        public Comment(String id, String userID, String content, String time) {
            this.id = id;
            this.content = content;
            this.time = time;
            this.userID = userID;

        }

        public String getuserID() {return userID;}

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
    private boolean IsExist_Friend(String item){
        return Friends.contains(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_item);

        ActionBar ab = getSupportActionBar();

        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        intent = getIntent();
        id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        userID = intent.getStringExtra("userID");
        key = intent.getStringExtra("key");
        btn_register = (Button) findViewById(R.id.btn_lol_comment_Register);
        btn_recommendation = (Button) findViewById(R.id.btn_lol_board_Recommendations);
        btn_edit = (Button) findViewById(R.id.btn_lol_board_edit);
        btn_delete = (Button) findViewById(R.id.btn_lol_board_delete);
        mn_pf_addFriend = (Button)findViewById(R.id.mn_pf_addFriend);
        et_comment = (EditText) findViewById(R.id.et_lol_Comment);
        database = FirebaseDatabase.getInstance().getReference();
        listView = (ListView) findViewById(R.id.lv_board_view);
        scrollView = (ScrollView) findViewById(R.id.sv_lol_board_item);
        adapter = new CommViewAdapter(commVO);
        listView.setAdapter(adapter);
        number = intent.getIntExtra("number",0);
        alert = new AlertDialog.Builder(this);
        board_type = intent.getStringExtra("board_type");
        game_type = intent.getStringExtra("game_type");


        tv_title = (TextView) findViewById(R.id.tv_lol_board_Title);
        tv_title.setText(intent.getStringExtra("title"));
        tv_content = (TextView) findViewById(R.id.tv_lol_board_Content);
        tv_content.setText(intent.getStringExtra("content"));
        tv_id = (TextView) findViewById(R.id.tv_lol_board_Id);
        tv_id.setText(intent.getStringExtra("userID"));
        tv_time = (TextView) findViewById(R.id.tv_lol_board_Time);
        tv_time.setText(intent.getStringExtra("time"));
        tv_views = (TextView) findViewById((R.id.tv_lol_board_Views));
        if(id.equals(intent.getStringExtra("id"))) {
            tv_views.setText("" + (intent.getIntExtra("views", 0)));
        }
        else{
            tv_views.setText("" + (intent.getIntExtra("views", 0) + 1));
        }
        tv_comments = (TextView) findViewById(R.id.tv_lol_board_Comments);
        tv_comments.setText(""+(intent.getIntExtra("comments",0)));
        tv_recommendations = (TextView) findViewById(R.id.tv_lol_board_Recommendations);
        tv_recommendations.setText(""+(intent.getIntExtra("recommendations", 0)));
        tv_recommendations2 = (TextView) findViewById(R.id.tv_lol_board_Recommendations2);

        userID = tv_id.getText().toString();
        switch (game_type) {
            case "League_of_Legend":
                ab.setTitle("롤");
                break;
            case "BattleGrounds":
                ab.setTitle("배그");
                break;
            case "OverWatch":
                ab.setTitle("오버워치");
                break;
            case "FifaOnline4":
                ab.setTitle("피파4");
                break;
            case "KartRider":
                ab.setTitle("카트");
                break;
            case "AmongUs":
                ab.setTitle("어몽어스");
                break;
            case "StarCraft2":
                ab.setTitle("스타2");
                break;
            case "HearthStone":
                ab.setTitle("하스스톤");
                break;
            case"ET_Cetera":
                ab.setTitle("기타");
                break;
            default:
        }

        database.child("User_list").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for(DataSnapshot s : snapshot.getChildren()){
                    if(s.getKey().equals("UID")){
                        if(s.getValue(String.class).equals(id)){
                            cm_userID = snapshot.getKey();
                            break;
                        }
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });


        tv_id.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu p = new PopupMenu(getApplicationContext(), view);
                getMenuInflater().inflate(R.menu.profile_menu, p.getMenu());
                p.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch(menuItem.getItemId()) {
                            case R.id.mn_pf_addFriend:
                                FriendAddActivity.addFriendID(getApplicationContext(), cm_userID, userID);
                               return true;
                            case R.id.mn_pf_Chat:
                                intent = new Intent(getApplicationContext(), ChattingRoom.class);
                                intent.putExtra("myID", cm_userID);
                                intent.putExtra("otherID", tv_id.getText().toString());
                                startActivity(intent);
                                return true;
                            case R.id.mn_pf_Profile:
                                intent = new Intent(getApplicationContext(), ProfileActivity.class);
                                intent.putExtra("friendID", userID);
                                startActivity(intent);
                                return true;
                            default:
                                return BoardItem.super.onOptionsItemSelected(menuItem);
                        }
                    }
                });
                p.show();
            }
        });

        listView.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                scrollView.requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        if(board_type.equals("Find")){
            btn_recommendation.setVisibility(View.GONE);
            tv_recommendations.setVisibility(View.GONE);
            tv_recommendations2.setVisibility(View.GONE);
        }
        if(intent.getStringExtra("id").equals(id)){
            btn_delete.setVisibility(View.VISIBLE);
            btn_edit.setVisibility(View.VISIBLE);
        }
        else{
            btn_delete.setVisibility(View.INVISIBLE);
            btn_edit.setVisibility(View.INVISIBLE);
        }
        if(id.equals("228bc064cfc41a98")){
            btn_delete.setVisibility(View.VISIBLE);
        }


//수정
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), board_edit.class);
                intent.putExtra("title", tv_title.getText().toString());
                intent.putExtra("content", tv_content.getText().toString());
                intent.putExtra("key", key);
                intent.putExtra("game_type", game_type);
                intent.putExtra("board_type", board_type);
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
                        database.child("Board_list").child(game_type).child(board_type).child(key).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(getApplicationContext(), "게시글이 삭제되었습니다.", Toast.LENGTH_LONG).show();
                                switch(game_type){
                                    case "League_of_Legend": {
                                        switch (board_type) {
                                            case "Notice": {
                                                intent = new Intent(getApplicationContext(), League_of_Legend.class);
                                            }
                                            break;
                                            case "Find": {
                                                intent = new Intent(getApplicationContext(), lol_find.class);
                                            }
                                            break;
                                            case "Free": {
                                                intent = new Intent(getApplicationContext(), lol_free.class);
                                            }
                                            break;
                                            default:
                                        }
                                    }break;
                                    case "BattleGrounds" :{
                                        switch (board_type) {
                                            case "Notice": {
                                                intent = new Intent(getApplicationContext(), BattleGrounds.class);
                                            }
                                            break;
                                            case "Find": {
                                                intent = new Intent(getApplicationContext(), BG_find.class);
                                            }
                                            break;
                                            case "Free": {
                                                intent = new Intent(getApplicationContext(), BG_free.class);
                                            }
                                            break;
                                            default:
                                        }
                                    }break;
                                    case "OverWatch" :{
                                        switch (board_type) {
                                            case "Notice": {
                                                intent = new Intent(getApplicationContext(), OverWatch.class);
                                            }
                                            break;
                                            case "Find": {
                                                intent = new Intent(getApplicationContext(), ow_find.class);
                                            }
                                            break;
                                            case "Free": {
                                                intent = new Intent(getApplicationContext(), ow_free.class);
                                            }
                                            break;
                                            default:
                                        }
                                    }break;
                                    case "FifaOnline4" :{
                                        switch (board_type) {
                                            case "Notice": {
                                                intent = new Intent(getApplicationContext(), FifaOnline4.class);
                                            }
                                            break;
                                            case "Find": {
                                                intent = new Intent(getApplicationContext(), ff_find.class);
                                            }
                                            break;
                                            case "Free": {
                                                intent = new Intent(getApplicationContext(), ff_free.class);
                                            }
                                            break;
                                            default:
                                        }
                                    }break;
                                    case "KartRider": {
                                        switch (board_type) {
                                            case "Notice": {
                                                intent = new Intent(getApplicationContext(), KartRider.class);
                                            }
                                            break;
                                            case "Find": {
                                                intent = new Intent(getApplicationContext(), kr_find.class);
                                            }
                                            break;
                                            case "Free": {
                                                intent = new Intent(getApplicationContext(), kr_free.class);
                                            }
                                            break;
                                            default:
                                        }
                                    }break;
                                    case "AmongUs": {
                                        switch (board_type) {
                                            case "Notice": {
                                                intent = new Intent(getApplicationContext(), AmongUs.class);
                                            }
                                            break;
                                            case "Find": {
                                                intent = new Intent(getApplicationContext(), au_find.class);
                                            }
                                            break;
                                            case "Free": {
                                                intent = new Intent(getApplicationContext(), au_free.class);
                                            }
                                            break;
                                            default:
                                        }
                                    }break;
                                    case "StarCraft2": {
                                        switch (board_type) {
                                            case "Notice": {
                                                intent = new Intent(getApplicationContext(), StarCraft2.class);
                                            }
                                            break;
                                            case "Find": {
                                                intent = new Intent(getApplicationContext(), sc_find.class);
                                            }
                                            break;
                                            case "Free": {
                                                intent = new Intent(getApplicationContext(), sc_free.class);
                                            }
                                            break;
                                            default:
                                        }
                                    }break;
                                    case "HearthStone": {
                                        switch (board_type) {
                                            case "Notice": {
                                                intent = new Intent(getApplicationContext(), HearthStone.class);
                                            }
                                            break;
                                            case "Find": {
                                                intent = new Intent(getApplicationContext(), hs_find.class);
                                            }
                                            break;
                                            case "Free": {
                                                intent = new Intent(getApplicationContext(), hs_free.class);
                                            }
                                            break;
                                            default:
                                        }
                                    }break;
                                    case "ET_Cetera": {
                                        switch (board_type) {
                                            case "Notice": {
                                                intent = new Intent(getApplicationContext(), ET_Cetera.class);
                                            }
                                            break;
                                            case "Find": {
                                                intent = new Intent(getApplicationContext(), etc_find.class);
                                            }
                                            break;
                                            case "Free": {
                                                intent = new Intent(getApplicationContext(), etc_free.class);
                                            }
                                            break;
                                            default:
                                        }
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
        database.child("Board_list").child(game_type).child(board_type).child(key).child("recommended").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                recommendations = (int)dataSnapshot.getChildrenCount();
                recommendeds.clear();
                for (DataSnapshot recSnapshot : dataSnapshot.getChildren()) {
                    recommended = recSnapshot.getValue(String.class);
                    recommendeds.add(recommended);
                }
                taskMap.put("/Board_list/" + game_type + "/" + board_type + "/" + key + "/recommendations", recommendations);
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
                    database.child("Board_list").child(game_type).child(board_type).child(key).child("recommended").push().setValue(id);
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

        database.child("Board_list").child(game_type).child(board_type).child(key).child("commented").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                comments = (int)dataSnapshot.getChildrenCount();
                taskMap.put("/Board_list/" + game_type + "/" + board_type + "/" + key + "/comments", comments);
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
                SimpleDateFormat simpleDate = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");
                simpleDate.setTimeZone(zone);
                Comment com = new Comment(id, cm_userID, et_comment.getText().toString(), simpleDate.format(mDate));
                database.child("Board_list").child(game_type).child(board_type).child(key).child("commented").push().setValue(com);
                Toast.makeText(getApplicationContext(), "댓글이 작성되었습니다.", Toast.LENGTH_LONG).show();
                intent = getIntent();
                finish();
                startActivity(intent);
            }
        });

        database.child("Board_list").child(game_type).child(board_type).child(key).child("commented").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                CommVO commVO = dataSnapshot.getValue(CommVO.class);
                adapter.addVO(commVO.getID(), commVO.getuserID(), commVO.getContent(), commVO.getTime());
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
                switch(game_type) {
                    case "League_of_Legend": {
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), League_of_Legend.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), lol_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), lol_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "BattleGrounds" :{
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), BattleGrounds.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), BG_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), BG_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "OverWatch" :{
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), OverWatch.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), ow_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), ow_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "FifaOnline4" :{
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), FifaOnline4.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), ff_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), ff_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "KartRider": {
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), KartRider.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), kr_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), kr_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "AmongUs": {
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), AmongUs.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), au_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), au_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "StarCraft2": {
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), StarCraft2.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), sc_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), sc_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "HearthStone": {
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), HearthStone.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), hs_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), hs_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "ET_Cetera": {
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), ET_Cetera.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), etc_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), etc_free.class);
                            }
                            break;
                            default:
                        }
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
                intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.btn_friend:
                intent = new Intent(getApplicationContext(), FriendAddActivity.class);
                startActivity(intent);
                return true;
            case R.id.btn_game:
                intent= new Intent(getApplicationContext(), Game.class);
                startActivity(intent);
                return true;
            case R.id.btn_chat:
                Intent intent = new Intent(getApplicationContext(), ChattingChannel.class);
                intent.putExtra("myID", cm_userID);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}