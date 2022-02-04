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

public class RegistrationActivity extends AppCompatActivity {

    private EditText name, email, password;
    private Button regBtn;
    private TextView loginn;

    private FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        name = findViewById(R.id.name);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        regBtn = findViewById(R.id.regBtn);
        loginn = findViewById(R.id.loginn);

        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        loginn.setOnClickListener((view)-> {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);

        });

        regBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                String emailString = email.getText().toString();
                String passwordString = password.getText().toString();

                if(TextUtils.isEmpty(emailString)){
                    email.setError("Email is required");
                }
                if (TextUtils.isEmpty(passwordString)){
                    password.setError("Password is required");
                }
                else {
                    progressDialog.setMessage("Register in progress");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    mAuth.createUserWithEmailAndPassword(emailString, passwordString).addOnCompleteListener((task)-> {
                        if (task.isSuccessful()) {
                            Intent intent = new Intent(RegistrationActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();
                        } else {
                            Toast.makeText(RegistrationActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }
            }
        });

    }
}

