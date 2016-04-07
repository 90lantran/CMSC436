package com.alantran.android.swipe;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllClassesFragment extends Fragment {

    String LOG_TAG = AllClassesFragment.class.getSimpleName();

    CustomAdapter mClassAdapter;

    OnDoneButtonClick mCallback;

    public AllClassesFragment() {
        // Required empty public constructor
    }


    public interface OnDoneButtonClick {
        public void onAddingItemToList(String description);
    }

    @Override
    public void onAttach(Context activity) {
        super.onAttach(activity);
        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnDoneButtonClick) activity;
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
        String[] data = {
                "Mon 6/23 - Sunny - 31/17",
                "Tue 6/24 - Foggy - 21/8",
                "Wed 6/25 - Cloudy - 22/17",
                "Thurs 6/26 - Rainy - 18/11",
                "Fri 6/27 - Foggy - 21/10",
                "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
                "Sun 6/29 - Sunny - 20/7",
                "Mon 6/23 - Sunny - 31/17",
                "Tue 6/24 - Foggy - 21/8",
                "Wed 6/25 - Cloudy - 22/17"
        };
        FetchClassesTask task = new FetchClassesTask();
        task.execute("CMSC");

        List<String> classes = new ArrayList<String>(Arrays.asList(data));

        mClassAdapter = new CustomAdapter(getContext(), classes);

        ListView mListView = (ListView) rootView.findViewById(R.id.listview_class);


        mListView.setAdapter(mClassAdapter);


        return rootView;


    }

    public class CustomAdapter extends ArrayAdapter<String> {

        public CustomAdapter(Context context, List<String> listItemModel){
            super(context, 0, listItemModel);
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final String listItemModel = getItem(position);

            if(convertView == null){
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_class_1,parent,false);

            }

            final TextView description = (TextView) convertView.findViewById(R.id.list_item_class_textview);
            description.setText(listItemModel);
            Button removed = (Button) convertView.findViewById(R.id.remove_button);
            Button pick = (Button) convertView.findViewById(R.id.pick_button);
            final View finalConvertView = convertView;
            removed.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(finalConvertView, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    if (mCallback == null) Log.e(LOG_TAG, "mCallback is null");
                    mCallback.onAddingItemToList(listItemModel);

                }
            });

            pick.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Snackbar.make(finalConvertView, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                }
            });

            return convertView;
        }


    }



    public class FetchClassesTask extends AsyncTask<String, Void, String[]> {

        private String[] getClassDataFromJson(String classJsonStr)
                throws JSONException {
            String COURSEID = "course_id";
            String NAME = "name";
            String DEPARTMENT = "department";
            String SEMESTER = "semester";
            String CREDITS = "credits";
            String GRADINGMETHOD = "grading_method";
            String CORE = "core";
            String GENED = "gen_ed";
            String DESCRIPTION = "description";
            String RELATIONSHIPS = "relationships";
            String SECTIONS = "sections";


            JSONArray classArray = new JSONArray(classJsonStr);
            String[] results = new String[classArray.length()];

            for (int i = 0; i < classArray.length(); i++) {
                String name;
                String courseID;
                String description;

                JSONObject klass = classArray.getJSONObject(i);
                results[i] = klass.getString(COURSEID) + ": " + klass.getString(NAME) + "\n";
                results[i] = results[i] + klass.getString(DESCRIPTION);
                Log.i(LOG_TAG, results[i]);

            }

            return results;


        }

        @Override
        protected String[] doInBackground(String... params) {

            if (params == null) return null;

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String classJsonStr = null;
            try {
                String CLASS_BASE_URL = "http://api.umd.io/v0/courses?";
                String DEPTID = "dept_id";

                Uri builtUri = Uri.parse(CLASS_BASE_URL).buildUpon()
                        .appendQueryParameter(DEPTID, params[0])
                        .build();


                URL url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
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
                Log.i(LOG_TAG, classJsonStr);

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

            try {
                return getClassDataFromJson(classJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String[] strings) {
            mClassAdapter.clear();
            mClassAdapter.addAll(strings);
            mClassAdapter.notifyDataSetChanged();
        }
    }

}
