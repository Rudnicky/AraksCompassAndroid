package com.example.arakjel.arakscompass.managers;

/**
 * Created by Pawel on 2017-03-17.
 */

public class SettingsManager {
    private static SettingsManager mInstance = null;

    public boolean isTrueHeadingVisible;
    public boolean isMagneticFieldVisible;
    public boolean isLongitudeVisible;
    public boolean isLatitudeVisible;

    private SettingsManager() {}

    public static synchronized SettingsManager getInstance() {
        if (mInstance == null)
            mInstance = new SettingsManager();
        return mInstance;
    }
}
