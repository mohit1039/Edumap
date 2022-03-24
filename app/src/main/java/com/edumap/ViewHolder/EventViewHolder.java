package com.edumap.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edumap.Activity.Common;
import com.edumap.R;

public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    public TextView eventName;
    public EventViewHolder(@NonNull View itemView) {
        super(itemView);
        eventName = itemView.findViewById(R.id.eventName);
        if (Common.checkAdmin(itemView.getContext())){
            itemView.setOnCreateContextMenuListener(this);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(77,0,getAdapterPosition(), Common.Update);
        contextMenu.add(77,0,getAdapterPosition(), Common.Delete);
    }
}
