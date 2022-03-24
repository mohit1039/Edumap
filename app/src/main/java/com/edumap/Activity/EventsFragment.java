package com.edumap.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.edumap.Model.College;
import com.edumap.Model.Event;
import com.edumap.R;
import com.edumap.ViewHolder.CollegeViewHolder;
import com.edumap.ViewHolder.EventViewHolder;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;


public class EventsFragment extends Fragment {


    View v;
    private String collegeID;
    private FirebaseDatabase mainfdb;
    DatabaseReference eventRef;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private FirebaseRecyclerOptions<Event> fac;
    private FirebaseRecyclerAdapter<Event, EventViewHolder> adapter;
    private FloatingActionButton addeventButton;

    public EventsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.v = view;
        if (getArguments() != null) {
            collegeID = getArguments().getString("collegeID");
        }
        mainfdb = FirebaseDatabase.getInstance();
        eventRef = mainfdb.getReference("Event");

        addeventButton = v.findViewById(R.id.addeventButton);
        recyclerView = v.findViewById(R.id.eventRecycler);

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        loaddetails();
        if (!Common.checkAdmin(v.getContext())) {
            addeventButton.setVisibility(View.GONE);
        }
        addeventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent event =new Intent(v.getContext(),AddEventActivity.class);
                event.putExtra("collegeID",collegeID);
                startActivity(event);
            }
        });
    }

    private void loaddetails() {
        fac = new FirebaseRecyclerOptions.Builder<Event>().setQuery(
                eventRef.orderByChild("collegeID").equalTo(collegeID)
                , Event.class).build();

        adapter = new FirebaseRecyclerAdapter<Event, EventViewHolder>(fac) {
            @Override
            protected void onBindViewHolder(@NonNull EventViewHolder eventViewHolder, int i, @NonNull Event event) {
                eventViewHolder.eventName.setText(event.getName());
            }

            @NonNull
            @Override
            public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.eventcardview,parent,false);
                return new EventViewHolder(view);
            }
        };

        adapter.startListening();
        recyclerView.setAdapter(adapter);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_events, container, false);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId() == 77) {
            if(item.getTitle().equals(Common.Update)){
                Intent event =new Intent(v.getContext(),AddEventActivity.class);
                event.putExtra("collegeID",collegeID);
                event.putExtra("eventID",adapter.getRef(item.getOrder()).getKey());
                startActivity(event);
            }
            else if (item.getTitle().equals(Common.Delete)){
                eventRef.child(Objects.requireNonNull(adapter.getRef(item.getOrder()).getKey())).removeValue();
            }
        }

        return super.onContextItemSelected(item);
    }
}