package com.example.androidapp2020.Chat;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.androidapp2020.R;

import java.util.ArrayList;
import java.util.List;

public class chattingAdapter extends BaseAdapter {
    List<chatMsg> msgList = new ArrayList<chatMsg>();
    Context context;

    public chattingAdapter(Context context) {
        this.context = context;
    }

    public chattingAdapter() {}

    public void add(chatMsg msg) {
        this.msgList.add(msg);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return msgList.size();
    }

    @Override
    public Object getItem(int i) {
        return msgList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        MessageViewHolder holder = new MessageViewHolder();
        LayoutInflater msgInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        chatMsg msg = msgList.get(i);

        if(msg.getIsMine()) {   // my msg
            view = msgInflater.inflate(R.layout.my_msg, null);
            holder.msgBody = (TextView) view.findViewById(R.id.message_body);
            view.setTag(holder);
            holder.msgBody.setText(msg.getText());
        }
        else {  // other msg
            view = msgInflater.inflate(R.layout.other_msg, null);
            holder.avatar = (View) view.findViewById(R.id.avatar);
            holder.name = (TextView) view.findViewById(R.id.name);
            holder.msgBody = (TextView) view.findViewById(R.id.message_body);
            view.setTag(holder);
            holder.name.setText(msg.getUser());
            holder.msgBody.setText(msg.getText());
        }
        return view;
    }
}

class MessageViewHolder {
    public View avatar;
    public TextView name;
    public TextView msgBody;
}
