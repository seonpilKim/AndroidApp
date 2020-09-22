package com.example.androidapp2020.Board.LoL;

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
import android.widget.ListView;
import android.widget.Toast;

import com.example.androidapp2020.Board.Adapter.ListViewAdapter;
import com.example.androidapp2020.Board.Board_Item.BoardItem;
import com.example.androidapp2020.Board.Board_Write.board_LoL;
import com.example.androidapp2020.Board.ListVO.ListVO;
import com.example.androidapp2020.Game;
import com.example.androidapp2020.MainActivity;
import com.example.androidapp2020.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class lol_star extends AppCompatActivity {
    private ListView listView;

    private ListViewAdapter adapter;

    private ArrayList<ListVO> listVO = new ArrayList<>();

    private DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    private Map<String, Object> taskMap = new HashMap<String, Object>();

    private Intent intent;

    private String Key;
    private String search;
    private String id;

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
        setContentView(R.layout.activity_lol_star); // Link to xml

        listView = (ListView) findViewById(R.id.lv_lol_star);
        adapter = new ListViewAdapter(listVO);
        listView.setAdapter(adapter);

        btn_search = (Button) findViewById(R.id.btn_lol_star_Search);
        btn_write = (Button) findViewById(R.id.btn_lol_star_write);
        btn_notice = (Button) findViewById(R.id.btn_lol_star_notice);
        btn_star = (Button) findViewById(R.id.btn_lol_star_star);
        btn_free = (Button) findViewById(R.id.btn_lol_star_free);
        btn_find = (Button) findViewById(R.id.btn_lol_star_find);
        et_search = (EditText) findViewById(R.id.et_lol_star_Search);

        id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
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

        ActionBar ab = getSupportActionBar();
        ab.setTitle("롤 인기게시판");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

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
        btn_star.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), lol_star.class);
                startActivity(intent);
            }
        });

        btn_write.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent(lol_star.this, board_LoL.class);
                intent.putExtra("type", "Star");
                startActivity(intent);
            }
        });

        database.child("Board_list").child("Star").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                ListVO listVO = dataSnapshot.getValue(ListVO.class);
                Key = dataSnapshot.getKey();
                adapter.addVO(listVO.getTitle(), listVO.getContent(), Key, listVO.getId(), listVO.getTime(), listVO.getT(),
                        listVO.getViews(), listVO.getComments(), listVO.getRecommendations(), listVO.getNum());
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
                String title = ((ListVO)adapter.getItem(position)).getTitle();
                String content = ((ListVO)adapter.getItem(position)).getContent();
                String key = ((ListVO)adapter.getItem(position)).getKey();
                String id = ((ListVO)adapter.getItem(position)).getId();
                String time = ((ListVO)adapter.getItem(position)).getTime();
                int comments = ((ListVO)adapter.getItem(position)).getComments();
                int recommendations = ((ListVO)adapter.getItem(position)).getRecommendations();
                int number = ((ListVO)adapter.getItem(position)).getNum();

                taskMap.put("/Board_list/Star/" + key + "/views", ((ListVO)adapter.getItem(position)).getViews() + 1);
                database.updateChildren(taskMap);
                int views = ((ListVO)adapter.getItem(position)).getViews();

                Intent it_boardItem = new Intent(lol_star.this, BoardItem.class);
                it_boardItem.putExtra("title", title);
                it_boardItem.putExtra("content", content);
                it_boardItem.putExtra("key", key);
                it_boardItem.putExtra("id", id);
                it_boardItem.putExtra("time", time);
                it_boardItem.putExtra("views", views);
                it_boardItem.putExtra("comments", comments);
                it_boardItem.putExtra("recommendations", recommendations);
                it_boardItem.putExtra("number", number);
                it_boardItem.putExtra("type", "Star");
                startActivity(it_boardItem);
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