package com.example.androidapp2020.Board.Board_List.ETC;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.androidapp2020.Board.Adapter.ListViewAdapter;
import com.example.androidapp2020.Board.Board_Item.BoardItem;
import com.example.androidapp2020.Board.Board_List.AmUs.AmongUs;
import com.example.androidapp2020.Board.Board_List.AmUs.au_find;
import com.example.androidapp2020.Board.Board_List.AmUs.au_free;
import com.example.androidapp2020.Board.Board_List.AmUs.au_star;
import com.example.androidapp2020.Board.Board_Write.board_write;
import com.example.androidapp2020.Board.ListVO.ListVO;
import com.example.androidapp2020.FriendAddActivity;
import com.example.androidapp2020.Game;
import com.example.androidapp2020.MenuActivity;
import com.example.androidapp2020.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ET_Cetera extends AppCompatActivity {
    private ListView listView;

    private ListViewAdapter adapter;

    private ArrayList<ListVO> listVO = new ArrayList<>();

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private Map<String, Object> taskMap = new HashMap<String, Object>();

    private Intent intent;

    private String Key;
    private String search;
    private String id;
    private String myid;
    private String title;
    private String content;
    private String key;
    private String time;
    private String userID;

    private int comments;
    private int recommendations;
    private int number;
    private int views;
    private long time2= 0;

    private Button btn_search;
    private Button btn_write;
    private Button btn_notice;
    private Button btn_star;
    private Button btn_free;
    private Button btn_find;

    private EditText et_search;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_e_t__cetera);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("기타 공지사항");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        listView = (ListView) findViewById(R.id.lv_etc_notice);
        adapter = new ListViewAdapter(listVO);
        listView.setAdapter(adapter);

        btn_search = (Button) findViewById(R.id.btn_etc_notice_Search);
        btn_write = (Button) findViewById(R.id.btn_etc_notice_write);
        btn_notice = (Button) findViewById(R.id.btn_etc_notice_notice);
        btn_star = (Button) findViewById(R.id.btn_etc_notice_star);
        btn_free = (Button) findViewById(R.id.btn_etc_notice_free);
        btn_find = (Button) findViewById(R.id.btn_etc_notice_find);
        et_search = (EditText) findViewById(R.id.et_etc_notice_Search);
        id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        myid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        search = "";
        ((ListViewAdapter)listView.getAdapter()).getFilter().filter(search);

// 검색
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search = et_search.getText().toString();
                if (search.replace(" ", "").equals("") || search.length() == 0) {
                    Toast.makeText(getApplicationContext(), "검색할 키워드를 입력해주세요.", Toast.LENGTH_SHORT).show();
                    et_search.setText("");
                } else {
                    ((ListViewAdapter) listView.getAdapter()).getFilter().filter(search, new Filter.FilterListener() {
                        @Override
                        public void onFilterComplete(int count) {
                            if (count == 0) {
                                Toast.makeText(getApplicationContext(), "검색 결과가 없습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "검색되었습니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        btn_notice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), ET_Cetera.class);
                startActivity(intent);
            }
        });

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), etc_find.class);
                startActivity(intent);
            }
        });
        btn_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), etc_free.class);
                startActivity(intent);
            }
        });
        btn_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), etc_star.class);
                startActivity(intent);
            }
        });

// 작성
        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(id.equals("228bc064cfc41a98")) {
                    Intent it_board_LoL = new Intent(getApplicationContext(), board_write.class);
                    it_board_LoL.putExtra("board_type", "Notice");
                    it_board_LoL.putExtra("game_type", "ET_Cetera");
                    startActivity(it_board_LoL);
                }
                else{
                    Toast.makeText(getApplicationContext(),"권한이 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        database.child("Board_list").child("ET_Cetera").child("Notice").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                ListVO listVO = dataSnapshot.getValue(ListVO.class);
                Key = dataSnapshot.getKey();
                adapter.addVO(listVO.getTitle(), listVO.getContent(), Key, listVO.getId(), listVO.getTime(), listVO.getT(),
                        listVO.getuserID(), listVO.getViews(), listVO.getComments(), listVO.getRecommendations(), listVO.getNum());
                adapter.notifyDataSetChanged();
                listView.setAdapter(adapter);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {

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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long i) {
                title = ((ListVO)adapter.getItem(position)).getTitle();
                content = ((ListVO)adapter.getItem(position)).getContent();
                key = ((ListVO)adapter.getItem(position)).getKey();
                id = ((ListVO)adapter.getItem(position)).getId();
                time = ((ListVO)adapter.getItem(position)).getTime();
                userID = ((ListVO)adapter.getItem(position)).getuserID();
                comments = ((ListVO)adapter.getItem(position)).getComments();
                recommendations = ((ListVO)adapter.getItem(position)).getRecommendations();
                number = ((ListVO)adapter.getItem(position)).getNum();
                if(id.equals(myid)){
                    taskMap.put("/Board_list/ET_Cetera/Notice/" + key + "/views", ((ListVO) adapter.getItem(position)).getViews());
                    views = ((ListVO)adapter.getItem(position)).getViews();
                }
                else {
                    taskMap.put("/Board_list/ET_Cetera/Notice/" + key + "/views", ((ListVO) adapter.getItem(position)).getViews() + 1);
                    views = ((ListVO)adapter.getItem(position)).getViews();
                }
                database.updateChildren(taskMap);

                Intent it_boardItem = new Intent(ET_Cetera.this, BoardItem.class);
                it_boardItem.putExtra("title", title);
                it_boardItem.putExtra("content", content);
                it_boardItem.putExtra("key", key);
                it_boardItem.putExtra("id", id);
                it_boardItem.putExtra("userID", userID);
                it_boardItem.putExtra("time", time);
                it_boardItem.putExtra("views", views);
                it_boardItem.putExtra("comments", comments);
                it_boardItem.putExtra("recommendations", recommendations);
                it_boardItem.putExtra("number", number);
                it_boardItem.putExtra("board_type", "Notice");
                it_boardItem.putExtra("game_type", "ET_Cetera");
                startActivity(it_boardItem);
            }
        });
    }

    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() - time2 >= 2000){
            time2=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
        else if(System.currentTimeMillis() - time2 < 2000 ){
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
            case R.id.btn_main:
                intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
                return true;
            case R.id.btn_profile:
                // 화면전환
                return true;
            case R.id.btn_friend:
                intent = new Intent(getApplicationContext(), FriendAddActivity.class);
                startActivity(intent);
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