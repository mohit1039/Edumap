package com.edumap.Activity;

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

import com.edumap.Adapter.StreamAdapter;
import com.edumap.Interface.StreamListener;
import com.edumap.Model.College;
import com.edumap.Model.Stream;
import com.edumap.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class AddCollegeActivity extends AppCompatActivity implements StreamListener {
    ArrayList<Stream> stream = new ArrayList<>();
    ArrayList<String> streamIds = new ArrayList<>();
    private EditText collegeName,longEditText,latEditext,addressEditext,phoneEdittext;
    RecyclerView checkboxRecyclerView;
    StreamAdapter streamAdapter;
    FirebaseDatabase mainfdb;
    DatabaseReference streamData;
    private DatabaseReference ref;
    private String update;
    private College updateCollege;
    String key;
    private FloatingActionButton add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_college);

        collegeName = findViewById(R.id.addCollegename);
        longEditText = findViewById(R.id.addLongitude);
        latEditext = findViewById(R.id.addLatitude);
        addressEditext = findViewById(R.id.addCollegeAddress);
        phoneEdittext = findViewById(R.id.addCollegePhone);
        add = findViewById(R.id.addCollegeData);
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
                add.setEnabled(false);
                ref.child(update).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        updateCollege = dataSnapshot.getValue(College.class);
                        if (updateCollege != null) {
                            collegeName.setText(StringUtils.capitalize(updateCollege.getFullname()));
                            longEditText.setText(String.valueOf(updateCollege.getLongitude()));
                            latEditext.setText(String.valueOf(updateCollege.getLatitude()));
                            phoneEdittext.setText(String.valueOf(updateCollege.getPhone()));
                            addressEditext.setText(updateCollege.getAddress());
                            if (updateCollege.getStreams() != null){
                                streamAdapter = new StreamAdapter(AddCollegeActivity.this,stream,AddCollegeActivity.this,updateCollege.getStreams());
                            }
                            checkboxRecyclerView.setAdapter(streamAdapter);
                            add.setEnabled(true);
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

        String name = collegeName.getText().toString().trim();
        String address = addressEditext.getText().toString().trim();
        if (!name.isEmpty() && !address.isEmpty() && !phoneEdittext.getText().toString().isEmpty() && !longEditText.getText().toString().isEmpty() && !latEditext.getText().toString().isEmpty()){
            double longitude = Double.parseDouble(longEditText.getText().toString());
            double latitude = Double.parseDouble(latEditext.getText().toString());
            String phone = phoneEdittext.getText().toString();
            if (update == null) {
                key = ref.push().getKey();
                College college = new College(name.toLowerCase(),key,address,phone,longitude,latitude,streamIds);
                ref.child(key).setValue(college);
            }
            else {
                key = update;
                College college = new College(name.toLowerCase(),updateCollege.getHistoryId(),
                        key,address,phone,longitude,latitude,streamIds);

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
        //Toast.makeText(this,String.valueOf(streamList),Toast.LENGTH_SHORT).show();
        streamIds = streamList;
    }
}