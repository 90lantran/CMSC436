package com.alantran.android.swipe;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class DisplaySchedules extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{



    private ArrayList<Schedule> listSchedules = new ArrayList<>();


    private void displaySchedules(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_schedules);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(
                DisplaySchedules.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


//        Schedule{sectionsList=[CMSC122-0101
//            Lecture: MWF 10:00am 10:50am
//            CSI 3117, color=0
//                    , CMSC250-0101
//            Lecture: TuTh 11:00am 12:15pm
//            CSI 1115, color=0
//                    , CMSC106-0101
//            Lecture: MWF 5:00pm 5:50pm
//            CSI 1121, color=0
//            ], num=1}
//       Schedule{sectionsList=[CMSC122-0101
//            Lecture: MWF 10:00am 10:50am
//            CSI 3117, color=0
//                    , CMSC250-0201
//            Lecture: TuTh 12:30pm 1:45pm
//            CSI 1115, color=0
//                    , CMSC106-0101
//            Lecture: MWF 5:00pm 5:50pm
//            CSI 1121, color=0
//            ], num=2}

        listSchedules = getIntent().getParcelableArrayListExtra("Schedule_list");
        if (listSchedules == null) {
            listSchedules = new ArrayList<>();
            Sections section1 = new Sections("CMSC122-0101", "Lecture", "MWF", "10:00am", "10:50am", "CSI", "3117");
            Sections section2 = new Sections("CMSC131-0101", "Lecture", "TuTh", "11:00am", "12:15pm", "CSI", "1115");
            Sections section3 = new Sections("CMSC106-0101", "Lecture", "MWF", "5:00pm", "5:50pm", "CSI", "1121");
            List<Sections> list1 = new ArrayList<>();
            list1.add(section1);
            list1.add(section2);
            list1.add(section3);
            Schedule s1 = new Schedule(1, list1);

            Sections section4 = new Sections("CMSC122-0101", "Lecture", "MWF", "10:00am", "10:50am", "CSI", "3117");
            Sections section5 = new Sections("CMSC131-0201", "Lecture", "TuTh", "12:30pm", "1:45pm", "CSI", "1115");
            Sections section6 = new Sections("CMSC106-0101", "Lecture", "MWF", "5:00pm", "5:50pm", "CSI", "1121");
            List<Sections> list2 = new ArrayList<>();
            list2.add(section4);
            list2.add(section5);
            list2.add(section6);
            Schedule s2 = new Schedule(2, list2);
            listSchedules.add(s1);
            listSchedules.add(s2);
        }

        //
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.all_schedules_recycler_view);
        recyclerView.setAdapter(new SchedulesRecyclerViewAdapter());
        for(Schedule c : listSchedules){
            Log.i("in DisplaySchedules", c.toString());
        }



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
            Intent intent = new Intent(this, UserInfo.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_user_info) {
            Intent intent = new Intent(DisplaySchedules.this,UserInfo.class);
            startActivity(intent);
        } else if (id == R.id.nav_create_new_schedule) {
            Intent intent = new Intent(DisplaySchedules.this,MainActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_recent_view_schedule) {
            Intent intent = new Intent(DisplaySchedules.this,DisplaySchedules.class);
            startActivity(intent);
        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class SchedulesRecyclerViewAdapter extends RecyclerView.Adapter<SchedulesRecyclerViewAdapter.ViewHolder> {

        public SchedulesRecyclerViewAdapter(){

        }

        @Override
        public SchedulesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.all_schedules_recycler_view_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SchedulesRecyclerViewAdapter.ViewHolder holder, int position) {
            //Classes currentClass = listClasses.get(position);
            Log.i("In onBindViewHolder","");
            Schedule s = listSchedules.get(position);
            List<Sections> listSections = s.getSectionsList();
            StringBuilder sb = new StringBuilder();
            for(Sections se : listSections) {
                sb.append(se.toString() + "\n");
            }
//            holder.location.setText(currentClass.getBuilding() + " " + currentClass.getRoom());
//            holder.time.setText(currentClass.getStartTime() + " " + currentClass.getEndTime());
            holder.sectionId.setText(sb.toString());
            holder.scheduleName.setText("Schedule " + s.getNum());

        }

        @Override
        public int getItemCount() {
            return listSchedules == null ? 0 : listSchedules.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView sectionId;
            TextView scheduleName;
//            TextView location;

            public ViewHolder(View itemView) {
                super(itemView);
                sectionId = (TextView)itemView.findViewById(R.id.schdule_class_id);
//                location = (TextView) itemView.findViewById(R.id.schedule_location_textview);
                scheduleName = (TextView) itemView.findViewById(R.id.schedule_name);

            }

        }
    }






}
