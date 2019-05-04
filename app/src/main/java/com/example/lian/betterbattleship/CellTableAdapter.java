package com.example.lian.betterbattleship;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CellTableAdapter extends BaseAdapter {

    private final int RED = 51;
    private final int GREEN = 153;
    private final int BLUE = 255;
    private Context context;
    private List<Score> list;

    public CellTableAdapter(Context context, List<Score> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return (list.size()+1);
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view;

        if (convertView == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_layout, parent, false);
        }

        else {
            view = convertView;
        }

        TextView tvName = (TextView) view.findViewById(R.id.textName);
        TextView tvScore = (TextView) view.findViewById(R.id.textScore);

        if(position == 0) {
            view.findViewById(R.id.linear).setBackgroundColor(Color.rgb(RED,GREEN,BLUE));
            tvName.setText(context.getResources().getString(R.string.name_table));
            tvScore.setText(context.getResources().getString(R.string.score_table));
        }

        else {
            tvName.setText(list.get(position-1).getName());
            tvScore.setText(list.get(position-1).toStringScore());
        }

        return view;
    }
}
