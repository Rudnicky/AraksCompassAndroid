package com.example.arakjel.arakscompass.managers;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arakjel.arakscompass.conversions.RoundNumber;


public class CompassManager implements SensorEventListener {

    private RoundNumber mRoundNumber;
    private SensorManager mSensorManager;
    private Sensor mAccelerometerSensor;
    private Sensor mMagneticFieldSensor;
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float mAzimuth = 0f;
    private float mCurrentAzimuth = 0;
    private double mMagneticFieldStrength;

    public ImageView compassImageView = null;
    public TextView magneticStrengthTextView = null;
    public TextView trueheadingTextView = null;


    public CompassManager(Context context) {
        mSensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        mAccelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mMagneticFieldSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        mRoundNumber = new RoundNumber();
    }

    public void start() {
        mSensorManager.registerListener(this, mAccelerometerSensor,
                SensorManager.SENSOR_DELAY_GAME);
        mSensorManager.registerListener(this, mMagneticFieldSensor,
                SensorManager.SENSOR_DELAY_GAME);
    }

    public void stop() {
        mSensorManager.unregisterListener(this);
    }

    private void adjustArrow() {
        if (compassImageView == null) {
            return;
        }

        Animation an = new RotateAnimation(-mCurrentAzimuth, -mAzimuth,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mCurrentAzimuth = mAzimuth;

        an.setDuration(500);
        an.setRepeatCount(0);
        an.setFillAfter(true);

        compassImageView.startAnimation(an);
        if (magneticStrengthTextView != null) {
            magneticStrengthTextView.setText("" + mRoundNumber.round(mMagneticFieldStrength, 1));
        }

        if (trueheadingTextView != null) {
            trueheadingTextView.setText("" + mRoundNumber.round(mCurrentAzimuth, 1));
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;

        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {

                mGravity[0] = alpha * mGravity[0] + (1 - alpha)
                        * event.values[0];
                mGravity[1] = alpha * mGravity[1] + (1 - alpha)
                        * event.values[1];
                mGravity[2] = alpha * mGravity[2] + (1 - alpha)
                        * event.values[2];
            }

            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

                mMagneticFieldStrength = Math.sqrt((event.values[0]*event.values[0])+
                        (event.values[1]*event.values[1])+
                        (event.values[2]*event.values[2]));

                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha)
                        * event.values[0];
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha)
                        * event.values[1];
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)
                        * event.values[2];
            }

            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity,
                    mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                mAzimuth = (float) Math.toDegrees(orientation[0]); // orientation
                mAzimuth = (mAzimuth + 360) % 360;
                adjustArrow();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}