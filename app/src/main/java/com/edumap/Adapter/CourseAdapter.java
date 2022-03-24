package com.edumap.Adapter;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edumap.Activity.Common;
import com.edumap.Interface.ItemClickListener;
import com.edumap.Model.Course;
import com.edumap.R;

import java.util.ArrayList;

public class CourseAdapter  extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {
    View view;
    private Context context;
    private ArrayList<Course> courses;
    private int position;

    public CourseAdapter(Context context, ArrayList<Course> courses) {
        this.context = context;
        this.courses = courses;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @NonNull
    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.coursecardview,parent, false);
        return new CourseAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CourseAdapter.ViewHolder holder, int position) {
        holder.courseName.setText(courses.get(position).getName());
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView courseName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            courseName = itemView.findViewById(R.id.courseName);
            if (Common.checkAdmin(itemView.getContext())){
                itemView.setOnCreateContextMenuListener(this);
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            contextMenu.setHeaderTitle("Select the action");
            contextMenu.add(3,0,getAdapterPosition(), Common.Delete);
        }
    }
}
