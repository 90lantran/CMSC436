package com.alantran.android.swipe;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.preference.DialogPreference;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import static android.provider.Settings.Global.getString;

/**
 * Created by michaelmarbukh on 5/7/16.
 */


class TimePreference extends DialogPreference   {
    private TimePicker picker = null;

    private Context mContext;
    private int lastHour=0;
    private int lastMinute=0;



    public static int getHour(String time) {
        String[] pieces=time.split(":");

        return(Integer.parseInt(pieces[0]));
    }

    public static int getMinute(String time) {
        String[] pieces=time.split(":");

        return(Integer.parseInt(pieces[1]));
    }



    public TimePreference(Context ctxt) {
        this(ctxt, null);
    }

    public TimePreference(Context ctxt, AttributeSet attrs) {
        this(ctxt, attrs, android.R.attr.dialogPreferenceStyle);
    }

    public TimePreference(Context ctxt, AttributeSet attrs, int defStyle) {
        super(ctxt, attrs, defStyle);
        setPositiveButtonText("Set");
        setNegativeButtonText("Cancel");

    }

    @Override
    protected View onCreateDialogView() {
        picker = new TimePicker(getContext());
        return (picker);
    }

    @Override
    protected void onBindDialogView(View v) {
        super.onBindDialogView(v);


    }

    @Override
    protected void onDialogClosed(boolean positiveResult) {
        super.onDialogClosed(positiveResult);

        if (positiveResult) {

            //marbukh
            // Access default SharedPreferences
//            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(mContext);
//
//            SharedPreferences.Edixtor editor = settings.edit();
//            editor.putString("StartTime", "");
//            editor.commit();


        }
    }

    @Override
    protected Object onGetDefaultValue(TypedArray a, int index) {
        return (a.getString(index));
    }

    @Override
    protected void onSetInitialValue(boolean restoreValue, Object defaultValue) {
    }

//    @Override
//    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//
//        String time = hourOfDay + ":" + minute;
//        SharedPreferences sharedPref = getActivity(R.xml.preferences).getPreferences(Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPref.edit();
//        editor.putString(getKey(R.string.timePrefA_title), time);
//        editor.commit();
//    }



//

}
