package com.example.androidapp2020;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener{
    ///////// 화면 UI ////////////////////////////////////////////////////////////////////////////////
    //버튼이나 체크박스 데이터베이스 변수들
    DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();// 서버의 루트 변경해서 사용하면 됨
    Context context;
    Button btn_Update;
    private static final int PICK_IMAGE = 1994;
    ImageView user_picture;

    CheckBox check_lol;
    CheckBox check_battle;
    CheckBox check_over;
    CheckBox check_fifa4;
    CheckBox check_kart;
    CheckBox check_hearth;
    CheckBox check_star2;
    CheckBox check_amongus;


    TextView user_age;
    TextView user_gender;
    TextView user_dico;


    //  포지션 체크박스 용
    // lol포지션
    CheckBox lol_top;
    CheckBox lol_jungle;
    CheckBox lol_mid;
    CheckBox lol_bottom;
    CheckBox lol_support;

    // battle map
    CheckBox battle_sa;
    CheckBox battle_eran;
    CheckBox battle_mi;
    CheckBox battle_ka;
    CheckBox battle_bi;
    //인원
    CheckBox battle_1;
    CheckBox battle_2;
    CheckBox battle_3;
    CheckBox battle_4;
    CheckBox battle_5;
    CheckBox battle_6;
    //over
    CheckBox over_tanker;
    CheckBox over_dps;
    CheckBox over_healer;
    //fifa
    CheckBox fifa4_gon;
    CheckBox fifa4_chi;
    CheckBox fifa4_onebyone;
    CheckBox fifa4_twobytwo;
    CheckBox fifa4_bol;
    //Visible
    TextView game;
    TextView lol;
    TextView battle;
    TextView over;
    TextView fifa4;
    TextView kartrider;
    TextView hearthstone;
    TextView star2;

    RelativeLayout hidden2;
    RelativeLayout hidden3;
    RelativeLayout hidden4;
    RelativeLayout hidden5;
    RelativeLayout hidden6;
    RelativeLayout hidden7;
    RelativeLayout hidden8;

    Spinner lol_spinner1;
    Spinner lol_spinner2;
    Spinner battle_spinner;
    Spinner over_spinner;
    Spinner fifa4_spinner;
    Spinner kart_spinner1;
    Spinner kart_spinner2;
    Spinner hearth_spinner1;
    Spinner hearth_spinner2;
    Spinner star_spinner1;
    Spinner star_spinner2;

    ///////// 화면 UI ////////////////////////////////////////////////////////////////////////////////
    // 화면에 설정되고 있는 사용자 프로파일
    ProfileData pro_data = new ProfileData();
    String loginID;
    String friendID;
    String ID = "testUser";

    Boolean profileEditable; // 내것만 편집가능, 남의 것은 편집안됨.
    Boolean editState;    // 내것에서 편집모드

    String sort = "id"; // 아이디로 처음에 설정

    ArrayAdapter<String> arrayAdapter;

    static ArrayList<String> arrayIndex =  new ArrayList<String>();
    static ArrayList<String> arrayData = new ArrayList<String>();

    private void setImageAvatar(Context context, String imgBase64){
        try {
            Resources res = getResources();
            Bitmap src;
            if (imgBase64.equals("default")) {
                src = BitmapFactory.decodeResource(res, R.drawable.default_picture);
            } else {
                byte[] decodedString = Base64.decode(imgBase64, Base64.DEFAULT);
                src = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            }

            user_picture.setImageDrawable(ImageUtils.roundedImage(context, src));
        }catch (Exception e){
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);    //내용 저장용
        setContentView(R.layout.activity_profile); //

        ActionBar ab = getSupportActionBar();
        ab.setTitle("프로필");
        ab.setDisplayHomeAsUpEnabled(true);
        // ab.setDisplayShowHomeEnabled(true);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        loginID = pref.getString("loginID", "testUser");

        context = this;

        Intent intent = getIntent();
        friendID = intent.getStringExtra( "friendID");

        if (friendID == null) {  // 내 프로필
            ID = loginID;
            profileEditable = true;
            editState = false;
        } else {                 // 친구 프로필
            ID = friendID;
            profileEditable = false;
            editState = false;

        }
        ab.setSubtitle(ID);

        btn_Update = (Button) findViewById(R.id.btn_update);
        btn_Update.setOnClickListener(this);

        lol_spinner1 = (Spinner)findViewById(R.id.freerank);
        lol_spinner2 = (Spinner)findViewById(R.id.sololank);
        battle_spinner = (Spinner)findViewById(R.id.battle_tier);
        over_spinner = (Spinner)findViewById(R.id.over_lank);
        fifa4_spinner = (Spinner)findViewById(R.id.fifa4_tiers);
        kart_spinner1 = (Spinner)findViewById(R.id.kartrider_tier1);
        kart_spinner2 = (Spinner)findViewById(R.id.kartrider_tier2);
        hearth_spinner1 = (Spinner)findViewById(R.id.hearthstone_lank);
        hearth_spinner2 = (Spinner)findViewById(R.id.hearthstone_dack);
        star_spinner1 = (Spinner)findViewById(R.id.star2_triber);
        star_spinner2 = (Spinner)findViewById(R.id.star2_tiers);

        user_picture = (ImageView) findViewById(R.id.user_picture);
        user_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ID == loginID) {
                    new AlertDialog.Builder(view.getContext())
                            .setTitle("사진 설정")
                            .setMessage("프로필사진을 바꾸시겠어요?")
                            .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    Intent intent = new Intent();
                                    intent.setType("image/*");
                                    intent.setAction(Intent.ACTION_PICK);
                                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
                                    dialogInterface.dismiss();
                                }
                            })
                            .setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).show();
                }
            }
        });

        AdapterView.OnItemSelectedListener item_listener =  new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View arg1, int position, long id) {
                if (lol_spinner1.equals(parent)) {
                    pro_data.lol_selected1 = id;
                }
                else if (lol_spinner2.equals(parent)) {
                    pro_data.lol_selected2 = id;
                }
                else if (battle_spinner.equals(parent)) {
                    pro_data.battle_selected = id;
                }
                else if (over_spinner.equals(parent)) {
                    pro_data.over_selected = id;
                }
                else if (fifa4_spinner.equals(parent)) {
                    pro_data.fifa4_selected = id;
                }
                else if (kart_spinner1.equals(parent)) {
                    pro_data.kart_selected1 = id;
                }
                else if (kart_spinner2.equals(parent)) {
                    pro_data.kart_selected2 = id;
                }
                else if (hearth_spinner1.equals(parent)) {
                    pro_data.hearth_selected1 = id;
                }
                else if (hearth_spinner2.equals(parent)) {
                    pro_data.hearth_selected2 = id;
                }
                else if (star_spinner1.equals(parent)) {
                    pro_data.star2_selected1 = id;
                }
                else if (star_spinner2.equals(parent)) {
                    pro_data.star2_selected2 = id;
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        };

        lol_spinner1.setOnItemSelectedListener(item_listener);
        lol_spinner2.setOnItemSelectedListener(item_listener);

        battle_spinner.setOnItemSelectedListener(item_listener);
        over_spinner.setOnItemSelectedListener(item_listener);
        fifa4_spinner.setOnItemSelectedListener(item_listener);

        kart_spinner1.setOnItemSelectedListener(item_listener);
        kart_spinner2.setOnItemSelectedListener(item_listener);

        hearth_spinner1.setOnItemSelectedListener(item_listener);
        hearth_spinner2.setOnItemSelectedListener(item_listener);

        star_spinner2.setOnItemSelectedListener(item_listener);
        star_spinner1.setOnItemSelectedListener(item_listener);


        user_age = (TextView) findViewById(R.id.user_age);

        user_dico = (TextView) findViewById(R.id.user_dico);
        user_gender = (TextView) findViewById(R.id.user_gender);


        check_lol = (CheckBox) findViewById(R.id.check_lol);
        check_lol.setOnClickListener(this);

        check_battle = (CheckBox) findViewById(R.id.check_battle);
        check_battle.setOnClickListener(this);

        check_over = (CheckBox) findViewById(R.id.check_over);
        check_over.setOnClickListener(this);

        check_fifa4 = (CheckBox) findViewById(R.id.check_fifa4);
        check_fifa4.setOnClickListener(this);

        check_kart = (CheckBox) findViewById(R.id.check_kartrider);
        check_kart.setOnClickListener(this);

        check_hearth = (CheckBox) findViewById(R.id.check_hearthstone);
        check_hearth.setOnClickListener(this);

        check_star2 = (CheckBox) findViewById(R.id.check_star2);
        check_star2.setOnClickListener(this);

        check_amongus = (CheckBox) findViewById(R.id.check_amongus);
        check_amongus.setOnClickListener(this);

        //롤 포지션
        lol_top = (CheckBox) findViewById(R.id.check_top);
        lol_top.setOnClickListener(this);

        lol_jungle = (CheckBox) findViewById(R.id.check_jg);
        lol_jungle.setOnClickListener(this);

        lol_mid = (CheckBox) findViewById(R.id.check_mid);
        lol_mid.setOnClickListener(this);

        lol_bottom = (CheckBox) findViewById(R.id.check_ad);
        lol_bottom.setOnClickListener(this);

        lol_support = (CheckBox) findViewById(R.id.check_sp);
        lol_support.setOnClickListener(this);

        //배그 맵
        battle_sa = (CheckBox) findViewById(R.id.check_sa);
        battle_sa.setOnClickListener(this);

        battle_eran = (CheckBox) findViewById(R.id.check_eran);
        battle_eran.setOnClickListener(this);

        battle_mi = (CheckBox) findViewById(R.id.check_mi);
        battle_mi.setOnClickListener(this);

        battle_ka = (CheckBox) findViewById(R.id.check_ca);
        battle_ka.setOnClickListener(this);

        battle_bi = (CheckBox) findViewById(R.id.check_bi);
        battle_bi.setOnClickListener(this);

        //인원
        battle_1 = (CheckBox) findViewById(R.id.check_solo);
        battle_1.setOnClickListener(this);

        battle_2 = (CheckBox) findViewById(R.id.check_duo);
        battle_2.setOnClickListener(this);

        battle_3 = (CheckBox) findViewById(R.id.check_solos);
        battle_3.setOnClickListener(this);

        battle_4 = (CheckBox) findViewById(R.id.check_dus);
        battle_4.setOnClickListener(this);

        battle_5 = (CheckBox) findViewById(R.id.check_three);
        battle_5.setOnClickListener(this);

        battle_6 = (CheckBox) findViewById(R.id.check_four);
        battle_6.setOnClickListener(this);

        //over 포지션
        over_tanker = (CheckBox) findViewById(R.id.check_tanker);
        over_tanker.setOnClickListener(this);

        over_dps = (CheckBox) findViewById(R.id.check_dps);
        over_dps.setOnClickListener(this);

        over_healer = (CheckBox) findViewById(R.id.check_healer);
        over_healer.setOnClickListener(this);

        //fifa 경기용
        fifa4_gon = (CheckBox) findViewById(R.id.check_gon);
        fifa4_gon.setOnClickListener(this);

        fifa4_chi = (CheckBox) findViewById(R.id.check_chi);
        fifa4_chi.setOnClickListener(this);

        fifa4_onebyone = (CheckBox) findViewById(R.id.check_onebyone);
        fifa4_onebyone.setOnClickListener(this);


        fifa4_twobytwo = (CheckBox) findViewById(R.id.check_twobytwo);
        fifa4_twobytwo.setOnClickListener(this);

        fifa4_bol = (CheckBox) findViewById(R.id.check_bol);
        fifa4_bol.setOnClickListener(this);


        game = (TextView) findViewById(R.id.game);
        game.setOnClickListener(this);

        lol = (TextView) findViewById(R.id.lol);
        lol.setOnClickListener(this);

        battle = (TextView) findViewById(R.id.battle);
        battle.setOnClickListener(this);

        over = (TextView) findViewById(R.id.over);
        over.setOnClickListener(this);

        fifa4 = (TextView) findViewById(R.id.fifa4);
        fifa4.setOnClickListener(this);

        kartrider = (TextView) findViewById(R.id.kartrider);
        kartrider.setOnClickListener(this);

        hearthstone = (TextView) findViewById(R.id.hearthstone);
        hearthstone.setOnClickListener(this);

        star2 = (TextView) findViewById(R.id.star2);
        star2.setOnClickListener(this);

        hidden2 = (RelativeLayout) findViewById(R.id.hidden2);
        hidden3 = (RelativeLayout) findViewById(R.id.hidden3);
        hidden4 = (RelativeLayout) findViewById(R.id.hidden4);
        hidden5 = (RelativeLayout) findViewById(R.id.hidden5);
        hidden6 = (RelativeLayout) findViewById(R.id.hidden6);
        hidden7 = (RelativeLayout) findViewById(R.id.hidden7);
        hidden8 = (RelativeLayout) findViewById(R.id.hidden8);

        // 사용자 ID 값을 DB에서 가져옴.
        //  getFirebaseDatabase(ID);
        ValueEventListener dataListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                ProfileData get = dataSnapshot.getValue(ProfileData.class);

                if (get == null) {

                    setProfileEditable(profileEditable, editState);

                    hidden2.setVisibility(View.GONE);
                    hidden3.setVisibility(View.GONE);
                    hidden4.setVisibility(View.GONE);
                    hidden5.setVisibility(View.GONE);
                    hidden6.setVisibility(View.GONE);
                    hidden7.setVisibility(View.GONE);
                    hidden8.setVisibility(View.GONE);
                    return;
                }
                pro_data = get;

                if (get.game_selected != null) {
                    if (get.game_selected.get("LOL") != null && get.game_selected.get("LOL").booleanValue() == true) {
                        check_lol.setChecked(true);
                        hidden2.setVisibility(View.VISIBLE);
                    } else {
                        hidden2.setVisibility(View.GONE);
                    }
                    if (get.game_selected.get("배그") != null && get.game_selected.get("배그").booleanValue() == true){
                        check_battle.setChecked(true);
                        hidden3.setVisibility(View.VISIBLE);
                    } else {
                        hidden3.setVisibility(View.GONE);
                    }
                    if (get.game_selected.get("오버워치") != null && get.game_selected.get("오버워치").booleanValue() == true){
                        check_over.setChecked(true);
                        hidden4.setVisibility(View.VISIBLE);
                    } else {
                        hidden4.setVisibility(View.GONE);
                    }
                    if (get.game_selected.get("피파4") != null && get.game_selected.get("피파4").booleanValue() == true){
                        check_fifa4.setChecked(true);
                        hidden5.setVisibility(View.VISIBLE);
                    } else {
                        hidden5.setVisibility(View.GONE);
                    }

                    if (get.game_selected.get("카트라이더") != null && get.game_selected.get("카트라이더").booleanValue() == true){
                        check_kart.setChecked(true);
                        hidden6.setVisibility(View.VISIBLE);
                    } else {
                        hidden6.setVisibility(View.GONE);
                    }
                    if (get.game_selected.get("하스스톤") != null && get.game_selected.get("하스스톤").booleanValue() == true){
                        check_hearth.setChecked(true);
                        hidden7.setVisibility(View.VISIBLE);
                    } else {
                        hidden7.setVisibility(View.GONE);
                    }
                    if (get.game_selected.get("스타2") != null && get.game_selected.get("스타2").booleanValue() == true){
                        check_star2.setChecked(true);
                        hidden8.setVisibility(View.VISIBLE);
                    } else {
                        hidden8.setVisibility(View.GONE);
                    }
                    if (get.game_selected.get("어몽어스") != null && get.game_selected.get("어몽어스").booleanValue() == true)
                        check_amongus.setChecked(true);
                }

                if (get.lol_selected1 != null) {
                    lol_spinner1.setSelection(get.lol_selected1.intValue());
                }
                if (get.lol_selected2 != null) {
                    lol_spinner2.setSelection(get.lol_selected2.intValue());
                }
                if (get.lol_position != null) {
                    if (get.lol_position.get("top") != null && get.lol_position.get("top").booleanValue() == true)
                        lol_top.setChecked(true);
                    if (get.lol_position.get("bottom") != null && get.lol_position.get("bottom").booleanValue() == true)
                        lol_bottom.setChecked(true);
                    if (get.lol_position.get("jungle") != null && get.lol_position.get("jungle").booleanValue() == true)
                        lol_jungle.setChecked(true);
                    if (get.lol_position.get("mid") != null && get.lol_position.get("mid").booleanValue() == true)
                        lol_mid.setChecked(true);
                    if (get.lol_position.get("support") != null && get.lol_position.get("support").booleanValue() == true)
                        lol_support.setChecked(true);
                }
                if (get.battle_selected != null) {
                    battle_spinner.setSelection(get.battle_selected.intValue());
                    // check_battle.setChecked(true);
                }
                if (get.battle_map != null) {
                    if (get.battle_map.get("사녹") != null && get.battle_map.get("사녹").booleanValue() == true)
                        battle_sa.setChecked(true);
                    if (get.battle_map.get("에란겔") != null && get.battle_map.get("에란겔").booleanValue() == true)
                        battle_eran.setChecked(true);
                    if (get.battle_map.get("미라마") != null && get.battle_map.get("미라마").booleanValue() == true)
                        battle_mi.setChecked(true);
                    if (get.battle_map.get("카라틴") != null && get.battle_map.get("카라틴").booleanValue() == true)
                        battle_ka.setChecked(true);
                    if (get.battle_map.get("비켄디") != null && get.battle_map.get("비켄디").booleanValue() == true)
                        battle_bi.setChecked(true);
                }
                if (get.battle_squad != null) {
                    if (get.battle_squad.get("솔로") != null && get.battle_squad.get("솔로").booleanValue() == true)
                        battle_1.setChecked(true);
                    if (get.battle_squad.get("듀오") != null && get.battle_squad.get("듀오").booleanValue() == true)
                        battle_2.setChecked(true);
                    if (get.battle_squad.get("솔쿼드") != null && get.battle_squad.get("솔쿼드").booleanValue() == true)
                        battle_3.setChecked(true);
                    if (get.battle_squad.get("튜쿼드") != null && get.battle_squad.get("튜쿼드").booleanValue() == true)
                        battle_4.setChecked(true);
                    if (get.battle_squad.get("3인스쿼드") != null && get.battle_squad.get("3인스쿼드").booleanValue() == true)
                        battle_5.setChecked(true);
                    if (get.battle_squad.get("4인스쿼드") != null && get.battle_squad.get("4인스쿼드").booleanValue() == true)
                        battle_6.setChecked(true);
                }
                if (get.over_selected != null) {
                    over_spinner.setSelection(get.over_selected.intValue());
                    //check_over.setChecked(true);
                }
                if (get.over_position != null) {
                    if (get.over_position.get("탱커") != null && get.over_position.get("탱커").booleanValue() == true)
                        over_tanker.setChecked(true);
                    if (get.over_position.get("딜러") != null && get.over_position.get("딜러").booleanValue() == true)
                        over_dps.setChecked(true);
                    if (get.over_position.get("힐러") != null && get.over_position.get("힐러").booleanValue() == true)
                        over_healer.setChecked(true);
                }
                if (get.fifa4_game != null) {
                    if (get.fifa4_game.get("공식경기") != null && get.fifa4_game.get("공식경기").booleanValue() == true)
                        fifa4_gon.setChecked(true);
                    if (get.fifa4_game.get("친선경기") != null && get.fifa4_game.get("친선경기").booleanValue() == true)
                        fifa4_chi.setChecked(true);
                    if (get.fifa4_game.get("1 vs 1") != null && get.fifa4_game.get("1 vs 1").booleanValue() == true)
                        fifa4_onebyone.setChecked(true);
                    if (get.fifa4_game.get("2 vs 2") != null && get.fifa4_game.get("2 vs 2").booleanValue() == true)
                        fifa4_twobytwo.setChecked(true);
                    if (get.fifa4_game.get("볼타") != null && get.fifa4_game.get("볼타").booleanValue() == true)
                        fifa4_bol.setChecked(true);
                }
                if (get.fifa4_selected != null) {
                    fifa4_spinner.setSelection(get.fifa4_selected.intValue());
                }
                if (get.kart_selected1 != null) {
                    kart_spinner1.setSelection(get.kart_selected1.intValue());
                }
                if (get.kart_selected2 != null) {
                    kart_spinner2.setSelection(get.kart_selected2.intValue());
                }
                if (get.hearth_selected1 != null) {
                    hearth_spinner1.setSelection(get.hearth_selected1.intValue());
                }
                if (get.hearth_selected2 != null) {
                    hearth_spinner2.setSelection(get.hearth_selected2.intValue());
                }
                if (get.star2_selected1 != null) {
                    star_spinner1.setSelection(get.star2_selected1.intValue());
                }
                if (get.star2_selected2 != null) {
                    star_spinner2.setSelection(get.star2_selected2.intValue());
                }

                setProfileEditable(profileEditable, editState);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
            }
        };
        Query data = mPostReference.child("Profile_list").child(ID).orderByKey();
        data.addListenerForSingleValueEvent(dataListener);

        ValueEventListener dataListener0 = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserData0 userData = snapshot.getValue(UserData0.class);
                user_age.setText(userData.Age);
                user_gender.setText(userData.Gender);
                user_dico.setText(userData.Discord);
                if (userData.Picture == null) {
                    setImageAvatar(context, "default");
                } else {
                    setImageAvatar(context, userData.Picture);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) { }
        };

        Query data0 = mPostReference.child("User_list").child(ID).orderByKey();
        data0.addListenerForSingleValueEvent(dataListener0);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == AppCompatActivity.RESULT_OK) {
            if (data == null) {
                Toast.makeText(this, "사진이 선택되지 않았습니다.", Toast.LENGTH_LONG).show();
                return;
            }
            try {
                InputStream inputStream = getContentResolver().openInputStream(data.getData());

                Bitmap imgBitmap = BitmapFactory.decodeStream(inputStream);
                imgBitmap = ImageUtils.cropToSquare(imgBitmap);
                InputStream is = ImageUtils.convertBitmapToInputStream(imgBitmap);
                final Bitmap liteImage = ImageUtils.makeImageLite(is,
                        imgBitmap.getWidth(), imgBitmap.getHeight(),
                        ImageUtils.AVATAR_WIDTH, ImageUtils.AVATAR_HEIGHT);

                String imageBase64 = ImageUtils.encodeBase64(liteImage);
                //myAccount.avata = imageBase64;

                mPostReference.child("User_list/"+ID).child("Picture").setValue(imageBase64)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Log.w("onActivityResult","Picure is updated");
                                    user_picture.setImageDrawable(ImageUtils.roundedImage(context, liteImage));
                                    Toast.makeText(context, "사진이 변경되었습니다.", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(context, "서버에 저장중 에러가 발생되었습니다.", Toast.LENGTH_LONG).show();
                            }
                        });
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void setProfileEditable(Boolean profileEditable, Boolean editable) {
        if (profileEditable) {
            if (editable) {
                btn_Update.setText("저장");
            } else {
                btn_Update.setText("수정");
            }
        } else {
            btn_Update.setVisibility(View.GONE);
        }
        check_lol.setEnabled(editable);
        check_battle.setEnabled(editable);
        check_over.setEnabled(editable);
        check_fifa4.setEnabled(editable);
        check_kart.setEnabled(editable);
        check_hearth.setEnabled(editable);
        check_star2.setEnabled(editable);
        check_amongus.setEnabled(editable);
        lol_spinner1.setEnabled(editable);
        lol_spinner2.setEnabled(editable);

        lol_top.setEnabled(editable);
        lol_bottom.setEnabled(editable);
        lol_jungle.setEnabled(editable);
        lol_mid.setEnabled(editable);
        lol_support.setEnabled(editable);

        battle_spinner.setEnabled(editable);

        battle_sa.setEnabled(editable);
        battle_eran.setEnabled(editable);
        battle_mi.setEnabled(editable);
        battle_ka.setEnabled(editable);
        battle_bi.setEnabled(editable);

        battle_1.setEnabled(editable);
        battle_2.setEnabled(editable);
        battle_3.setEnabled(editable);
        battle_4.setEnabled(editable);
        battle_5.setEnabled(editable);
        battle_6.setEnabled(editable);

        over_spinner.setEnabled(editable);
        over_dps.setEnabled(editable);
        over_tanker.setEnabled(editable);
        over_healer.setEnabled(editable);

        fifa4_gon.setEnabled(editable);
        fifa4_chi.setEnabled(editable);
        fifa4_onebyone.setEnabled(editable);
        fifa4_twobytwo.setEnabled(editable);
        fifa4_bol.setEnabled(editable);
        fifa4_spinner.setEnabled(editable);
        kart_spinner1.setEnabled(editable);
        kart_spinner2.setEnabled(editable);
        hearth_spinner1.setEnabled(editable);
        hearth_spinner2.setEnabled(editable);
        star_spinner1.setEnabled(editable);
        star_spinner2.setEnabled(editable);
    }

    public void postFirebaseDatabase(boolean add){
        mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;
        if(add){
            postValues = pro_data.toMap();
        }
        childUpdates.put("/Profile_list/" + ID, postValues);  //key 값과 child 데이터 합치기
        mPostReference.updateChildren(childUpdates); // firebase 넣어주기
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_update: // 내 프로필에서만 올수 있다.
                if (editState) {
                    postFirebaseDatabase(true);
                    Toast.makeText(this.getApplicationContext(),"사용자 프로필이 수정되었습니다.", Toast.LENGTH_SHORT).show();
                    editState = false;
                    setProfileEditable(profileEditable, editState);
                } else {
                    editState = true;
                    setProfileEditable(profileEditable, editState);
                }
                break;

            //////////////
            case R.id.check_lol:
                if (check_lol.isChecked()) {
                    pro_data.game_selected.put("LOL", true);
                    hidden2.setVisibility(View.VISIBLE);
                } else {
                    pro_data.game_selected.put("LOL", false);
                    hidden2.setVisibility(View.GONE);
                }
                break;

            case R.id.check_battle:
                if (check_battle.isChecked())
                {pro_data.game_selected.put("배그", true);
                    hidden3.setVisibility(View.VISIBLE);}
                else{
                    pro_data.game_selected.put("배그", false);
                    hidden3.setVisibility(View.GONE);
                }
                break;

            case R.id.check_over:
                if (check_over.isChecked())
                {pro_data.game_selected.put("오버워치", true);
                    hidden4.setVisibility(View.VISIBLE);}
                else
                {    pro_data.game_selected.put("오버워치", false);
                    hidden4.setVisibility(View.GONE);}
                break;

            case R.id.check_fifa4:
                if (check_fifa4.isChecked()) {
                    pro_data.game_selected.put("피파4", true);
                    hidden5.setVisibility(View.VISIBLE);
                }
                else {
                    pro_data.game_selected.put("피파4", false);
                    hidden5.setVisibility(View.GONE);
                }
                break;

            case R.id.check_kartrider:
                if (check_kart.isChecked()) {
                    pro_data.game_selected.put("카트라이더", true);
                    hidden6.setVisibility(View.VISIBLE);
                }
                else
                {   pro_data.game_selected.put("카트라이더", false);
                    hidden6.setVisibility(View.GONE);}
                break;


            case R.id.check_hearthstone:
                if (check_hearth.isChecked())
                {   pro_data.game_selected.put("하스스톤", true);
                    hidden7.setVisibility(View.VISIBLE);
                }
                else
                {   pro_data.game_selected.put("하스스톤", false);
                    hidden7.setVisibility(View.GONE);
                }
                break;

            case R.id.check_star2:
                if (check_star2.isChecked()) {
                    pro_data.game_selected.put("스타2", true);
                    hidden8.setVisibility(View.VISIBLE);
                }
                else {
                    pro_data.game_selected.put("스타2", false);
                    hidden8.setVisibility(View.GONE);
                }
                break;

            case R.id.check_amongus:
                if (check_amongus.isChecked())
                    pro_data.game_selected.put("어몽어스", true);
                else
                    pro_data.game_selected.put("어몽어스", false);
                break;




            case R.id.check_top:
                //Toast.makeText(MainActivity.this, "top" + lol_top.isChecked(), Toast.LENGTH_SHORT).show();
                if (lol_top.isChecked())
                    pro_data.lol_position.put("top", true);
                else
                    pro_data.lol_position.put("top", false);
                break;
            case R.id.check_jg:
                if (lol_jungle.isChecked())
                    pro_data.lol_position.put("jungle", true);
                else
                    pro_data.lol_position.put("jungle", false);
                break;
            case R.id.check_mid:
                if (lol_mid.isChecked())
                    pro_data.lol_position.put("mid", true);
                else
                    pro_data.lol_position.put("mid", false);
                break;
            case R.id.check_ad:
                if (lol_bottom.isChecked())
                    pro_data.lol_position.put("bottom", true);
                else
                    pro_data.lol_position.put("bottom", false);
                break;
            case R.id.check_sp:
                if (lol_support.isChecked())
                    pro_data.lol_position.put("support", true);
                else
                    pro_data.lol_position.put("support", false);
                break;

            case R.id.check_sa:
                if (battle_sa.isChecked())
                    pro_data.battle_map.put("사녹", true);
                else
                    pro_data.battle_map.put("사녹", false);
                break;
            case R.id.check_eran:
                if (battle_eran.isChecked())
                    pro_data.battle_map.put("에란겔", true);
                else
                    pro_data.battle_map.put("에란겔", false);
                break;

            case R.id.check_mi:
                if (battle_mi.isChecked())
                    pro_data.battle_map.put("미라마", true);
                else
                    pro_data.battle_map.put("미라마", false);
                break;

            case R.id.check_ca:
                if (battle_ka.isChecked())
                    pro_data.battle_map.put("카라틴", true);
                else
                    pro_data.battle_map.put("카라틴", false);
                break;

            case R.id.check_bi:
                if (battle_bi.isChecked())
                    pro_data.battle_map.put("비켄디", true);
                else
                    pro_data.battle_map.put("비켄디", false);
                break;

            case R.id.check_solo:
                if (battle_1.isChecked())
                    pro_data.battle_squad.put("솔로", true);
                else
                    pro_data.battle_squad.put("솔로", false);
                break;

            case R.id.check_duo:
                if (battle_2.isChecked())
                    pro_data.battle_squad.put("듀오", true);
                else
                    pro_data.battle_squad.put("듀오", false);
                break;

            case R.id.check_solos:
                if (battle_3.isChecked())
                    pro_data.battle_squad.put("솔쿼드", true);
                else
                    pro_data.battle_squad.put("솔쿼드", false);
                break;

            case R.id.check_dus:
                if (battle_4.isChecked())
                    pro_data.battle_squad.put("듀쿼드", true);
                else
                    pro_data.battle_squad.put("듀쿼드", false);
                break;

            case R.id.check_three:
                if (battle_5.isChecked())
                    pro_data.battle_squad.put("3인 스쿼드", true);
                else
                    pro_data.battle_squad.put("3인 스쿼드", false);
                break;

            case R.id.check_four:
                if (battle_6.isChecked())
                    pro_data.battle_squad.put("4인 스쿼드", true);
                else
                    pro_data.battle_squad.put("4인 스쿼드", false);
                break;

            case R.id.check_tanker:
                if (over_tanker.isChecked())
                    pro_data.over_position.put("탱커", true);
                else
                    pro_data.over_position.put("탱커", false);
                break;

            case R.id.check_healer:
                if (over_healer.isChecked())
                    pro_data.over_position.put("힐러", true);
                else
                    pro_data.over_position.put("힐러", false);
                break;

            case R.id.check_dps:
                if (over_dps.isChecked())
                    pro_data.over_position.put("딜러", true);
                else
                    pro_data.over_position.put("딜러", false);
                break;


            case R.id.check_gon:
                if (fifa4_gon.isChecked())
                    pro_data.fifa4_game.put("공식경기", true);
                else
                    pro_data.fifa4_game.put("공식경기", false);
                break;

            case R.id.check_chi:
                if (fifa4_chi.isChecked())
                    pro_data.fifa4_game.put("친선경기", true);
                else
                    pro_data.fifa4_game.put("친선경기", false);
                break;

            case R.id.check_onebyone:
                if (fifa4_onebyone.isChecked())
                    pro_data.fifa4_game.put("1 vs 1", true);
                else
                    pro_data.fifa4_game.put("1 vs 1", false);
                break;

            case R.id.check_twobytwo:
                if (fifa4_twobytwo.isChecked())
                    pro_data.fifa4_game.put("2 vs 2", true);
                else
                    pro_data.fifa4_game.put("2 vs 2", false);
                break;

            case R.id.check_bol:
                if (fifa4_bol.isChecked())
                    pro_data.fifa4_game.put("볼타", true);
                else
                    pro_data.fifa4_game.put("볼타", false);
                break;



        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:{ //toolbar의 back키 눌렀을 때 동작
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}

class UserData0 {
    public String Age;
    public String Discord;
    public String Gender;
    public String Picture;
}