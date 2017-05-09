package com.example.arakjel.arakscompass.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.GeomagneticField;
import android.location.Location;
import android.os.Build;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.example.arakjel.arakscompass.controls.MenuControl;
import com.example.arakjel.arakscompass.conversions.ConvertCoordinates;
import com.example.arakjel.arakscompass.managers.CompassManager;
import com.example.arakjel.arakscompass.managers.GPSManager;
import com.example.arakjel.arakscompass.managers.SettingsManager;
import com.example.arakjel.arakscompass.managers.ThemeManager;
import com.example.arakjel.arakscompass.R;
import com.google.android.gms.common.ConnectionResult;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

public class CompassActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    //region Variables
    // const vars
    private final static int UPDATE_INTERVAL = 10000; // 10 sec
    private final static int FATEST_INTERVAL = 5000; // 5 sec
    private final static int DISPLACEMENT = 10; // 10 meters
    private long mLastClickTime = 0;

    // ui vars
    private ImageView mBackgroundImageView;
    private ImageView mCompassImageView;
    private TextView mLatitudeTextView;
    private TextView mLongitudeTextView;
    private TextView mLongitudeReadOnlyTextView;
    private TextView mLatitudeReadOnlyTextView;
    private TextView mMagneticStrengthTextView;
    private TextView mMagneticStrengthTextViewReadOnly;
    private TextView mTrueHeadingTextView;
    private TextView mTrueHeadingTextViewReadOnly;
    private TextClock mTextClock;
    private RelativeLayout mDataLayout;
    private RelativeLayout mGeoDataHolder;
    private MenuControl mMenuControl;
    private MenuControl mCustomMenuView;

    // primitive vars
    private boolean isDayThemeApplied;

    // gps vars
    private GPSManager mGpsManager;
    private CompassManager compassManager;
    private GeomagneticField geomagneticField;
    private GoogleApiClient mGoogleApiClient;
    private boolean mRequestingLocationUpdates = false;
    private LocationRequest mLocationRequest;
    private Location mLastLocation;

    // conversion var
    private ConvertCoordinates mConvertCoordinates;

    // custom toast vars
    private LayoutInflater inflater;
    private View layout;
    private ImageView image;
    private TextView text;
    private Toast toast;

    private ThemeManager mThemeManager = ThemeManager.getInstance();
    private SettingsManager mSettingsManager = SettingsManager.getInstance();
    //endregion

    //region Ctor
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_compass);

        initialize();
        buildGoogleApiClient();
        createLocationRequest();
        checkIFLocationIsAvailable();
        checkTheme();
    }
    //endregion

    private void checkTheme() {
        if (mThemeManager.isDayThemeApplied) {
            setDayTheme();
        } else {
            setNightTheme();
        }
    }

    private void checkOptions() {
        if (mSettingsManager.isTrueHeadingVisible) {
            mTrueHeadingTextView.setVisibility(View.VISIBLE);
            mTrueHeadingTextViewReadOnly.setVisibility(View.VISIBLE);
        } else {
            mTrueHeadingTextView.setVisibility(View.INVISIBLE);
            mTrueHeadingTextViewReadOnly.setVisibility(View.INVISIBLE);
        }

        if (mSettingsManager.isMagneticFieldVisible) {
            mMagneticStrengthTextView.setVisibility(View.VISIBLE);
            mMagneticStrengthTextViewReadOnly.setVisibility(View.VISIBLE);
        } else {
            mMagneticStrengthTextView.setVisibility(View.INVISIBLE);
            mMagneticStrengthTextViewReadOnly.setVisibility(View.INVISIBLE);
        }

        if (mSettingsManager.isLongitudeVisible) {
            mLongitudeTextView.setVisibility(View.VISIBLE);
            mLongitudeReadOnlyTextView.setVisibility(View.VISIBLE);
        } else {
            mLongitudeTextView.setVisibility(View.INVISIBLE);
            mLongitudeReadOnlyTextView.setVisibility(View.INVISIBLE);
        }

        if (mSettingsManager.isLatitudeVisible) {
            mLatitudeTextView.setVisibility(View.VISIBLE);
            mLatitudeReadOnlyTextView.setVisibility(View.VISIBLE);
        } else {
            mLatitudeTextView.setVisibility(View.INVISIBLE);
            mLatitudeReadOnlyTextView.setVisibility(View.INVISIBLE);
        }

        if (mSettingsManager.isLatitudeVisible || mSettingsManager.isLongitudeVisible) {
            mDataLayout.setVisibility(View.VISIBLE);
        } else if (!mSettingsManager.isLongitudeVisible && !mSettingsManager.isLongitudeVisible) {
            mDataLayout.setVisibility(View.INVISIBLE);
        }
    }

    private void checkIFLocationIsAvailable() {
        if (!mGpsManager.isGpsProviderEnabled()) {
            setToast("Gps's not available. Please turn on GPS");
        }
    }

    private void setFullScreen() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initialize() {
        mBackgroundImageView = (ImageView) findViewById(R.id.backgroundImageView);
        mCompassImageView = (ImageView) findViewById(R.id.compassImageView);
        mLatitudeTextView = (TextView) findViewById(R.id.latitudeTextView);
        mLongitudeTextView = (TextView) findViewById(R.id.longitudeTextView);
        mLatitudeReadOnlyTextView = (TextView) findViewById(R.id.latitudeReadOnlyTextView);
        mLongitudeReadOnlyTextView = (TextView) findViewById(R.id.longitudeReadOnlyTextView);
        mMagneticStrengthTextView = (TextView) findViewById(R.id.magneticStrengthTextView);
        mMagneticStrengthTextViewReadOnly = (TextView) findViewById(R.id.magneticStrengthTextViewReadOnly);
        mTrueHeadingTextView = (TextView) findViewById(R.id.trueHeadingTextView);
        mTrueHeadingTextViewReadOnly = (TextView) findViewById(R.id.trueHeadingTextViewReadOnly);
        mDataLayout = (RelativeLayout) findViewById(R.id.geoDataHolder);
        mTextClock = (TextClock) findViewById(R.id.textClock);
        mCustomMenuView = (MenuControl) findViewById(R.id.customMenuView);

        mMenuControl = new MenuControl(this);
        mGpsManager = new GPSManager(this);
        mConvertCoordinates = new ConvertCoordinates();
        compassManager = new CompassManager(this);
        isDayThemeApplied = false;
        compassManager.compassImageView = (ImageView) findViewById(R.id.compassImageView);
        compassManager.magneticStrengthTextView = (TextView) findViewById(R.id.magneticStrengthTextView);
        compassManager.trueheadingTextView = (TextView) findViewById(R.id.trueHeadingTextView);
        Picasso.with(this).load(R.drawable.img_compass).resize(1000, 1000).into(compassManager.compassImageView);

    }

    private void setToast(String textToDisplay) {

        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(R.drawable.img_gps);
        text = (TextView) layout.findViewById(R.id.text);
        text.setText(textToDisplay);

        toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    private void setDayToast(String textToDisplay) {

        inflater = getLayoutInflater();
        layout = inflater.inflate(R.layout.custom_toast_day,
                (ViewGroup) findViewById(R.id.toast_layout_root));

        image = (ImageView) layout.findViewById(R.id.image);
        image.setImageResource(R.drawable.img_gps_day);
        text = (TextView) layout.findViewById(R.id.text);
        text.setText(textToDisplay);

        toast = new Toast(getApplicationContext());
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.setDuration(Toast.LENGTH_LONG);
        toast.setView(layout);
        toast.show();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private void displayLocation() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED) {
                mLastLocation = LocationServices.FusedLocationApi
                        .getLastLocation(mGoogleApiClient);
                updateUI();
            }
        } else {
            mLastLocation = LocationServices.FusedLocationApi
                    .getLastLocation(mGoogleApiClient);
            updateUI();
        }
    }

    private void updateUI() {
        checkIFLocationIsAvailable();
        if (mLastLocation != null) {
            mLatitudeTextView.setText("" + mConvertCoordinates.convert(mLastLocation.getLatitude()));
            mLongitudeTextView.setText("" + mConvertCoordinates.convert(mLastLocation.getLongitude()));
            mMagneticStrengthTextView.setText("");
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        compassManager.start();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        compassManager.stop();
        stopLocationUpdates();
    }

    @Override
    protected void onResume() {
        super.onResume();
        compassManager.start();
        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
        }
        checkOptions();
    }

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FATEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setSmallestDisplacement(DISPLACEMENT); // 10 meters
    }

    protected void startLocationUpdates() {

        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                LocationServices.FusedLocationApi.requestLocationUpdates(
                        mGoogleApiClient, mLocationRequest, this);
            }
        } else {
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, mLocationRequest, this);
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        compassManager.stop();
    }

    @Override
    public void onBackPressed() {
        this.finishAffinity();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        setToast("Location has changed!");
        displayLocation();
    }

    /* menu items events */
    public void onCalibrateMenuItemClicked(View view) {
        Intent intent = new Intent(this, CalibrationActivity.class);
        startActivity(intent);
        mCustomMenuView.hamburgerMenuDrawable.toggle();
        mCustomMenuView.startAnimation();
    }

    public void onSunMoonMenuItemClicked(View view) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < 2000){
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        mCustomMenuView.hamburgerMenuDrawable.toggle();
        mCustomMenuView.startAnimation();

        if (!isDayThemeApplied) {
            setDayTheme();
            isDayThemeApplied = true;
        } else {
            setNightTheme();
            isDayThemeApplied = false;
        }
    }

    private void setDayTheme() {
        setDayToast("Theme has changed");
        mThemeManager.isDayThemeApplied = true;

        Picasso.with(this).load(R.drawable.img_background_day).resize(400, 400).into(mBackgroundImageView);
        Picasso.with(this).load(R.drawable.img_compass_day).resize(700, 700).into(compassManager.compassImageView);
        Picasso.with(this).load(R.drawable.img_calibrate_day).into(mCustomMenuView.item1);
        Picasso.with(this).load(R.drawable.img_moon).into(mCustomMenuView.item2);
        Picasso.with(this).load(R.drawable.img_settings_day).into(mCustomMenuView.item3);
        Picasso.with(this).load(R.drawable.img_question_day).into(mCustomMenuView.item4);


        mDataLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.background_border_day));
        mTextClock.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        mLongitudeTextView.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        mLatitudeTextView.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        mLongitudeReadOnlyTextView.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        mLatitudeReadOnlyTextView.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        mMagneticStrengthTextView.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        mMagneticStrengthTextViewReadOnly.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        mTrueHeadingTextViewReadOnly.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        mTrueHeadingTextView.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));

        mCustomMenuView.changeColor(ContextCompat.getColor(this, R.color.colorBlack));
    }

    private void setNightTheme() {

        setToast("Theme has changed");
        mThemeManager.isDayThemeApplied = false;

        Picasso.with(this).load(R.drawable.img_background).resize(400, 400).into(mBackgroundImageView);
        Picasso.with(this).load(R.drawable.img_compass).resize(700, 700).into(compassManager.compassImageView);
        Picasso.with(this).load(R.drawable.img_calibrate).into(mCustomMenuView.item1);
        Picasso.with(this).load(R.drawable.img_sun).into(mCustomMenuView.item2);
        Picasso.with(this).load(R.drawable.img_settings).into(mCustomMenuView.item3);
        Picasso.with(this).load(R.drawable.img_question).into(mCustomMenuView.item4);

        mDataLayout.setBackground(ContextCompat.getDrawable(this, R.drawable.background_border));
        mTextClock.setTextColor(ContextCompat.getColor(this, R.color.colorSea));
        mLongitudeTextView.setTextColor(ContextCompat.getColor(this, R.color.colorSea));
        mLatitudeTextView.setTextColor(ContextCompat.getColor(this, R.color.colorSea));
        mLongitudeReadOnlyTextView.setTextColor(ContextCompat.getColor(this, R.color.colorSea));
        mLatitudeReadOnlyTextView.setTextColor(ContextCompat.getColor(this, R.color.colorSea));
        mTrueHeadingTextView.setTextColor(ContextCompat.getColor(this, R.color.colorSea));
        mTrueHeadingTextViewReadOnly.setTextColor(ContextCompat.getColor(this, R.color.colorSea));
        mMagneticStrengthTextViewReadOnly.setTextColor(ContextCompat.getColor(this, R.color.colorSea));
        mMagneticStrengthTextView.setTextColor(ContextCompat.getColor(this, R.color.colorSea));

        mCustomMenuView.changeColor(ContextCompat.getColor(this, R.color.colorSea));
    }

    public void onSettingsMenuItemClicked(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
        mCustomMenuView.hamburgerMenuDrawable.toggle();
        mCustomMenuView.startAnimation();
    }

    public void onQuestionMenuItemClicked(View view) {
        Intent intent = new Intent(CompassActivity.this, AboutActivity.class);
        startActivity(intent);
        mCustomMenuView.hamburgerMenuDrawable.toggle();
        mCustomMenuView.startAnimation();

    }
}
