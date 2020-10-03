package com.example.androidapp2020;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private Button _RegBtn;
    private EditText _ID;
    private String userID;
    private Map<String, Object> taskMap = new HashMap<String, Object>();
    private DatabaseReference database;
    private ArrayList<String> uid_list;
    private ArrayList<String> user_list;
    private String ID;

    public boolean IsExistUser(String item){
        return this.user_list.contains(item);
    }

    // 계속 return false....
    public boolean IsExistUid(String item){
        return this.uid_list.contains(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database = FirebaseDatabase.getInstance().getReference();
        _RegBtn = (Button) findViewById(R.id.registerBtn);
        _RegBtn.setEnabled(false);
        _ID = (EditText) findViewById(R.id.inputID);
        _ID.setCursorVisible(false);
        _ID.setInputType(EditorInfo.TYPE_NULL);
        ID = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        uid_list = new ArrayList<>();
        user_list = new ArrayList<>();
        userID = _ID.getText().toString();

        database.child("User_list").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user_list.clear();
                uid_list.clear();
                for (DataSnapshot ds : snapshot.getChildren()) {
                    user_list.add(ds.getKey());
                    UserData userData = ds.getValue(UserData.class);
                    uid_list.add(userData.UID);
                }

                if(IsExistUid(ID)) {

                    saveUserID(user_list.get(uid_list.indexOf(ID)));

                    Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), "자동로그인되었습니다.", Toast.LENGTH_LONG).show();
                }
                else {
                    _RegBtn.setEnabled(true);
                    _ID.setCursorVisible(true);
                    _ID.setInputType(EditorInfo.TYPE_CLASS_TEXT);
                    _RegBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            userID = _ID.getText().toString();

                            if (!IsExistUser(userID)) {
                                taskMap.put("/User_list/" + userID + "/UID", ID);
                                Toast.makeText(getApplicationContext(), "등록되었습니다.", Toast.LENGTH_LONG).show();
                                database.updateChildren(taskMap);
                                saveUserID(userID);
                                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                                intent.putExtra("userID", userID);
                                startActivity(intent);
                            } else {
                                Toast.makeText(getApplicationContext(), "이미 존재하는 닉네임입니다.", Toast.LENGTH_LONG).show();
                            }
                            _ID.setText("");
                        }
                    });
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });
    }

    void saveUserID(String saveUserID) {
        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString("loginID", saveUserID);
        editor.commit();
    }
}

class UserData {
    public String UID;
}