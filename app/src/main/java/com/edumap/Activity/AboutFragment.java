package com.edumap.Activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.edumap.Adapter.AcademicAdapter;
import com.edumap.Adapter.CourseAdapter;
import com.edumap.Interface.OnGetDataListener;
import com.edumap.Model.Academic;
import com.edumap.Model.College;
import com.edumap.Model.Course;
import com.edumap.Model.Event;
import com.edumap.Model.History;
import com.edumap.Model.Stream;
import com.edumap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseError;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ktx.Firebase;

import java.util.ArrayList;


public class AboutFragment extends Fragment {
    View v;
    private String collegeID;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    private FirebaseDatabase mainfdb;
    private DatabaseReference historyRef, academicRef,collegeRef,streamRef;
    private College college = new College();
    private ImageButton historyEditAdd,historyCollapse,academicCollapse,academicAdd;
    private TextView yearTextView, historyTextView;
    private History history;
    private CardView historyCardView,academicsRecyclerCard;
    private boolean edit;
    private ArrayList<Academic> academics = new ArrayList<>();
    private ArrayList<Stream> streams = new ArrayList<>();
    private AcademicAdapter adapter;

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.v = view;

        if (getArguments() != null) {
            collegeID = getArguments().getString("collegeID");
        }
        academicsRecyclerCard = v.findViewById(R.id.academicsRecyclerCard);
        recyclerView = v.findViewById(R.id.academicsRecycler);
        yearTextView = v.findViewById(R.id.year);
        historyTextView = v.findViewById(R.id.aboutHistoryText);
        historyEditAdd = v.findViewById(R.id.addEditHistory);
        historyCardView = v.findViewById(R.id.historydatacard);
        historyCollapse = v.findViewById(R.id.collapse);
        academicAdd = v.findViewById(R.id.addAcademics);
        academicCollapse = v.findViewById(R.id.academicCollapse);
        historyCollapse.setEnabled(false);
        academicCollapse.setEnabled(false);
        historyEditAdd.setEnabled(false);
        mainfdb = FirebaseDatabase.getInstance();
        historyRef = mainfdb.getReference("History");
        academicRef = mainfdb.getReference("Academic");
        collegeRef = mainfdb.getReference("College");
        streamRef = mainfdb.getReference("Stream");

        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        checkboxRecyclerView();
        registerForContextMenu(recyclerView);
        academicCollapse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (academicsRecyclerCard.getVisibility() == View.GONE){
                    academicsRecyclerCard.setVisibility(View.VISIBLE);
                }
                else {
                    academicsRecyclerCard.setVisibility(View.GONE);
                }
            }
        });

        if (!Common.checkAdmin(v.getContext())) {
            historyEditAdd.setVisibility(View.GONE);
            academicAdd.setVisibility(View.GONE);
        }
        historyCollapse.setOnClickListener(view1 -> {
            if (historyCardView.getVisibility() == View.GONE){
                historyCardView.setVisibility(View.VISIBLE);
            }
            else {
                historyCardView.setVisibility(View.GONE);
            }
        });

        historyEditAdd.setOnClickListener(view12 -> {
            if (edit){
                Intent event =new Intent(v.getContext(),AddHistoryActivity.class);
                event.putExtra("collegeID",collegeID);
                event.putExtra("historyID",history.getHistoryID());
                startActivity(event);
            }else{
                Intent event =new Intent(v.getContext(),AddHistoryActivity.class);
                event.putExtra("collegeID",collegeID);
                startActivity(event);
            }
        });


        academicAdd.setOnClickListener(view13 -> {

                Intent event =new Intent(v.getContext(),AddRankers.class);
                event.putExtra("collegeID",collegeID);
                startActivity(event);
                academics.clear();
                streams.clear();
        });


        Common.readData(collegeRef.child(collegeID), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                college = dataSnapshot.getValue(College.class);
                assert college != null;
                if (college.getHistoryId() != null){
                    historyEditAdd.setImageResource(R.drawable.edit);
                    edit = true;
                    historyCollapse.setEnabled(true);
                    Common.readData(historyRef.child(college.getHistoryId()), new OnGetDataListener() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            history = dataSnapshot.getValue(History.class);
                            assert history != null;
                            yearTextView.setText(history.getYear());
                            historyTextView.setText(history.getAbout());
                        }
                        @Override
                        public void onFailure() {

                        }
                    });
                }
                historyEditAdd.setEnabled(true);
            }
            @Override
            public void onFailure() {
                Log.d("onFailure", "Failed");
            }
        });


        initAcademicdata();


    }

    private void initAcademicdata() {
        Query query = academicRef.orderByChild("collegeID").equalTo(collegeID);
        Common.readQueryData(query,new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnaps: dataSnapshot.getChildren()){
                    Academic academic = dataSnaps.getValue(Academic.class);
                    academics.add(academic);
                }
                if (academics.size() == 0){
                    academicCollapse.setEnabled(false);
                    recyclerView.setVisibility(View.GONE);
                }
                else {
                    academicCollapse.setEnabled(true);
                }
                Common.readData(streamRef, new OnGetDataListener() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        for (DataSnapshot dataSnaps: dataSnapshot.getChildren()){
                            Stream stream = dataSnaps.getValue(Stream.class);
                            streams.add(stream);
                        }
                        adapter.notifyDataSetChanged();
                    }
                    @Override
                    public void onFailure() {

                    }
                });
            }

            @Override
            public void onFailure() {

            }
        });
    }


    private void checkboxRecyclerView() {
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AcademicAdapter(getContext(),streams,academics);
        recyclerView.setAdapter(adapter);
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getGroupId() == 1) {
            int position = -1;
            position = adapter.getPosition();
            if (item.getTitle().equals(Common.Update)) {
                Intent event =new Intent(v.getContext(),AddRankers.class);
                event.putExtra("collegeID",collegeID);
                event.putExtra("academicID",academics.get(position).getAcademicID());
                academics.clear();
                streams.clear();
                startActivity(event);
            }
            else if (item.getTitle().equals(Common.Delete)){
                academicRef.child(academics.get(position).getAcademicID()).removeValue();
                academics.clear();
                adapter.notifyDataSetChanged();
            }
        }
        return super.onContextItemSelected(item);
    }
}