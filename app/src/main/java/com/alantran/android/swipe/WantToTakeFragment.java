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

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fab);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Sections[] wantToTakeClasses = composeSchedules();
                Intent intent = new Intent(getActivity(), DisplaySchedules.class);
                //EditText editText = (EditText) findViewById(R.id.edit_message);
                //String message = editText.getText().toString();
                intent.putParcelableArrayListExtra("Schedule_list", new ArrayList<Sections>
                        (Arrays.asList(wantToTakeClasses)));
                startActivity(intent);
            }
        });

        return rootView;
    }

    public CardArrayAdapter getArrayAdapter(){
        return mCardArrayAdapter;
    }

    public boolean onAddingItemToList(final Classes currentClass) {
        // wantToTake.add(currentClass);
        //String newItem = currentClass.getCourseID() + " with " + currentClass.getInstructor();


        //CardListView  listView = (CardListView)getActivity().findViewById(R.id.myList);
        //Log.e("in OnAddingItemToClass ", (listView == null) + "" );

        //TextView instructor = (TextView) listView.findViewById(R.id.list_item_pick_textview_instructor);
        //course.setText(currentClass.getCourseID());
        //instructor.setText(currentClass.getInstructor());
        if ( !wantToTake.contains(currentClass)){
            wantToTake.add(currentClass);
            //for (Sections section :currentClass.getSectionsList()) {
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
            newItem.setTitle(currentClass.getInstructor() + "\n" +
                            "This class has " + (int) Math.floor(currentClass.getSectionsList().size() / 2) + " sections"

            );

            newItem.setOnSwipeListener(new Card.OnSwipeListener() {
                @Override
                public void onSwipe(Card card) {
                    Log.e(LOG_TAG, card.getTitle());
                    mCardArrayAdapter.remove(card);
                    mCardArrayAdapter.notifyDataSetChanged();
                    wantToTake.remove(currentClass);
                }
            });

            mCardArrayAdapter.add(newItem);
            mCardArrayAdapter.notifyDataSetChanged();
            //}


            return true;
        } else {
            return false;
        }

    }

    public Sections[] composeSchedules(){


        List<Sections> sections = new ArrayList<>();

        // Pre-process classes with multiple sections
        // need to separate sections without conflict
        // wantToTakeSectionsList =
        for(Classes klass : wantToTake){
            if (klass.getSectionsList().size() > 2){
                


                for(int i = 0; i < klass.getSectionsList().size() - 1; i++ ){
                    if (klass.getSectionsList().get(i).overLaps(klass.getSectionsList().get(i+1))>0){

                    }else {

                    }
                }
            }
        }

        for (Classes klass : wantToTake){
            sections.addAll(klass.getSectionsList());
        }
        Sections[] sectionsWantToTake = sections.toArray(new Sections[sections.size()]);


        // convert time to sort by start time
        SimpleDateFormat sdf = new SimpleDateFormat("hh:mma");
        for (Sections currentSection: sections ){
            try {
                currentSection.setStartTimeSimple(sdf.parse(currentSection.getStartTime()));
                currentSection.setEndTimeSimple(sdf.parse(currentSection.getEndTime()));

            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        // sort by start time
        Arrays.sort(sectionsWantToTake, new Comparator<Sections>() {
            @Override
            public int compare(Sections lhs, Sections rhs) {
                return lhs.getStartTimeSimple().compareTo(rhs.getStartTimeSimple());
            }
        });

        // color each class by the algorithm


        for (int i = 0; i < sectionsWantToTake.length; i++){

            Integer[] colors = new Integer[100];
            int counter = 0;
            for (int j = 0; j < i ; j++){
                if (sectionsWantToTake[j].overLaps(sectionsWantToTake[i]) > 0){
                    colors[counter] = sectionsWantToTake[j].getColor();
                    counter ++;
                }
            }

            //int smalestColor = getSmallestColor();
            int index = 0;
            while(colors[index] != null){
                index = index + 1;
            }
            sectionsWantToTake[i].setColor(index);

            Log.i(LOG_TAG, "class " + sectionsWantToTake[i].getSectionID() +
                    " color  " + sectionsWantToTake[i].getColor());
        }


        // after that class with same color will be in 1 schedule

        // sort by color
        Arrays.sort(sectionsWantToTake, new Comparator<Sections>() {
            @Override
            public int compare(Sections lhs, Sections rhs) {
                return lhs.getColor().compareTo(rhs.getColor());
            }
        });

        for(int i = 0; i < sectionsWantToTake.length; i++){
            Log.i(LOG_TAG, sectionsWantToTake[i].toString());
            Log.i(LOG_TAG,"\n");
        }

        return sectionsWantToTake;
    }

}
