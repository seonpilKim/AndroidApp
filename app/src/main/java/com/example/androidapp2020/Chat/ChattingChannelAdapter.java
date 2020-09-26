package com.example.androidapp2020.Chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.androidapp2020.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class ChattingChannelAdapter extends BaseAdapter {
    List<ListCard> cardList = new ArrayList<ListCard>();
    Context context;

    public ChattingChannelAdapter(Context context) {
        this.context = context;
    }
    public  ChattingChannelAdapter() {}

    public void add(ListCard card) {
        this.cardList.add(card);
        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return cardList.size();
    }

    @Override
    public Object getItem(int i) {
        return cardList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        CardViewHolder holder = new CardViewHolder();
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ListCard card = cardList.get(i);

        long now = card.getTime();
        Date date = new Date(now);
        SimpleDateFormat time = new SimpleDateFormat("HH:mm");
        TimeZone zone = TimeZone.getTimeZone("Asia/Seoul");
        time.setTimeZone(zone);
        String strTime = time.format(date);

        view = inflater.inflate(R.layout.chat_channel, null);
        holder.game_icon = (View) view.findViewById(R.id.game_icon);
        holder.name_textView = (TextView) view.findViewById(R.id.name_textView);
        holder.recentMsg = (TextView) view.findViewById(R.id.recentMsg);
        holder.list_time = (TextView) view.findViewById(R.id.list_time);
        view.setTag(holder);
        holder.name_textView.setText(card.getUserName());
        holder.recentMsg.setText(card.getRecentMsg());
        holder.list_time.setText(strTime);

        return view;
    }
}

class CardViewHolder {
    public View game_icon;
    public TextView name_textView;
    public TextView recentMsg;
    public TextView list_time;
}