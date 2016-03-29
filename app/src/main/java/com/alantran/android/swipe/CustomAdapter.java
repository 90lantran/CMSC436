package com.alantran.android.swipe;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

/**
 * Created by alantran on 3/28/16.
 */
public class CustomAdapter extends ArrayAdapter<String> {

    public CustomAdapter(Context context, List<String> listItemModel){
        super(context,0, listItemModel);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final String listItemModel = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_class_1,parent,false);

        }

        final TextView description = (TextView) convertView.findViewById(R.id.list_item_class_textview);
        description.setText(listItemModel);
        Button done = (Button) convertView.findViewById(R.id.remove_button);
        Button take = (Button) convertView.findViewById(R.id.pick_button);
        final View finalConvertView = convertView;
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(finalConvertView, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                



            }
        });

        take.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(finalConvertView, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });

        return convertView;
    }
}



