package com.alantran.android.swipe;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by michaelmarbukh on 4/19/16.
 */
public  class SettingsFragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

    }

}