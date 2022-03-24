package com.edumap.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.edumap.Model.Course;
import com.edumap.Model.Stream;

import java.util.ArrayList;

public class StreamSpinAdapter extends ArrayAdapter<Stream> {

    private Context context;
    private ArrayList<Stream> streams;

    public StreamSpinAdapter(Context context, int textViewResourceId,
                       ArrayList<Stream> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.streams = values;
    }

    @Override
    public int getCount(){
        return streams.size();
    }

    @Override
    public Stream getItem(int position){
        return streams.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(streams.get(position).getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(streams.get(position).getName());
        return label;
    }
}
