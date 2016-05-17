package com.alantran.android.swipe;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alantran.android.swipe.objects.Classes;

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
        if (listView!=null){
            listView.setAdapter(mCardArrayAdapter);
        }
        return rootView;
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
        newItem.setTitle(currentClass.getInstructor());

        newItem.setOnSwipeListener(new Card.OnSwipeListener() {
            @Override
            public void onSwipe(Card card) {
                selectedClasses.remove(currentClass);
            }
        });

        if (mCardArrayAdapter.getPosition(newItem) == -1) {
            mCardArrayAdapter.add(newItem);
            mCardArrayAdapter.notifyDataSetChanged();
            selectedClasses.add(currentClass);
            return true;
        } else {
            return false;
        }
    }
}
