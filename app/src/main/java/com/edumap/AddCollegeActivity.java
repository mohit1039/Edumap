package com.edumap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.edumap.Activity.ShowCollege;
import com.edumap.Adapter.SpinAdapter;
import com.edumap.Adapter.StreamAdapter;
import com.edumap.Interface.StreamListener;
import com.edumap.Model.College;
import com.edumap.Model.Course;
import com.edumap.Model.Stream;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AddCollegeActivity extends AppCompatActivity implements StreamListener {
    ArrayList<Stream> stream = new ArrayList<>();
    ArrayList<String> streamIds = new ArrayList<>();
    private EditText collegeName,longEditText,latEditext;
    RecyclerView checkboxRecyclerView;
    StreamAdapter streamAdapter;
    FirebaseDatabase mainfdb;
    DatabaseReference streamData;
    private DatabaseReference ref;
    private String update;
    private College updateCollege;
    String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_college);

        collegeName = findViewById(R.id.addCollegename);
        longEditText = findViewById(R.id.addLongitude);
        latEditext = findViewById(R.id.addLatitude);

        FirebaseApp.initializeApp(this);
        mainfdb = FirebaseDatabase.getInstance();
        streamData = mainfdb.getReference("Stream");
        ref = mainfdb.getReference("College");
        checkboxRecyclerView = findViewById(R.id.checkboxRecycler);
        checkboxRecyclerView();
        initem();

        if (getIntent() != null) {
            update = getIntent().getStringExtra("collegeID");
            if (update != null) {

                ref.child(update).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        updateCollege = dataSnapshot.getValue(College.class);
                        if (updateCollege != null) {
                            collegeName.setText(updateCollege.getFullname());
                            longEditText.setText(String.valueOf(updateCollege.getLongitude()));
                            latEditext.setText(String.valueOf(updateCollege.getLatitude()));
                            streamAdapter = new StreamAdapter(AddCollegeActivity.this,stream,AddCollegeActivity.this,updateCollege.getStreams());
                            checkboxRecyclerView.setAdapter(streamAdapter);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        }
    }
    private void checkboxRecyclerView() {
        checkboxRecyclerView.setHasFixedSize(true);
        checkboxRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        streamAdapter = new StreamAdapter(this,stream,this);
    }
    private void initem() {
        streamData.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Stream i = dataSnapshot1.getValue(Stream.class);
                        stream.add(i);
                    }
                }
                checkboxRecyclerView.setAdapter(streamAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void add(View view) {
        savedata();
    }

    public void savedata(){

        String name = collegeName.getText().toString();


        if (!name.isEmpty() && !longEditText.getText().toString().isEmpty() && !latEditext.getText().toString().isEmpty()){
            float longitude = Float.valueOf(longEditText.getText().toString());
            float latitude = Float.valueOf(latEditext.getText().toString());
            if (update == null) {
                key = ref.push().getKey();
                College college = new College(name,key,longitude,latitude,streamIds);
                ref.child(key).setValue(college);
            }
            else {
                College college = new College(name,updateCollege.getEventId(),updateCollege.getHistoryId(),
                        updateCollege.getAcademyId(),key,longitude,latitude,streamIds);
                key = update;
                ref.child(key).setValue(college);
            }
            startActivity(new Intent(this, ShowCollege.class));
            finish();

        } else {
            Snackbar snackbar = Snackbar
                    .make(collegeName, "Enter the complete details!", Snackbar.LENGTH_LONG)
                    .setAction("Ok", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sbView = snackbar.getView();
            TextView textView = sbView.findViewById(com.google.android.material.R.id.snackbar_text);
            textView.setTextColor(Color.BLACK);
            snackbar.show();
        }
    }

    @Override
    public void streamChanger(ArrayList<String> streamList) {
        Toast.makeText(this,String.valueOf(streamList),Toast.LENGTH_SHORT).show();
        streamIds = streamList;
    }
}