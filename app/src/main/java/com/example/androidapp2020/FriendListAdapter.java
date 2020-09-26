package com.example.androidapp2020;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FriendListAdapter extends ArrayAdapter {
    ArrayList<String> listViewNames;
    Context mCtx;


    public FriendListAdapter(ArrayList<String> friendNames, Context context) {
        super(context, R.layout.listviewrow, friendNames);
        this.listViewNames=friendNames;
        this.mCtx=context;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            LayoutInflater inflater=LayoutInflater.from(mCtx);
            convertView=inflater.inflate(R.layout.listviewrow, null);
            viewHolder=new ViewHolder();
            viewHolder.textName=(TextView) convertView.findViewById(R.id.childTextView);
            viewHolder.button=(Button) convertView.findViewById(R.id.childButton);
            viewHolder.button2=(Button) convertView.findViewById(R.id.childButton2);
            convertView.setTag(viewHolder);
        } else {
            viewHolder=(ViewHolder) convertView.getTag();
        }

        viewHolder.textName.setText(listViewNames.get(position));

        //프로필 확인
        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), viewHolder.textName.getText().toString(), Toast.LENGTH_LONG).show();
                Intent intent=new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("friendID", viewHolder.textName.getText().toString());
                mCtx.startActivity(intent);
            }
        });
        //채팅창 활성화
        viewHolder.button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), viewHolder.textName.getText().toString(), Toast.LENGTH_LONG).show();

            }});


        return convertView;
    }

    public class ViewHolder {
        TextView textName;
        Button button;
        Button button2;
    }
}