package com.example.finalproject.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.finalproject.Controllers.LoginController;
import com.example.finalproject.R;

public class LoginUser extends AppCompatActivity {

    private EditText et_email;
    private EditText et_password;
    Button btn_login;
    Button btn_SentToRegister;

    private String email = "";
    private String password = "";
    LoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_user);

        loginController = new LoginController();

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_login = findViewById(R.id.btn_login);
        btn_SentToRegister = findViewById(R.id.btn_sendToRegister);

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
                    loginController.loginUser(email, password, LoginUser.this);
                }
                else {
                    Toast.makeText(LoginUser.this, "Complete los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}