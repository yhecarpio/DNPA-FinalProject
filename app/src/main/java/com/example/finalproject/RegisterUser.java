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
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.DatabaseReference;

import java.util.HashMap;
import java.util.Map;

public class RegisterUser extends AppCompatActivity {

    private EditText et_name;
    private EditText et_email;
    private EditText et_password;
    private EditText et_repassword;
    Button btn_register;
    Button btn_SentToLogin;

    private String name = "";
    private String email = "";
    private String password = "";
    private String repassword = "";

    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        et_name = (EditText) findViewById(R.id.et_name);
        et_email = (EditText) findViewById(R.id.et_email);
        et_password = (EditText) findViewById(R.id.et_password);
        et_repassword = (EditText) findViewById(R.id.et_repassword);
        btn_register = (Button) findViewById(R.id.btn_register);
        btn_SentToLogin = (Button) findViewById(R.id.btn_sendToLogin);

        btn_SentToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterUser.this, LoginUser.class));

            }
        });

        //Checking possible interface errors before launching the register method
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name = et_name.getText().toString();
                email = et_email.getText().toString();
                password = et_password.getText().toString();
                repassword = et_repassword.getText().toString();

                if(!name.isEmpty() && !email.isEmpty() && !password.isEmpty() && isEqual(password,repassword)){
                    if (password.length() >= 6){
                        registerUser();
                    }
                    else {
                        Toast.makeText(RegisterUser.this, "Password debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    }

                }

                else {
                    Toast.makeText(RegisterUser.this, "Debe completar los campos", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //Registering Users, if some data are incorrect, it shows the error
    private void registerUser(){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    //startActivity(new Intent(MainActivity.this, NavDraw.class));
                    try {
                        finish();
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                    Map<String, Object> map = new HashMap<>();
                    map.put("name", name);
                    map.put("email", email);
                    map.put("password", password);
                    String id = mAuth.getCurrentUser().getUid();
                    mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                startActivity(new Intent(RegisterUser.this, MapsActivity.class));
                                try {
                                    finish();
                                } catch (Throwable throwable) {
                                    throwable.printStackTrace();
                                }
                            }
                            else{
                                Toast.makeText(RegisterUser.this, "No" +
                                        " se pudieron crear los datos correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(RegisterUser.this, "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    //Verifying if the password's fields are equal
    private boolean isEqual(String psswd, String repsswd){
        return psswd.equals(repsswd);
    }
}