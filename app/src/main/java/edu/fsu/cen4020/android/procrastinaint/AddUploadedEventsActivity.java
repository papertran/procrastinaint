package edu.fsu.cen4020.android.procrastinaint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static edu.fsu.cen4020.android.procrastinaint.ReadCalendarActivity.epochToDate;
import static edu.fsu.cen4020.android.procrastinaint.ReadCalendarActivity.epochToTime;

public class AddUploadedEventsActivity extends AppCompatActivity {

    private static final String TAG = AddUploadedEventsActivity.class.getCanonicalName();
    private ArrayList<String[]> eventArrayList = new ArrayList<String[]>();
    private EditText searchEventEditText;
    DatabaseReference mRef =  FirebaseDatabase.getInstance().getReference("Events");
    RecyclerView firebaseEventsRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_uploaded_events);


    }

    // https://www.youtube.com/watch?v=jEmq1B1gveM
    // Read from the FireBase

    @Override
    protected void onStart() {
        super.onStart();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                eventArrayList.clear();
                for(DataSnapshot eventSnapShot : dataSnapshot.getChildren()){
                    Event event = eventSnapShot.getValue(Event.class);
                    String title = event.getTitle();
                    Long DTSTART = event.getDTSTART();
                    Long DTEND = event.getDTEND();
                    Long LAST_DATE = event.getLAST_DATE();
                    String duration = event.getDuration();


                    String startDate = "";
                    String endDate = "";
                    String startTime = "";
                    String endTime = "";

                    // Reuse code from viewEvents
                    if (DTSTART != null) {
                        startDate = epochToDate(DTSTART);
                        startTime = epochToTime(DTSTART);
                    }

                    if (LAST_DATE != null) {
                        endDate = epochToDate(LAST_DATE);
                    }

                    // Check DT for correct end time
                    if (DTEND != null ) {
                        endDate = epochToDate(DTEND);
                        endTime = epochToTime(LAST_DATE);
                    } else {
                        // DTEND is null if its a recurring event, then need to get time from duration
                        Long newDuration = ReadCalendarActivity.RFC2445ToMilliseconds(duration);
                        endTime = epochToTime(DTSTART + newDuration);
                    }



                    Log.i(TAG, "viewFirebaseEventOnStart: " +
                            "\nevent Title = " + event.Title +
                            "\nevent Desc = " + event.Description +
                            "\nevent RRule = " + event.RRULE +
                            "\nevent DTSTART = " + event.DTSTART +
                            "\nevent DTEND = " + event.DTEND +
                            "\nevent LAST_DATE = " + event.LAST_DATE);



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

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView firebaseEventsRecyclerView= (RecyclerView) findViewById(R.id.firebaseEventRecyclerView);
        EventRecyclerViewAdapter adapter = new EventRecyclerViewAdapter(this, eventArrayList);
        firebaseEventsRecyclerView.setAdapter(adapter);
        firebaseEventsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }
}
