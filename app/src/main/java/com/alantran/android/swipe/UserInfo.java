package com.alantran.android.swipe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

public class UserInfo extends AppCompatActivity {
    private AutoCompleteTextView mEmailView;
    private AutoCompleteTextView status ;
    private AutoCompleteTextView major ;
    private AutoCompleteTextView time ;

//    @Override
//    public void onSaveInstanceState(Bundle savedInstanceState) {
//        Log.i("USERINFO", "onSaveInstanceState");
//        savedInstanceState.putString("Email", mEmailView.getText().toString());
//        savedInstanceState.putString("Status", status.getText().toString());
//        savedInstanceState.putString("Major", major.getText().toString());
//        savedInstanceState.putString("Time", time.getText().toString());
//        super.onSaveInstanceState(savedInstanceState);
//    }

    private static final String[] STATUSES = new String[] {
            "Freshman", "Sophomore", "Junior", "Senior"
    };
    private static final String[] MAJORS = new String[] {
            "CMSC", "PHYS", "MATH", "STAT"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences preferences = getPreferences(MODE_PRIVATE);

            Log.i("USERINFO", "onCreate() savedInstanceState is not null");
            mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
            mEmailView.setText(preferences.getString("Email", null));


//            status = (AutoCompleteTextView) findViewById(R.id.status);
//            status.setText(preferences.getString("Status", null));
//            status.setAdapter(new ArrayAdapter<String>(this,
//                    android.R.layout.simple_dropdown_item_1line, STATUSES));

            major = (AutoCompleteTextView) findViewById(R.id.major);
            major.setText(preferences.getString("Major",null));
            major.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, MAJORS));

            time = (AutoCompleteTextView) findViewById(R.id.time);
            time.setText(preferences.getString("Time",null));




        Button button = (Button) findViewById(R.id.button_finish);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString("Email", mEmailView.getText().toString());
//                editor.putString("Status", status.getText().toString());
                editor.putString("Major", major.getText().toString());
                editor.putString("Time", time.getText().toString());

                editor.commit();

                Intent intent = new Intent(UserInfo.this,MainActivity.class);
                startActivity(intent);
            }
        });




        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("Email", mEmailView.getText().toString());
//        editor.putString("Status", status.getText().toString());
        editor.putString("Major", major.getText().toString());
        editor.putString("Time", time.getText().toString());

        editor.commit();

    }
}
