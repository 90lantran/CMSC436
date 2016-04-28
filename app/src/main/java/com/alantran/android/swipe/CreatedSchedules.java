package com.alantran.android.swipe;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.alantran.android.swipe.com.alantran.android.swipe.objects.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CreatedSchedules extends AppCompatActivity {
    ArrayList<Classes> selectedClasses;
    Schedule schedules;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_created_schedules);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, selectedClasses.get(0).toString(), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        selectedClasses = getIntent().getParcelableArrayListExtra("classes");
        schedules = new Schedule(selectedClasses);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.all_schedules_recycler_view);
        recyclerView.setAdapter(new SchedulesRecyclerViewAdapter());
    }

    public class SchedulesRecyclerViewAdapter extends RecyclerView.Adapter<SchedulesRecyclerViewAdapter.ViewHolder> {
        public SchedulesRecyclerViewAdapter(){

        }

        @Override
        public SchedulesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.created_schedules_recycler_view, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SchedulesRecyclerViewAdapter.ViewHolder holder, int position) {
            //Classes currentClass = selectedClasses.get(position);
            HashMap<String, ArrayList<Section>> schedule = schedules.getSchedules().get(position);

            StringBuilder s = new StringBuilder();
            for (Map.Entry<String, ArrayList<Section>> entry : schedule.entrySet()) {
                //s.append(entry.getKey() + ": ");

                ArrayList<Section> sections = entry.getValue();
                for (Section section : sections) {
                    if (section.getClasstype().length() == 0) {
                        s.append(section.getSectionId() + " (Lecture): ");
                        s.append(section.getBuilding() + " " + section.getStartTime() + " - " + section.getEndTime() + "\n");
                    } else {
                        s.append(section.getSectionId() + " (" + section.getClasstype() + "): ");
                        s.append(section.getBuilding() + " " + section.getStartTime() + " - " + section.getEndTime() + "\n");
                    }
                }
            }

            holder.courseId.setText(s.toString());
            //holder.location.setText(currentClass.getBuilding() + " " + currentClass.getRoom());
            //holder.time.setText(currentClass.getStartTime() + " " + currentClass.getEndTime());

        }

        @Override
        public int getItemCount() {
            return schedules == null ? 0 : schedules.getSchedules().size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView courseId;
            TextView time;
            TextView location;

            public ViewHolder(View itemView) {
                super(itemView);
                courseId = (TextView)itemView.findViewById(R.id.schdule_class_id);
                location = (TextView) itemView.findViewById(R.id.schedule_location_textview);
                time = (TextView) itemView.findViewById(R.id.schdule_time_textview);

            }

        }
    }
}
