package com.example.arakjel.arakscompass.activities;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arakjel.arakscompass.managers.ThemeManager;
import com.example.arakjel.arakscompass.R;
import com.squareup.picasso.Picasso;

public class CalibrationActivity extends AppCompatActivity {

    //region Variables
    // singleton var
    private ThemeManager mThemeManager = ThemeManager.getInstance();

    // ui vars
    private ImageView mBackImageView;
    private ImageView mBackgroundImageView;
    private ImageView mHowToCalibrateImageView;
    private ImageView mHowToCalibrateAxisImageView;
    private TextView mHeaderTextView;
    private TextView mExplenationTextView;
    //endregion

    //region Ctor
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_calibration);

        initialize();
        setListeners();
        checkTheme();
    }
    //endregion

    //region Listeners
    private void setListeners() {

        mBackImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    //endregion

    //region Methods
    private void checkTheme() {
        if (mThemeManager.isDayThemeApplied) {
            setDayTheme();
        } else {
            setNightTheme();
        }
    }

    private void setFullScreen() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void initialize() {
        mBackImageView = (ImageView) findViewById(R.id.arrowImageView);
        mBackgroundImageView = (ImageView) findViewById(R.id.backgroundImageView);
        mHowToCalibrateImageView = (ImageView) findViewById(R.id.howToCalibrateImageView);
        mHowToCalibrateAxisImageView = (ImageView) findViewById(R.id.howToCalibrateAxisImageView);
        mHeaderTextView = (TextView) findViewById(R.id.headertextView);
        mExplenationTextView = (TextView) findViewById(R.id.explenationTextView);
    }

    private void setDayTheme() {
        mThemeManager.isDayThemeApplied = true;

        Picasso.with(this).load(R.drawable.img_arrow_day).into(mBackImageView);
        Picasso.with(this).load(R.drawable.img_how_to_calibrate_day).into(mHowToCalibrateImageView);
        Picasso.with(this).load(R.drawable.img_rotate_axis_day).resize(200, 200).into(mHowToCalibrateAxisImageView);
        Picasso.with(this).load(R.drawable.img_background_day).resize(400, 400).into(mBackgroundImageView);

        mHeaderTextView.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        mExplenationTextView.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
    }

    private void setNightTheme() {
        mThemeManager.isDayThemeApplied = false;

        Picasso.with(this).load(R.drawable.img_arrow).into(mBackImageView);
        Picasso.with(this).load(R.drawable.img_how_to_calibrate).into(mHowToCalibrateImageView);
        Picasso.with(this).load(R.drawable.img_rotate_axis).resize(200, 200).into(mHowToCalibrateAxisImageView);
        Picasso.with(this).load(R.drawable.img_background).resize(400, 400).into(mBackgroundImageView);

        mHeaderTextView.setTextColor(ContextCompat.getColor(this, R.color.colorSea));
        mExplenationTextView.setTextColor(ContextCompat.getColor(this, R.color.colorSea));
    }
    //endregion
}
