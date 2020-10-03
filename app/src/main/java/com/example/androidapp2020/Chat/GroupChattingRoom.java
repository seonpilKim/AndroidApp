package com.example.androidapp2020.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp2020.R;
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
    Intent needed:
    myID: String            (ID of user)
    members: List<String>   (ID of other members in chat)
 */

public class GroupChattingRoom extends AppCompatActivity {
    private List<String> members = new ArrayList<String>();
    private String myID = "a";
    private BaseAdapter adapter;
    private EditText editText;
    private ListView listView;
    private long now;
    private ImageButton sendBtn;
    private List<UserData> memberData;
    private DatabaseReference chatRef;
    private DatabaseReference sendRef;
    private UserData combinedUID;
    private Map<String, Object> taskMap = new HashMap<String, Object>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        sendBtn = (ImageButton)findViewById(R.id.sendBtn);
        editText = (EditText)findViewById(R.id.editText);
        listView = findViewById(R.id.message_view);

        Intent intent = getIntent();
        members = intent.getStringArrayListExtra("members");
        myID = intent.getStringExtra("myID");
        members.add(myID);

        adapter = new chattingAdapter();
        adapter = new chattingAdapter(this);
        listView.setAdapter(adapter);

        memberData = new ArrayList<UserData>();
        combinedUID = new UserData();
        combinedUID.UID = "a";

        chatRef = FirebaseDatabase.getInstance().getReference();
        final ChildEventListener childEventListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                chatMsg c = snapshot.getValue(chatMsg.class);
                if(c.getSentBy().equals(myID))
                    c.setIsMine(true);
                else
                    c.setIsMine(false);
                ((chattingAdapter) adapter).add(c);
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

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference userRef = rootRef.child("User_list");
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()) {
                    for(int i = 0; i < members.size(); i++) {
                        if(members.get(i).equals(ds.getKey())) {
                            UserData ud = new UserData();
                            ud.UID = ds.child("UID").getValue(String.class);
                            memberData.add(ud);
                        }
                    }
                    combinedUID.UID = "";
                    memberData = sortUID(memberData);

                    for(int i = 0; i < memberData.size(); i++) {
                        combinedUID.UID = String.format("%s_%s", combinedUID.UID, memberData.get(i).UID);
                    }

                    chatRef = FirebaseDatabase.getInstance().getReference().child("ChatMessages").child(combinedUID.UID);
                    chatRef.addChildEventListener(childEventListener);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        userRef.addListenerForSingleValueEvent(valueEventListener);

        sendRef = FirebaseDatabase.getInstance().getReference();
        sendBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String str = editText.getText().toString();

                if(str.length() > 0) {
                    now = System.currentTimeMillis();
                    chatMsg c = new chatMsg(str, myID, true, now);
                    taskMap.put("/ChatMessages/" + combinedUID.UID + "/" + adapter.getCount(), c);
                    LastMsg l = new LastMsg(str, now, true, members);
                    String room = members.get(0);
                    for(int i = 1; i < members.size(); i++) {
                        room = String.format("%s, %s", room, members.get(i));
//                        taskMap.put("/UserChats/" + myID + "/" + members.get(i), l);
//                        taskMap.put("/UserChats/" + members.get(i) + "/" + myID, l);
                    }
                    taskMap.put("/UserChats/" + myID + "/" + room, l);
                    for(int i = 0; i < members.size(); i++) {
                        taskMap.put("/UserChats/" + members.get(i) + "/" + room, l);
                    }
                    sendRef.updateChildren(taskMap);
                    editText.setText("");
                }

            }
        });

    }

    List<UserData> sortUID(List<UserData> list) {
        int n = list.size();
        int compRes;
        for(int i = 0; i < n - 1; i++) {
            for(int j = 0; j < n - i - 1; j++) {
                compRes = list.get(j).UID.compareTo(list.get(j+1).UID);
                if(compRes > 0) {
                    UserData ud = list.get(j);
                    list.set(j, list.get(j+1));
                    list.set(j+1, ud);
                }
            }
        }

        return list;
    }
}
