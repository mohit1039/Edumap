package com.edumap.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.edumap.Model.College;
import com.edumap.Model.Event;
import com.edumap.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddEventActivity extends AppCompatActivity {

    private EditText nameEditText,descEditText;
    private FirebaseDatabase mainfdb;
    private DatabaseReference eventDataRef;
    private String eventName,eventDesc;
    private String updateKey;
    Event updateEvent;
    private String collegeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        nameEditText = findViewById(R.id.addEvent);
        descEditText = findViewById(R.id.addEventDetails);
        mainfdb = FirebaseDatabase.getInstance();
        eventDataRef = mainfdb.getReference("Event");

        if (getIntent() != null) {
            updateKey = getIntent().getStringExtra("eventID");
            collegeID = getIntent().getStringExtra("collegeID");
            if (updateKey != null) {
                eventDataRef.child(updateKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            updateEvent = dataSnapshot.getValue(Event.class);
                            assert updateEvent != null;
                            nameEditText.setText(updateEvent.getName());
                            descEditText.setText(updateEvent.getDescription());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        }
    }

    public void add(View view) {


        eventName = nameEditText.getText().toString().trim();
        eventDesc = descEditText.getText().toString().trim();


        if (!eventName.isEmpty() &&!eventDesc.isEmpty()){
            Event event = new Event(updateKey,eventName,eventDesc,collegeID);
            if (updateKey == null) {
                updateKey = eventDataRef.push().getKey();
                assert updateKey != null;
                eventDataRef.child(updateKey).setValue(event);
            }
            else {
                eventDataRef.child(updateKey).setValue(event);
            }
            startActivity(new Intent(this, ShowCollegeDetails.class));
            finish();

        } else {
            Snackbar snackbar = Snackbar
                    .make(descEditText, "Enter the complete details!", Snackbar.LENGTH_LONG)
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
}