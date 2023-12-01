package com.example.rgbmix;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private float lastAcceleration;
    private static final float g = 9.81f;
    private float last_x,last_y,last_z;
    private long lastUpdate;

    int red=0;
    int green=0;
    int blue=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        lastAcceleration = SensorManager.GRAVITY_EARTH;

        Button redB= findViewById(R.id.button);
        Button greenB = findViewById(R.id.button2);
        Button blueB= findViewById(R.id.button3);

        redB.setBackgroundColor(Color.rgb(255,0,0));
        greenB.setBackgroundColor(Color.rgb(0,255,0));
        blueB.setBackgroundColor(Color.rgb(0,0,255));

        redB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                red=(red+32)%256;
                redB.setText(String.valueOf(red));
            }
        });

        greenB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                green=(green+32)%256;
                greenB.setText(String.valueOf(green));
            }
        });

        blueB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blue=(blue+32)%256;
                blueB.setText(String.valueOf(blue));
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if (mySensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];

            long curTime = System.currentTimeMillis();

            if ((curTime - lastUpdate) > 100) {
                long diffTime = (curTime - lastUpdate);
                lastUpdate = curTime;

                float speed = Math.abs(x + y + z - last_x - last_y - last_z) / diffTime * 10000;

                if (speed > g) {
                    ConstraintLayout layout = findViewById(R.id.layout);
                    layout.setBackgroundColor(Color.rgb(red, green, blue));
                }

                last_x = x;
                last_y = y;
                last_z = z;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Not used
    }
}
