package com.example.androidapp2020.Chat;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp2020.R;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.ArrayList;
import java.util.List;

public class ChattingRoom extends AppCompatActivity {
    public BaseAdapter bAdapter;
    private List<chatMsg> msgList;
    private EditText editText;
    private ListView listView;
    private ImageButton sendBtn;
    private DatabaseReference dbRef;
    private chatMsg chatting;
    String otherID = "getFromDB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);


        // send msg
        sendBtn = (ImageButton)findViewById(R.id.sendBtn);
        editText = (EditText)findViewById(R.id.editText);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = editText.getText().toString();

                if(str.length() > 0) {
                    chatMsg c = new chatMsg(str, otherID, true);
                    dbRef.push().setValue(c);
                }
            }
        });

        listView = findViewById(R.id.message_view);
        msgList = new ArrayList<>();
        chatting = new chatMsg();
        bAdapter = new chattingAdapter();

        bAdapter = new chattingAdapter(this);
        listView.setAdapter(bAdapter);

        // write to DB
        FirebaseDatabase db = FirebaseDatabase.getInstance();
        dbRef = db.getReference();

        dbRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                Log.d("CHATTING", snapshot.getValue().toString());
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

