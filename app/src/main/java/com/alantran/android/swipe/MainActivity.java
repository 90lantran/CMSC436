package com.alantran.android.swipe;

import android.app.SearchManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.alantran.android.swipe.objects.Classes;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AllClassesFragment.OnPickButtonClick {
    String LOG_TAG = MainActivity.class.getSimpleName();
    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ViewPager mViewPager;
    private String query = "CMSC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handleIntent(getIntent());

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createSchedules(view);
            }
        });

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(mViewPager);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            this.query = query;

            AllClassesFragment f = (AllClassesFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, 0);
            f.resetData();
        }
    }

    public String getQuery() {
        return query;
    }

    private void createSchedules(View view) {
        Intent buildSchedules = new Intent(getBaseContext(), CreatedSchedules.class);
        WantToTakeFragment f = (WantToTakeFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, 1);

        ArrayList<Classes> classes = new ArrayList<Classes>(); // list of classes to send over
        // This feels like an extremely ugly get around
        // Get each class from the adapter and put it into this arraylist
        for (Classes c : f.getSelectedClasses()) {
            classes.add(c);
        }

        buildSchedules.putParcelableArrayListExtra("classes", classes);
        startActivity(buildSchedules);
        //Snackbar.make(view, f.getArrayAdapter().getItem(0), Snackbar.LENGTH_LONG)
                //.setAction("Action", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(new ComponentName(getApplicationContext(), MainActivity.class)));

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
    public void onAddingItemToList(Classes currentClass, String fragName) {
        Log.e(LOG_TAG, currentClass.toString());
        if (fragName.equals("pick")) {
            WantToTakeFragment f = (WantToTakeFragment) mSectionsPagerAdapter.instantiateItem(mViewPager, 1);
            if (f == null) Log.e(LOG_TAG, "F is NULL");
            if (f.getArrayAdapter() == null) Log.e(LOG_TAG, "mClassAdapter is null");
            if (f.onAddingItemToList(currentClass)){
                // Make a toast
                CharSequence text = "Added " + currentClass.getCourseID();

                Toast toast = Toast.makeText(getApplicationContext(),text, Toast.LENGTH_SHORT);
                toast.show();
            } else {
                CharSequence text = currentClass.getCourseID() + " already added!";

                Toast toast = Toast.makeText(getApplicationContext(),text, Toast.LENGTH_SHORT);
                toast.show();
            }
        }
    }

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
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "ALL CLASSES";
                case 1:
                    return "PICKED CLASSES";
            }
            return null;
        }
    }
}
