package com.alantran.android.swipe;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alantran.android.swipe.objects.Classes;
import com.alantran.android.swipe.objects.Schedule;
import com.alantran.android.swipe.objects.Section;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
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
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        CreateSchedulesTask task = new CreateSchedulesTask();
        task.execute();
    }

    public class CreateSchedulesTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            selectedClasses = getIntent().getParcelableArrayListExtra("classes");
            getIntent().getParcelableArrayListExtra("classes");
            schedules = new Schedule(selectedClasses);
            return "";
        }

        @Override
        protected void onPostExecute(String str) {
            RecyclerView recyclerView = (RecyclerView)findViewById(R.id.all_schedules_recycler_view);
            recyclerView.setAdapter(new SchedulesRecyclerViewAdapter());
            findViewById(R.id.loadingPanel).setVisibility(View.GONE);
        }

        @Override
        protected void onPreExecute() {
            findViewById(R.id.loadingPanel).setVisibility(View.VISIBLE);
        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }
    }

    // Start email app to send schedule details
    // Will start the email app with the schedule details filled out
    private void sendEmail(HashMap<String, ArrayList<Section>> schedule) {
        StringBuilder s = new StringBuilder();
        s = new StringBuilder();

        ArrayList<String> keys = new ArrayList<String>();
        for (String str : schedule.keySet()) {
            keys.add(str);
        }
        Collections.sort(keys);

        for (String sectionKey : keys) {
            ArrayList<Section> currentSections = schedule.get(sectionKey);
            Collections.sort(currentSections, new Comparator<Section>() {
                @Override
                public int compare(Section lhs, Section rhs) {
                    return lhs.getSectionId().compareTo(rhs.getSectionId());
                }
            });

            if (currentSections.size() > 0) {
                Section firstSection = currentSections.get(0);
                s.append(firstSection.getSectionId() + ": " + firstSection.getInstructor() + "\n");
            }
            for (Section sec : currentSections) {
                s.append(sec.getClasstype() + ": " + sec.getBuilding() + " " + sec.getRoom() + " "
                        + sec.getDays() + " " + sec.getStartTime() + " - " + sec.getEndTime() + "\n");
            }
            s.append("Open Seats: " + currentSections.get(0).getOpenSeats() + "\n\n");
        }

        String today = new SimpleDateFormat("MM/dd/yyyy").format(new Date(System.currentTimeMillis()));
        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        //emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"test@email.com"});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "UMD Schedule " + today);
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
            HashMap<String, ArrayList<Section>> schedule = schedules.getSchedules().get(position);

            StringBuilder s = new StringBuilder();

            int totalCredits = 0;
            boolean waitlist = false;
            boolean walkingDistance = false; // One day... this shall be implemented!
            boolean incomplete = false; // This too!
            for (Classes c : selectedClasses) {
                totalCredits += c.getCredits();
            }

            for (String key : schedule.keySet()) {
                ArrayList<Section> sections = schedule.get(key);
                for (Section section : sections) {
                    if (Integer.parseInt(section.getOpenSeats()) == 0) {
                        waitlist = true;
                    }
                }
            }

            if (waitlist) {
                s.append("<font color='#EE0000'>*Some sections are waitlisted!</font><br>");
            }
            if (walkingDistance) {
                s.append("<font color='#EE0000'>*Make sure there is enough time to walk between classes!</font><br>");
            }
            if (incomplete) {
                s.append("<font color='#EE0000'>*Incomplete schedule!</font><br>");
            }

            s.append("Total Credits: " + totalCredits + "<br>");
            for (Map.Entry<String, ArrayList<Section>> entry : schedule.entrySet()) {
                ArrayList<Section> sections = entry.getValue();
                for (Section section : sections) {
                    if (section.getClasstype().length() == 0) {
                        s.append(section.getSectionId() + " (Lecture): ");
                        s.append(section.getDays() + " " + section.getStartTime() + " - " + section.getEndTime() + "<br>");
                    } else {
                        s.append(section.getSectionId() + " (" + section.getClasstype() + "): ");
                        s.append(section.getDays() + " " + section.getStartTime() + " - " + section.getEndTime() + "<br>");
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

            holder.getDescriptionTextView().setText(Html.fromHtml(s.toString()));

            // Print out more details
            s = new StringBuilder();

            ArrayList<String> keys = new ArrayList<String>();
            for (String str : schedule.keySet()) {
                keys.add(str);
            }
            Collections.sort(keys);

            for (String sectionKey : keys) {
                ArrayList<Section> currentSections = schedule.get(sectionKey);
                Collections.sort(currentSections, new Comparator<Section>() {
                    @Override
                    public int compare(Section lhs, Section rhs) {
                        return lhs.getSectionId().compareTo(rhs.getSectionId());
                    }
                });

                if (currentSections.size() > 0) {
                    Section firstSection = currentSections.get(0);
                    s.append("<b>" + firstSection.getSectionId() + "</b>: " + firstSection.getInstructor() + "<br>");
                }
                for (Section sec : currentSections) {
                    s.append("<b>" + sec.getClasstype() + "</b>: " + sec.getBuilding() + " " + sec.getRoom() + " "
                            + sec.getDays() + " " + sec.getStartTime() + " - " + sec.getEndTime() + "<br>");
                }
                s.append("<b>Open Seats:</b> " + currentSections.get(0).getOpenSeats() + "<br><br>");
            }
            holder.getExpandTextView().setText(Html.fromHtml(s.toString()));

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
