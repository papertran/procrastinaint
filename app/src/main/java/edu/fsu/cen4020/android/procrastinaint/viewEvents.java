package edu.fsu.cen4020.android.procrastinaint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;

import static edu.fsu.cen4020.android.procrastinaint.ReadCalendarActivity.epochToDate;
import static edu.fsu.cen4020.android.procrastinaint.ReadCalendarActivity.epochToTime;

public class viewEvents extends AppCompatActivity {

    private static final String TAG = viewEvents.class.getCanonicalName();
    private ArrayList<String[]> eventArrayList = new ArrayList<String[]>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_events);


        final Cursor mCursor = getContentResolver().query(MainCP.CONTENT_URI, null, null,
                null, null);


        while (mCursor.getCount() > 0 && !mCursor.isLast()) {
            mCursor.moveToNext();

            String startDate = "";
            String endDate = "";
            String startTime = "";
            String endTime = "";

            String title = mCursor.getString(mCursor.getColumnIndex(MainCP.TITLE));
            String DTSTART = mCursor.getString(mCursor.getColumnIndex(MainCP.DTSTART));
            String DTEND = mCursor.getString(mCursor.getColumnIndex(MainCP.DTEND));
            String LAST_DATE = mCursor.getString(mCursor.getColumnIndex(MainCP.LAST_DATE));
            String DURATION = mCursor.getString(mCursor.getColumnIndex(MainCP.DURATION));
            Log.i(TAG, "onCreate: viewEvents" +
                    "\nTitle = " + title +
                    "\nDTSTART = " + DTSTART +
                    "\nDTEND = " + DTEND +
                    "\nLAST_DATE = " + LAST_DATE +
                    "\nDURATION = " + DURATION);

                        // Reuse code from ReadCalendarActivity
            if (DTSTART != null) {
                startDate = epochToDate(Long.parseLong(DTSTART));
                startTime = epochToTime(Long.parseLong(DTSTART));
            }

            if (LAST_DATE != null) {
                endDate = epochToDate(Long.parseLong(LAST_DATE));
            }

            // Check DT for correct end time
            if (DTEND != null ) {
                endDate = epochToDate(Long.parseLong(DTEND));
                endTime = epochToTime(Long.parseLong(LAST_DATE));
            } else {
                // DTEND is null if its a recurring event, then need to get time from duration
                Long newDuration = ReadCalendarActivity.RFC2445ToMilliseconds(DURATION);
                endTime = epochToTime(Long.parseLong(DTSTART) + newDuration);
            }


            String[] recylerViewItems = new String[]{
                    title,
                    startDate,
                    endDate,
                    startTime,
                    endTime,};
            eventArrayList.add(recylerViewItems);
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
