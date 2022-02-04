package com.example.duitku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText email, password;
    private Button loginBtn;
    private TextView registerr;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginBtn =  findViewById(R.id.loginBtn);
        registerr = findViewById(R.id.registerr);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);


        registerr.setOnClickListener((view)->{
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
        });

        loginBtn.setOnClickListener((view)-> {
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();

                if(TextUtils.isEmpty(emailString)){
                    email.setError("Email is required");
                }
                if (TextUtils.isEmpty(passwordString)){
                    password.setError("Password is required");
                }

                else {
                    progressDialog.setMessage("Login in progress");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    mAuth.signInWithEmailAndPassword(emailString, passwordString).addOnCompleteListener((task)-> {
                            if (task.isSuccessful()) {
                                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                                progressDialog.dismiss();
                            } else {
                                Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();
                            }
                    });
                }
        });
    }
}
