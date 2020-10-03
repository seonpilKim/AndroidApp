package com.example.androidapp2020;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.example.androidapp2020.Board.Board_List.AmUs.AmongUs;
import com.example.androidapp2020.Board.Board_List.BG.BattleGrounds;
import com.example.androidapp2020.Board.Board_List.ETC.ET_Cetera;
import com.example.androidapp2020.Board.Board_List.FIFA.FifaOnline4;
import com.example.androidapp2020.Board.Board_List.HS.HearthStone;
import com.example.androidapp2020.Board.Board_List.KR.KartRider;
import com.example.androidapp2020.Board.Board_List.LoL.League_of_Legend;
import com.example.androidapp2020.Board.Board_List.OW.OverWatch;
import com.example.androidapp2020.Board.Board_List.SC2.StarCraft2;
import com.example.androidapp2020.Chat.ChattingChannel;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Game extends AppCompatActivity {
    private Intent intent;
    private DatabaseReference database;
    private String id;
    private String cm_userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("게임");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        database = FirebaseDatabase.getInstance().getReference();

        database.child("User_list").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for(DataSnapshot s : snapshot.getChildren()){
                    if(s.getKey().equals("UID")){
                        if(s.getValue(String.class).equals(id)){
                            cm_userID = snapshot.getKey();
                            break;
                        }
                    }
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) { }
            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) { }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        });

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
                intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);
                return true;
            case R.id.btn_friend:
                intent = new Intent(getApplicationContext(), FriendAddActivity.class);
                startActivity(intent);
                return true;
            case R.id.btn_chat:
                Intent intent = new Intent(getApplicationContext(), ChattingChannel.class);
                intent.putExtra("myID", cm_userID);
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