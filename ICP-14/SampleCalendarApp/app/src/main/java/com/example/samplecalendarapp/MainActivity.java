// Created by Vijaya Yeruva on 11/20/2020

package com.example.samplecalendarapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button disp = (Button) findViewById(R.id.dispbut);
        disp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Auto-generated method stub
                disp();
            }
        });
    }

    public void disp() {
        Calendar rightNow = Calendar.getInstance();
        Uri uri = Uri.parse("content://com.android.calendar/time/"
                + String.valueOf(rightNow.getTimeInMillis()));
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        // Use the Calendar app to view the time.
        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
}