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
public class WantToTakeFragment extends Fragment {
    String LOG_TAG = WantToTakeFragment.class.getSimpleName();

    ArrayAdapter<String> mClassAdapter ;

    public WantToTakeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_want_to_take, container, false);
        String[] data = {};
        List<String> classes = new ArrayList<String>(Arrays.asList(data));

        mClassAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.list_item_want_to_take,
                R.id.list_item_textview,
                classes
        );

        ListView mListView = (ListView) rootView.findViewById(R.id.listview_want_to_take);

        mListView.setAdapter(mClassAdapter);

        return rootView;
    }

    public ArrayAdapter<String> getArrayAdapter(){
        return mClassAdapter;
    }

    public boolean onAddingItemToList(Classes currentClass) {
        String newItem = currentClass.getCourseID() + " with " + currentClass.getInstructor();
        if (mClassAdapter.getPosition(newItem) == -1) {
            mClassAdapter.add(newItem);
            mClassAdapter.notifyDataSetChanged();
            return true;
        } else {
            return false;
        }
    }

}
