package com.example.androidapp2020.Board.Board_Write;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp2020.Board.Board_List.AmUs.AmongUs;
import com.example.androidapp2020.Board.Board_List.AmUs.au_find;
import com.example.androidapp2020.Board.Board_List.AmUs.au_free;
import com.example.androidapp2020.Board.Board_List.BG.BG_find;
import com.example.androidapp2020.Board.Board_List.BG.BG_free;
import com.example.androidapp2020.Board.Board_List.BG.BattleGrounds;
import com.example.androidapp2020.Board.Board_List.ETC.ET_Cetera;
import com.example.androidapp2020.Board.Board_List.ETC.etc_find;
import com.example.androidapp2020.Board.Board_List.ETC.etc_free;
import com.example.androidapp2020.Board.Board_List.FIFA.FifaOnline4;
import com.example.androidapp2020.Board.Board_List.FIFA.ff_find;
import com.example.androidapp2020.Board.Board_List.FIFA.ff_free;
import com.example.androidapp2020.Board.Board_List.HS.HearthStone;
import com.example.androidapp2020.Board.Board_List.HS.hs_find;
import com.example.androidapp2020.Board.Board_List.HS.hs_free;
import com.example.androidapp2020.Board.Board_List.KR.KartRider;
import com.example.androidapp2020.Board.Board_List.KR.kr_find;
import com.example.androidapp2020.Board.Board_List.KR.kr_free;
import com.example.androidapp2020.Board.Board_List.LoL.League_of_Legend;
import com.example.androidapp2020.Board.Board_List.LoL.lol_find;
import com.example.androidapp2020.Board.Board_List.LoL.lol_free;
import com.example.androidapp2020.Board.Board_List.OW.OverWatch;
import com.example.androidapp2020.Board.Board_List.OW.ow_find;
import com.example.androidapp2020.Board.Board_List.OW.ow_free;
import com.example.androidapp2020.Board.Board_List.SC2.StarCraft2;
import com.example.androidapp2020.Board.Board_List.SC2.sc_find;
import com.example.androidapp2020.Board.Board_List.SC2.sc_free;
import com.example.androidapp2020.Chat.ChattingChannel;
import com.example.androidapp2020.FriendAddActivity;
import com.example.androidapp2020.Game;
import com.example.androidapp2020.MenuActivity;
import com.example.androidapp2020.ProfileActivity;
import com.example.androidapp2020.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class board_edit extends AppCompatActivity {
    private Button btn_write;
    private EditText et_content;
    private EditText et_title;
    private DatabaseReference database;
    private Intent intent;
    private String key;
    private String board_type;
    private String game_type;
    private Map<String, Object> taskMap = new HashMap<String, Object>();
    private AlertDialog.Builder alert;
    private String my_userID;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_edit);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("글 수정");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        alert = new AlertDialog.Builder(this);
        database = FirebaseDatabase.getInstance().getReference();
        intent = getIntent();
        id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        database.child("User_list").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                for(DataSnapshot s : snapshot.getChildren()){
                    if(s.getKey().equals("UID")){
                        if(s.getValue(String.class).equals(id)){
                            my_userID = snapshot.getKey();
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

        btn_write = (Button) findViewById(R.id.btn_lol_board_edit_write);
        et_title = (EditText) findViewById(R.id.et_lol_board_edit_title);
        et_content = (EditText) findViewById(R.id.et_lol_board_edit_content);

        et_title.setText(intent.getStringExtra("title"));
        et_content.setText(intent.getStringExtra("content"));
        key = intent.getStringExtra("key");
        board_type = intent.getStringExtra("board_type");
        game_type = intent.getStringExtra("game_type");

        btn_write.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                taskMap.put("/Board_list/" + game_type + "/" + board_type + "/" + key + "/title" , et_title.getText().toString());
                database.updateChildren(taskMap);
                taskMap.put("/Board_list/" + game_type + "/" + board_type + "/" + key + "/content" , et_content.getText().toString());
                database.updateChildren(taskMap);
                switch(game_type) {
                    case "League_of_Legend": {
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), League_of_Legend.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), lol_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), lol_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "BattleGrounds" :{
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), BattleGrounds.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), BG_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), BG_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "OverWatch" :{
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), OverWatch.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), ow_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), ow_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "FifaOnline4" :{
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), FifaOnline4.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), ff_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), ff_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "KartRider": {
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), KartRider.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), kr_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), kr_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "AmongUs": {
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), AmongUs.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), au_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), au_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "StarCraft2": {
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), StarCraft2.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), sc_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), sc_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "HearthStone": {
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), HearthStone.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), hs_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), hs_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "ET_Cetera": {
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), ET_Cetera.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), etc_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), etc_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    default:
                }
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "게시글이 수정되었습니다.", Toast.LENGTH_LONG).show();
            }
        });
    }
    @Override
    public void onBackPressed(){
        alert.setTitle("");
        alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                switch(game_type) {
                    case "League_of_Legend": {
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), League_of_Legend.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), lol_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), lol_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "BattleGrounds" :{
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), BattleGrounds.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), BG_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), BG_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "FifaOnline4" :{
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), FifaOnline4.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), ff_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), ff_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "OverWatch" :{
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), OverWatch.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), ow_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), ow_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "KartRider": {
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), KartRider.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), kr_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), kr_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "AmongUs": {
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), AmongUs.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), au_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), au_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "StarCraft2": {
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), StarCraft2.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), sc_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), sc_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "HearthStone": {
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), HearthStone.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), hs_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), hs_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    case "ET_Cetera": {
                        switch (board_type) {
                            case "Notice": {
                                intent = new Intent(getApplicationContext(), ET_Cetera.class);
                            }
                            break;
                            case "Find": {
                                intent = new Intent(getApplicationContext(), etc_find.class);
                            }
                            break;
                            case "Free": {
                                intent = new Intent(getApplicationContext(), etc_free.class);
                            }
                            break;
                            default:
                        }
                    }break;
                    default:
                }
                startActivity(intent);
            }
        });
        alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        alert.setMessage("지금 나가시면 이 글은 저장되지 않습니다.\n그래도 나가시겠습니까?");
        alert.show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch(item.getItemId()){
            case android.R.id.home: {
                alert.setTitle("");
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch(game_type) {
                            case "League_of_Legend" : {
                                switch (board_type) {
                                    case "Notice": {
                                        intent = new Intent(getApplicationContext(), League_of_Legend.class);
                                    }
                                    break;
                                    case "Find": {
                                        intent = new Intent(getApplicationContext(), lol_find.class);
                                    }
                                    break;
                                    case "Free": {
                                        intent = new Intent(getApplicationContext(), lol_free.class);
                                    }
                                    break;
                                    default:
                                }
                            }break;
                            case "BattleGrounds" :{
                                switch (board_type) {
                                    case "Notice": {
                                        intent = new Intent(getApplicationContext(), BattleGrounds.class);
                                    }
                                    break;
                                    case "Find": {
                                        intent = new Intent(getApplicationContext(), BG_find.class);
                                    }
                                    break;
                                    case "Free": {
                                        intent = new Intent(getApplicationContext(), BG_free.class);
                                    }
                                    break;
                                    default:
                                }
                            }break;
                            case "OverWatch" :{
                                switch (board_type) {
                                    case "Notice": {
                                        intent = new Intent(getApplicationContext(), OverWatch.class);
                                    }
                                    break;
                                    case "Find": {
                                        intent = new Intent(getApplicationContext(), ow_find.class);
                                    }
                                    break;
                                    case "Free": {
                                        intent = new Intent(getApplicationContext(), ow_free.class);
                                    }
                                    break;
                                    default:
                                }
                            }break;
                            case "FifaOnline4" :{
                                switch (board_type) {
                                    case "Notice": {
                                        intent = new Intent(getApplicationContext(), FifaOnline4.class);
                                    }
                                    break;
                                    case "Find": {
                                        intent = new Intent(getApplicationContext(), ff_find.class);
                                    }
                                    break;
                                    case "Free": {
                                        intent = new Intent(getApplicationContext(), ff_free.class);
                                    }
                                    break;
                                    default:
                                }
                            }break;
                            case "KartRider": {
                                switch (board_type) {
                                    case "Notice": {
                                        intent = new Intent(getApplicationContext(), KartRider.class);
                                    }
                                    break;
                                    case "Find": {
                                        intent = new Intent(getApplicationContext(), kr_find.class);
                                    }
                                    break;
                                    case "Free": {
                                        intent = new Intent(getApplicationContext(), kr_free.class);
                                    }
                                    break;
                                    default:
                                }
                            }break;
                            case "AmongUs": {
                                switch (board_type) {
                                    case "Notice": {
                                        intent = new Intent(getApplicationContext(), AmongUs.class);
                                    }
                                    break;
                                    case "Find": {
                                        intent = new Intent(getApplicationContext(), au_find.class);
                                    }
                                    break;
                                    case "Free": {
                                        intent = new Intent(getApplicationContext(), au_free.class);
                                    }
                                    break;
                                    default:
                                }
                            }break;
                            case "StarCraft2": {
                                switch (board_type) {
                                    case "Notice": {
                                        intent = new Intent(getApplicationContext(), StarCraft2.class);
                                    }
                                    break;
                                    case "Find": {
                                        intent = new Intent(getApplicationContext(), sc_find.class);
                                    }
                                    break;
                                    case "Free": {
                                        intent = new Intent(getApplicationContext(), sc_free.class);
                                    }
                                    break;
                                    default:
                                }
                            }break;
                            case "HearthStone": {
                                switch (board_type) {
                                    case "Notice": {
                                        intent = new Intent(getApplicationContext(), HearthStone.class);
                                    }
                                    break;
                                    case "Find": {
                                        intent = new Intent(getApplicationContext(), hs_find.class);
                                    }
                                    break;
                                    case "Free": {
                                        intent = new Intent(getApplicationContext(), hs_free.class);
                                    }
                                    break;
                                    default:
                                }
                            }break;
                            case "ET_Cetera": {
                                switch (board_type) {
                                    case "Notice": {
                                        intent = new Intent(getApplicationContext(), ET_Cetera.class);
                                    }
                                    break;
                                    case "Find": {
                                        intent = new Intent(getApplicationContext(), etc_find.class);
                                    }
                                    break;
                                    case "Free": {
                                        intent = new Intent(getApplicationContext(), etc_free.class);
                                    }
                                    break;
                                    default:
                                }
                            }break;
                            default:
                        }
                        startActivity(intent);
                    }
                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.setMessage("지금 나가시면 이 글은 저장되지 않습니다.\n그래도 나가시겠습니까?");
                alert.show();
                return true;
            }
            case R.id.btn_main:
                alert.setTitle("");
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        intent = new Intent(getApplicationContext(), MenuActivity.class);
                        startActivity(intent);
                    }
                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.setMessage("지금 나가시면 이 글은 저장되지 않습니다.\n그래도 나가시겠습니까?");
                alert.show();
                return true;
            case R.id.btn_profile:
                alert.setTitle("");
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        startActivity(intent);
                    }
                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.setMessage("지금 나가시면 이 글은 저장되지 않습니다.\n그래도 나가시겠습니까?");
                alert.show();
                return true;
            case R.id.btn_friend:
                alert.setTitle("");
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        intent = new Intent(getApplicationContext(), FriendAddActivity.class);
                        startActivity(intent);
                    }
                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.setMessage("지금 나가시면 이 글은 저장되지 않습니다.\n그래도 나가시겠습니까?");
                alert.show();
                return true;
            case R.id.btn_chat:
                alert.setTitle("");
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        intent = new Intent(getApplicationContext(), ChattingChannel.class);
                        intent.putExtra("myID", my_userID);
                        startActivity(intent);
                    }
                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.setMessage("지금 나가시면 이 글은 저장되지 않습니다.\n그래도 나가시겠습니까?");
                alert.show();
                return true;
            case R.id.btn_game:
                alert.setTitle("");
                alert.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        intent= new Intent(getApplicationContext(), Game.class);
                        startActivity(intent);
                    }
                });
                alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                alert.setMessage("지금 나가시면 이 글은 저장되지 않습니다.\n그래도 나가시겠습니까?");
                alert.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
