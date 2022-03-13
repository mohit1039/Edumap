package com.edumap.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.edumap.Model.Event;
import com.edumap.Model.History;
import com.edumap.R;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class AddHistoryActivity extends AppCompatActivity {

    private EditText yearEditText,descEditText;
    private FirebaseDatabase mainfdb;
    private DatabaseReference historyDataRef;
    private String historyYear,historyDesc;
    private String updateKey;
    History updateHistory;
    private String collegeID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_history);

        yearEditText = findViewById(R.id.addEstablishmentYear);
        descEditText = findViewById(R.id.addHistory);
        mainfdb = FirebaseDatabase.getInstance();
        historyDataRef = mainfdb.getReference("History");

        if (getIntent() != null) {
            updateKey = getIntent().getStringExtra("historyID");
            collegeID = getIntent().getStringExtra("collegeID");
            if (updateKey != null) {
                historyDataRef.child(updateKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            updateHistory = dataSnapshot.getValue(History.class);
                            assert updateHistory != null;
                            yearEditText.setText(updateHistory.getYear());
                            descEditText.setText(updateHistory.getAbout());
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

        historyYear = yearEditText.getText().toString().trim();
        historyDesc = descEditText.getText().toString().trim();

        if (!historyYear.isEmpty() &&!historyDesc.isEmpty()){
            History history = new History(updateKey,historyYear,historyDesc,collegeID);
            if (updateKey == null) {
                updateKey = historyDataRef.push().getKey();
                assert updateKey != null;
                historyDataRef.child(updateKey).setValue(history);
            }
            else {
                historyDataRef.child(updateKey).setValue(history);
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