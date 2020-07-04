package com.example.finalproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class ScannedBarcode extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];

    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];

    private float lastX, lastY, lastZ;
    private boolean firstTime = true;
    private final float THRESHOLD = 5;
    TextView txtBarcode, txtBarcodeHorizontal, txtBarcodeHorizontalInverted, shakingMessage;;

    SurfaceView surfaceView;
    private BarcodeDetector barcodeDetector;
    private CameraSource cameraSource;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    FloatingActionButton returnMap;
    String intentData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanned_barcode);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        txtBarcode = (TextView) findViewById(R.id.txtBarcodeValue);
        txtBarcodeHorizontal = (TextView) findViewById(R.id.txtBarcodeValueHorizontal);
        txtBarcodeHorizontalInverted = (TextView) findViewById(R.id.txtBarcodeValueHorizontalInverted);
        shakingMessage = (TextView) findViewById(R.id.shaking_message);

        initViews();
    }

    //Initializing the components to start the activity
    private void initViews() {
        surfaceView = findViewById(R.id.surfaceView);
        returnMap = findViewById(R.id.btn_return_map);
        returnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ScannedBarcode.this, MapsActivity.class));
            }
        });
    }

    //Setting up the QR Scanner config
    private void initialiseDetectorsAndSources() {

        Toast.makeText(getApplicationContext(), "Barcode scanner started", Toast.LENGTH_SHORT).show();

        barcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(ScannedBarcode.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        cameraSource.start(surfaceView.getHolder());
                    } else {
                        ActivityCompat.requestPermissions(ScannedBarcode.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                cameraSource.stop();
            }
        });


        barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {

                    if (barcodes.valueAt(0).displayValue != null) {
                        intentData = barcodes.valueAt(0).displayValue;
                        startActivity(new Intent(ScannedBarcode.this, ScannedDetailActivity.class).putExtra("id", Integer.parseInt(intentData)));
                    } else {
                        Toast.makeText(getApplicationContext(), "No se pudo escanear correctamente", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });
    }


    @Override
    protected void onPause() {
        super.onPause();
        cameraSource.release();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();

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