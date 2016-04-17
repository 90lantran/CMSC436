package com.alantran.android.swipe;


import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;



/**
 * A simple {@link Fragment} subclass.
 */
public class AllClassesFragment extends Fragment {

    String LOG_TAG = AllClassesFragment.class.getSimpleName();

    CustomAdapter mClassAdapter;

    OnPickButtonClick mCallback;

    ArrayList<Classes> classes = new ArrayList<>();


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

        // make API call to get data  which will use to populate listView of this fragement
        FetchClassesTask task = new FetchClassesTask();
        task.execute("CMSC");

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
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_class,parent,false);

            }

            final TextView description = (TextView) convertView.findViewById(R.id.list_item_class_textview);
            description.setText(listItemModel);
            Button removed = (Button) convertView.findViewById(R.id.remove_button);
            Button picked = (Button) convertView.findViewById(R.id.pick_button);
            final View finalConvertView = convertView;
//            removed.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Snackbar.make(finalConvertView, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//
//                    if (mCallback == null) Log.e(LOG_TAG, "mCallback is null");
//                    mCallback.onAddingItemToList(listItemModel, "remove");
//
//                }
//            });
//
//            picked.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Snackbar.make(finalConvertView, "Replace with your own action", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                    if (mCallback == null) Log.e(LOG_TAG, "mCallback is null");
//                    mCallback.onAddingItemToList(listItemModel, "pick");
//                }
//            });

            return convertView;
        }

    }

    public class ClassesRecyclerViewAdapter extends RecyclerView.Adapter<ClassesRecyclerViewAdapter.ViewHolder>{


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.allclasses_recycler_view_item, parent, false);

            CardView cardView = (CardView) view.findViewById(R.id.card_view);


            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Classes currentClass = classes.get(position);
            holder.getDescription().setText(currentClass.getDescription());
            holder.getName().setText(currentClass.getCourseID() + ": " + currentClass.getName());
            holder.getPickButton().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mCallback.onAddingItemToList(currentClass, "pick");
                }


            });
        }

        @Override
        public int getItemCount() {
            return classes == null ? 0 : classes.size();

        }

        public class ViewHolder extends RecyclerView.ViewHolder{
            private TextView name;
            private TextView description;
            private Button pickButton;
            private Button expandButton;


            public ViewHolder(View itemView) {
                super(itemView);
                name = (TextView) itemView.findViewById(R.id.class_textview_1);
                description = (TextView) itemView.findViewById(R.id.class_textview_2);
                pickButton = (Button) itemView.findViewById(R.id.pick_button);
                expandButton = (Button) itemView.findViewById(R.id.expand_button);
            }

            public TextView getName() { return name; }
            public  TextView getDescription() { return description;}
            public Button getPickButton(){return pickButton;}
            public Button getExpandButton(){return expandButton;}
        }
    }

    public class FetchClassesTask extends AsyncTask<String, Void, Classes[]> {

        private Classes[] getClassDataFromJson(String classJsonStr)
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
            Classes[] results = new Classes[classArray.length()];

            for (int i = 0; i < classArray.length(); i++) {
                String name;
                String courseID;
                String description;

                JSONObject klass = classArray.getJSONObject(i);
                Classes currentClass = new Classes();
                currentClass.setCourseID(klass.getString(COURSEID));
                currentClass.setDescription(klass.getString(DESCRIPTION));
                currentClass.setName(klass.getString(NAME));
                results[i] = currentClass;

                Log.i(LOG_TAG, results[i].toString());

            }

            return results;

        }

        @Override
        protected Classes[] doInBackground(String... params) {

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

            }

            return null;
        }

        @Override
        protected void onPostExecute(Classes[] strings) {
            classes = new ArrayList<Classes>(Arrays.asList(strings));
            RecyclerView view = (RecyclerView) getActivity().findViewById(R.id.allclasses_recycler_view);
            view.setAdapter(new ClassesRecyclerViewAdapter());

        }
    }

}
