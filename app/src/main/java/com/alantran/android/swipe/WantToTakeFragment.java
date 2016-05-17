package com.alantran.android.swipe;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alantran.android.swipe.objects.Classes;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashSet;

import it.gmariotti.cardslib.library.internal.Card;
import it.gmariotti.cardslib.library.internal.CardArrayAdapter;
import it.gmariotti.cardslib.library.internal.CardHeader;
import it.gmariotti.cardslib.library.internal.CardThumbnail;
import it.gmariotti.cardslib.library.view.CardListView;

public class WantToTakeFragment extends Fragment {
    String LOG_TAG = WantToTakeFragment.class.getSimpleName();

    CardArrayAdapter mCardArrayAdapter;
    HashSet<Classes> selectedClasses;

    public WantToTakeFragment() {
        selectedClasses = new HashSet<Classes>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_want_to_take, container, false);
        ArrayList<Card> cards = new ArrayList<Card>();

        mCardArrayAdapter = new CardArrayAdapter(getActivity(),cards);
        mCardArrayAdapter.setEnableUndo(true);

        CardListView listView = (CardListView) rootView.findViewById(R.id.myList);
        if (listView != null) {
            listView.setAdapter(mCardArrayAdapter);
        }
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mCardArrayAdapter.getCount() == 0) {
            try {
                FileInputStream fis = getContext().openFileInput("currentClasses");
                ObjectInputStream is = new ObjectInputStream(fis);
                ArrayList<Classes> classes = (ArrayList<Classes>) is.readObject();

                for (Classes c : classes) {
                    if (!selectedClasses.contains(c)) {
                        onAddingItemToList(c);
                    }
                }

                is.close();
                fis.close();
            } catch (Exception e) {
                e.printStackTrace(); // do nothing, lose data
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        ArrayList<Classes> classes = new ArrayList<Classes>();
        for (Classes c : selectedClasses) {
            if (!classes.contains(selectedClasses)) {
                classes.add(c);
            }
        }

        try {
            FileOutputStream fos = getContext().openFileOutput("currentClasses", Context.MODE_PRIVATE);
            ObjectOutputStream os = new ObjectOutputStream(fos);
            os.writeObject(classes);
            os.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace(); // do nothing, lose data
        }

    }

    public CardArrayAdapter getArrayAdapter(){
        return mCardArrayAdapter;
    }

    public HashSet<Classes> getSelectedClasses() {
        return selectedClasses;
    }

    public boolean onAddingItemToList(final Classes currentClass) {
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

        String title = currentClass.getInstructor();
        if (title.length() > 80) {
            title = title.substring(0, 80);
            title = title + "...";
        }
        newItem.setTitle(title);

        newItem.setOnSwipeListener(new Card.OnSwipeListener() {
            @Override
            public void onSwipe(Card card) {
                selectedClasses.remove(currentClass);
            }
        });

        if (mCardArrayAdapter.getPosition(newItem) == -1 && !selectedClasses.contains(currentClass)) {
            mCardArrayAdapter.add(newItem);
            mCardArrayAdapter.notifyDataSetChanged();
            selectedClasses.add(currentClass);
            return true;
        } else {
            return false;
        }
    }
}
