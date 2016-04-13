package com.alantran.android.swipe;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity implements AllClassesFragment.OnDoneButtonClick {
    String LOG_TAG = MainActivity.class.getSimpleName();
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

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

        // Set the middle tab is the default tab
        mViewPager.setCurrentItem(1);

        // Try to tell android keeps 3 tabs alive all the time]
        // Seem like this did not work
        mViewPager.setOffscreenPageLimit(3);
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAddingItemToList(String description, String fragName) {
        Log.e(LOG_TAG, description);
        //DoneFragment f = (DoneFragment) mSectionsPagerAdapter.getItem(0);
//        if (f == null) Log.e(LOG_TAG, "F is NULL");
//        if (f.getArrayAdapter() == null) Log.e(LOG_TAG, "mClassAdapter is null");
//        f.onAddingItemToList(description);

        if (fragName.equals("remove")) {
            DoneFragment f = (DoneFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, 0);
            if (f == null) Log.e(LOG_TAG, "F is NULL");
            if (f.getArrayAdapter() == null) Log.e(LOG_TAG, "mClassAdapter is null");
            f.onAddingItemToList(description);
        } else {
            WantToTakeFragment f = (WantToTakeFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, 2);
            if (f == null) Log.e(LOG_TAG, "F is NULL");
            if (f.getArrayAdapter() == null) Log.e(LOG_TAG, "mClassAdapter is null");
            f.onAddingItemToList(description);
        }
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
                case 1:
                    fragment = new AllClassesFragment();


                    break;
                case 0:
                    fragment = new DoneFragment();



                    break;
                case 2:
                    fragment = new WantToTakeFragment();
                    break;
            }
            return fragment;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "REMOVE";
                case 1:
                    return "ALL CLASSES";
                case 2:
                    return "PICK";
            }
            return null;
        }


    }


}
