package com.example.androidapp2020.Chat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp2020.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;

public class ChattingChannel extends AppCompatActivity {
    private ListView chat_channel_list;
    private ChattingChannelAdapter adapter;
    private String otherID = "a";
    private String myID = "b";
    private FirebaseDatabase fbDB;
    private DatabaseReference dbRef;
    private Intent intent;
    private long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting_list);

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
        dbRef.child("ChattingList").child(myID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
//                chatMsg c = snapshot.getValue(chatMsg.class);
                time = System.currentTimeMillis();
//                ListCard card = new ListCard(c.getUser(), c.getText(), time);

                String user = snapshot.getKey();
                ListCard card = new ListCard(user ,"s" , time);
                ((ChattingChannelAdapter) adapter).add(card);
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



        chat_channel_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView name_TextView = (TextView) findViewById(R.id.name_textView);
                otherID = name_TextView.getText().toString();
                Intent tmp = new Intent(ChattingChannel.this, ChattingRoom.class);
                tmp.putExtra("myID", myID);
                tmp.putExtra("otherID", otherID);
                startActivity(tmp);
            }
        });

    }
}
