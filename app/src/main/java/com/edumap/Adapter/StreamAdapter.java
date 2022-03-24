package com.edumap.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.edumap.Interface.StreamListener;
import com.edumap.Model.Stream;
import com.edumap.R;

import java.util.ArrayList;

public class StreamAdapter extends RecyclerView.Adapter<StreamAdapter.ViewHolder> {

    View view;
    Context context;
    ArrayList<Stream> streamLists;
    StreamListener streamListener;
    ArrayList<String> streamListsPass = new ArrayList<String>();

    public StreamAdapter(Context context, ArrayList<Stream> streamLists, StreamListener streamListener) {
        this.context = context;
        this.streamLists = streamLists;
        this.streamListener = streamListener;
    }

    public StreamAdapter(Context context, ArrayList<Stream> streamLists, StreamListener streamListener, ArrayList<String> streamListsPass) {
        this.context = context;
        this.streamLists = streamLists;
        this.streamListener = streamListener;
        this.streamListsPass = streamListsPass;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view = LayoutInflater.from(context).inflate(R.layout.streamrecyclerlayout,parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StreamAdapter.ViewHolder holder, int position) {
        if (streamLists !=  null && streamLists.size() > 0){
            holder.streamCheckBox.setText(streamLists.get(position).getName());

            if (streamListsPass != null){
                if (streamListsPass.contains(streamLists.get(position).getId())) {
                    holder.streamCheckBox.setChecked(true);
                }
                else{
                    holder.streamCheckBox.setChecked(false);
                }
            }

            holder.streamCheckBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.streamCheckBox.isChecked()) {
                        streamListsPass.add(streamLists.get(position).getId());
                    } else {
                        streamListsPass.remove(streamLists.get(position).getId());
                    }
                    streamListener.streamChanger(streamListsPass);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return streamLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox streamCheckBox;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            streamCheckBox = itemView.findViewById(R.id.streamCheckbox);
        }
    }

}
