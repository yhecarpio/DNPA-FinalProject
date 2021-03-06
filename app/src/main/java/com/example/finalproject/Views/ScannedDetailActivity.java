package com.example.finalproject.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.Controllers.DownloadImageTask;
import com.example.finalproject.Controllers.TouristPlacesLandmarksController;
import com.example.finalproject.Models.LandMark;
import com.example.finalproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ScannedDetailActivity extends AppCompatActivity {

    TouristPlacesLandmarksController t_places;
    ArrayList<LandMark> landMarks;
    TextView place;
    TextView title;
    ImageView img_landmark;
    TextView description;
    FloatingActionButton return_map;
    FloatingActionButton return_qr_scanner;
    FloatingActionButton take_photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_detail);

        init();
    }
    //Initializing the components to start the activity
    private void init() {
        t_places = new TouristPlacesLandmarksController(false);
        landMarks = t_places.getLandMarks();
        place = findViewById(R.id.tv_place);
        title = findViewById(R.id.tv_title);
        img_landmark = findViewById(R.id.iv_img_landmark);
        description = findViewById(R.id.tv_description);
        return_map = findViewById(R.id.btn_return_map);
        return_map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScannedDetailActivity.this, MapsActivity.class));
            }
        });
        return_qr_scanner = findViewById(R.id.btn_return_qr_scanner);
        return_qr_scanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScannedDetailActivity.this, ScannedBarcode.class));
            }
        });
        take_photo = findViewById(R.id.btn_take_photo);
        take_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScannedDetailActivity.this, Camera.class));
            }
        });
        int id = (int) getIntent().getSerializableExtra("id");
        updateDetail(id);
    }

    //Setting up the activity's components to show them
    private void updateDetail(int id) {
        LandMark landMark = landMarks.get(id);
        place.setText(landMark.getPlace());
        title.setText(landMark.getName());
        //Executing the image charge from an URL using AsyncTask
        new DownloadImageTask((ImageView) findViewById(R.id.iv_img_landmark))
                .execute(landMark.getImage());

        description.setText(landMark.getDescription());
    }


}