package com.edumap.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.edumap.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    private EditText foremail;
    private ProgressDialog progressDialog;
    private FirebaseAuth resauth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        progressDialog = new ProgressDialog(this);
        resauth = FirebaseAuth.getInstance();
        foremail = findViewById(R.id.forgotEmail);
    }

    public void Reset(View view) {
        String email = foremail.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplication(), "Enter your registered email id", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("Please wait ...");
            progressDialog.show();
            resauth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                progressDialog.dismiss();
                                Toast.makeText(ForgotPassword.this, "We have sent you instructions to reset your password!", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(ForgotPassword.this, LoginActivity.class));
                                finish();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(ForgotPassword.this, "Failed to send reset email!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}