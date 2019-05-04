package com.example.lian.betterbattleship;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

public class ServiceSensor extends Service implements SensorEventListener {

    public interface ServiceCallbacks {
        void react();
        void warningOn();
        void warningOff();
    }

    private ServiceCallbacks serviceCallbacks;
    private final IBinder mBinder = new MyBinder();
    private SensorManager mSensorManager;
    private Sensor mSensor;
    private float lastX, lastY, lastZ, originX, originY, originZ;
    private boolean isFirstTime = true;
    private long startTime = 0, diffTime = 0;
    private long currentTime;
    private boolean flag = false;
    private final double RANGE = 0.15;
    private final int TIME = 4000;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Get sensor manager on starting the service
        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // Get default sensor type
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);

        // Registering
        mSensorManager.registerListener(this, mSensor, SensorManager.SENSOR_DELAY_NORMAL);

        return Service.START_NOT_STICKY;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        diffTime = 0;

        if(isFirstTime) {
            originX = event.values[0];
            lastX = event.values[0];
            originY = event.values[1];
            lastY = event.values[1];
            originZ = event.values[2];
            lastZ = event.values[2];

            currentTime = System.currentTimeMillis();
            diffTime = currentTime - startTime;
            startTime = currentTime;

            isFirstTime = false;
        }

        else {
            lastX = event.values[0];
            lastY = event.values[1];
            lastZ = event.values[2];
        }

        if (Math.abs(originX-lastX) > RANGE || Math.abs(originY-lastY) > RANGE || Math.abs(originZ-lastZ) > RANGE) {

            //Log.d("SERVICE SENSOR", "CHANGED");

            if(!flag) {

                if (serviceCallbacks != null) {
                    serviceCallbacks.warningOn();
                }

                currentTime = System.currentTimeMillis();
                startTime = currentTime;
            }

            currentTime = System.currentTimeMillis();
            diffTime = currentTime - startTime;

            flag = true;
        }

        else {
            if (serviceCallbacks != null) {
                serviceCallbacks.warningOff();
            }

            flag = false;
        }

        if(diffTime > TIME && flag) {
            flag = false;
            //Log.d("SERVICE SENSOR", "5 SEC");
            if(serviceCallbacks != null) {
                serviceCallbacks.react();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {}

    public class MyBinder extends Binder {
        ServiceSensor getService() {
            return ServiceSensor.this;
        }
    }

    public SensorManager getmSensorManager() {
        return mSensorManager;
    }

    public void setCallbacks(ServiceCallbacks callbacks) {
        serviceCallbacks = callbacks;
    }
}
