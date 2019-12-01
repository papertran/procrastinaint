package edu.fsu.cen4020.android.procrastinaint;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class HENTimePicker extends AppCompatActivity {

    private String name;
    private String description;
    private Long duration;
    private Long startDate;
    private Long endDate;
    private Long weekLengthInMilliseconds = Long.valueOf(604800000);
    private Cursor mCursor;
    private ListView lv;

    //The next strings are used to get from the intent
    public String NAME = "NAME";
    public String DESCRIPTION = "DESCRIPTION";
    public String TIME = "TIME";
    public String STARTDATE = "STARTDATE";
    public String ENDDATE = "ENDDATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hentime_picker);

        lv = (ListView) findViewById(R.id.listView);


        Bundle extras = getIntent().getExtras();

        name = extras.getString(NAME);
        description = extras.getString(DESCRIPTION);
        duration = extras.getLong(TIME);
        startDate = extras.getLong(STARTDATE);        // Start date is the morning of the day selected 12:00 AM
        endDate = extras.getLong(ENDDATE) + 86340000; // End date is the end of the day selected 11:59 PM


        mCursor = getContentResolver().query(MainCP.CONTENT_URI,null,null,
                null, MainCP.DTSTART);


        int myInt = 1;
        List<Pair<Long,Long>> list = new ArrayList<Pair<Long,Long>>();

        Long pairDtStart = startDate;
        Long pairDtEnd = Long.valueOf(0);
        boolean worksFine = false;

        while (mCursor.moveToNext()){
            if (mCursor.getString(4) == null) {
                Long tempval = mCursor.getLong(5);
                if (tempval > startDate){
                    pairDtEnd = mCursor.getLong(5); // This gets the start of the next event
                    Pair<Long,Long> temp = new Pair<>(pairDtStart,pairDtEnd); // The start and end times of possible time slots

                    if (pairDtEnd-pairDtStart >= duration) {
                        list.add(temp);
                    }
                    pairDtStart = mCursor.getLong(6);
                }
                if (tempval > endDate){
                    pairDtEnd = endDate;
                    if (pairDtEnd-pairDtStart >= duration) {
                        Pair<Long,Long> temp = new Pair<>(pairDtStart,pairDtEnd);
                        list.add(temp);
                        worksFine = true;
                    }
                    break;
                }
            }
        }

        if (!worksFine)
        {
            pairDtEnd = endDate;
            if (pairDtEnd-pairDtStart >= duration) {
                Pair<Long, Long> temp = new Pair<>(pairDtStart, pairDtEnd);
                list.add(temp);
            }

        }

        List<String> start_to_end = new ArrayList<String>();

        for ( int x =0; x < list.size(); x++){
            String temp = "Between " + ReadCalendarActivity.epochToDate(list.get(x).first) + " " +
                    ReadCalendarActivity.epochToTime(list.get(x).first)  + " and " +
                    ReadCalendarActivity.epochToDate(list.get(x).second) + " " +
                    ReadCalendarActivity.epochToTime(list.get(x).second);
            Log.i("LOL", temp);
        }
    }
}
