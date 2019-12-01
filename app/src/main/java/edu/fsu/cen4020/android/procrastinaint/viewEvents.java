package edu.fsu.cen4020.android.procrastinaint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.ArrayList;

import static edu.fsu.cen4020.android.procrastinaint.ReadCalendarActivity.epochToDate;
import static edu.fsu.cen4020.android.procrastinaint.ReadCalendarActivity.epochToTime;

public class viewEvents extends AppCompatActivity {

    private static final String TAG = viewEvents.class.getCanonicalName();
    private ArrayList<Event> eventArrayList = new ArrayList<Event>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);


        final Cursor cur = getContentResolver().query(MainCP.CONTENT_URI, null, null,
                null, null);


        while (cur.getCount() > 0 && !cur.isLast()) {
            cur.moveToNext();

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


            // Items to store into eventRecyclerView dataset
            Log.i(TAG, "readEvent: \n" +
                    "Title = " + event.getTitle() +
                    "\nStart Date = " + event.getEventStartDate() +
                    "\nEnd Date = " + event.getEventEndDate() +
                    "\nStart Time= " + event.getEventStartTime() +
                    "\nEnd Time = " + event.getEventEndTime());


            eventArrayList.add(event);

        }

        initRecyclerView();
    }




    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView eventRecyclerView = (RecyclerView) findViewById(R.id.readEventsRecyclerView);
        EventRecyclerViewAdapter adapter = new EventRecyclerViewAdapter(this, eventArrayList);
        eventRecyclerView.setAdapter(adapter);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
