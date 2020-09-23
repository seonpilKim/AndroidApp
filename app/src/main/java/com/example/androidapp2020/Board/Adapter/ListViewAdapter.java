package com.example.androidapp2020.Board.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import com.example.androidapp2020.Board.ListVO.ListVO;
import com.example.androidapp2020.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter implements Filterable {
    private ArrayList<ListVO> listVO = new ArrayList<ListVO>();
    private Filter listFilter;
    private ArrayList<ListVO> filteredItemList = listVO;

    public ListViewAdapter(ArrayList<ListVO> listVO){
        this.listVO = listVO;
    }

    @Override
    public int getCount() {
        return filteredItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return filteredItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final int pos = position;
        final Context context = parent.getContext();
        final ListVO listViewItem = filteredItemList.get(position);

        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.custom_listview, parent, false);
        }

        TextView id = (TextView) convertView.findViewById(R.id.tv_lol_board_item_Id);
        TextView t = (TextView) convertView.findViewById(R.id.tv_lol_board_item_T);
        TextView title = (TextView) convertView.findViewById(R.id.tv_lol_board_item_Title);
        TextView views = (TextView) convertView.findViewById(R.id.tv_lol_board_item_Views);
        TextView comments = (TextView) convertView.findViewById(R.id.tv_lol_board_item_Comments);
        TextView recommendations = (TextView) convertView.findViewById(R.id.tv_lol_board_item_Recommendations);
        TextView recommendations2 = (TextView) convertView.findViewById(R.id.tv_lol_board_item_Recommendations2);

        comments.setText(""+listViewItem.getComments());
        if(listViewItem.getRecommendations() != -1) {
            recommendations.setText("" + listViewItem.getRecommendations());
            recommendations2.setVisibility(View.VISIBLE);
            recommendations.setVisibility(View.VISIBLE);
        }
        views.setText(""+listViewItem.getViews());
        title.setText(listViewItem.getTitle());
        id.setText(listViewItem.getId());
        t.setText(listViewItem.getT());

        return convertView;
    }

    public void addVO(String Title, String Content, String Key, String id, String time, String t
    , int views, int comments, int recommendations, int num){
        ListVO item = new ListVO();

        item.setNum(num);
        item.setComments(comments);
        item.setRecommendations(recommendations);
        item.setViews(views);
        item.setT(t);
        item.setTime(time);
        item.setId(id);
        item.setKey(Key);
        item.setTitle(Title);
        item.setContent(Content);

        listVO.add(0,item);
    }

    @Override
    public Filter getFilter() {
        if(listFilter == null){
            listFilter = new ListFilter();
        }
        return listFilter;
    }

    private class ListFilter extends Filter {

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults() ;

            if (constraint == null || constraint.length() == 0) {
                results.values = listVO ;
                results.count = listVO.size() ;
            } else {
                ArrayList<ListVO> itemList = new ArrayList<ListVO>() ;

                for (ListVO item : listVO) {
                    if (item.getTitle().toUpperCase().contains(constraint.toString().toUpperCase()))
                    {
                        itemList.add(item) ;
                    }
                }

                results.values = itemList ;
                results.count = itemList.size() ;
            }
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            // update listview by filtered data list.
            filteredItemList = (ArrayList<ListVO>) results.values ;

            // notify
            if (results.count > 0) {
                notifyDataSetChanged() ;
            } else {
                notifyDataSetInvalidated() ;
            }
        }
    }
}
