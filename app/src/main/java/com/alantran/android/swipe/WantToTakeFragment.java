package com.alantran.android.swipe;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;


/**
 * A simple {@link Fragment} subclass.
 */
public class WantToTakeFragment extends Fragment {
    String LOG_TAG = WantToTakeFragment.class.getSimpleName();

    //ArrayAdapter<String> mClassAdapter ;
    CardArrayAdapter mCardArrayAdapter;
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


        ArrayList<Card> cards = new ArrayList<Card>();



        mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);
        mCardArrayAdapter.setEnableUndo(true);

        CardListView listView = (CardListView) rootView.findViewById(R.id.myList);
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }


//        String[] data = {};
//        List<String> classes = new ArrayList<String>(Arrays.asList(data));
//
//        mClassAdapter = new ArrayAdapter<String>(getActivity(),
//                R.layout.list_item_want_to_take,
//                R.id.list_item_textview,
//                classes
//        );
//
//        ListView mListView = (ListView) rootView.findViewById(R.id.listview_want_to_take);
//
//        mListView.setAdapter(mClassAdapter);
//
//
//
        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Classes[] wantToTakeClasses = composeSchedules();
                Intent intent = new Intent(getActivity(), DisplaySchedules.class);
                //EditText editText = (EditText) findViewById(R.id.edit_message);
                //String message = editText.getText().toString();
                intent.putParcelableArrayListExtra("Schedule_list", new ArrayList<Classes>
                        (Arrays.asList(wantToTakeClasses)));
                startActivity(intent);
            }
        });

        return rootView;
    }

    public CardArrayAdapter getArrayAdapter(){
        return mCardArrayAdapter;
    }

    public boolean onAddingItemToList(Classes currentClass) {
       // wantToTake.add(currentClass);
        //String newItem = currentClass.getCourseID() + " with " + currentClass.getInstructor();
        Card newItem = new Card(getContext());

        CardHeader cardHeader = new CardHeader(getContext());
        cardHeader.setTitle(currentClass.getCourseID());

        newItem.addCardHeader(cardHeader);

        CardThumbnail cardThumbnail = new CardThumbnail(getContext());
        cardThumbnail.setInnerLayout(R.layout.card_thumbnail);
        cardThumbnail.setDrawableResource(R.drawable.testudo);


        newItem.addCardThumbnail(cardThumbnail);


        newItem.setSwipeable(true);
        newItem.setId("xxx");
        newItem.setTitle(currentClass.getInstructor());

        //CardListView  listView = (CardListView)getActivity().findViewById(R.id.myList);
        //Log.e("in OnAddingItemToClass ", (listView == null) + "" );

        //TextView instructor = (TextView) listView.findViewById(R.id.list_item_pick_textview_instructor);
        //course.setText(currentClass.getCourseID());
        //instructor.setText(currentClass.getInstructor());
        if ( !wantToTake.contains(currentClass)){
            wantToTake.add(currentClass);
            mCardArrayAdapter.add(newItem);
            mCardArrayAdapter.notifyDataSetChanged();

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
