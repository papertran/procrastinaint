package edu.fsu.cen4020.android.procrastinaint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ReadCalendarActivity extends AppCompatActivity {


    private String TAG = ReadCalendarActivity.class.getCanonicalName();
    private Button readCalander;
    private Spinner calanderSpinner;
    private Long currentTime;
    HashMap<String, Long> calanderValues = new HashMap<>();

    private ArrayList<Event> eventArrayList = new ArrayList<Event>();
    private HashMap<Event, Long> localEventHM= new HashMap<Event, Long>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_calendar);

        readCalander = (Button) findViewById(R.id.readCalendarButton);
        calanderSpinner = (Spinner) findViewById(R.id.calendarSpinner);
        currentTime = System.currentTimeMillis();
        Log.i(TAG, "onCreate: time is " + currentTime.toString());
        // Query though the content provider and get the names of the calanders
        calanderValues = getCalanders();
        getContentProviderEvents();

        final List<String> calandersNames = new ArrayList<String>();

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
                // Clear the arraylist when a new calendar is read
                eventArrayList.clear();

                if(calandersNames.size() == 0){
                    readCalander.setEnabled(false);
                }
                else {
                    readEvent(view);

                    // Populate the eventRecyclerView after getting events
                    if (eventArrayList.size() == 0) {
                        Toast.makeText(ReadCalendarActivity.this, "No events found", Toast.LENGTH_SHORT).show();
                    } else {
                        initRecyclerView();
                    }
                }
            }
        });



    }


    public void saveEvent(Event event){
        // Track if event is reoccuring or singular

        if(event.isRecurring()){
            ArrayList<Event> newEvents = event.recurringToSingular(currentTime);

            for(Event item : newEvents) {

                ContentValues values = new ContentValues();
                Log.i(TAG, "saveButton singularEvent " +
                        "\nTitle =" + item.getTitle() +
                        "\nDTStart ="  + item.getEventStartDate() +
                        "\nDTEND ="  + item.getEventEndDate() +
                        "\nDTStartTime ="  + item.getEventStartTime() +
                        "\nDTENDTime = " + item.getEventEndTime());
//                values.put(MainCP.TITLE, item.getTitle());
//                values.put(MainCP.DTSTART, item.getDTSTART());
//                values.put(MainCP.DTEND, item.getDTEND());
//                values.put(MainCP.LAST_DATE, item.getLAST_DATE());
//                values.put(MainCP.NEW, 0);
//                getContentResolver().insert(MainCP.CONTENT_URI, values);
            }

        }else{
            Log.i(TAG, "saveButton singularEvent " +
                    "\nTitle =" + event.getTitle() +
                    "\nDTStart ="  + event.getDTSTART() +
                    "\nDTEND = " + event.getDTEND());

            ContentValues values = new ContentValues();
            values.put(MainCP.TITLE, event.getTitle());
            values.put(MainCP.DTSTART, event.getDTSTART());
            values.put(MainCP.DTEND, event.getDTEND());
            values.put(MainCP.LAST_DATE, event.getLAST_DATE());
            values.put(MainCP.NEW, 0);
            getContentResolver().insert(MainCP.CONTENT_URI, values);
        }

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

        Log.i(TAG, "readCalander finished");
        return calanderValues;
    }

    private void getContentProviderEvents(){
        String[] projection = {
                MainCP.TITLE,
                MainCP.RRule,
                MainCP.DURATION,
                MainCP.DTSTART,
                MainCP.DTEND,
                MainCP.LAST_DATE};

        String selection =
                MainCP.DTSTART + " >= ? OR " +
                MainCP.LAST_DATE +
                " >= ?";
        String[] selectionArgs = new String[]{
                currentTime.toString(),
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
                    localEventHM.put(event, DTSTART);
                }while(cursor.moveToNext());
            }
        }

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
                        CalendarContract.Events.CALENDAR_ID,
                        CalendarContract.Events.TITLE,
                        CalendarContract.Events.EVENT_LOCATION,
                        CalendarContract.Events.DTSTART,
                        CalendarContract.Events.DTEND,
                        CalendarContract.Events.DURATION,
                        CalendarContract.Events.RRULE,
                        CalendarContract.Events.RDATE,
                        CalendarContract.Events.LAST_DATE,

                };

        Uri uri = CalendarContract.Events.CONTENT_URI;
        String selection = CalendarContract.Events.CALENDAR_ID + " = ? AND (" +
                CalendarContract.Events.DTSTART + " >= ? OR " +
                CalendarContract.Events.LAST_DATE +
                " >= ? )";
        String[] selectionArgs = new String[]{
                calanderID.toString(),
                currentTime.toString(),
                currentTime.toString()
        };

        cur = cr.query(uri, mProjection, selection, selectionArgs, null);

        while (cur.moveToNext()) {
            Log.i(TAG, "readEvent: Starting calander");
            String title = cur.getString(cur.getColumnIndex(CalendarContract.Events.TITLE));
            Long DTSTART = cur.getLong(cur.getColumnIndex(CalendarContract.Events.DTSTART));
            Long DTEND = cur.getLong(cur.getColumnIndex(CalendarContract.Events.DTEND));
            Long LAST_DATE = cur.getLong(cur.getColumnIndex(CalendarContract.Events.LAST_DATE));
            Integer CalenderID = cur.getInt(cur.getColumnIndex(CalendarContract.Events.CALENDAR_ID));
            String duration = cur.getString(cur.getColumnIndex(CalendarContract.Events.DURATION));
            String rDate = cur.getString(cur.getColumnIndex(CalendarContract.Events.RDATE));
            String rRule = cur.getString(cur.getColumnIndex(CalendarContract.Events.RRULE));

            // https://stackoverflow.com/questions/9754600/converting-epoch-time-to-date-string/9754625
            // Event(String title, String description, String RRULE, String duration, Long DTSTART, Long DTEND, Long LAST_DATE) {
            Event event = new Event(title, null, rRule, duration, DTSTART, DTEND, LAST_DATE);
            Log.i(TAG, "readEvent: \n" +
                    "Title = " + event.getTitle() +
                    "\nStart Date = " + event.getEventStartDate() +
                    "\nEnd Date = " + event.getEventEndDate() +
                    "\nStart Time= " + event.getEventStartTime() +
                    "\nEnd Time = " + event.getEventEndTime() +
                    "\nRrule = " + event.getRRULE() +
                    "\nCurrentTime = " + currentTime);


            if(localEventHM.containsKey(event)){
                continue;
            }
            else {

                eventArrayList.add(event);
            }

        }

    }


    // https://stackoverflow.com/questions/20654967/convert-unix-epoch-time-to-formatted-date-unexpected-date
    public static String epochToDate(Long epocSeconds){
        Date updateDate = new Date(epocSeconds);
        SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        return format.format(updateDate);

    }

    //https://stackoverflow.com/questions/4142313/convert-timestamp-in-milliseconds-to-string-formatted-time-in-java
    public static String epochToTime(Long epocSeconds){
        Date date = new Date(epocSeconds);
        SimpleDateFormat sdf = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        return sdf.format(date);
    }


    // Duration is given in RFC2445 this was found on stackoverflow to convert
    public static long RFC2445ToMilliseconds(String str)
    {


        if(str == null || str.isEmpty())
            throw new IllegalArgumentException("Null or empty RFC string");

        int sign = 1;
        int weeks = 0;
        int days = 0;
        int hours = 0;
        int minutes = 0;
        int seconds = 0;

        int len = str.length();
        int index = 0;
        char c;

        c = str.charAt(0);

        if (c == '-')
        {
            sign = -1;
            index++;
        }

        else if (c == '+')
            index++;

        if (len < index)
            return 0;

        c = str.charAt(index);

        if (c != 'P')
            throw new IllegalArgumentException("Duration.parse(str='" + str + "') expected 'P' at index="+ index);

        index++;
        c = str.charAt(index);
        if (c == 'T')
            index++;

        int n = 0;
        for (; index < len; index++)
        {
            c = str.charAt(index);

            if (c >= '0' && c <= '9')
            {
                n *= 10;
                n += ((int)(c-'0'));
            }

            else if (c == 'W')
            {
                weeks = n;
                n = 0;
            }

            else if (c == 'H')
            {
                hours = n;
                n = 0;
            }

            else if (c == 'M')
            {
                minutes = n;
                n = 0;
            }

            else if (c == 'S')
            {
                seconds = n;
                n = 0;
            }

            else if (c == 'D')
            {
                days = n;
                n = 0;
            }

            else if (c == 'T')
            {
            }
            else
                throw new IllegalArgumentException ("Duration.parse(str='" + str + "') unexpected char '" + c + "' at index=" + index);
        }

        long factor = 1000 * sign;
        long result = factor * ((7*24*60*60*weeks)
                + (24*60*60*days)
                + (60*60*hours)
                + (60*minutes)
                + seconds);

        return result;
    }

    public String epochToDateTime(Long epocDate){

        Date date = new Date(epocDate);
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE,MMMM d,yyyy h:mm,a", Locale.ENGLISH);
        sdf.setTimeZone(TimeZone.getTimeZone("EST"));
        return sdf.format(date);
    }


    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        final RecyclerView eventRecyclerView = (RecyclerView) findViewById(R.id.readEventsRecyclerView);
        final EventRecyclerViewAdapter adapter = new EventRecyclerViewAdapter(this, eventArrayList);
        eventRecyclerView.setAdapter(adapter);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));



        // https://stackoverflow.com/questions/26422948/how-to-do-swipe-to-delete-cardview-in-android-using-support-library
        // This is used for the swipe functionailty

        SwipeableRecyclerViewTouchListener swipeTouchListener = new SwipeableRecyclerViewTouchListener(eventRecyclerView, new SwipeableRecyclerViewTouchListener.SwipeListener() {
            @Override
            public boolean canSwipeLeft(int position) {
                return false;
            }

            @Override
            public boolean canSwipeRight(int position) {
                return true;
            }

            @Override
            public void onDismissedBySwipeLeft(RecyclerView recyclerView, int[] reverseSortedPositions) {

            }

            @Override
            public void onDismissedBySwipeRight(RecyclerView recyclerView, int[] reverseSortedPositions) {
                for(int position : reverseSortedPositions){
                    Event event = eventArrayList.get(position);
                    Event secondEvent = new Event(event.getTitle(), event.getDescription(), event.getRRULE(), event.getDuration(), event.getDTSTART() + 3600000, event.getDTEND(), event.getLAST_DATE());
                    saveEvent(secondEvent);
                    Log.i(TAG, "onDismissedBySwipeRight: SavedEvent");
                    eventArrayList.remove(position);
                    adapter.notifyItemRemoved(position);
                }
                adapter.notifyDataSetChanged();
            }
        });
        eventRecyclerView.addOnItemTouchListener(swipeTouchListener);
    }
}
