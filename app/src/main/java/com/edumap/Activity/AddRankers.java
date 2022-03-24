package com.edumap.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.edumap.Adapter.SpinAdapter;
import com.edumap.Adapter.StreamAdapter;
import com.edumap.Adapter.StreamSpinAdapter;
import com.edumap.Interface.OnGetDataListener;
import com.edumap.Model.Academic;
import com.edumap.Model.Course;
import com.edumap.Model.Event;
import com.edumap.Model.Stream;
import com.edumap.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddRankers extends AppCompatActivity {

    private FirebaseDatabase mainfdb;
    private DatabaseReference streamData,rankers;
    private ArrayList<Stream> streamArrayList = new ArrayList<>();
    private ArrayList<String> streamIds = new ArrayList<>();
    private String streamID, collegeID;
    private EditText rankersName;
    private Spinner streamSpinner;
    private StreamSpinAdapter adapter;
    private String updateKey;
    private Academic updateAcademic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_rankers);


        streamSpinner = findViewById(R.id.streamSpinner);
        rankersName = findViewById(R.id.addRankers);
        mainfdb = FirebaseDatabase.getInstance();
        streamData = mainfdb.getReference("Stream");
        rankers = mainfdb.getReference("Academic");
        adapter = new StreamSpinAdapter(this,
                android.R.layout.simple_spinner_item,
                streamArrayList);
        streamSpinner.setAdapter(adapter);

        Common.readData(streamData, new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                    Stream i = dataSnapshot1.getValue(Stream.class);
                    streamIds.add(i.getId());
                    streamArrayList.add(i);
                }
                if (getIntent() != null) {
                    updateKey = getIntent().getStringExtra("academicID");
                    collegeID = getIntent().getStringExtra("collegeID");
                    if (updateKey != null) {
                        rankers.child(updateKey).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    updateAcademic = dataSnapshot.getValue(Academic.class);
                                    assert updateAcademic != null;
                                    rankersName.setText(updateAcademic.getRanker());
                                }
                                adapter.notifyDataSetChanged();
                                if(updateKey != null && updateAcademic.getStreamID() != null){
                                    int loc = streamIds.indexOf(updateAcademic.getStreamID());
                                    if (loc >= 0){
                                        streamSpinner.setSelection(loc+1);
                                        streamID = updateAcademic.getStreamID();
                                    }
                                }

                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
                else {
                    adapter.notifyDataSetChanged();
                }


            }

            @Override
            public void onFailure() {

            }
        });

        streamSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0) {
                    streamID = adapter.getItem(i).getId();
                }
                else {
                    streamID = "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    public void add(View view) {

        String name = rankersName.getText().toString();

        if (!name.isEmpty() && !streamID.isEmpty()){

            if (updateKey == null) {
                updateKey = rankers.push().getKey();
                assert updateKey != null;
                Academic academic = new Academic(name,updateKey,collegeID,streamID);
                rankers.child(updateKey).setValue(academic);
            }
            else {
                Academic academic = new Academic(name,updateKey,collegeID,streamID);
                rankers.child(updateKey).setValue(academic);
            }
            finish();
        } else {
            Snackbar snackbar = Snackbar
                    .make(rankersName, "Enter the complete details!", Snackbar.LENGTH_LONG)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.BLACK);
            snackbar.show();
        }
    }



}