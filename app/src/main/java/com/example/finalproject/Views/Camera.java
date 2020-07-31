package com.example.finalproject.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.media.Image;
import android.media.ImageReader;
import android.os.Bundle;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;

import com.example.finalproject.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

public class Camera extends AppCompatActivity {

    private static int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    private static String TAG = "MainActivity";
    private static final int CAMERAID = 0;
    private SurfaceView surfaceView;
    private CameraDevice cameraDevice;
    private SurfaceHolder surfaceHolder;
    private ImageReader imageReader;
    private CameraManager cameraManager;
    private String cameraID = null;
    private CameraCaptureSession cameraCaptureSession;
    private CaptureRequest.Builder previewCaptureRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        verifyStoragePermissions(this);
        Button button = findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "Foto tomada");
                takePicture();
            }
        });
        init();
    }
    private void init() {
        surfaceView = findViewById(R.id.surfaceView);
        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.setKeepScreenOn(true);
        surfaceHolder.addCallback(surfaceHolderCallback);
        surfaceHolder.setFixedSize(1080, 2000);
    }

    private SurfaceHolder.Callback surfaceHolderCallback = new SurfaceHolder.Callback() {

        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            if (ActivityCompat.checkSelfPermission(Camera.this,
                    Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                startCameraCaptureSession();
                Log.d(TAG, "Start Camera Session");
            } else {
                ActivityCompat.requestPermissions(Camera.this, new
                        String[]{Manifest.permission.CAMERA}, 201);
            }
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {

        }
    };

    private void startCameraCaptureSession() {
        int largestWidth = 1080;
        int largestHeight = 2000;

        imageReader = ImageReader.newInstance(largestWidth, largestHeight, ImageFormat.JPEG, 1);
        imageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                try (Image image = reader.acquireNextImage()) {
                    Image.Plane[] planes = image.getPlanes();
                    if (planes.length > 0) {
                        ByteBuffer buffer = planes[0].getBuffer();
                        byte[] data = new byte[buffer.remaining()];
                        buffer.get(data);
                        saveImage(data);
                        startActivity(new Intent(Camera.this, MapsActivity.class));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, null);

        cameraManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            cameraID = cameraManager.getCameraIdList()[CAMERAID];

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            cameraManager.openCamera(cameraID, cameraDeviceCallback, null);
            Log.d(TAG, "Camera is open");

        } catch (Exception e) {
            Log.d(TAG, "unable to open the camera");
        }
    }

    private CameraDevice.StateCallback cameraDeviceCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(@NonNull CameraDevice camera) {
            cameraDevice = camera;
            takePreview();
        }

        @Override
        public void onDisconnected(@NonNull CameraDevice camera) {
            camera.close();
            cameraDevice = null;
        }

        @Override
        public void onError(@NonNull CameraDevice camera, int error) {
            camera.close();
            cameraDevice = null;
            Log.e(TAG, "Camera error: " + error);
        }
    };

    private void takePreview() {
        if (cameraDevice == null || surfaceHolder.isCreating()){
            return;
        }

        try {
            Surface previewSurface = surfaceHolder.getSurface();
            previewCaptureRequest = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

            previewCaptureRequest.addTarget(previewSurface);

            cameraDevice.createCaptureSession(Arrays.asList(previewSurface, imageReader.getSurface()),
                    captureSessionCallback, null);

        } catch (CameraAccessException e){
            Log.e(TAG, "Camera Access Exception: " + e);
        }
    }

    private CameraCaptureSession.StateCallback captureSessionCallback = new CameraCaptureSession.StateCallback() {
        @Override
        public void onConfigured(@NonNull CameraCaptureSession session) {
            cameraCaptureSession = session;
            try {
                cameraCaptureSession.setRepeatingRequest(previewCaptureRequest.build(), null, null);
            } catch (CameraAccessException | IllegalStateException e){
                Log.e(TAG, "Capture Session Exception: " + e);
            }
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession session) {

        }
    };

    private void saveImage(byte[] data) {
        FileOutputStream outputStream = null;
        try {
            String path = getApplicationContext().getCacheDir().toString();
            Log.d(TAG, path);
            File outputFile = new File(path, "test.jpg");


            outputStream = new FileOutputStream(outputFile);
            outputStream.write(data);
            outputStream.close();
        } catch (FileNotFoundException e){
            Log.e(TAG, "File not found: " + e);
        } catch (IOException e){
            Log.e(TAG, "IO Exception: " + e);
        }
    }

    private void takePicture() {
        if (cameraDevice == null)
            return;
        try {
            CaptureRequest.Builder takePictureBuilder = cameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
            takePictureBuilder.addTarget(imageReader.getSurface());
            CaptureRequest captureRequest = takePictureBuilder.build();
            cameraCaptureSession.capture(captureRequest, null, null);
        } catch (CameraAccessException e){
            Log.e(TAG, "Error capturing the photo:" + e);
        }
    }

    private void verifyStoragePermissions(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    public void checkCamera(){
        try{
            CameraManager cManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);

            String[] cameraIds = cManager.getCameraIdList();

            for (String cameraId: cameraIds){
                Log.d(TAG, cameraId);
                CameraCharacteristics characteristics = cManager.getCameraCharacteristics(cameraId);

                int facing = characteristics.get(CameraCharacteristics.LENS_FACING);
                if (facing == CameraCharacteristics.LENS_FACING_BACK){
                    Log.d(TAG, "Back Camera");
                } else if (facing == CameraCharacteristics.LENS_FACING_FRONT){
                    Log.d(TAG, "Front Camera");
                } else {
                    Log.d(TAG, "External Camera");
                }

            }
        } catch (CameraAccessException e){
            e.printStackTrace();
        }
    }
}