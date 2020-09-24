package com.example.androidapp2020;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.androidapp2020.Board.AmongUs;
import com.example.androidapp2020.Board.BattleGrounds;
import com.example.androidapp2020.Board.ET_Cetera;
import com.example.androidapp2020.Board.FifaOnline4;
import com.example.androidapp2020.Board.HearthStone;
import com.example.androidapp2020.Board.KartRider;
import com.example.androidapp2020.Board.LoL.League_of_Legend;
import com.example.androidapp2020.Board.OverWatch;
import com.example.androidapp2020.Board.StarCraft2;

public class Game extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("게임");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        Button btn_LoL = (Button) findViewById(R.id.btn_LoL);
        btn_LoL.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent it_LoL = new Intent(getApplicationContext(), League_of_Legend.class);
                startActivity(it_LoL);
            }
        });

        Button btn_BG = (Button) findViewById(R.id.btn_BG);
        btn_BG.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent it_BG = new Intent(getApplicationContext(), BattleGrounds.class);
                startActivity(it_BG);
            }
        });

        Button btn_OW = (Button) findViewById(R.id.btn_OW);
        btn_OW.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent it_OW = new Intent(getApplicationContext(), OverWatch.class);
                startActivity(it_OW);
            }
        });

        Button btn_FI = (Button) findViewById(R.id.btn_FI);
        btn_FI.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent it_FI = new Intent(getApplicationContext(), FifaOnline4.class);
                startActivity(it_FI);
            }
        });

        Button btn_KT = (Button) findViewById(R.id.btn_KT);
        btn_KT.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent it_KT = new Intent(getApplicationContext(), KartRider.class);
                startActivity(it_KT);
            }
        });

        Button btn_AM = (Button) findViewById(R.id.btn_AM);
        btn_AM.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent it_AM = new Intent(getApplicationContext(), AmongUs.class);
                startActivity(it_AM);
            }
        });

        Button btn_SC = (Button) findViewById(R.id.btn_SC);
        btn_SC.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent it_SC = new Intent(getApplicationContext(), StarCraft2.class);
                startActivity(it_SC);
            }
        });

        Button btn_HS = (Button) findViewById(R.id.btn_HS);
        btn_HS.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent it_HS = new Intent(getApplicationContext(), HearthStone.class);
                startActivity(it_HS);
            }
        });

        Button btn_ETC = (Button) findViewById(R.id.btn_ETC);
        btn_ETC.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent it_ETC = new Intent(getApplicationContext(), ET_Cetera.class);
                startActivity(it_ETC);
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
                intent = new Intent(getApplicationContext(), FriendAddActivity.class);
                startActivity(intent);
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