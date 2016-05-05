package com.alantran.android.swipe;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class DisplaySchedules extends AppCompatActivity {
    private ArrayList<Sections> listClasses;
    private ArrayList<String> schedules;
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

        // listClasses = getIntent().getParcelableArrayListExtra("Schedule_list");
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.all_schedules_recycler_view);
        recyclerView.setAdapter(new SchedulesRecyclerViewAdapter());
//        for(Classes c : listClasses){
//            Log.i("in DisplaySchedules", c.toString());
//        }
    }

    private void displaySchedules(){

    }

    public class SchedulesRecyclerViewAdapter extends RecyclerView.Adapter<SchedulesRecyclerViewAdapter.ViewHolder> {

        public SchedulesRecyclerViewAdapter(){
                schedules = separateSchedules();
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
            String s = schedules.get(position);
            holder.courseId.setText(s);
//            holder.location.setText(currentClass.getBuilding() + " " + currentClass.getRoom());
//            holder.time.setText(currentClass.getStartTime() + " " + currentClass.getEndTime());

        }

        @Override
        public int getItemCount() {
            return schedules == null ? 0 : schedules.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            TextView courseId;
//            TextView time;
//            TextView location;

            public ViewHolder(View itemView) {
                super(itemView);
                courseId = (TextView)itemView.findViewById(R.id.schdule_class_id);
//                location = (TextView) itemView.findViewById(R.id.schedule_location_textview);
//                time = (TextView) itemView.findViewById(R.id.schdule_time_textview);

            }

        }
    }

    private ArrayList<String> separateSchedules(){
        ArrayList<String> schedules = new ArrayList<>();
       ArrayList<Integer> divider = new ArrayList<>();
//
//           for(int i = 0; i < listClasses.size() - 1; i++){
//               if(listClasses.get(i).getColor() != listClasses.get(i+1).getColor()){
//                   divider.add(i);
//               }
//           }
//
//        int start = 0;
//        int end;
//        for(Integer d : divider) {
//            end = d;
//            int i;
//            String s = "";
//            for (i = start; i <= end; i++) {
//               s +=  (listClasses.get(i).getCourseID() + " "+listClasses.get(i).getBuilding() + " "+listClasses.get(i).getRoom() +
//                        " "+ listClasses.get(i).getStartTime() + " "+listClasses.get(i).getEndTime() + "\n");
//            }
//            schedules.add(s);
//            start = i;
//
//        }
        return schedules;
    }




}
