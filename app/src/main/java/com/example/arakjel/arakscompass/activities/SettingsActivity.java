package com.example.arakjel.arakscompass.activities;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.arakjel.arakscompass.managers.SettingsManager;
import com.example.arakjel.arakscompass.managers.ThemeManager;
import com.example.arakjel.arakscompass.R;
import com.squareup.picasso.Picasso;

public class SettingsActivity extends AppCompatActivity {

    //region Variables
    // singleton vars
    private ThemeManager mThemeManager = ThemeManager.getInstance();
    private SettingsManager mSettingsManager = SettingsManager .getInstance();

    // ui vars
    private ImageView mBackImageView;
    private ImageView mBackgroundImageView;
    private CheckBox mTrueHeadingCheckBox;
    private CheckBox mMagneticFieldCheckBox;
    private CheckBox mLongitudeCheckBox;
    private CheckBox mLatitudeCheckBox;
    private TextView mTrueHeadingTextView;
    private TextView mMagneticFieldTextView;
    private TextView mLongitudeTextView;
    private TextView mLatitudeTextView;
    //endregion

    //region Ctor
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_settings);

        initialize();
        setDefaultOptions();
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

        mTrueHeadingCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTrueHeadingCheckBox.isChecked()) {
                    mSettingsManager.isTrueHeadingVisible = true;
                } else {
                    mSettingsManager.isTrueHeadingVisible = false;
                }
            }
        });

        mMagneticFieldCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mMagneticFieldCheckBox.isChecked()) {
                    mSettingsManager.isMagneticFieldVisible = true;
                } else {
                    mSettingsManager.isMagneticFieldVisible = false;
                }
            }
        });

        mLongitudeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLongitudeCheckBox.isChecked()) {
                    mSettingsManager.isLongitudeVisible = true;
                } else {
                    mSettingsManager.isLongitudeVisible = false;
                }
            }
        });

        mLatitudeCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mLatitudeCheckBox.isChecked()) {
                    mSettingsManager.isLatitudeVisible = true;
                } else {
                    mSettingsManager.isLatitudeVisible = false;
                }
            }
        });
    }
    //endregion

    //region Methods
    @Override
    protected void onResume() {
        super.onResume();
        setDefaultOptions();
    }

    private void setFullScreen() {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    private void checkTheme() {
        if (mThemeManager.isDayThemeApplied) {
            setDayTheme();
        } else {
            setNightTheme();
        }
    }

    private void initialize() {
        mBackImageView = (ImageView) findViewById(R.id.arrowImageView);
        mBackgroundImageView = (ImageView) findViewById(R.id.backgroundImageView);
        mTrueHeadingCheckBox = (CheckBox) findViewById(R.id.trueHeadingCheckBox);
        mMagneticFieldCheckBox = (CheckBox) findViewById(R.id.magneticFieldCheckBox);
        mLongitudeCheckBox = (CheckBox) findViewById(R.id.longitudeCheckBox);
        mLatitudeCheckBox = (CheckBox) findViewById(R.id.latitudeCheckBox);
        mTrueHeadingTextView = (TextView) findViewById(R.id.trueHeadingTextView);
        mMagneticFieldTextView = (TextView) findViewById(R.id.magneticFieldTextView);
        mLongitudeTextView = (TextView) findViewById(R.id.longitudeTextView);
        mLatitudeTextView = (TextView) findViewById(R.id.latitudeTextView);
    }



    private void setDefaultOptions() {
        setCheckBoxState(mSettingsManager.isTrueHeadingVisible, mTrueHeadingCheckBox);
        setCheckBoxState(mSettingsManager.isMagneticFieldVisible, mMagneticFieldCheckBox);
        setCheckBoxState(mSettingsManager.isLongitudeVisible, mLongitudeCheckBox);
        setCheckBoxState(mSettingsManager.isLatitudeVisible, mLatitudeCheckBox);
    }

    private void setCheckBoxState(boolean isOptionVisible, CheckBox checkBox) {
        if (isOptionVisible) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }
    }

    private void setDayTheme() {
        mThemeManager.isDayThemeApplied = true;
        Picasso.with(this).load(R.drawable.img_arrow_day).into(mBackImageView);
        Picasso.with(this).load(R.drawable.img_background_day).resize(400, 400).into(mBackgroundImageView);
        mTrueHeadingCheckBox.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_checkbox_day));
        mMagneticFieldCheckBox.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_checkbox_day));
        mLongitudeCheckBox.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_checkbox_day));
        mLatitudeCheckBox.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_checkbox_day));
        mTrueHeadingTextView.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        mMagneticFieldTextView.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        mLongitudeTextView.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
        mLatitudeTextView.setTextColor(ContextCompat.getColor(this, R.color.colorBlack));
    }

    private void setNightTheme() {
        mThemeManager.isDayThemeApplied = false;
        Picasso.with(this).load(R.drawable.img_arrow).into(mBackImageView);
        Picasso.with(this).load(R.drawable.img_background).resize(400, 400).into(mBackgroundImageView);
        mTrueHeadingCheckBox.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_checkbox));
        mMagneticFieldCheckBox.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_checkbox));
        mLongitudeCheckBox.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_checkbox));
        mLatitudeCheckBox.setBackground(ContextCompat.getDrawable(this, R.drawable.custom_checkbox));
        mTrueHeadingTextView.setTextColor(ContextCompat.getColor(this, R.color.colorSea));
        mMagneticFieldTextView.setTextColor(ContextCompat.getColor(this, R.color.colorSea));
        mLongitudeTextView.setTextColor(ContextCompat.getColor(this, R.color.colorSea));
        mLatitudeTextView.setTextColor(ContextCompat.getColor(this, R.color.colorSea));
    }
    //endregion
}
