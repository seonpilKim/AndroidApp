package com.example.androidapp2020.Chat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.androidapp2020.FriendAddActivity;
import com.example.androidapp2020.Game;
import com.example.androidapp2020.MenuActivity;
import com.example.androidapp2020.ProfileActivity;
import com.example.androidapp2020.R;
//import com.firebase.ui.database.FirebaseListAdapter;
import com.example.androidapp2020.UserData;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

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
    private DatabaseReference chatRef;
    private DatabaseReference sendRef;
    private String otherID = "a";
    private String myID = "b";
    private long now;
    private UserData myUID, otherUID, combinedUID;
    private Intent intent;
    private long time2 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setDisplayShowHomeEnabled(true);

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
        getSupportActionBar().setTitle(otherID);


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
                /*
                chatMsg dateMsg;
                if(bAdapter.getCount() != 0) {
                    chatMsg prevMsg = (chatMsg) bAdapter.getItem(bAdapter.getCount() - 1);
                    long prev = prevMsg.getTime();
                    Date msgDate = new Date(c1.getTime());
                    Date prevDate = new Date(prev);
                    SimpleDateFormat time = new SimpleDateFormat("MM.dd");
                    TimeZone zone = TimeZone.getTimeZone("Asia/Seoul");
                    time.setTimeZone(zone);
                    String prevTime = time.format(prevDate);
                    String msgTime = time.format(msgDate);
                    if (prevTime.equals(msgTime)) {
                        dateMsg = new chatMsg("0721commandDatecommand0721", "command", false, c1.getTime());
                        ((chattingAdapter) bAdapter).add_icon(dateMsg);
                    }
                }
                else {
                    dateMsg = new chatMsg("0721commandDatecommand0721", "command", false, c1.getTime());
                    ((chattingAdapter)bAdapter).add_icon(dateMsg);
                }
            */
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
                    LastMsg l = new LastMsg(str, now, false);
                    taskMap.put("/UserChats/" + myID + "/" + otherID, l);
                    sendRef.updateChildren(taskMap);
                    taskMap.put("/UserChats/" + otherID + "/" + myID, l);
                    sendRef.updateChildren(taskMap);
                    editText.setText("");
                }
            }
        });
    }

    boolean sameMsg(chatMsg c1, chatMsg c2) {
        if(c1.getText().equals(c2.getText()) && c1.getSentBy().equals(c2.getSentBy()) && c1.getTime() == c2.getTime()) {
            return true;
        }
        return false;
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
        getMenuInflater().inflate(R.menu.chatting_channel_menu, menu);
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
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
                Intent intent = new Intent(getApplicationContext(), ChattingChannel.class);
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
