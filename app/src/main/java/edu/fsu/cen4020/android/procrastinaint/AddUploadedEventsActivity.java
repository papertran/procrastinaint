package edu.fsu.cen4020.android.procrastinaint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ContentValues;
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
    private ArrayList<Event> eventArrayList = new ArrayList<Event>();

    private EditText searchEventEditText;
    DatabaseReference mRef =  FirebaseDatabase.getInstance().getReference("Events");
    RecyclerView firebaseEventsRecyclerView;
     EventRecyclerViewAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_uploaded_events);
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView eventRecyclerView = (RecyclerView) findViewById(R.id.firebaseEventRecyclerView);
        adapter = new EventRecyclerViewAdapter(this, eventArrayList);
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

                    saveEvent(event);
                    Log.i(TAG, "onDismissedBySwipeRight: SavedEvent");
                    eventArrayList.remove(position);
                    adapter.notifyItemRemoved(position);
                }
                adapter.notifyDataSetChanged();
            }
        });
        eventRecyclerView.addOnItemTouchListener(swipeTouchListener);


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

                    eventArrayList.add(event);

                }
                adapter.notifyDataSetChanged();

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    public void saveEvent(Event item){
        // Track if event is reoccuring or singular
        boolean flag = false;
        if(item.getRRULE() == null){
            flag = true;
        }
        if(flag) {
            Log.i(TAG, "saveButton ReoccuringEvent " +
                    "\nTitle =" + item.getTitle() +
                    "\nDTStart = " + item.getDTSTART() +
                    "\nRRule =" + item.getRRULE() +
                    "\nDuration = " + item.getDuration() +
                    "\nEnd Date = " + item.getEventEndDate());

            // Saves these values into content provider
            ContentValues values = new ContentValues();
            values.put(MainCP.TITLE, item.getTitle());
            values.put(MainCP.DTSTART, item.getDTSTART());
            values.put(MainCP.LAST_DATE, item.getLAST_DATE());
            values.put(MainCP.RRule, item.getRRULE());
            values.put(MainCP.DURATION, item.getDuration());
            values.put(MainCP.NEW, 0);
            getContentResolver().insert(MainCP.CONTENT_URI, values);
        }else{
            Log.i(TAG, "saveButton singularEvent " +
                    "\nTitle =" + item.getTitle() +
                    "\nDTStart ="  + item.getDTSTART() +
                    "\nDTEND = " + item.getDTEND());

            ContentValues values = new ContentValues();
            values.put(MainCP.TITLE, item.getTitle());
            values.put(MainCP.DTSTART, item.getDTSTART());
            values.put(MainCP.DTEND, item.getDTEND());
            values.put(MainCP.LAST_DATE, item.getLAST_DATE());
            values.put(MainCP.NEW, 0);
            getContentResolver().insert(MainCP.CONTENT_URI, values);
        }

    }
}
