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

import com.edumap.Adapter.StreamAdapter;
import com.edumap.Interface.StreamListener;
import com.edumap.Model.College;
import com.edumap.Model.Course;
import com.edumap.Model.Stream;
import com.edumap.Model.Users;
import com.edumap.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AddCourseActivity extends AppCompatActivity implements StreamListener {

    private EditText courseNameEditText;
    private String courseName;
    RecyclerView checkboxRecyclerView;
    StreamAdapter streamAdapter;
    private FirebaseDatabase mainfdb;
    private DatabaseReference streamData;
    private ArrayList<Stream> stream = new ArrayList<>();
    private ArrayList<String> streamIds = new ArrayList<>();
    private DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_course);
        FirebaseApp.initializeApp(this);
        mainfdb = FirebaseDatabase.getInstance();
        streamData = mainfdb.getReference("Stream");
        courseNameEditText = findViewById(R.id.addCourses);
        checkboxRecyclerView = findViewById(R.id.checkboxStreamRecycler);
        checkboxRecyclerView();
        initem();
    }

    private void checkboxRecyclerView() {
        checkboxRecyclerView.setHasFixedSize(true);
        checkboxRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        streamAdapter = new StreamAdapter(this,stream,this);
    }
    public void add(View view) {
        savedata();
    }

    public void savedata(){
        ref = mainfdb.getReference("Course");
        String name = courseNameEditText.getText().toString();

        if (!name.isEmpty() && streamIds.size() > 0){
            String key = ref.push().getKey();
            Course course = new Course(key,name,streamIds);
            if (key != null) {
                ref.child(key).setValue(course);
                startActivity(new Intent(this, ShowCollege.class));
                finish();
            }
        } else {
            Snackbar snackbar = Snackbar
                    .make(courseNameEditText, "Enter the complete details!", Snackbar.LENGTH_LONG)
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

    @Override
    public void streamChanger(ArrayList<String> streamList) {
        streamIds = streamList;
    }
}