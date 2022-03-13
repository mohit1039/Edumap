package com.edumap.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.edumap.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    DatabaseReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_main);

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (Common.isConnectedtoInternet(MainActivity.this)) {
                    auth = FirebaseAuth.getInstance();
                    if (auth.getCurrentUser() != null) {
                        startActivity(new Intent(MainActivity.this, ShowCollege.class));
                        finish();
                    } else {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Please check your Intenet Connection", Toast.LENGTH_LONG).show();
                }
            }
        }, 2000);
    }
}