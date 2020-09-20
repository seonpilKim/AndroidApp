package com.example.androidapp2020.Board.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.androidapp2020.Board.ListVO.CommVO;
import com.example.androidapp2020.R;

import java.util.ArrayList;

public class CommViewAdapter extends BaseAdapter {
    private ArrayList<CommVO> commVO;

    public CommViewAdapter(ArrayList<CommVO> commVO) {this.commVO = commVO;}

    @Override
    public int getCount() {
        return commVO.size();
    }

    @Override
    public Object getItem(int position) {
        return commVO.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        TextView content;

        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            CommVO commViewItem = commVO.get(position);

            convertView = inflater.inflate(R.layout.custom_commview, parent, false);

            TextView time = (TextView) convertView.findViewById(R.id.tv_comm_time);
            content = (TextView) convertView.findViewById(R.id.tv_comm_content);
            TextView id = (TextView) convertView.findViewById(R.id.tv_comm_id);

            content.setText(commViewItem.getContent());
            time.setText(commViewItem.getTime());
            id.setText(commViewItem.getID());
        }
        return convertView;
    }

    public void addVO(String id, String content, String time){
        CommVO item = new CommVO();

        item.setID(id);
        item.setContent(content);
        item.setTime(time);

        commVO.add(item);
    }
}
