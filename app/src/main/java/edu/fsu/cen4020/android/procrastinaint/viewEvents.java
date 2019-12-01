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

        String[] projection = {
                MainCP.TITLE,
                MainCP.RRule,
                MainCP.DURATION,
                MainCP.DTSTART,
                MainCP.DTEND,
                MainCP.LAST_DATE};
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
                    eventArrayList.add(event);
                }while(cursor.moveToNext());
            }
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
