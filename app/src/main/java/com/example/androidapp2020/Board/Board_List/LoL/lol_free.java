package com.example.androidapp2020.Board.Board_List.LoL;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.androidapp2020.Board.Adapter.ListViewAdapter;
import com.example.androidapp2020.Board.Board_Item.BoardItem;
import com.example.androidapp2020.Board.Board_Write.board_write;
import com.example.androidapp2020.Board.ListVO.ListVO;
import com.example.androidapp2020.Chat.ChattingChannel;
import com.example.androidapp2020.FriendAddActivity;
import com.example.androidapp2020.Game;
import com.example.androidapp2020.MenuActivity;
import com.example.androidapp2020.ProfileActivity;
import com.example.androidapp2020.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class lol_free extends AppCompatActivity {
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
    private String my_userID;

    private int comments;
    private int recommendations;
    private int number;
    private int views;
    private long time2 = 0;

    private ImageButton btn_search;
    private ImageButton btn_write;
    private Button btn_notice;
    private Button btn_free;
    private Button btn_find;

    private EditText et_search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lol_free); // Link to xml
        ActionBar ab = getSupportActionBar();
        ab.setTitle("롤 자유게시판");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);
        listView = (ListView) findViewById(R.id.lv_lol_free);
        adapter = new ListViewAdapter(listVO);
        listView.setAdapter(adapter);

        btn_search = (ImageButton) findViewById(R.id.btn_lol_free_Search);
        btn_write = (ImageButton) findViewById(R.id.btn_lol_free_write);
        btn_notice = (Button) findViewById(R.id.btn_lol_free_notice);
        btn_free = (Button) findViewById(R.id.btn_lol_free_free);
        btn_find = (Button) findViewById(R.id.btn_lol_free_find);
        et_search = (EditText) findViewById(R.id.et_lol_free_Search);

        id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        myid = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        search = "";
        ((ListViewAdapter)listView.getAdapter()).getFilter().filter(search);

        database.child("Board_list").child("League_of_Legend").child("Free").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                ListVO listVO = dataSnapshot.getValue(ListVO.class);
                Key = dataSnapshot.getKey();
                adapter.addVO(listVO.getTitle(), listVO.getContent(), Key, listVO.getId(), listVO.getTime(), listVO.getT(),
                        listVO.getuserID(), listVO.getViews(), listVO.getComments(), listVO.getRecommendations(), listVO.getNum(),
                        "League_of_Legend");
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
                intent = new Intent(getApplicationContext(), League_of_Legend.class);
                startActivity(intent);
            }
        });

        btn_find.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), lol_find.class);
                startActivity(intent);
            }
        });
        btn_free.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), lol_free.class);
                startActivity(intent);
            }
        });

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(lol_free.this, board_write.class);
                intent.putExtra("board_type", "Free");
                intent.putExtra("game_type", "League_of_Legend");
                startActivity(intent);
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
                    taskMap.put("/Board_list/League_of_Legend/Free/" + key + "/views", ((ListVO) adapter.getItem(position)).getViews());
                }
                else {
                    taskMap.put("/Board_list/League_of_Legend/Free/" + key + "/views", ((ListVO) adapter.getItem(position)).getViews() + 1);
                }
                database.updateChildren(taskMap);
                views = ((ListVO)adapter.getItem(position)).getViews();

                Intent it_boardItem = new Intent(lol_free.this, BoardItem.class);
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
                it_boardItem.putExtra("board_type", "Free");
                it_boardItem.putExtra("game_type", "League_of_Legend");;
                startActivity(it_boardItem);
            }
        });
        database.child("User_list").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for(DataSnapshot s : snapshot.getChildren()){
                    if(s.getKey().equals("UID")){
                        if(s.getValue(String.class).equals(id)){
                            my_userID = snapshot.getKey();
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
    }

    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() - time2 >= 2000){
            time2 = System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
        else if(System.currentTimeMillis() - time2 < 2000 ){
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
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
            case R.id.btn_chat:
                Intent intent = new Intent(getApplicationContext(), ChattingChannel.class);
                intent.putExtra("myID", my_userID);
                startActivity(intent);
                return true;
            case R.id.btn_game:
                intent = new Intent(getApplicationContext(), Game.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}