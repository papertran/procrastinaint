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
    private ArrayList<Event> eventArrayList = new ArrayList<Event>();
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

                    eventArrayList.add(event);

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
