package com.example.androidapp2020.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp2020.FriendAddActivity;
import com.example.androidapp2020.Game;
import com.example.androidapp2020.MenuActivity;
import com.example.androidapp2020.R;
import com.example.androidapp2020.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Date;

public class ChattingChannel extends AppCompatActivity {
    private ListView chat_channel_list;
    private ChattingChannelAdapter adapter;
    private String otherID = "a";
    private String myID = "b";
    private FirebaseDatabase fbDB;
    private DatabaseReference dbRef;
    private DatabaseReference uidRef;
    private Intent intent;
    private String recentMsg = "s";
    private long time;
    private UserData myUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_list);
        ActionBar bar = getSupportActionBar();
        bar.setTitle("채팅");

        intent = getIntent();
        myID = intent.getStringExtra("myID");

        // click list -> move to chatting room activity
        chat_channel_list = (ListView) findViewById(R.id.chat_channel_list);

        adapter = new ChattingChannelAdapter();
        adapter = new ChattingChannelAdapter(this);
        chat_channel_list.setAdapter(adapter);

        fbDB = FirebaseDatabase.getInstance();
        dbRef = fbDB.getReference();

        // if new message
        dbRef.child("UserChats").child(myID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LastMsg l = snapshot.getValue(LastMsg.class);
                String user = snapshot.getKey();
                ListCard card = new ListCard(user ,l.getText() , l.getTime());
                ((ChattingChannelAdapter) adapter).add(card);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                LastMsg l = snapshot.getValue(LastMsg.class);
                for(int i = 0; i < adapter.getCount(); i++) {
                    ListCard card = (ListCard)adapter.getItem(i);
                    if(card.getUserName().equals(snapshot.getKey())) {
                        card.setRecentMsg(l.getText());
                        card.setTime(l.getTime());
                        adapter.update(card, i);
                        break;
                    }
                }
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





        chat_channel_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ListCard card = (ListCard) adapter.getItem(i);
                otherID = card.getUserName();
                Intent tmp = new Intent(ChattingChannel.this, ChattingRoom.class);
                tmp.putExtra("myID", myID);
                tmp.putExtra("otherID", otherID);
                startActivity(tmp);
            }
        });


    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.chatting_channel_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if(item.getItemId() == R.id.add_abIcon) {
            
        }

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