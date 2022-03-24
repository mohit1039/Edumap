package com.edumap.Adapter;

import android.content.Context;
import android.os.Build;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.edumap.Activity.Common;
import com.edumap.Model.Academic;
import com.edumap.Model.Course;
import com.edumap.Model.Stream;
import com.edumap.R;

import java.util.ArrayList;

public class AcademicAdapter extends RecyclerView.Adapter<AcademicAdapter.ViewHolder> {

    View view;
    private Context context;
    private ArrayList<Stream> streams;
    private ArrayList<Academic> academics;
    private int position;

    public AcademicAdapter(Context context, ArrayList<Stream> streams, ArrayList<Academic> academics) {
        this.context = context;
        this.streams = streams;
        this.academics = academics;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @NonNull
    @Override
    public AcademicAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.academiccardview,parent, false);
        return new AcademicAdapter.ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull AcademicAdapter.ViewHolder holder, int position) {
        holder.academicName.setText(academics.get(position).getRanker());
       for (Stream stream : streams) {
           if (stream.getId().equals(academics.get(position).getStreamID())){
               holder.streamName.setText(stream.getName());
           }
       }
       holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
           @Override
           public boolean onLongClick(View view) {
               setPosition(holder.getAdapterPosition());
               return false;
           }
       });
    }

    @Override
    public int getItemCount() {
        return academics.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

        TextView academicName, streamName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            academicName = itemView.findViewById(R.id.academicName);
            streamName = itemView.findViewById(R.id.streamName);
            if (Common.checkAdmin(itemView.getContext())){
                itemView.setOnCreateContextMenuListener(this);
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            contextMenu.setHeaderTitle("Select the action");
            contextMenu.add(1,0,getAdapterPosition(), Common.Update);
            contextMenu.add(1,0,getAdapterPosition(), Common.Delete);
        }
    }

}
