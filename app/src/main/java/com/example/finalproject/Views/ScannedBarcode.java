package com.example.finalproject.Views;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.Controllers.QRScannerController;
import com.example.finalproject.Models.Orientation;
import com.example.finalproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ScannedBarcode extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    TextView txtBarcode, txtBarcodeHorizontal, txtBarcodeHorizontalInverted;
    ImageView shakingViewTriangle, shakingViewPhone;

    SurfaceView surfaceView;
    FloatingActionButton returnMap;
    QRScannerController qrScannerController;

    Orientation orientation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_barcode);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        int threshold = 5;
        orientation = new Orientation(threshold);

        initViews();
    }

    //Initializing the components to start the activity
    private void initViews() {
        qrScannerController = new QRScannerController();
        surfaceView = findViewById(R.id.surfaceView);
        returnMap = findViewById(R.id.btn_return_map);
        returnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScannedBarcode.this, MapsActivity.class));
            }
        });

        txtBarcode = findViewById(R.id.txtBarcodeValue);
        txtBarcodeHorizontal = findViewById(R.id.txtBarcodeValueHorizontal);
        txtBarcodeHorizontalInverted = findViewById(R.id.txtBarcodeValueHorizontalInverted);
        shakingViewTriangle = findViewById(R.id.imgShakingTriangle);
        shakingViewPhone = findViewById(R.id.imgShakingPhone);
    }

    @Override
    protected void onPause() {
        super.onPause();
        qrScannerController.releaseCamera();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        qrScannerController.initialiseDetectorsAndSources(surfaceView, ScannedBarcode.this);

        Sensor accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometer != null) {
            sensorManager.registerListener(this, accelerometer,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
        Sensor magneticField = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        if (magneticField != null) {
            sensorManager.registerListener(this, magneticField,
                    SensorManager.SENSOR_DELAY_NORMAL, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            orientation.updateAccelerometerReading(event.values);
            updateOrientationAngles();
            detectShakingMotion();
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            orientation.updateMagnetometerReading(event.values);
            updateOrientationAngles();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(orientation.getRotationMatrix(), null,
                orientation.getAccelerometerReading(), orientation.getMagnetometerReading());

        // "mRotationMatrix" now has up-to-date information.
        SensorManager.getOrientation(orientation.getRotationMatrix(), orientation.getOrientationAngles());
        System.out.println("filtered: "+orientation.getOrientationAngles()[1]);

        if (orientation.getOrientationAngles()[1] < 0.4 && orientation.getOrientationAngles()[1] > -0.4) {
            if (orientation.getOrientationAngles()[2] < 0) {
                txtBarcode.setVisibility(View.INVISIBLE);
                txtBarcodeHorizontal.setVisibility(View.VISIBLE);
                txtBarcodeHorizontalInverted.setVisibility(View.INVISIBLE);
            } else {
                txtBarcode.setVisibility(View.INVISIBLE);
                txtBarcodeHorizontal.setVisibility(View.INVISIBLE);
                txtBarcodeHorizontalInverted.setVisibility(View.VISIBLE);
            }
        }
        else {
            txtBarcode.setVisibility(View.VISIBLE);
            txtBarcodeHorizontal.setVisibility(View.INVISIBLE);
            txtBarcodeHorizontalInverted.setVisibility(View.INVISIBLE);
        }
    }

    private void detectShakingMotion() {
        float currentX = orientation.getAccelerometerReading()[0];
        float currentY = orientation.getAccelerometerReading()[1];
        float currentZ = orientation.getAccelerometerReading()[2];

        orientation.getmHighPass()[0] = orientation.highPassFilter(currentX, orientation.getLastX(), orientation.getmHighPass()[0]);
        orientation.getmHighPass()[1] = orientation.highPassFilter(currentY, orientation.getLastY(), orientation.getmHighPass()[1]);
        orientation.getmHighPass()[2] = orientation.highPassFilter(currentZ, orientation.getLastZ(), orientation.getmHighPass()[2]);

        if (!orientation.isFirstTime()){
            float xDifference = Math.abs(orientation.getLastX()- orientation.getmHighPass()[0]);
            float yDifference = Math.abs(orientation.getLastY()- orientation.getmHighPass()[1]);
            float zDifference = Math.abs(orientation.getLastZ()- orientation.getmHighPass()[2]);

            if (xDifference > orientation.getThreshold() || yDifference > orientation.getThreshold() || zDifference > orientation.getThreshold()) {
                shakingViewTriangle.setVisibility(View.VISIBLE);
                shakingViewTriangle.setAlpha((float) 0.3);
                shakingViewPhone.setVisibility(View.VISIBLE);
                shakingViewPhone.setAlpha((float) 0.3);
            } else{
                shakingViewTriangle.setVisibility(View.INVISIBLE);
                shakingViewPhone.setVisibility(View.INVISIBLE);
            }
        }

        orientation.setLastX(orientation.getmHighPass()[0]);
        orientation.setLastY(orientation.getmHighPass()[1]);
        orientation.setLastZ(orientation.getmHighPass()[2]);

        orientation.setFirstTime(false);
    }

}