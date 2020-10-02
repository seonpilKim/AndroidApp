package com.example.androidapp2020.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp2020.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CreateChatRoom extends AppCompatActivity {
    private String myID = "a";
    private ListView listView;
    private Button addBtn;
    private List<String> friendList = new ArrayList<String>();
    private BaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_chatroom);

        Intent intent = getIntent();
        myID = intent.getStringExtra("myID");

        adapter = new CreateChatRoomAdapter();
        adapter = new CreateChatRoomAdapter(this);

        listView = findViewById(R.id.friend_add_list);
        listView.setAdapter(adapter);
        addBtn = findViewById(R.id.add_btn);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference friendRef = rootRef.child("Friend_list");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    if(myID.equals(ds.getKey())) {
                        for(DataSnapshot dss : ds.getChildren()) {
                            friendList.add(dss.getValue(String.class));
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
