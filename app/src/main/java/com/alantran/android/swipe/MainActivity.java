package com.alantran.android.swipe;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;

public class MainActivity extends AppCompatActivity implements AllClassesFragment.OnPickButtonClick {
    String LOG_TAG = MainActivity.class.getSimpleName();
    /**
     * The {@link PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);


        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();


        // retrieving data from preferences
        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        String email = getPrefs.getString("email", "N/A!");
        String major = getPrefs.getString("major", "N/A!");
        String minor = getPrefs.getString("minor", "N/A!");
        String upperlevel = getPrefs.getString("upperlevel", "N/A!");

//       if(email != "N/A!") {
//           Log.d("myApp", "Attained Email");
//       }else{
//           Log.d("myApp", "Did not retrieve Email");
//       }
//        if(major != "N/A!") {
//            Log.d("myApp", "Attained major");
//        }else{
//            Log.d("myApp", "Did not retrieve major");
//        }
//        if(minor != "N/A!") {
//            Log.d("myApp", "Attained minor");
//        }else{
//            Log.d("myApp", "Did not retrieve minor");
//        }
//        if(upperlevel != "N/A") {
//            Log.d("myApp", "Attained upperlevel");
//        }else{
//            Log.d("myApp", "Did not retrieve upperlevel");
//        }




    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //sets up preference page
        if (id == R.id.action_settings) {
            Intent p = new Intent("android.intent.action.SETTINGSACTIVITY");
            startActivity(p);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onAddingItemToList(Classes currentClass, String fragName) {
        Log.e(LOG_TAG, currentClass.toString());
        //DoneFragment f = (DoneFragment) mSectionsPagerAdapter.getItem(0);
//        if (f == null) Log.e(LOG_TAG, "F is NULL");
//        if (f.getArrayAdapter() == null) Log.e(LOG_TAG, "mClassAdapter is null");
//        f.onAddingItemToList(description);

        if (fragName.equals("remove")) {
            DoneFragment f = (DoneFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, 0);
            if (f == null) Log.e(LOG_TAG, "F is NULL");
            if (f.getArrayAdapter() == null) Log.e(LOG_TAG, "mClassAdapter is null");
            f.onAddingItemToList(currentClass.toString());
        } else {
            WantToTakeFragment f = (WantToTakeFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, 1);
            if (f == null) Log.e(LOG_TAG, "F is NULL");
            if (f.getArrayAdapter() == null) Log.e(LOG_TAG, "mClassAdapter is null");
            f.onAddingItemToList(currentClass);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.alantran.android.swipe/http/host/path")
        );
        AppIndex.AppIndexApi.start(client, viewAction);
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        Action viewAction = Action.newAction(
                Action.TYPE_VIEW, // TODO: choose an action type.
                "Main Page", // TODO: Define a title for the content shown.
                // TODO: If you have web page content that matches this app activity's content,
                // make sure this auto-generated web page URL is correct.
                // Otherwise, set the URL to null.
                Uri.parse("http://host/path"),
                // TODO: Make sure this auto-generated app deep link URI is correct.
                Uri.parse("android-app://com.alantran.android.swipe/http/host/path")
        );
        AppIndex.AppIndexApi.end(client, viewAction);
        client.disconnect();
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Fragment fragment = null;
            switch (position) {
                case 0:
                    fragment = new AllClassesFragment();
                    break;

                case 1:
                    fragment = new WantToTakeFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ALL CLASSES";
                case 1:
                    return "PICK";
            }
            return null;
        }
        }

    }


