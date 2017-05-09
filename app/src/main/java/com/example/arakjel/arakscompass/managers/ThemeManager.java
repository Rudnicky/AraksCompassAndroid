package com.example.arakjel.arakscompass.managers;

/**
 * Created by Pawel on 2017-03-12.
 */
public class ThemeManager {

    private static ThemeManager mInstance = null;

    public boolean isDayThemeApplied;

    private ThemeManager() {

    }

    public static synchronized ThemeManager getInstance() {
        if (mInstance == null)
            mInstance = new ThemeManager();

        return mInstance;
    }
}
