package com.example.androidapp2020.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp2020.R;
//import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChattingRoom extends AppCompatActivity {
    private Map<String, Object> taskMap = new HashMap<String, Object>();
    public BaseAdapter bAdapter;
    private EditText editText;
    private ListView listView;
    private ImageButton sendBtn;
    private FirebaseDatabase fbDB = FirebaseDatabase.getInstance();
    private DatabaseReference dbRef = fbDB.getReference();
    private String otherID = "a";
    private String myID = "b";
    private long now;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        Intent intent = getIntent();
        otherID = intent.getStringExtra("otherID");
        myID = intent.getStringExtra("myID");
        sendBtn = (ImageButton)findViewById(R.id.sendBtn);
        editText = (EditText)findViewById(R.id.editText);

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = editText.getText().toString();

                if(str.length() > 0) {
                    now = System.currentTimeMillis();
                    chatMsg c = new chatMsg(str, myID, true, now);
                    taskMap.put("/ChattingList/" + myID + "/" + otherID + "/" + myID + "/" + bAdapter.getCount(), c);
                    dbRef.updateChildren(taskMap);
                    taskMap.put("/ChattingList/" + myID + "/" + otherID + "/recentMsg", str);
                    dbRef.updateChildren(taskMap);
                    chatMsg c1 = new chatMsg(str, myID, false, now);
                    taskMap.put("/ChattingList/" + otherID + "/" + myID + "/" + myID + "/" + bAdapter.getCount(), c1);
                    dbRef.updateChildren(taskMap);
                    taskMap.put("/ChattingList/" + otherID + "/" + myID + "/recentMsg", str);
                    dbRef.updateChildren(taskMap);
                    editText.setText("");
                }

                /*
                if(str.length() > 0) {
                    chatMsg c = new chatMsg(str, otherID, true);
                    dbRef.child("chatting").child(myID).push().setValue(c);
                    c.setIsMine(false);
                    dbRef.child("chatting").child(otherID).push().setValue(c);
                    editText.setText("");
                }
                */
            }
        });


        listView = findViewById(R.id.message_view);
        bAdapter = new chattingAdapter();

        bAdapter = new chattingAdapter(this);
        listView.setAdapter(bAdapter);
        fbDB = FirebaseDatabase.getInstance();
        dbRef = fbDB.getReference();

        dbRef.child("ChattingList").child(myID).child(otherID).child(myID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                chatMsg c = snapshot.getValue(chatMsg.class);
                ((chattingAdapter) bAdapter).add(c);
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

        dbRef.child("ChattingList").child(myID).child(otherID).child(otherID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                chatMsg c = snapshot.getValue(chatMsg.class);
                ((chattingAdapter) bAdapter).add(c);

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


        /*
        //display chat msg
        ListView message_view = (ListView)findViewById(R.id.message_view);
        //display my msg
        adapter = new FirebaseListAdapter<chatMsg>(this, chatMsg.class, R.layout.my_msg, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, chatMsg model, int position) {
                TextView time = (TextView)v.findViewById(R.id.message_time);
                TextView text = (TextView)v.findViewById(R.id.message_body);
                text.setText(model.getText());
                time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getTime()));
            }
        };
        //display other msg
        adapter = new FirebaseListAdapter<chatMsg>(this, chatMsg.class, R.layout.other_msg, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, chatMsg model, int position) {
                TextView name = (TextView)v.findViewById(R.id.name);
                TextView text = (TextView)v.findViewById(R.id.message_body);
                TextView time = (TextView)v.findViewById(R.id.message_time);
                text.setText(model.getText());
                name.setText(model.getUser());
                time.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)", model.getTime()));
            }
        };
        message_view.setAdapter(adapter);
         */
    }

}