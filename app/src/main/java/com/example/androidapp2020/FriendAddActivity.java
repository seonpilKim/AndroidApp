package com.example.androidapp2020;


import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FriendAddActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {

    DatabaseReference mPostReference = FirebaseDatabase.getInstance().getReference();

    Button friend_add_button;
    EditText friend_edit_view;
    ListView friend_names;
    ListView user_names;

    String friend_name;

    LinearLayout layer_friend_view;

    String ID="testUser";
    //String ID = SharedProperty.getUserID();

    FriendList friend_data = new FriendList();

    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> userAdapter;

    static ArrayList<String> userList =  new ArrayList<String>();
    static ArrayList<String> friendList =  new ArrayList<String>();
//    static ArrayList<String> arrayData = new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend);

        ActionBar ab = getSupportActionBar();
        ab.setTitle("친구 추가");
        ab.setDisplayHomeAsUpEnabled(true);
        // ab.setDisplayShowHomeEnabled(true);

        SharedPreferences pref = getSharedPreferences("pref", MODE_PRIVATE);
        ID = pref.getString("loginID", "testUser");

        friend_add_button = (Button) findViewById(R.id.friend_add);
        friend_add_button.setOnClickListener(this);
        friend_edit_view = (EditText) findViewById(R.id.friend_name);

        //       arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        arrayAdapter = new FriendListAdapter(friendList, this);
        friend_names = (ListView) findViewById(R.id.friend_name_list);
        friend_names.setAdapter(arrayAdapter);
        friend_names.setOnItemLongClickListener(longClickListener);

        user_names = (ListView) findViewById(R.id.user_name_list);
        user_names.setOnItemClickListener(userOnClickListener);

        friend_edit_view.addTextChangedListener(this);

        layer_friend_view = (LinearLayout) findViewById(R.id.layer_friend_view);

        getFriendListFromServer(ID);

        friend_add_button.setEnabled(true);
    }

    private AdapterView.OnItemLongClickListener longClickListener = new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d("Long Click", "position = " + position);
            final String user_name = friendList.get(position).toString();

            AlertDialog.Builder dialog = new AlertDialog.Builder(FriendAddActivity.this);
            dialog.setTitle("친구 삭제")
                    .setMessage("친구에서 삭제 하시겠습니까?" + "\n" + user_name)
                    .setPositiveButton("네", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            friend_data.friend_names.remove(user_name);
                            putFriendListToServer(ID);
                            getFriendListFromServer(ID);
                            Toast.makeText(FriendAddActivity.this, "친구를 삭제했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("아니오", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(FriendAddActivity.this, "친구삭제를 취소했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .create()
                    .show();
            return false;
        }
    };

    private AdapterView.OnItemClickListener userOnClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String user_name = userList.get(position).toString();
            friend_edit_view.setText(user_name);
        }
    };

    public boolean IsExistID(String name){
        return userList.contains(name);
    }

    public boolean IsExistFriend(String name){
        return friendList.contains(name);
    }

    public void putFriendListToServer(String ID){
        //mPostReference = FirebaseDatabase.getInstance().getReference();
        Map<String, Object> childUpdates = new HashMap<>();
        Map<String, Object> postValues = null;

        postValues = friend_data.toMap();

        childUpdates.put("/Friend_list/" + ID, postValues);
        mPostReference.updateChildren(childUpdates);
    }

    public void getRegisteredUserList(){
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.e("getFirebaseDatabase", "key: " + dataSnapshot.getChildrenCount());
                userList.clear();
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {
                    String key = postSnapshot.getKey();
                    if (!IsExistFriend(key) && !ID.equals(key)) { //TODO userAdapter bug
                        Log.d("getFirebaseDatabase", "Add key: " + key);
                        userList.add(key);
                    }
                    //Log.d("getFirebaseDatabase", "key: " + key);
                }
                userAdapter = new ArrayAdapter<String>(FriendAddActivity.this, android.R.layout.simple_list_item_1);
                userAdapter.addAll(userList);
                user_names.setAdapter(userAdapter);
                userAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnItems(user_names);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
            }
        };
        Query sortbyAge = mPostReference.child("User_list").orderByKey();
        sortbyAge.addListenerForSingleValueEvent(postListener);
    }

    public void getFriendListFromServer(String id){
        ValueEventListener postListener2 = new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                FriendList get = dataSnapshot.getValue(FriendList.class);

                if (get == null) {
                    friendList.clear();
                    arrayAdapter.notifyDataSetChanged();
                    setListViewHeightBasedOnItems(friend_names);
                    getRegisteredUserList();
                    return;
                }
                friend_data = get;

                friendList.clear();
                for (String friend : friend_data.friend_names) {
                    friendList.add(friend);
                }

                arrayAdapter.notifyDataSetChanged();
                setListViewHeightBasedOnItems(friend_names);
                getRegisteredUserList();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.w("getFirebaseDatabase","loadPost:onCancelled", databaseError.toException());
            }
        };
        Query data = mPostReference.child("Friend_list").child(ID).orderByKey();
        data.addListenerForSingleValueEvent(postListener2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.friend_add:
                friend_name = friend_edit_view.getText().toString().trim();

                if(!IsExistID(friend_name)){
                    Toast.makeText(FriendAddActivity.this, "존재하지 않는 ID 입니다. 다른 ID로 설정해주세요.", Toast.LENGTH_LONG).show();
                }else{
                    if(IsExistFriend(friend_name)) {
                        Toast.makeText(FriendAddActivity.this, "이미 친구로 등록된 ID 입니다.", Toast.LENGTH_LONG).show();
                    } else if (friend_name.equals(ID)) {
                        Toast.makeText(FriendAddActivity.this, "당신의 ID 입니다.", Toast.LENGTH_LONG).show();
                    } else {
                        friend_data.friend_names.add(0, friend_name);
                        friend_edit_view.getText().clear();
                        putFriendListToServer(ID);
                        getFriendListFromServer(ID);
                    }
                }
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if(friend_edit_view.getText().length() == 0) {
            user_names.clearTextFilter();
            user_names.setTextFilterEnabled(false);
            layer_friend_view.setVisibility(View.VISIBLE);
        } else if (friend_edit_view.getText().length() >= 1) {
            user_names.setTextFilterEnabled(true);
            user_names.setFilterText(friend_edit_view.getText().toString());
            layer_friend_view.setVisibility(View.GONE);
        }
    }

    public boolean setListViewHeightBasedOnItems(ListView listView) {

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter != null) {

            int numberOfItems = listAdapter.getCount();

            // Get total height of all items.
            int totalItemsHeight = 0;
            for (int itemPos = 0; itemPos < numberOfItems; itemPos++) {
                View item = listAdapter.getView(itemPos, null, listView);
                float px = 500 * (listView.getResources().getDisplayMetrics().density);
                item.measure(View.MeasureSpec.makeMeasureSpec((int) px, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
                totalItemsHeight += item.getMeasuredHeight();
            }

            // Get total height of all item dividers.
            int totalDividersHeight = listView.getDividerHeight() *
                    (numberOfItems - 1);
            // Get paddingdfd
            int totalPadding = listView.getPaddingTop() + listView.getPaddingBottom();

            // Set list height.
            ViewGroup.LayoutParams params = listView.getLayoutParams();
            params.height = totalItemsHeight + totalDividersHeight + totalPadding;
            listView.setLayoutParams(params);
            listView.requestLayout();
            //setDynamicHeight(listView);
            return true;

        } else {
            return false;
        }
    }
}