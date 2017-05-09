package com.example.arakjel.arakscompass.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.example.arakjel.arakscompass.managers.SettingsManager;
import com.example.arakjel.arakscompass.R;


public class MainActivity extends AppCompatActivity {

    private static final int FINE_LOCATION_PERMISSION_REQUEST = 0;
    private SettingsManager mSettingsManager = SettingsManager.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        setDefaultOptions();
        getPermissionToUserLocation();
    }

    private void setDefaultOptions() {
        mSettingsManager.isLatitudeVisible = true;
        mSettingsManager.isLongitudeVisible = true;
        mSettingsManager.isMagneticFieldVisible = true;
        mSettingsManager.isTrueHeadingVisible = true;
    }

    private void getPermissionToUserLocation() {

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            // The permission is NOT already granted.
            // Check if the user has been asked about this permission already and denied
            // it. If so, we want to give more explanation about why the permission is needed.

            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {

                // Show our own UI to explain to the user why we need to read the contacts
                // before actually requesting the permission and showing the default UI

            }

            // Fire off an async request to actually get the permission
            // This will show the standard permission request dialog UI

            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    FINE_LOCATION_PERMISSION_REQUEST);

        } else {

            Intent intent = new Intent(getApplicationContext(), CompassActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case FINE_LOCATION_PERMISSION_REQUEST: {

                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Intent intent = new Intent(getApplicationContext(), CompassActivity.class);
                    startActivity(intent);

                } else {

                    this.finishAffinity();
                }
                return;
            }
        }
    }
}
