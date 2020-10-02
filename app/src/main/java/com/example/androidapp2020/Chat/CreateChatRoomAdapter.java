package com.example.androidapp2020.Chat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.androidapp2020.R;

import java.util.ArrayList;
import java.util.List;

public class CreateChatRoomAdapter extends BaseAdapter {
    List<String> friendList = new ArrayList<String>();
    Context context;

    public CreateChatRoomAdapter() {}

    public CreateChatRoomAdapter(Context context) {
        this.context = context;
    }

    public void add(String name) {
        friendList.add(name);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Holder holder = new Holder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        String name = friendList.get(i);

        view = inflater.inflate(R.layout.friend_list_item, null);
        holder.friend_name = (TextView) view.findViewById(R.id.friend_name_view);
        holder.sw_checkBox = (CheckBox) view.findViewById(R.id.sw_checkBox);
        view.setTag(holder);
        holder.friend_name.setText(name);

        return view;
    }
}

class Holder {
    public TextView friend_name;
    public CheckBox sw_checkBox;
}
