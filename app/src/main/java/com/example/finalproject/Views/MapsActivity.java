package com.example.finalproject.Views;

import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import com.example.finalproject.Controllers.MapsController;
import com.example.finalproject.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    //Initializing variables
    FloatingActionButton scan_qr;
    FloatingActionButton take_photo;
    private GoogleMap mMap;
    private MapsController mapsController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        init();


    }

    //Initializing the components to start the activity
    private void init(){
        mapsController = new MapsController(MapsActivity.this);
        mapsController.populateGeofenceList();
        mapsController.addGeofences();
        scan_qr = findViewById(R.id.btn_scan_qr);
        scan_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, ScannedBarcode.class));
            }
        });

        take_photo = findViewById(R.id.btn_take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MapsActivity.this, Camera.class));
            }
        });
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (!mapsController.checkPermissions()) {
            mapsController.requestPermissions();
        }
        else {
            mMap.setMyLocationEnabled(true);
            // Add a marker in current location and move the camera
            mapsController.fillMarkers(mMap);
            LatLng casa = mapsController.getLocation();
            if (casa != null)
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(casa,15));
        }
    }
}