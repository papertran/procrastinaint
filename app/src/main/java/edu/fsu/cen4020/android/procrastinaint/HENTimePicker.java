package edu.fsu.cen4020.android.procrastinaint;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
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

        Bundle extras = getIntent().getExtras();

        name = extras.getString(NAME);
        description = extras.getString(DESCRIPTION);
        duration = extras.getLong(TIME);
        startDate = extras.getLong(STARTDATE);
        endDate = extras.getLong(ENDDATE);


        mCursor = getContentResolver().query(MainCP.CONTENT_URI,null,null,
                null, MainCP.DTSTART);

        int counter = 0;

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
                    counter++;

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

        for ( int x =0; x < list.size(); x++){
            Log.i("LOL", "Start time = " + list.get(x).first + " End time = " + list.get(x).second);
        }

    }
}
