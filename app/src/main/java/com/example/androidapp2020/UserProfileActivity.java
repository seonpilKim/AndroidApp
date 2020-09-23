package com.example.androidapp2020;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.os.Build.ID;

public class UserProfileActivity extends AppCompatActivity {


    private Button user_reg_btn;
    private RadioGroup gender;
    private RadioButton gender_select;
    private RadioGroup mic;
    private RadioButton mic_select;
    private String gender_to_string;
    private String mic_to_string;
    private DatabaseReference database;
    private Map<String, Object> taskMap = new HashMap<String, Object>();
    private EditText age;

    public class User_Profile{
        String User_age;
        User_Profile(){}

        public User_Profile(String User_age){
            this.User_age=User_age;
        }

        public String getUser_age() {
            return User_age;
        }
    }

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener1 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            gender_select = gender.findViewById(i);

            switch(i){
                case R.id.bt_man:
                    gender_to_string = gender_select.getText().toString().trim();
                    break;
                case R.id.bt_woman:
                    gender_to_string = gender_select.getText().toString().trim();
                    break;
                default:
            }
        }
    };

    RadioGroup.OnCheckedChangeListener radioGroupButtonChangeListener2 = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup, int i) {
            mic_select = mic.findViewById(i);

            switch(i){
                case R.id.bt_yes:
                    mic_to_string = mic_select.getText().toString().trim();
                    break;
                case R.id.bt_no:
                    mic_to_string = mic_select.getText().toString().trim();
                    break;
                default:
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        database = FirebaseDatabase.getInstance().getReference();
        user_reg_btn = (Button) findViewById(R.id.bt_user_reg);
        age = findViewById(R.id.tn_age);

        Intent intent = getIntent();

        final String user_ID = intent.getExtras().getString("userID");

        gender = (RadioGroup) findViewById(R.id.radioGroup_gender);
        gender.setOnCheckedChangeListener(radioGroupButtonChangeListener1);

        mic = (RadioGroup) findViewById(R.id.radioGroup_mic);
        mic.setOnCheckedChangeListener(radioGroupButtonChangeListener2);

        user_reg_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                taskMap.put("/User_list/" + user_ID + "/Age",age.getText().toString());
                taskMap.put("/User_list/" + user_ID + "/Discord",mic_to_string);
                taskMap.put("/User_list/" + user_ID + "/Gender",gender_to_string);
                database.updateChildren(taskMap);
                Intent intent = new Intent(UserProfileActivity.this, MenuActivity.class);
                startActivity(intent);
            }
        });
    }
}
