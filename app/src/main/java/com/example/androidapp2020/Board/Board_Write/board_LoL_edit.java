package com.example.androidapp2020.Board.Board_Write;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.androidapp2020.Board.LoL.League_of_Legend;
import com.example.androidapp2020.Board.LoL.lol_find;
import com.example.androidapp2020.Board.LoL.lol_free;
import com.example.androidapp2020.Board.LoL.lol_star;
import com.example.androidapp2020.Game;
import com.example.androidapp2020.MainActivity;
import com.example.androidapp2020.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class board_LoL_edit extends AppCompatActivity {
    private Button btn_write;
    private EditText et_content;
    private EditText et_title;
    private DatabaseReference database;
    private Intent intent;
    private String key;
    private String type;
    private Map<String, Object> taskMap = new HashMap<String, Object>();
    private AlertDialog.Builder alert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board_lol_edit);
        ActionBar ab = getSupportActionBar();
        ab.setTitle("롤");
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowHomeEnabled(true);

        alert = new AlertDialog.Builder(this);
        database = FirebaseDatabase.getInstance().getReference();
        intent = getIntent();

        btn_write = (Button) findViewById(R.id.btn_lol_board_edit_write);
        et_title = (EditText) findViewById(R.id.et_lol_board_edit_title);
        et_content = (EditText) findViewById(R.id.et_lol_board_edit_content);

        et_title.setText(intent.getStringExtra("title"));
        et_content.setText(intent.getStringExtra("content"));
        key = intent.getStringExtra("key");
        type = intent.getStringExtra("type");

        btn_write.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                taskMap.put("/Board_list/" + type + "/" + key + "/title" , et_title.getText().toString());
                database.updateChildren(taskMap);
                taskMap.put("/Board_list/" + type + "/" + key + "/content" , et_content.getText().toString());
                database.updateChildren(taskMap);
                switch(type){
                    case "Notice" :{
                        intent = new Intent(getApplicationContext(), League_of_Legend.class);
                    }break;
                    case "Find" :{
                        intent = new Intent(getApplicationContext(), lol_find.class);
                    }break;
                    case "Free" :{
                        intent = new Intent(getApplicationContext(), lol_free.class);
                    }break;
                    case "Star" :{
                        intent = new Intent(getApplicationContext(), lol_star.class);
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
                switch(type){
                    case "Notice" :{
                        intent = new Intent(getApplicationContext(), League_of_Legend.class);
                    }break;
                    case "Find" :{
                        intent = new Intent(getApplicationContext(), lol_find.class);
                    }break;
                    case "Free" :{
                        intent = new Intent(getApplicationContext(), lol_free.class);
                    }break;
                    case "Star" :{
                        intent = new Intent(getApplicationContext(), lol_star.class);
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
                        switch(type){
                            case "Notice" :{
                                intent = new Intent(getApplicationContext(), League_of_Legend.class);
                            }break;
                            case "Find" :{
                                intent = new Intent(getApplicationContext(), lol_find.class);
                            }break;
                            case "Free" :{
                                intent = new Intent(getApplicationContext(), lol_free.class);
                            }break;
                            case "Star" :{
                                intent = new Intent(getApplicationContext(), lol_star.class);
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
                Intent intent1 = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent1);
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
                Intent intent5= new Intent(getApplicationContext(), Game.class);
                startActivity(intent5);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
