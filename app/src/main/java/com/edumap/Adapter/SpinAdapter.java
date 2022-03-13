package com.edumap.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.edumap.Model.Course;

import java.util.ArrayList;

public class SpinAdapter extends ArrayAdapter<Course> {

    private Context context;
    private ArrayList<Course> courses;

    public SpinAdapter(Context context, int textViewResourceId,
                       ArrayList<Course> values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.courses = values;
    }

    @Override
    public int getCount(){
        return courses.size();
    }

    @Override
    public Course getItem(int position){
        return courses.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView label = (TextView) super.getView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(courses.get(position).getName());
        return label;
    }

    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setTextColor(Color.BLACK);
        label.setText(courses.get(position).getName());
        return label;
    }
}
