package com.alantran.android.swipe;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.util.StringBuilderPrinter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.alantran.android.swipe.objects.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AllClassesFragment extends Fragment {
    String LOG_TAG = AllClassesFragment.class.getSimpleName();
    OnPickButtonClick mCallback;
    ArrayList<Classes> classes = new ArrayList<>();
    Map<String, String> descriptions = new LinkedHashMap<>();
    Map<String, String> classNames = new HashMap<>();
    Map<String, Integer> classCredits = new HashMap<>();
    Map<String, String> classCore = new HashMap<>();
    Map<String, String> classGenEd = new HashMap<>();
    String query;

    public AllClassesFragment() {

    }

    public interface OnPickButtonClick {
        public void onAddingItemToList(Classes currentClass, String fragName);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnPickButtonClick) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_display_classes, container, false);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        String q = preferences.getString("query", "");
        if(!q.equalsIgnoreCase("") && (q.length() == 4 || q.length() == 7 || q.length() == 8)) {
            query = q;
        } else {
            query = ((MainActivity) getActivity()).getQuery();
        }
        if (savedInstanceState != null) {
            query = savedInstanceState.getString("query");
        }
        FetchClassesTask task = new FetchClassesTask();
        task.execute(query);
        return rootView;
    }

    @Override
    public void onPause() {
        super.onPause();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("query", query);
        editor.apply();
    }

    // User typed something into the search bar, time to reload the data
    public void resetData() {
        query = ((MainActivity) getActivity()).getQuery();
        classes.clear();
        descriptions.clear();
        classNames.clear();
        classCredits.clear();
        classCore.clear();
        classGenEd.clear();
        FetchClassesTask task = new FetchClassesTask();
        task.execute(query);
        RecyclerView v = (RecyclerView) getActivity().findViewById(R.id.allclasses_recycler_view);
        v.getAdapter().notifyDataSetChanged();
    }

    public class ClassesRecyclerViewAdapter extends RecyclerView.Adapter<ClassesRecyclerViewAdapter.ViewHolder> {
        List<Boolean> expandableItem;

        public ClassesRecyclerViewAdapter() {
            expandableItem = new ArrayList<>(classes.size());
            for (int i = 0; i < classes.size(); i++) {
                expandableItem.add(false);
            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.allclasses_recycler_view_item, parent, false);

            return new ViewHolder(view);
        }

        private String genEd(String str) {
            switch (str) {
                case "FSAW":
                    break;
            }
            return null;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Classes currentClass = classes.get(position);
            holder.getName().setText(currentClass.getCourseID() + ": " + currentClass.getName());

            // Print out instructors and credits
            StringBuilder s = new StringBuilder();
            s.append("Instructor(s): " + currentClass.getInstructor());
            s.append("\nCredits: " + currentClass.getCredits());

            try {
                JSONArray core = new JSONArray(currentClass.getCore());
                s.append("\nCore: ");
                for (int i = 0; i < core.length() - 1; i++) {
                    s.append(core.get(i) + ", ");
                }
                s.append(core.get(core.length() - 1));
            } catch (Exception e) {
                // do nothing
            }
            try {
                JSONArray genEd = new JSONArray(currentClass.getGenEd());
                s.append("\nGen Ed: ");
                for (int i = 0; i < genEd.length() - 1; i++) {
                    s.append(genEd.get(i) + ", ");
                }
                s.append(genEd.get(genEd.length() - 1));
            } catch (Exception e) {
                // do nothing
            }
            holder.getInstructor().setText(s.toString());

            // Print out description
            String description = currentClass.getDescription();
            description = description.replaceAll("\\.(\\D)", ".\n$1");
            description = description.replaceAll("\\s\\s", "\n");
            holder.getDetails().setText(description);

            // Print out section data
            s = new StringBuilder();
            HashMap<String, ArrayList<Section>> sections = currentClass.getSections();

            ArrayList<String> keys = new ArrayList<String>();
            for (String str : sections.keySet()) {
                keys.add(str);
            }
            Collections.sort(keys);

            for (String sectionKey : keys) {
                ArrayList<Section> currentSections = sections.get(sectionKey);
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

            holder.getDescription().setText(Html.fromHtml(s.toString()));

            holder.getPickButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onAddingItemToList(currentClass, "pick");
                }
            });
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
        }

        @Override
        public int getItemCount() {
            return classes == null ? 0 : classes.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView name;
            private TextView description;
            private Button pickButton;
            private Button expandButton;
            private ViewGroup expandableLayout;
            private TextView instructor;
            private TextView details;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.class_name);
                description = (TextView) itemView.findViewById(R.id.expand_textview);
                pickButton = (Button) itemView.findViewById(R.id.pick_button);
                expandButton = (Button) itemView.findViewById(R.id.expand_button);
                expandableLayout = (ViewGroup) itemView.findViewById(R.id.expandable_layout);
                instructor = (TextView) itemView.findViewById(R.id.instructor_textview);
                details = (TextView) itemView.findViewById(R.id.details_textview);
            }

            public TextView getName() {
                return name;
            }

            public TextView getDescription() {
                return description;
            }

            public Button getPickButton() {
                return pickButton;
            }

            public Button getExpandButton() {
                return expandButton;
            }

            public ViewGroup getExpandableLayout() {
                return expandableLayout;
            }

            public TextView getInstructor() {
                return instructor;
            }

            public TextView getDetails() {
                return details;
            }
        }
    }

    public class FetchClassesTask extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... params) {

            if (params == null) return null;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String classJsonStr = null;
            try {
                String CLASS_BASE_URL = "http://api.umd.io/v0/courses?";
                String SINGLE_CLASS_BASE_URL = "http://api.umd.io/v0/courses/";
                String DEPTID = "dept_id";
                String PERPAGE = "per_page";
                Integer page = 100;

                Uri builtUri = Uri.parse(CLASS_BASE_URL).buildUpon()
                        .appendQueryParameter(DEPTID, params[0])
                        .appendQueryParameter(PERPAGE, page.toString())
                        .build();
                if (params[0].length() > 4) {
                    builtUri = Uri.parse(SINGLE_CLASS_BASE_URL).buildUpon()
                            .appendPath(params[0])
                            .build();
                }

                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                classJsonStr = buffer.toString();

            } catch (IOException e) {
                Log.e(LOG_TAG, "Error", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                    }
                }
            }
            return classJsonStr;

        }

        @Override
        protected void onPostExecute(String classJsonStr) {
            String COURSEID = "course_id";
            String NAME = "name";
            String DESCRIPTION = "description";
            String CREDITS = "credits";
            String CORE = "core";
            String GENED = "gen_ed";

            JSONArray classArray = null;
            try {
                classArray = new JSONArray(classJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e) {
                // User searched for non-existent class; do nothing
                return;
            }

            if (classArray == null) {
                JSONObject klass = null;
                try {
                    klass = new JSONObject(classJsonStr);
                    descriptions.put(klass.getString(COURSEID), klass.getString(DESCRIPTION));
                    classNames.put(klass.getString(COURSEID), klass.getString(NAME));
                    classCredits.put(klass.getString(COURSEID), klass.getInt(CREDITS));
                    classCore.put(klass.getString(COURSEID), klass.getString(CORE));
                    classGenEd.put(klass.getString(COURSEID), klass.getString(GENED));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                for (int i = 0; i < classArray.length(); i++) {
                    Log.e(LOG_TAG, "In populating maps");
                    JSONObject klass = null;
                    try {
                        klass = classArray.getJSONObject(i);
                        //Log.e(LOG_TAG, classArray.getJSONObject(i).getString(DESCRIPTION));
                        descriptions.put(klass.getString(COURSEID), klass.getString(DESCRIPTION));
                        classNames.put(klass.getString(COURSEID), klass.getString(NAME));
                        classCredits.put(klass.getString(COURSEID), klass.getInt(CREDITS));
                        classCore.put(klass.getString(COURSEID), klass.getString(CORE));
                        classGenEd.put(klass.getString(COURSEID), klass.getString(GENED));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            Set<String> temp = descriptions.keySet();
            new FetchSectionsTask().execute( descriptions.keySet().toArray(new String[descriptions.keySet().size()]));
        }

        public class FetchSectionsTask extends AsyncTask<String, Void, Classes[]> {
            StringBuilder sb = new StringBuilder();

            private void getClassDataFromJson(String sectionJsonStr, Classes currentClass) throws JSONException {
                String NUMBER = "number";
                String COURSE = "course";
                String INSTRUCTOR = "instructors";
                String MEETINGS = "meetings";
                String DAYS = "days";
                String START_TIME = "start_time";
                String END_TIME = "end_time";
                String BUILDING = "building";
                String ROOM = "room";
                String CLASS_TYPE = "classtype";
                String SECTION_ID = "section_id";
                String OPEN_SEATS = "open_seats";

                JSONArray sectionArray = new JSONArray(sectionJsonStr);

                for (int i = 0; i < sectionArray.length(); i++) { // For each section
                    JSONObject section = sectionArray.getJSONObject(i);
                    JSONArray meetings = section.getJSONArray(MEETINGS); // Need to find how many section objects we need

                    // TODO: Make sure this thing actually works. Are there classes without sections or other missing data?
                    for (int j = 0; j < meetings.length(); j++) {
                        Section currentSection = new Section();
                        currentSection.setSectionId(section.getString(SECTION_ID)); // CMSC132-0101
                        currentSection.setName(classNames.get(section.getString(COURSE)));
                        currentSection.setDescription(descriptions.get(section.getString(COURSE)));

                        JSONArray instructorArr = section.getJSONArray(INSTRUCTOR);
                        if (instructorArr.length() >= 1) {
                            currentSection.setInstructor(instructorArr.get(0).toString()); // TODO: what if there are many or no instructors?
                        }
                        JSONObject meeting = meetings.getJSONObject(j);
                        currentSection.setDays(meeting.getString(DAYS));

                        currentSection.setStartTime(meeting.getString(START_TIME));
                        currentSection.setEndTime(meeting.getString(END_TIME));
                        currentSection.setBuilding(meeting.getString(BUILDING));
                        currentSection.setRoom(meeting.getString(ROOM));
                        currentSection.setClasstype(meeting.getString(CLASS_TYPE));
                        currentSection.setOpenSeats(section.getString(OPEN_SEATS));
                        currentClass.addSection(section.getString(SECTION_ID), currentSection);
                        //Log.i(LOG_TAG, currentSection.toString());
                    }
                }
            }

            @Override
            protected Classes[] doInBackground(String... params) {
                String CLASS_BASE_URL = "http://api.umd.io/v0/courses/sections?";
                String COURSE = "course";
                List<Classes> list = new ArrayList<>();
                if (params == null) return null;

                for(int i = 0 ; i < params.length; i++) {
                    HttpURLConnection urlConnection = null;
                    BufferedReader reader = null;
                    String sectionJsonStr = null;
                    try {
                        Uri builtUri = Uri.parse(CLASS_BASE_URL).buildUpon()
                                .appendQueryParameter(COURSE, params[i])
                                .build();

                        URL url = new URL(builtUri.toString());
                        urlConnection = (HttpURLConnection) url.openConnection();
                        urlConnection.setRequestMethod("GET");
                        urlConnection.connect();

                        // Read the input stream into String
                        InputStream inputStream = urlConnection.getInputStream();

                        if (inputStream == null) {
                            return null;
                        }
                        reader = new BufferedReader(new InputStreamReader(inputStream));
                        StringBuffer buffer = new StringBuffer();
                        String line;

                        while ((line = reader.readLine()) != null && line.length() != 0) {
                            buffer.append(line );
                        }

                        if (buffer.length() == 0) {
                            return null;
                        }

                        // Build the new class object
                        Classes newClass = new Classes();
                        newClass.setCourseID(params[i]);
                        newClass.setDescription(descriptions.get(params[i]));
                        newClass.setName(classNames.get(params[i]));
                        newClass.setCredits(classCredits.get(params[i]));
                        newClass.setCore(classCore.get(params[i]));
                        newClass.setGenEd(classGenEd.get(params[i]));

                        getClassDataFromJson(buffer.toString(), newClass);
                        list.add(newClass);

                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error", e);
                    } catch (JSONException e) {
                        Log.e(LOG_TAG, "Error parsing", e);
                    } finally {
                        if (urlConnection != null) {
                            urlConnection.disconnect();
                        }
                        if (reader != null) {
                            try {
                                reader.close();
                            } catch (final IOException e) {
                                Log.e(LOG_TAG, "Error closing stream", e);
                            }
                        }
                    }
                }

                Classes[] arr = list.toArray(new Classes[list.size()]);
                return arr;
            }

            @Override
            protected void onPostExecute(Classes[] strings) {
                Log.e(LOG_TAG + "classes to display" , strings.length + "");
                classes.addAll(Arrays.asList(strings));
                RecyclerView view = (RecyclerView) getActivity().findViewById(R.id.allclasses_recycler_view);
                view.setAdapter(new ClassesRecyclerViewAdapter());

            }
        }
    }
}