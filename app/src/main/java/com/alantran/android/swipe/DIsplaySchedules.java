

package com.alantran.android.swipe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class DisplaySchedules extends AppCompatActivity implements  NavigationView.OnNavigationItemSelectedListener{



    private ArrayList<Schedule> listSchedules = new ArrayList<>();
    private RecyclerView recyclerView;
    private ArrayList<String> message = new ArrayList<String>();

    private void displaySchedules(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_schedules);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(
                DisplaySchedules.this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_display);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = "Share schedule to you and your friends";
                Toast toast = Toast.makeText(getApplicationContext(),text, Toast.LENGTH_SHORT);
                toast.show();

                Intent intent = new Intent(getApplicationContext(), SendSchedule.class);
//                //EditText editText = (EditText) findViewById(R.id.edit_message);
//                //String message = editText.getText().toString();
                intent.putStringArrayListExtra("Message", message);
                //startActivity(intent);

                SharedPreferences settings = getApplication().getSharedPreferences("UserInfo", 0);

                String myEmail = settings.getString("Email", "").toString();

                Log.i("Send email", "");
                String[] TO = {myEmail};
                String[] CC = {""};
                Intent emailIntent = new Intent(Intent.ACTION_SEND);

                // emailIntent.setData(Uri.parse("mailto:90lantram@gmail.com"));
                emailIntent.setType("plain/text");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"90lantran@gmail.com"});
                //emailIntent.putExtra(Intent.EXTRA_CC, CC);
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My Schedule");
                emailIntent.putExtra(Intent.EXTRA_TEXT, message.get(0));
                //emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                emailIntent.setType("message/rfc822");
                try {
                    startActivity(Intent.createChooser(emailIntent, "Send mail..."));
                    finish();
                    Log.i("Sent email...", "");
                }
                catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(DisplaySchedules.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
                }


            }
        });


        listSchedules = getIntent().getParcelableArrayListExtra("Schedule_list");
        if (listSchedules == null) {

            // Call asynctask here to get arraylist<schedule> from db here
            // also set RecyclerView in onPostExecute()
            ScheduleDBHelper scheduleDBHelper = new ScheduleDBHelper(getApplicationContext());
            LoadScheduleTask task = new LoadScheduleTask();
            task.execute(scheduleDBHelper);



        } else {
            // drop the one in database
            // add all new schedule to DB
            recyclerView = (RecyclerView)findViewById(R.id.all_schedules_recycler_view);
            recyclerView.setAdapter(new SchedulesRecyclerViewAdapter());
            for(Schedule c : listSchedules){
                Log.i("onCreate()", c.toString());
            }

            ScheduleDBHelper scheduleDBHelper = new ScheduleDBHelper(getApplicationContext());
            InsertScheduleTask task = new InsertScheduleTask();
            task.execute(scheduleDBHelper);


        }

        //
//        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.all_schedules_recycler_view);
//        recyclerView.setAdapter(new SchedulesRecyclerViewAdapter());
//        for(Schedule c : listSchedules){
//            Log.i("in DisplaySchedules", c.toString());
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
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {

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
        public void onBindViewHolder(final SchedulesRecyclerViewAdapter.ViewHolder holder, int position) {
            //Classes currentClass = listClasses.get(position);
            Log.i("In onBindViewHolder","");
            Schedule s = listSchedules.get(position);
            List<Sections> listSections = s.getSectionsList();
            StringBuilder sb = new StringBuilder();
            for(Sections se : listSections) {
                sb.append(se.toString() );
            }
//            holder.location.setText(currentClass.getBuilding() + " " + currentClass.getRoom());
//            holder.time.setText(currentClass.getStartTime() + " " + currentClass.getEndTime());
            holder.sectionId.setText(sb.toString());
            holder.scheduleName.setText("Schedule " + s.getNum());

            final String m = holder.scheduleName.getText() + "\n" + holder.sectionId.getText() +"\n";
            holder.start.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (holder.start.getId() == R.id.start_off){
                        holder.start.setImageResource(R.drawable.star_on);
                        holder.start.setId(R.id.start_off + 20);
                        message.add(m);
                        Log.i("Adding message to send", m.toString());
                    } else {
                        holder.start.setImageResource(R.drawable.star_off);
                        holder.start.setId(R.id.start_off);
                        if (message.contains(m)) {
                            message.remove(m);
                            Log.i("Remove message to send", m.toString());
                        }
                    }
                }
            });

        }

        @Override
        public int getItemCount() {
            return listSchedules == null ? 0 : listSchedules.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView sectionId;
            TextView scheduleName;
            ImageView start;

            public ViewHolder(View itemView) {
                super(itemView);
                sectionId = (TextView)itemView.findViewById(R.id.schdule_class_id);
//                location = (TextView) itemView.findViewById(R.id.schedule_location_textview);
                scheduleName = (TextView) itemView.findViewById(R.id.schedule_name);
                start = (ImageView) itemView.findViewById(R.id.start_off);
            }

        }
    }



    private class LoadScheduleTask extends AsyncTask<ScheduleDBHelper, Void, ArrayList<Schedule>>{

        @Override
        protected ArrayList<Schedule> doInBackground(ScheduleDBHelper... params) {
            ScheduleDBHelper dbHelper = params[0];
            return dbHelper.getSchedules();
        }

        @Override
        protected void onPostExecute(ArrayList<Schedule> schedules) {
            //Log.i(" onPostExecute()", "set listSchedules to the on in database , row = " + schedules.size());
            if (schedules != null) {
                listSchedules = schedules;
            } else { // default
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
            recyclerView = (RecyclerView)findViewById(R.id.all_schedules_recycler_view);
            recyclerView.setAdapter(new SchedulesRecyclerViewAdapter());
            for(Schedule c : listSchedules){
                Log.i("in PostExecute()", c.toString());
            }
        }
    }

    private class InsertScheduleTask extends AsyncTask<ScheduleDBHelper, Void, Void> {

        @Override
        protected Void doInBackground(ScheduleDBHelper... params) {
            ScheduleDBHelper dbHelper = params[0];
            dbHelper.insertSchedules(listSchedules);

            return null;
        }
    }






}
