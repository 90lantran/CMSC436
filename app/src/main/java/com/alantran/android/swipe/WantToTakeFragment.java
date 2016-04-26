package com.alantran.android.swipe;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WantToTakeFragment extends Fragment {
    String LOG_TAG = WantToTakeFragment.class.getSimpleName();

    ArrayAdapter<String> mClassAdapter ;
    List<Classes> wantToTake ;

    public WantToTakeFragment() {
        // Required empty public constructor
        wantToTake = new ArrayList<>();
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


        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Classes[] wantClaases = composeSchedules();
                Intent intent = new Intent(this, DisplayScheduleActivity.class);
                EditText editText = (EditText) findViewById(R.id.edit_message);
                String message = editText.getText().toString();
                intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });

        return rootView;
    }

    public ArrayAdapter<String> getArrayAdapter(){
        return mClassAdapter;
    }

    public boolean onAddingItemToList(Classes currentClass) {
        wantToTake.add(currentClass);
        String newItem = currentClass.getCourseID() + " with " + currentClass.getInstructor();
        if (mClassAdapter.getPosition(newItem) == -1) {
            mClassAdapter.add(newItem);
            mClassAdapter.notifyDataSetChanged();

            return true;
        } else {
            return false;
        }

    }

    public Classes[] composeSchedules(){
        // convert time to sort by start time
        //int numClass = mClassAdapter.getCount();
        Classes[] wantToTakeArr = wantToTake.toArray(new Classes[wantToTake.size()]);
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mma");
        for (Classes currentClass: wantToTakeArr ){
            try {
                currentClass.setStartTimeSimple(sdf.parse(currentClass.getStartTime()));
                currentClass.setEndTimeSimple(sdf.parse(currentClass.getEndTime()));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // sort by start time
        Arrays.sort(wantToTakeArr, new Comparator<Classes>() {
            @Override
            public int compare(Classes lhs, Classes rhs) {
                return lhs.getStartTimeSimple().compareTo(rhs.getStartTimeSimple());
            }
        });

        // color each class by the algorithm


        for (int i = 0; i < wantToTakeArr.length; i++){

            Integer[] colors = new Integer[100];
            int counter = 0;
            for (int j = 0; j < i ; j++){
                if (wantToTakeArr[j].overLaps(wantToTakeArr[i]) > 0){
                    colors[counter] = wantToTakeArr[j].getColor();
                    counter ++;
                }
            }

            //int smalestColor = getSmallestColor();
            int index = 0;
            while(colors[index] != null){
                index = index + 1;
            }
            wantToTakeArr[i].setColor(index);

            Log.i(LOG_TAG,"class " + wantToTakeArr[i].getCourseID() +
                    " color  " + wantToTakeArr[i].getColor() );
        }


        // after that class with same color will be in 1 schedule

        // sort by color
        Arrays.sort(wantToTakeArr, new Comparator<Classes>() {
            @Override
            public int compare(Classes lhs, Classes rhs) {
                return lhs.getColor().compareTo(rhs.getColor());
            }
        });

        for(int i = 0; i < wantToTakeArr.length; i++){
            Log.i(LOG_TAG, wantToTakeArr[i].toString());
            Log.i(LOG_TAG,"\n");
        }

        return wantToTakeArr;
    }

}
