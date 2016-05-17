package com.alantran.android.swipe;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.util.ArrayList;

public class SendSchedule extends AppCompatActivity {
    private ArrayList<String> message = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_schedule);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }
        });


        message = getIntent().getStringArrayListExtra("Message");
        Log.i("In Share", "" + message.toString());
        Button button = (Button) findViewById(R.id.send);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    protected void sendEmail() {
        SharedPreferences settings = getApplication().getSharedPreferences("UserInfo", 0);

        String myEmail = settings.getString("Email", "").toString();

        Log.i("Send email", "");
        String[] TO = {myEmail};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

       // emailIntent.setData(Uri.parse("mailto:90lantram@gmail.com"));
        emailIntent.setType("plain/text");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"90lantran@gmail.com"});
        //emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "My Schedule");
        emailIntent.putExtra(Intent.EXTRA_TEXT, message.get(0));
        //emailIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        emailIntent.setType("message/rfc822");
        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("Sent email...", "");
        }
        catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(SendSchedule.this, "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }


}
