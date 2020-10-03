package com.example.androidapp2020.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.androidapp2020.FriendAddActivity;
import com.example.androidapp2020.Game;
import com.example.androidapp2020.MenuActivity;
import com.example.androidapp2020.ProfileActivity;
import com.example.androidapp2020.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/*
    Intent needed:
    myID : String           (ID of user)
 */

public class CreateChatRoom extends AppCompatActivity {
    private String myID = "a";
    private ListView listView;
    private Button addBtn;
    private Button exitBtn;
    private List<String> friendList = new ArrayList<String>();
    private List<String> addedFriend = new ArrayList<String>();
    private BaseAdapter adapter;
    private long time2 = 0;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chatroom);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);
        bar.setTitle("대화상대 선택");

        intent = getIntent();
        myID = intent.getStringExtra("myID");

        adapter = new CreateChatRoomAdapter();
        adapter = new CreateChatRoomAdapter(this);

        listView = findViewById(R.id.friend_add_list);
        listView.setAdapter(adapter);
        addBtn = findViewById(R.id.add_btn);
        exitBtn = findViewById(R.id.exit_btn);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference friendRef = rootRef.child("Friend_list");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    if(myID.equals(ds.getKey())) {
                        for(DataSnapshot dss : ds.getChildren()) {
                            friendList = (List<String>) dss.getValue(Object.class);
                        }
                    }
                }
                for(int i = 0; i < friendList.size(); i++) {
                    FriendClass friend = new FriendClass(friendList.get(i), false);
                    ((CreateChatRoomAdapter)adapter).add(friend);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        friendRef.addListenerForSingleValueEvent(valueEventListener);

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                intent = new Intent(getApplicationContext(), ChattingChannel.class);
                intent.putExtra("myID", myID);
                startActivity(intent);
            }
        });

        addBtn.setOnClickListener(new View.OnClickListener() {
            FriendClass fc;
            @Override
            public void onClick(View view) {
                for(int i = 0; i < adapter.getCount(); i++) {
                    fc = (FriendClass) adapter.getItem(i);
                    if(fc.isChecked())
                        addedFriend.add(fc.getName());
                }

                // move to group chat room
                if(addedFriend.size() != 0) {
                    Intent intent = new Intent(CreateChatRoom.this, GroupChattingRoom.class);
                    addedFriend.add(myID);
                    intent.putStringArrayListExtra("members", (ArrayList<String>) addedFriend);
                    intent.putExtra("myID", myID);
                    startActivity(intent);
                }
            }
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
