package com.alantran.android.swipe;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllClassesFragment extends Fragment {

    String LOG_TAG = AllClassesFragment.class.getSimpleName();

    OnPickButtonClick mCallback;

    ArrayList<Classes> classes = new ArrayList<>();
    Map<String, String> descriptions = new LinkedHashMap<>();// courseID -> description
    Map<String, String> classNames = new HashMap<>(); // courseID -> name
    Map<String, Classes> classToSection = new LinkedHashMap<>();//


    public AllClassesFragment() {
        // Required empty public constructor
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

        FetchClassesTask task = new FetchClassesTask();
        String major = "CMSC"; // default
        SharedPreferences settings = getActivity().getSharedPreferences("UserInfo", 0);
        if (!settings.getString("Major","").equals("CMSC")){
            major = settings.getString("Major", "").toString();
        }
        task.execute(major);
        return rootView;
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

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final Classes currentClass = classes.get(position);
            holder.getDescription().setText(currentClass.getDescription());
            holder.getName().setText(currentClass.getCourseID() + ": " + currentClass.getName());

            holder.getInstructor().setText("Instructor: " + currentClass.getInstructor());
            holder.getCredits().setText("Credits: " + currentClass.getCredit());
//            holder.getTime().setText("Time: " + currentClass.getDays()+ "  " + currentClass.getStartTime()+ " - " + currentClass.getEndTime());
//            Log.i(LOG_TAG,"Time: " + currentClass.getDays()+ "  " + currentClass.getStartTime()+ " - " + currentClass.getEndTime() );
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
            private TextView credits;
            //private TextView time;

            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.class_name);
                description = (TextView) itemView.findViewById(R.id.expand_textview);
                pickButton = (Button) itemView.findViewById(R.id.pick_button);
                expandButton = (Button) itemView.findViewById(R.id.expand_button);
                expandableLayout = (ViewGroup) itemView.findViewById(R.id.expandable_layout);
                instructor = (TextView) itemView.findViewById(R.id.instructor_textview);
                credits = (TextView) itemView.findViewById(R.id.credit_textview);
//                time = (TextView) itemView.findViewById(R.id.time_textview);
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

            public TextView getCredits() {
                return credits;
            }
//            public TextView getTime() {
//                return time;
//            }
        }
    }


    // First make API call to http://api.umd.io/v0/courses?dept_id=CMSC&per_page=100 to get all
    // course ID like CMSC100, name of class and description of class
    public class FetchClassesTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            if (params == null) return null;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String classJsonStr = null;
            try {
                String CLASS_BASE_URL = "http://api.umd.io/v0/courses?";
                String DEPTID = "dept_id";
                String PERPAGE = "per_page";
                Integer page = 100;

                Uri builtUri = Uri.parse(CLASS_BASE_URL).buildUpon()
                        .appendQueryParameter(DEPTID, params[0])
                        .appendQueryParameter(PERPAGE,page.toString())
                        .build();

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
                        Log.e(LOG_TAG, "Error closing stream", e);
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
            String CREDITS ="credits";

            JSONArray classArray = null;
            try {
                classArray = new JSONArray(classJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            for (int i = 0; i < classArray.length(); i++) {
                Log.e(LOG_TAG, "In populating maps");
                JSONObject klass = null;
                try {
                    klass = classArray.getJSONObject(i);
                    Classes currentClass = new Classes();
                    currentClass.setCourseID(klass.getString(COURSEID));
                    currentClass.setCredit(klass.getString(CREDITS));
                    currentClass.setName(klass.getString(NAME));
                    currentClass.setDescription(klass.getString(DESCRIPTION));
                    classes.add(currentClass);
                    //Log.e(LOG_TAG, classArray.getJSONObject(i).getString(DESCRIPTION) );
                    //descriptions.put(klass.getString(COURSEID), klass.getString(DESCRIPTION));
                    classNames.put(klass.getString(COURSEID), klass.getString(NAME));
                    classToSection.put(klass.getString(COURSEID), currentClass);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            // make API call here to get detail section like instrcutor, time and building
            Set<String> courseIDs = classNames.keySet();
            new  FetchSectionsTask().execute(courseIDs.toArray(new String[courseIDs.size()]));
        }

        // Asynctask for sections
        public class FetchSectionsTask extends AsyncTask<String, Void, Void> {
            StringBuilder sb = new StringBuilder();
            private void getSectionDataFromJson(String sectionJsonStr) throws JSONException {
                // convert time to sort by start time
                SimpleDateFormat sdf = new SimpleDateFormat("hh:mma");

                String COURSE = "course";
                String INSTRUCTOR = "instructors";
                String MEETINGS = "meetings";
                String DAYS = "days";
                String START_TIME = "start_time";
                String END_TIME = "end_time";
                String BUILDING = "building";
                String ROOM = "room";
                String CLASSTYPE = "classtype";
                String SECTION_ID = "section_id";

                JSONArray sectionArray = new JSONArray(sectionJsonStr);

                //Sections[] results = new Sections[sectionArray.length()*2];

                for (int i = 0; i < sectionArray.length(); i++) {

                    JSONObject section = sectionArray.getJSONObject(i);
                    JSONArray meetings = section.getJSONArray(MEETINGS);
                    Classes currentClass = classToSection.get(section.getString(COURSE));
                    for (int j = 0; j < meetings.length(); j++) {
                        Sections currentSection = new Sections();
                        currentSection.setSectionID(section.getString(SECTION_ID)); // CMSC132-0101
                        currentSection.setName(classNames.get(section.getString(COURSE)));
                        //currentClass.setDescription(descriptions.get(section.getString(COURSE)));
                        JSONArray instructorArr = section.getJSONArray(INSTRUCTOR);
                        for(int k = 0; k < instructorArr.length(); k++){
                            if (instructorArr.get(k).toString() == null){
                                currentClass.setInstructor("Unknown");
                                currentSection.setInstructor("Unknown");
                            }else {
                                currentClass.setInstructor(instructorArr.get(k).toString());
                                currentSection.setInstructor(instructorArr.get(k).toString());
                            }
                        }



                        JSONObject meeting = meetings.getJSONObject(j);

                        currentSection.setDays(meeting.getString(DAYS));
                        currentSection.setStartTime(meeting.getString(START_TIME));
                        currentSection.setEndTime(meeting.getString(END_TIME));
                        currentSection.setBuilding(meeting.getString(BUILDING));
                        currentSection.setRoom(meeting.getString(ROOM));

                        if (j == 0) currentSection.setClassType("Lecture");
                        else {
                            currentSection.setClassType(meeting.getString(CLASSTYPE));
                        }

                        try {
                            if (currentSection.getStartTime().length() != 0
                                    && currentSection.getEndTime().length() != 0) {
                                currentSection.setStartTimeSimple(sdf.parse(currentSection.getStartTime()));

                                currentSection.setEndTimeSimple(sdf.parse(currentSection.getEndTime()));

                                currentClass.getSectionsList().add(currentSection);
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }




                        //results[i] = currentSection;
                        // Log.i(LOG_TAG, currentClass.toString());

                    }


                }
                //return results;
            }

            @Override
            protected Void doInBackground(String... params) {
                String CLASS_BASE_URL = "http://api.umd.io/v0/courses/sections?";
                String COURSE = "course";
                String PERPAGE = "per_page";
                //List<Sections> list = new ArrayList<>();
                if (params == null) return null;

                for(int i = 0 ; i < params.length; i++) {
                    HttpURLConnection urlConnection = null;
                    BufferedReader reader = null;
                    String sectionJsonStr = null;
                    try {

                        Uri builtUri = Uri.parse(CLASS_BASE_URL).buildUpon()
                                .appendQueryParameter(COURSE, params[i])
                                .appendQueryParameter(PERPAGE, 50+"")
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
                        getSectionDataFromJson(buffer.toString());
                        //list.addAll(Arrays.asList(temp));

                    } catch (IOException e) {
                        Log.e(LOG_TAG, "Error", e);
                    } catch (JSONException e) {
                        Log.e(LOG_TAG, "Parsing", e);

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

                //Sections[] arr = list.toArray(new Sections[list.size()]);
                //return arr;
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                //super.onPostExecute(aVoid);
                RecyclerView view = (RecyclerView) getActivity().findViewById(R.id.allclasses_recycler_view);
                view.setAdapter(new ClassesRecyclerViewAdapter());
            }
//            @Override
//            protected void onPostExecute(Sections[] sections) {
//                Log.e(LOG_TAG + "classes to display", sections.length + "");
//                //classes.addAll(Arrays.asList(strings));
//                RecyclerView view = (RecyclerView) getActivity().findViewById(R.id.allclasses_recycler_view);
//                view.setAdapter(new ClassesRecyclerViewAdapter());
//
//            }
        }

    }
}
