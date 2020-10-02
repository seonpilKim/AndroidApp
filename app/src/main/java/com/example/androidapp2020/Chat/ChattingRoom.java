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
import com.example.androidapp2020.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
    intent needed:
    myID : String       (ID of user)
    otherID : String    (ID of opponent)
 */
public class ChattingRoom extends AppCompatActivity {
    private Map<String, Object> taskMap = new HashMap<String, Object>();
    public BaseAdapter bAdapter;
    private EditText editText;
    private ListView listView;
    private ImageButton sendBtn;
    private FirebaseDatabase fbDB;
    private DatabaseReference chatRef;
    private DatabaseReference sendRef;
    private String otherID = "a";
    private String myID = "b";
    private long now;
    private UserData myUID, otherUID, combinedUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);

        // receiving intent
        Intent intent = getIntent();
        otherID = intent.getStringExtra("otherID");
        myID = intent.getStringExtra("myID");
        sendBtn = (ImageButton)findViewById(R.id.sendBtn);
        editText = (EditText)findViewById(R.id.editText);
        listView = findViewById(R.id.message_view);
        bAdapter = new chattingAdapter();
        bAdapter = new chattingAdapter(this);
        listView.setAdapter(bAdapter);


        // initializing UID
        myUID = new UserData();
        myUID.UID = "a";
        otherUID = new UserData();
        otherUID.UID = "a";
        combinedUID = new UserData();
        combinedUID.UID = "a";

        chatRef = FirebaseDatabase.getInstance().getReference();

        // extract msg from DB and adding to adapter
        final ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                chatMsg c1 = snapshot.getValue(chatMsg.class);
                if(c1.getSentBy().equals(myID))
                    c1.setIsMine(true);
                else
                    c1.setIsMine(false);
                ((chattingAdapter) bAdapter).add(c1);
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
        };

        // extracting UID from DB
        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child("User_list");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    if(myID.equals(ds.getKey()))
                        myUID.UID = ds.child("UID").getValue(String.class);
                    else if (otherID.equals(ds.getKey()))
                        otherUID.UID = ds.child("UID").getValue(String.class);
                }

                // make combined UID for "chat room UID"
                combinedUID.UID = "";
                int compRes = myUID.UID.compareTo(otherUID.UID);
                if(compRes > 0)
                    combinedUID.UID = String.format("%s_%s",myUID.UID, otherUID.UID);
                else if (compRes < 0)
                    combinedUID.UID = String.format("%s_%s",otherUID.UID, myUID.UID);

                chatRef = FirebaseDatabase.getInstance().getReference().child("ChatMessages").child(combinedUID.UID);
                chatRef.addChildEventListener(childEventListener);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        userRef.addListenerForSingleValueEvent(valueEventListener);


        sendRef = FirebaseDatabase.getInstance().getReference();
        // send msg
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = editText.getText().toString();

                // push msg to DB
                if(str.length() > 0) {
                    now = System.currentTimeMillis();
                    chatMsg c = new chatMsg(str, myID, true, now);
                    taskMap.put("/ChatMessages/" + combinedUID.UID + "/" + bAdapter.getCount(), c);
                    sendRef.updateChildren(taskMap);
                    LastMsg l = new LastMsg(str, now);
                    taskMap.put("/UserChats/" + myID + "/" + otherID, l);
                    sendRef.updateChildren(taskMap);
                    taskMap.put("/UserChats/" + otherID + "/" + myID, l);
                    sendRef.updateChildren(taskMap);
                    editText.setText("");
                }
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