package com.example.androidapp2020;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;


public class MenuActivity extends AppCompatActivity {
    private Intent intent;
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

    private long time= 0;

    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis() - time >= 2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"한 번 더 누르면 종료됩니다.", Toast.LENGTH_SHORT).show();
        }
        else if(System.currentTimeMillis() - time < 2000 ){
            ActivityCompat.finishAffinity(this);
            System.exit(0);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case R.id.btn_main:
                intent = new Intent(getApplicationContext(), MenuActivity.class);
                startActivity(intent);
                return true;
            case R.id.btn_profile:
                // 화면전환
                return true;
            case R.id.btn_friend:
                // 화면전환
                return true;
            case R.id.btn_setup:
                // 화면전환
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