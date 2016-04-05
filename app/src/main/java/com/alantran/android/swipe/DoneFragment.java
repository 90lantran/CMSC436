package com.alantran.android.swipe;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoneFragment extends Fragment implements AllClassesFragment.OnDoneButtonClick {

    String LOG_TAG = DoneFragment.class.getSimpleName();

    ArrayAdapter<String> mClassAdapter;

    public DoneFragment() {
        // Required empty public constructor
        super();
    }

    public ArrayAdapter<String> getmClassAdapter(){
        return mClassAdapter;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_done, container, false);
//        String[] data = {
//                "Mon 6/23 - Sunny - 31/17",
//                "Tue 6/24 - Foggy - 21/8",
//                "Wed 6/25 - Cloudy - 22/17",
//                "Thurs 6/26 - Rainy - 18/11",
//                "Fri 6/27 - Foggy - 21/10",
//                "Sat 6/28 - TRAPPED IN WEATHERSTATION - 23/18",
//                "Sun 6/29 - Sunny - 20/7",
//                "Mon 6/23 - Sunny - 31/17",
//                "Tue 6/24 - Foggy - 21/8",
//                "Wed 6/25 - Cloudy - 22/17"
//        };
        String[] data = {

        };

        List<String> classes = new ArrayList<String>(Arrays.asList(data));

        mClassAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.list_item_done,
                R.id.list_item_done_textview,
                classes
        );


        ListView mListView = (ListView) rootView.findViewById(R.id.listview_done);

        mListView.setAdapter(mClassAdapter);



        return rootView;
    }

    @Override
    public void onAddingItemToList(String description) {
        mClassAdapter.add(description);
        mClassAdapter.notifyDataSetChanged();
    }
}
