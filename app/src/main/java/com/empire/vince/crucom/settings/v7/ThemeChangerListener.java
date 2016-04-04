package com.empire.vince.crucom.settings.v7;

import android.app.Activity;
import android.support.v7.preference.Preference;


/**
 * Created by VinceGee on 2/6/2016.
 */
public class ThemeChangerListener implements Preference.OnPreferenceChangeListener {
    private final Activity mActivity;

    public ThemeChangerListener(Activity activity) {
        mActivity = activity;
    }

    @Override
    public boolean onPreferenceChange(Preference preference, Object newValue) {
        if (android.os.Build.VERSION.SDK_INT >= 11) {
            mActivity.recreate();
        } else {
            mActivity.startActivity(mActivity.getIntent());
            mActivity.finish();
        }

        return true;
    }
}

