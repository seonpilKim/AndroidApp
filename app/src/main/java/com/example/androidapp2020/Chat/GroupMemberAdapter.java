package com.example.androidapp2020.Chat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.androidapp2020.ProfileActivity;
import com.example.androidapp2020.R;

import java.util.ArrayList;
import java.util.List;

public class GroupMemberAdapter extends BaseAdapter {
    private List<FriendClass> members = new ArrayList<FriendClass>();
    Context context;

    public GroupMemberAdapter() {}

    public GroupMemberAdapter(Context context) {
        this.context = context;
    }

    public void add(FriendClass name) {
        members.add(name);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return members.size();
    }

    @Override
    public Object getItem(int i) {
        return members.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder = new ViewHolder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final FriendClass member = members.get(i);

        view = inflater.inflate(R.layout.group_item, null);
        holder.name_view = (TextView) view.findViewById(R.id.member_name_view);
        holder.profile_btn = (Button) view.findViewById(R.id.sw_profile_btn);
        view.setTag(holder);
        holder.name_view.setText(member.getName());

        holder.profile_btn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ProfileActivity.class);
                intent.putExtra("friendID", member.getName());
                context.startActivity(intent);
            }
        });

        return view;
    }
}

class ViewHolder {
    public TextView name_view;
    public Button profile_btn;
}