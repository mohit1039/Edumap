package com.edumap.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.edumap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.internal.api.FirebaseNoSignedInUserException;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail,inputPassword;
    public String email,password;
    FirebaseDatabase mainfdb;
    DatabaseReference ref;
    private FirebaseAuth auth;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_login);
        progressDialog = new ProgressDialog(this);
        mainfdb = FirebaseDatabase.getInstance();

        inputEmail = findViewById(R.id.editTextLoginEmail);
        inputPassword = findViewById(R.id.editTextLoginPassword);
        auth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference("Login");

        if (Common.isConnectedtoInternet(this)) {
            if (auth.getCurrentUser() != null) {
                finish();
                startActivity(new Intent(LoginActivity.this, ShowCollege.class));
            }
        } else {
            Toast.makeText(this, "Please check your Intenet Connection", Toast.LENGTH_LONG).show();
        }
    }

    public void onLoginClick(View View) {
        startActivity(new Intent(this, RegisterActivity.class));
        overridePendingTransition(R.anim.slide_in_right, R.anim.stay);
    }

    public void Login(View view) {

        email = inputEmail.getText().toString().trim();
        password = inputPassword.getText().toString();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(this, "Please enter email", Toast.LENGTH_LONG).show();
        }
        if (TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please enter the password", Toast.LENGTH_LONG).show();
        }
        progressDialog.setMessage("Please wait ...");
        progressDialog.show();
        if (Common.isConnectedtoInternet(this)) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            progressDialog.dismiss();
                            if (!task.isSuccessful()) {
                                try{
                                    throw Objects.requireNonNull(task.getException());
                                } catch (FirebaseAuthInvalidCredentialsException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this,"Authentication Failed",Toast.LENGTH_LONG).show();
                                } catch (FirebaseNoSignedInUserException e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this,"Please register to Login",Toast.LENGTH_LONG).show();
                                } catch (Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(LoginActivity.this,""+e,Toast.LENGTH_LONG).show();
                                }
                            }
                            else {
                                try {
                                    String uid = auth.getUid();
                                    transfer(uid);
                                } catch (Exception e) {
                                    Log.e("Error",""+e);
                                }
                            }
                        }
                    });
        } else {
            progressDialog.dismiss();
            Toast.makeText(this, "Please check your Intenet Connection", Toast.LENGTH_LONG).show();
        }

    }

    private void transfer(final String uid) {
        ref.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String admin = dataSnapshot.child("admin").getValue().toString();
                if (admin.equals("true")){
                    SharedPreferences settings = getApplicationContext().getSharedPreferences("Admin", MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putBoolean("Admin", true);
                    editor.apply();
                }
                startActivity(new Intent(LoginActivity.this, ShowCollege.class));
                finish();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    public void forgotPassword(View view) {
        startActivity(new Intent(LoginActivity.this, ForgotPassword.class));
    }
}

