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
import android.widget.TextView;

import com.example.finalproject.Controllers.QRScannerController;
import com.example.finalproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class ScannedBarcode extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];

    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];

    private float lastX, lastY, lastZ;
    private boolean firstTime = true;
    private final float THRESHOLD = 5;
    TextView txtBarcode, txtBarcodeHorizontal, txtBarcodeHorizontalInverted, shakingMessage;

    SurfaceView surfaceView;
    FloatingActionButton returnMap;
    QRScannerController qrScannerController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_barcode);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        txtBarcode = findViewById(R.id.txtBarcodeValue);
        txtBarcodeHorizontal = findViewById(R.id.txtBarcodeValueHorizontal);
        txtBarcodeHorizontalInverted = findViewById(R.id.txtBarcodeValueHorizontalInverted);
        shakingMessage = findViewById(R.id.shaking_message);

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
            System.arraycopy(event.values, 0, accelerometerReading,
                    0, accelerometerReading.length);
            updateOrientationAngles();
            detectShakingMotion();
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, magnetometerReading,
                    0, magnetometerReading.length);
            updateOrientationAngles();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    public void updateOrientationAngles() {
        // Update rotation matrix, which is needed to update orientation angles.
        SensorManager.getRotationMatrix(rotationMatrix, null,
                accelerometerReading, magnetometerReading);

        // "mRotationMatrix" now has up-to-date information.

        SensorManager.getOrientation(rotationMatrix, orientationAngles);

        if (orientationAngles[1] <= 0.2 && orientationAngles[1] >= -0.2) {
            if (orientationAngles[2] < 0) {
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
        float currentX = accelerometerReading[0];
        float currentY = accelerometerReading[1];
        float currentZ = accelerometerReading[2];

        if (!firstTime){
            float xDifference = Math.abs(lastX- currentX);
            float yDifference = Math.abs(lastY- currentY);
            float zDifference = Math.abs(lastZ- currentZ);

            if (xDifference > THRESHOLD || yDifference > THRESHOLD || zDifference > THRESHOLD) {
                shakingMessage.setVisibility(View.VISIBLE);
            } else
                shakingMessage.setVisibility(View.INVISIBLE);
        }

        lastX = currentX;
        lastY = currentY;
        lastZ = currentZ;

        firstTime = false;
    }

}