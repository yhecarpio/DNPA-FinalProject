package com.example.finalproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginUser extends AppCompatActivity {

    private EditText et_email;
    private EditText et_password;
    Button btn_login;
    Button btn_SentToRegister;

    private String email = "";
    private String password = "";

    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        mAuth = FirebaseAuth.getInstance();

        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        btn_login = (Button) findViewById(R.id.btn_login);
        btn_SentToRegister = (Button) findViewById(R.id.btn_sendToRegister);

        btn_SentToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginUser.this, RegisterUser.class));

            }
        });

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = et_email.getText().toString();
                password = et_password.getText().toString();

                if(!email.isEmpty() && !password.isEmpty()){
                    loginUser();
                }
                else {
                    Toast.makeText(LoginUser.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loginUser(){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    startActivity(new Intent(LoginUser.this, MainActivity.class));
                    finish();
                }
                else{
                    Toast.makeText(LoginUser.this, "No se pudo iniciar sesión, compruebe datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}