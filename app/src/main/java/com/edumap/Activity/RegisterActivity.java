package com.edumap.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.edumap.Activity.LoginActivity;
import com.edumap.Adapter.SpinAdapter;
import com.edumap.Model.Course;
import com.edumap.Model.Users;
import com.edumap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText regName, regSurname, regemail, regpassword, regAddress ;
    private FirebaseAuth mauth;
    private Spinner courseSpinner;
    private ProgressDialog progressDialog;
    private String course = "";
    private ArrayList<Course> courseList = new ArrayList<>();
    FirebaseDatabase mainfdb;
    DatabaseReference ref,courseRef;
    private SpinAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        changeStatusBarColor();

        FirebaseApp.initializeApp(this);
        mauth = FirebaseAuth.getInstance();
        adapter = new SpinAdapter(this,
                android.R.layout.simple_spinner_item,
                 courseList);
        mainfdb = FirebaseDatabase.getInstance();
        courseRef = mainfdb.getReference("Course");
        initCourse();
        progressDialog = new ProgressDialog(this);
        regName = findViewById(R.id.editTextName);
        regSurname = findViewById(R.id.editTextSurname);
        regAddress = findViewById(R.id.editTextAddress);
        courseSpinner = findViewById(R.id.courseSpinner);
        regemail = findViewById(R.id.editTextEmail);
        regpassword = findViewById(R.id.editTextPassword);
        courseSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i >= 0) {
                    course = adapter.getItem(i).getId();
                }
                else {
                    course = "";
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void initCourse() {
        courseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                        Course i = dataSnapshot1.getValue(Course.class);
                        courseList.add(i);
                    }
                    courseSpinner.setAdapter(adapter);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @SuppressLint("ObsoleteSdkInt")
    private void changeStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.register_bk_color));
        }
    }

    public void onLoginClick(View view){
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in_left,android.R.anim.slide_out_right);
    }

    public void Register(View view) {
        String email = regemail.getText().toString().trim();
        String password = regpassword.getText().toString();
        ref = mainfdb.getReference("Users");

        String name = regName.getText().toString().trim();
        String address = regAddress.getText().toString().trim();
        String surname = regSurname.getText().toString().trim();

        if (TextUtils.isEmpty(email)){
            Toast.makeText(this,"Please enter email",Toast.LENGTH_LONG).show();
        } else if (TextUtils.isEmpty(password)){
            Toast.makeText(this,"Please enter the password",Toast.LENGTH_LONG).show();
        } else if (name.isEmpty() || surname.isEmpty() || address.isEmpty() || course.isEmpty()) {
            Snackbar snackbar = Snackbar
                    .make(regName, "Enter the complete details!", Snackbar.LENGTH_LONG)
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
        } else {
            progressDialog.setMessage("Registering user ...");
            progressDialog.show();

            mauth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(
                            new OnCompleteListener<AuthResult>()
                            {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task)
                                {
                                    if (!task.isSuccessful())
                                    {
                                        try
                                        {
                                            throw Objects.requireNonNull(task.getException());
                                        }
                                        // if user enters wrong email.
                                        catch (FirebaseAuthWeakPasswordException weakPassword)
                                        {   progressDialog.dismiss();
                                            Toast.makeText(RegisterActivity.this,"Weak Password",Toast.LENGTH_LONG).show();
                                        }
                                        // if user enters wrong password.
                                        catch (FirebaseAuthInvalidCredentialsException malformedEmail)
                                        {   progressDialog.dismiss();
                                            Toast.makeText(RegisterActivity.this,"Malformed Email",Toast.LENGTH_LONG).show();
                                        }
                                        catch (FirebaseAuthUserCollisionException existEmail)
                                        {   progressDialog.dismiss();
                                            Toast.makeText(RegisterActivity.this,"Email already exists",Toast.LENGTH_LONG).show();
                                        }
                                        catch (Exception e)
                                        {   progressDialog.dismiss();
                                            Toast.makeText(RegisterActivity.this,"registeration failed",Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    else{
                                        String uid = mauth.getUid();
                                        Users users = new Users(name,surname,uid,address,course,email);
                                        ref.child(uid).setValue(users);
                                        Toast.makeText(RegisterActivity.this,"User Registered",Toast.LENGTH_LONG).show();
                                        startActivity(new Intent(RegisterActivity.this, ShowCollege.class));
                                        finish();
                                    }
                                }
                            }
                    );
        }
    }

}