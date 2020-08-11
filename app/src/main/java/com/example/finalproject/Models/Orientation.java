package com.example.finalproject.Models;

public class Orientation {
    private final float[] accelerometerReading = new float[3];
    private final float[] magnetometerReading = new float[3];

    private final float[] rotationMatrix = new float[9];
    private final float[] orientationAngles = new float[3];
    private final float[] mLast = new float[3];
    private final float[] mHighPass = new float[3];

    private float lastX, lastY, lastZ;
    private boolean firstTime = true;
    private float threshold = 5;
    private final float a = 0.8f;

    public Orientation(float threshold){
        this.threshold = threshold;
    }

    public float[] getAccelerometerReading(){
        return accelerometerReading;
    }

    public float[] getMagnetometerReading() {
        return magnetometerReading;
    }

    public float[] getRotationMatrix() {
        return rotationMatrix;
    }

    public float[] getOrientationAngles() {
        return orientationAngles;
    }

    public float[] getmHighPass() {
        return mHighPass;
    }

    public float getA() {
        return a;
    }

    public float getLastX() {
        return lastX;
    }

    public float getLastY() {
        return lastY;
    }

    public float getLastZ() {
        return lastZ;
    }

    public boolean isFirstTime() {
        return firstTime;
    }

    public float getThreshold() {
        return threshold;
    }

    public void setLastX(float lastX) {
        this.lastX = lastX;
    }

    public void setLastY(float lastY) {
        this.lastY = lastY;
    }

    public void setLastZ(float lastZ) {
        this.lastZ = lastZ;
    }

    public void setFirstTime(boolean firstTime) {
        this.firstTime = firstTime;
    }

    public void updateAccelerometerReading(float[] values) {
        System.arraycopy(values, 0, accelerometerReading,
                0, accelerometerReading.length);
    }

    public float highPassFilter(float current, float last, float filtered) {
        return a * (filtered + current - last);
    }

    public void updateMagnetometerReading(float[] values) {
        System.arraycopy(values, 0, magnetometerReading,
                0, magnetometerReading.length);
    }
}
