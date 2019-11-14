package edu.fsu.cen4020.android.procrastinaint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class RWCalendarActivity extends AppCompatActivity {


    private String TAG = RWCalendarActivity.class.getCanonicalName();
    private Button readCalander;
    private TextView showText;
    private Spinner calanderSpinner;
    HashMap<String, Long> calanderValues = new HashMap<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rwcalendar);

        readCalander = (Button) findViewById(R.id.readCalendarButton);
        showText = (TextView) findViewById(R.id.readTextView);
        calanderSpinner = (Spinner) findViewById(R.id.calendarSpinner);

        // Query though the content provider and get the names of the calanders
        calanderValues = getCalanders();

        List<String> calandersNames = new ArrayList<String>();

        for(String Key : calanderValues.keySet()){
            calandersNames.add(Key);
        }

        // Spinner Tutorial
        // https://www.tutorialspoint.com/android/android_spinner_control.htm
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, calandersNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        calanderSpinner.setAdapter(dataAdapter);


        readCalander.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readEvent(view);
            }
        });



    }


    // This was taken from Google documentation on the calander contentprovider
    // https://developer.android.com/guide/topics/providers/calendar-provider#query
    private HashMap<String, Long> getCalanders(){

        // TODO Make Query Asynchronus
        // https://developer.android.com/reference/android/content/AsyncQueryHandler.html

        String[] EVENT_PROJECTION = new String[] {
                CalendarContract.Calendars._ID,                           // 0
                CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
                CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
                CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
        };

        // The indices for the projection array above.
        int PROJECTION_ID_INDEX = 0;
        int PROJECTION_ACCOUNT_NAME_INDEX = 1;
        int PROJECTION_DISPLAY_NAME_INDEX = 2;
        int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

        Log.i(TAG, "readCalander: Running Method");
        Cursor cur = null;
        ContentResolver cr = getContentResolver();
        Uri uri = CalendarContract.Calendars.CONTENT_URI;
        String selection = null;
        String[] selectionArgs = new String[] {};
        // Submit the query and get a Cursor object back.
        cur = cr.query(uri, EVENT_PROJECTION, null, null, null);

        HashMap<String, Long> calanderValues = new HashMap<>();

        while (cur.moveToNext()) {
            long calID = 0;
            String displayName = null;
            String accountName = null;
            String ownerName = null;

            // Get the field values
            calID = cur.getLong(PROJECTION_ID_INDEX);
            displayName = cur.getString(PROJECTION_DISPLAY_NAME_INDEX);
            accountName = cur.getString(PROJECTION_ACCOUNT_NAME_INDEX);
            ownerName = cur.getString(PROJECTION_OWNER_ACCOUNT_INDEX);


            calanderValues.put(displayName, calID);
            Log.i(TAG, "CalID " + calID + "\ndisplayName: " + displayName
                    + "\naccountName" + accountName + "\nOwnderName: " +ownerName); }

        return calanderValues;
    }

    
    private void readEvent(View view){
        Log.i(TAG, "readEvent: Started");
        String calanderName = calanderSpinner.getSelectedItem().toString();
        Long calanderID = calanderValues.get(calanderName);
        Log.i(TAG, "Name = " + calanderName + "\nreadEvent: id = " + calanderID);


        Cursor cur = null;
        ContentResolver cr = getContentResolver();

        String[] mProjection =
                {
                        "_id",
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.EVENT_LOCATION,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.DTEND,
                };

        Uri uri = CalendarContract.Events.CONTENT_URI;
        String selection = CalendarContract.Events.EVENT_LOCATION + " = ? ";
        String[] selectionArgs = new String[]{"London"};

        cur = cr.query(uri, mProjection, null, null, null);

        while (cur.moveToNext()) {
            Log.i(TAG, "readEvent: Starting calander");
            String title = cur.getString(cur.getColumnIndex(CalendarContract.Events.TITLE));
            String DTSTART = cur.getString(cur.getColumnIndex(CalendarContract.Events.DTSTART));
            String DTEND = cur.getString(cur.getColumnIndex(CalendarContract.Events.DTEND));
            Log.i(TAG, "readEvent: \n" + "Title = " + title + "\nDTSTART: " + DTSTART + "\nDTEND: " + DTEND);
        }
    }
}
