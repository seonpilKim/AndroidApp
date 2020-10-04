package com.example.androidapp2020.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.androidapp2020.FriendAddActivity;
import com.example.androidapp2020.Game;
import com.example.androidapp2020.MenuActivity;
import com.example.androidapp2020.ProfileActivity;
import com.example.androidapp2020.R;

import java.util.List;

public class GroupMember extends AppCompatActivity {
    private List<String> members;
    private long time2 = 0;
    private String myID = "a";
    private BaseAdapter adapter;
    private Intent intent;
    private ListView listView;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_member);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setTitle("멤버");

        adapter = new GroupMemberAdapter();
        adapter = new GroupMemberAdapter(this);

        listView = findViewById(R.id.group_list);
        listView.setAdapter(adapter);

        intent = getIntent();
        members = intent.getStringArrayListExtra("members");
        myID = intent.getStringExtra("myID");

        // 본인 아이디 제외
        for(int i = 0; i < members.size(); i++) {
            if(!members.get(i).equals(myID))  {
                FriendClass f = new FriendClass(members.get(i), false);
                ((GroupMemberAdapter) adapter).add(f);
            }
        }
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
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home:
                finish();
                return true;
            case R.id.add_abIcon:
                intent = new Intent(getApplicationContext(), CreateChatRoom.class);
                intent.putExtra("myID", myID);
                startActivity(intent);
                return true;
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
                intent = new Intent(getApplicationContext(), ChattingChannel.class);
                intent.putExtra("myID", myID);
                startActivity(intent);
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
