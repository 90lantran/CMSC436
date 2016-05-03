package com.alantran.android.swipe;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by michaelmarbukh on 4/27/16.
 */
public class SettingsActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Display the fragment as the main content.
        getFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
    }

}