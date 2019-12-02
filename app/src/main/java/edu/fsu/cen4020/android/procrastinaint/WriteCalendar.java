package edu.fsu.cen4020.android.procrastinaint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.icu.util.TimeZone;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class WriteCalendar extends AppCompatActivity {


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate and add items to actionbar
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), calendar.class));
                return true;
            case R.id.nav_login:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                return true;
            case R.id.nav_newevent:
                startActivity(new Intent(getApplicationContext(), EventAdderActivity.class));
                return true;
            case R.id.nav_read_cal:
                startActivity(new Intent(getApplicationContext(), ReadCalendarActivity.class));
                return true;
            case R.id.nav_hentimer:
                startActivity(new Intent(getApplicationContext(), HelperEventNagvigatorTimeActivityInterface.class));
                return true;
            case R.id.nav_timer:
                startActivity(new Intent(getApplicationContext(), timerActivity.class));
                return true;
            case R.id.nav_notes:
                startActivity(new Intent(getApplicationContext(), NotesActivity.class));
                return true;
            case R.id.nav_write_cal:
                startActivity(new Intent(getApplicationContext(), WriteCalendar.class));
            case R.id.nav_write_to_firebase:
                startActivity(new Intent(getApplicationContext(), AddUploadedEventsActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private static final String TAG = WriteCalendar.class.getCanonicalName();

    private Button writeButton;
    private ArrayList<Event> eventArrayList = new ArrayList<Event>();
    private Long currentTime;
    HashMap<String, Long> calanderValues = new HashMap<>();
    private Button readCalander;
    private Spinner calanderSpinner;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Write Calendar");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write_calendar);
        currentTime = System.currentTimeMillis();


        readCalander = (Button) findViewById(R.id.readCalendarButton);
        calanderSpinner = (Spinner) findViewById(R.id.calendarSpinner);
        calanderValues = getCalanders();
        final List<String> calandersNames = new ArrayList<String>();
        for(String Key : calanderValues.keySet()){
            calandersNames.add(Key);
        }
        // Spinner Tutorial
        // https://www.tutorialspoint.com/android/android_spinner_control.htm
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, calandersNames);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        calanderSpinner.setAdapter(dataAdapter);

        writeButton = (Button) findViewById(R.id.writeCalendarButton);
        writeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getEvents();
                writeCalendar();
            }
        });
    }


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

        Log.i(TAG, "readCalander finished");
        return calanderValues;
    }

    private void getEvents(){
        String[] projection = {
                MainCP.TITLE,
                MainCP.RRule,
                MainCP.DURATION,
                MainCP.DTSTART,
                MainCP.DTEND,
                MainCP.LAST_DATE};

        String selection =
                MainCP.DTSTART + " == 1 AND " +
                        MainCP.DTSTART + " >= ?";
        String[] selectionArgs = new String[]{
                "1",
                currentTime.toString()
        };

        Cursor cursor = getContentResolver().query(
                MainCP.CONTENT_URI,
                projection,
                null,
                null,
                MainCP.DTSTART

        );

        if (cursor.getCount()!= 0){
            if(cursor.moveToFirst()){
                do{
                    String title = cursor.getString(cursor.getColumnIndex(MainCP.TITLE));
                    String rRule = cursor.getString(cursor.getColumnIndex(MainCP.RRule));
                    String duration = cursor.getString(cursor.getColumnIndex(MainCP.DURATION));
                    Long DTSTART = cursor.getLong(cursor.getColumnIndex(MainCP.DTSTART));
                    Long DTEND = cursor.getLong(cursor.getColumnIndex(MainCP.DTEND));
                    Long LAST_DATE = cursor.getLong(cursor.getColumnIndex(MainCP.LAST_DATE));
                    Event event = new Event(title, null, rRule, duration, DTSTART, DTEND, LAST_DATE);
                    event.setWrite(0);
                    eventArrayList.add(event);
                }while(cursor.moveToNext());
            }
        }
    }

    private void writeCalendar(){


        // https://stackoverflow.com/questions/13709477/how-to-add-calendar-events-to-default-calendar-silently-without-intent-in-andr
        String calanderName = calanderSpinner.getSelectedItem().toString();

        Long calanderID = calanderValues.get(calanderName);

        Log.i(TAG, "writeCalendar: " + eventArrayList.toString());
        for(Event event : eventArrayList) {
            ContentResolver cr = getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.TITLE, event.getTitle());
            values.put(CalendarContract.Events.DESCRIPTION, event.getDescription());
            values.put(CalendarContract.Events.DTSTART, event.getDTSTART());
            values.put(CalendarContract.Events.DTEND, event.getDTEND());

            TimeZone timeZone = TimeZone.getDefault();
            values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
            // Default calendar
            values.put(CalendarContract.Events.CALENDAR_ID, calanderID);
            // Insert event to calendar
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
        }

        for(Event event : eventArrayList){
            ContentResolver resolver = getContentResolver();

            ContentValues values = new ContentValues();
            values.put(MainCP.TITLE, event.getTitle());
            values.put(MainCP.DTSTART, event.getDTSTART());
            values.put(MainCP.NEW, event.getWrite());
            resolver.update(MainCP.CONTENT_URI, values, "TITLE = ? AND DTSTART = ?", new String[]{event.getTitle(), event.getDTSTART().toString()});
        }

        eventArrayList.clear();
    }
}
