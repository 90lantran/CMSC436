package com.alantran.android.swipe;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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

        // sections will be all Lecture section
        List<Sections> sections = new ArrayList<>();


        // Only add Lecture to sections
        // lectureToDiscussion will map sectionID to Discussion section
        // from Lecture sectionID, you can get back its Discussion section


        Map<String, Sections> lectureToDiscussion = new LinkedHashMap<>();


        // For each class, create a Map<Integer, List<Sections>>()
        // Partition all sections of each class into group
        // Integer is the group number
        // List<Sections>: Lecture section which has conflict should be in the same group

        List<Map<Integer,List<Sections>>> partitionLectures = new ArrayList<>();


        for (Classes klass : wantToTake){

            Map<Integer, List<Sections>> currentPartition = new TreeMap<>();


            partitionLectures.add(currentPartition);

            List<Sections> allCurrentSections = klass.getSectionsList();
            int group = 0;
            currentPartition.put(group, new ArrayList<Sections>());

            for (int i = 0; i < allCurrentSections.size(); i++) {

                // Lecture : will be compared to partition
                // Discussion: will be map with its sectionID
                if (allCurrentSections.get(i).getClassType().equals("Lecture")) {
                    sections.add(allCurrentSections.get(i));
                    if (i == 0){
                        currentPartition.get(group).add(allCurrentSections.get(i));
                    }else {
                        int currentGroupSize = currentPartition.get(group).size();
                        Sections lastItemInGroup = currentPartition.get(group).get(currentGroupSize - 1);
                        if(allCurrentSections.get(i).getStartTimeSimple().compareTo(lastItemInGroup.getStartTimeSimple()) == 0 ){
                            currentPartition.get(group).add(allCurrentSections.get(i));
                        } else {
                            group++;
                            currentPartition.put(group, new ArrayList<Sections>());
                            currentPartition.get(group).add(allCurrentSections.get(i));
                        }

                    }


                } else if (allCurrentSections.get(i).getClassType().equals("Discussion")) {
                    lectureToDiscussion.put(allCurrentSections.get(i).getSectionID(), allCurrentSections.get(i));
                }
            }
        }

        // checking for separate lecture and discussion
        for(Sections section : sections){
            Log.i(LOG_TAG,"After separate lecture and discussion " + section.getSectionID() + " " + section.getClassType());
        }

        // checking partition
        for(int i = 0; i < partitionLectures.size(); i++){
            Log.i(LOG_TAG, "In partition " + i );
            int group = 0;
            for(Map.Entry<Integer, List<Sections>> entry : partitionLectures.get(i).entrySet()){

                Iterator<Sections> it = entry.getValue().iterator();
                while (it.hasNext()){
                    Log.i(LOG_TAG, "In group " + group + " --- " +it.next().getSectionID());
                }
                group ++;
            }
        }


        Collection<List<Sections>> currentResult = new ArrayList<>();
        Collection<List<Sections>> updatedResult = new ArrayList<>();
        for (int i = 0; i < partitionLectures.size(); i++){
            if (i == 0){

                currentResult = partitionLectures.get(i).values();
            } else {
                for(List<Sections> currentGroup : currentResult){
                    for(List<Sections> nextGroup : partitionLectures.get(i).values()){
                        Log.i(LOG_TAG, "Process group " + currentGroup.get(0).getSectionID());
                        Log.i(LOG_TAG, "" + nextGroup.size());
                        List<Sections> temp = new ArrayList<>(currentGroup);
                        temp.addAll(nextGroup);
                        updatedResult.add(temp);
                    }
                }
                currentResult = updatedResult;
            }
        }


       //  checking after combining all of the groups
        int z = 0;
        for (List<Sections> currentList : updatedResult){
            Iterator<Sections> it = currentList.iterator();
            Log.i(LOG_TAG,"List " + z++);
            while(it.hasNext()){
                Log.i(LOG_TAG, "" + it.next().getSectionID());
            }
        }



        Sections[] sectionsWantToTake = sections.toArray(new Sections[sections.size()]);
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

    private void nestedLoopOperation(int[] counters, int[] length, int level) {
        if(level == counters.length) performOperation(counters);
        else {
            for (counters[level] = 0; counters[level] < length[level]; counters[level]++)
                nestedLoopOperation(counters, length, level + 1);
        }
    }

    private void performOperation(int[] counters) {
        String counterAsString = "";
        for (int level = 0; level < counters.length; level++) {
            counterAsString = counterAsString + counters[level];
            if (level < counters.length - 1) counterAsString = counterAsString + ",";
        }
        System.out.println(counterAsString);
    }

    private ArrayList<String> separateSchedules(){
        ArrayList<String> schedules = new ArrayList<>();
        ArrayList<Integer> divider = new ArrayList<>();
//
//           for(int i = 0; i < listClasses.size() - 1; i++){
//               if(listClasses.get(i).getColor() != listClasses.get(i+1).getColor()){
//                   divider.add(i);
//               }
//           }
//
//        int start = 0;
//        int end;
//        for(Integer d : divider) {
//            end = d;
//            int i;
//            String s = "";
//            for (i = start; i <= end; i++) {
//               s +=  (listClasses.get(i).getCourseID() + " "+listClasses.get(i).getBuilding() + " "+listClasses.get(i).getRoom() +
//                        " "+ listClasses.get(i).getStartTime() + " "+listClasses.get(i).getEndTime() + "\n");
//            }
//            schedules.add(s);
//            start = i;
//
//        }
        return schedules;
    }

}
