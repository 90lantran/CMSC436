package com.alantran.android.swipe;

import android.content.Intent;
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
import android.widget.Button;
import android.widget.TextView;


import com.alantran.android.swipe.objects.*;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
        getIntent().getParcelableArrayListExtra("classes");
        schedules = new Schedule(selectedClasses);

        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.all_schedules_recycler_view);
        recyclerView.setAdapter(new SchedulesRecyclerViewAdapter());
    }

    // Start email app to send schedule details
    // Will start the email app with the schedule details filled out
    private void sendEmail(HashMap<String, ArrayList<Section>> schedule) {
        StringBuilder s = new StringBuilder();
        for (Map.Entry<String, ArrayList<Section>> entry : schedule.entrySet()) {
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

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"test@email.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Subject");
        emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, s.toString());
        startActivity(Intent.createChooser(emailIntent, "Send mail..."));
    }

    public class SchedulesRecyclerViewAdapter extends RecyclerView.Adapter<SchedulesRecyclerViewAdapter.ViewHolder> {
        List<Boolean> expandableItem;
        public SchedulesRecyclerViewAdapter(){
            expandableItem = new ArrayList<Boolean>(schedules.getSchedules().size());
            for (int i = 0; i < schedules.getSchedules().size(); i++) {
                expandableItem.add(false);
            }
        }

        @Override
        public SchedulesRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.created_schedules_recycler_view, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SchedulesRecyclerViewAdapter.ViewHolder holder, final int position) {
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

            // Schedule details expanded view
            final Button expandButton = holder.getExpandButton();
            holder.getExpandButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == expandButton.getId()) {
                        if (expandableItem.get(position)) {
                            holder.getExpandableLayout().setVisibility(View.GONE);

                            expandableItem.set(position, false);
                        } else {
                            holder.getExpandableLayout().setVisibility(View.VISIBLE);
                            expandableItem.set(position, true);
                        }
                    }
                }
            });

            // Send email button
            holder.getEmailButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == holder.getEmailButton().getId()) {
                        int id = position;
                        HashMap<String, ArrayList<Section>> sections = schedules.getSchedules().get(position);
                        CreatedSchedules.this.sendEmail(sections);
                    }
                }
            });

            holder.getDescriptionTextView().setText(s.toString());
            holder.getExpandTextView().setText("Test string");

        }

        @Override
        public int getItemCount() {
            return schedules == null ? 0 : schedules.getSchedules().size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView descriptionTextView;
            private Button expandButton;
            private Button emailButton;
            private ViewGroup expandableLayout;
            private TextView expandTextView;


            public ViewHolder(View itemView) {
                super(itemView);
                descriptionTextView = (TextView)itemView.findViewById(R.id.schedule_class_id);
                expandButton = (Button) itemView.findViewById(R.id.created_schedules_more_button);
                emailButton = (Button) itemView.findViewById(R.id.created_schedules_email_button);
                expandableLayout = (ViewGroup) itemView.findViewById(R.id.created_schedules_expandable_layout);
                expandTextView = (TextView) itemView.findViewById(R.id.created_schedules_expand_textview);
            }

            public TextView getDescriptionTextView() {
                return descriptionTextView;
            }

            public Button getExpandButton(){
                return expandButton;
            }

            public Button getEmailButton() {
                return emailButton;
            }

            public ViewGroup getExpandableLayout() {
                return expandableLayout;
            }

            public TextView getExpandTextView() {
                return expandTextView;
            }
        }
    }
}
