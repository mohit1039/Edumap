package com.edumap.ViewHolder;

import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edumap.Activity.Common;
import com.edumap.Interface.ItemClickListener;
import com.edumap.R;

import java.text.BreakIterator;

public class CollegeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

public ItemClickListener itemClickListener;
    public TextView collegeName;

    public CollegeViewHolder(@NonNull View itemView) {
        super(itemView);

        collegeName = itemView.findViewById(R.id.collegeName);
        itemView.setOnClickListener(this);
        if (Common.checkAdmin(itemView.getContext())){
            itemView.setOnCreateContextMenuListener(this);
        }

    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view, getAdapterPosition(), false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(), Common.Update);
        contextMenu.add(0,0,getAdapterPosition(), Common.Delete);
    }
}
