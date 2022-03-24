package com.edumap.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.edumap.Interface.OnGetDataListener;
import com.edumap.Model.Course;
import com.edumap.Model.Users;
import com.edumap.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ShowProfile extends AppCompatActivity {

    private TextView name,address,email,courseName;

    private DatabaseReference userRef,courseRef;
    private FirebaseDatabase mainfdb;
    private FirebaseAuth mauth;
    Users users;
    Course course;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);

        mainfdb = FirebaseDatabase.getInstance();
        mauth = FirebaseAuth.getInstance();
        userRef = mainfdb.getReference("Users");
        courseRef = mainfdb.getReference("Course");
        name = findViewById(R.id.studname);
        address = findViewById(R.id.studentaddress);
        email = findViewById(R.id.studentemail);
        courseName = findViewById(R.id.studcourse);

        initData();
    }

    private void initData() {
        Common.readData(userRef.child(mauth.getUid()), new OnGetDataListener() {
            @Override
            public void onSuccess(DataSnapshot dataSnapshot) {
                users = dataSnapshot.getValue(Users.class);
                if (users != null && users.getCourse() != null) {
                    Common.readData(courseRef.child(users.getCourse()), new OnGetDataListener() {
                        @Override
                        public void onSuccess(DataSnapshot dataSnapshot) {
                            course = dataSnapshot.getValue(Course.class);
                            name.setText(users.getName() + " " + users.getSurname());
                            email.setText(users.getEmail());
                            address.setText(users.getAddress());
                            courseName.setText(course.getName());
                        }

                        @Override
                        public void onFailure() {

                        }
                    });
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }
}