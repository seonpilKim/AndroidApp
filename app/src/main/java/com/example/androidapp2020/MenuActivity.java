package com.example.androidapp2020;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp2020.Board.LoL.League_of_Legend;

public class MenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_select);

        Button _bt_pro_adj = (Button) findViewById(R.id.bt_profile_adj);
        _bt_pro_adj.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
            }
        });

        Button _bt_board = (Button) findViewById(R.id.bt_board);
        _bt_board.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Game.class);
                startActivity(intent);
            }
        });

        Button _bt_chat = (Button) findViewById(R.id.bt_chat);
        _bt_chat.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), FriendAddActivity.class);
                startActivity(intent);
            }
        });
    }
}
