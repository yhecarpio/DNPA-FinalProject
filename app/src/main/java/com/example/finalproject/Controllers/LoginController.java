package com.example.finalproject.Controllers;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.finalproject.Views.MapsActivity;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Map;

public class LoginController {
    FirebaseAuth mAuth;
    DatabaseReference mDatabase;

    public LoginController(){
        this.mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    //Making a Login, if the credentials are corrected, the MapsActivity starts
    public void loginUser(String email, String password, final Activity activity){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("adsds", "Iniciando Login");
                if (task.isSuccessful()){
                    String id = mAuth.getCurrentUser().getUid();
                    LatLng position = new MapsController(activity).getLocation();
                    if (position != null){
                        mDatabase.child("Users").child(id).child("latitude").setValue(position.latitude);
                        mDatabase.child("Users").child(id).child("longitude").setValue(position.longitude);
                    }
                    else{
                        mDatabase.child("Users").child(id).child("latitude").setValue(0);
                        mDatabase.child("Users").child(id).child("longitude").setValue(0);
                    }
                    activity.getApplicationContext().startActivity(new Intent(activity.getApplicationContext(), MapsActivity.class));
                    activity.finish();
                }
                else{
                    Toast.makeText(activity.getApplicationContext(), "No se pudo iniciar sesión, compruebe datos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //Registering Users, if some data are incorrect, it shows the error
    public void registerUser(final String name, final String email, final String password, final Activity activity){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Map<String, Object> map = new HashMap<>();
                    LatLng position = new MapsController(activity).getLocation();
                    map.put("name", name);
                    if (position != null){
                        map.put("latitude", position.latitude);
                        map.put("longitude", position.longitude);
                    }
                    else{
                        map.put("latitude", 0);
                        map.put("longitude", 0);
                    }
                    map.put("email", email);
                    map.put("password", password);
                    String id = mAuth.getCurrentUser().getUid();
                    mDatabase.child("Users").child(id).setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task2) {
                            if (task2.isSuccessful()){
                                activity.getApplicationContext().startActivity(new Intent(activity.getApplicationContext(), MapsActivity.class));
                                activity.finish();
                            }
                            else{
                                Toast.makeText(activity.getApplicationContext(), "No" +
                                        " se pudieron crear los datos correctamente", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(activity.getApplicationContext(), "No se pudo registrar este usuario", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}
