package com.empire.vince.crucom.settings;

import android.app.Activity;
import android.preference.Preference;

/**
 * Created by VinceGee on 2/6/2016.
 */
public class ThemeChangeListener implements Preference.OnPreferenceChangeListener {
    private final Activity mActivity;

    public ThemeChangeListener(Activity activity) {
        mActivity = activity;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        mActivity.recreate();

        return true;
    }
}
