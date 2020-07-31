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

    LoginController loginController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);

        loginController = new LoginController();

        et_name = findViewById(R.id.et_name);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_repassword = findViewById(R.id.et_repassword);
        btn_register = findViewById(R.id.btn_register);
        btn_SentToLogin = findViewById(R.id.btn_sendToLogin);

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
                        loginController.registerUser(name, email, password, RegisterUser.this);
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

    //Verifying if the password's fields are equal
    private boolean isEqual(String psswd, String repsswd){
        return psswd.equals(repsswd);
    }
}