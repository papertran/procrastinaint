package edu.fsu.cen4020.android.procrastinaint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.widget.NumberPicker;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.internal.ObjectConstructor;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Locale;
import java.util.prefs.PreferenceChangeEvent;


public class timerActivity extends AppCompatActivity {

    private static final String TAG = timerActivity.class.getCanonicalName();
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeft;
    private long prevTime;
//    private long breakTime = 300000;
    private long breakTime = 5000;
    private long breakTime2 = 900000;
    private long breakTimeLeft = breakTime;
    public int pCounter = 0;
    public int fullCounter;
    private ArrayList<Event> eventArrayList = new ArrayList<Event>();

    private Long currentTime;

    private DatabaseReference mDatabase;
    private DatabaseReference usernameRef;
    private FirebaseAuth auth;

    //FOR SHARED PREFERENCES
    private long AllTimeP;
    private long AllTimeGP;
    private long AllTimeTime;


    private String userID;
    NumberPicker minutePicker;
    Button pomodoroButton;
    Button breakButton;
    TextView minuteView;
    TextView messageView;
    TextView pomodoroMessage;
    TextView pomodoroCounter;
    FloatingActionButton floatingActionButton;

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
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Pomodoro");
        setContentView(R.layout.activity_timer);
        currentTime = System.currentTimeMillis();

        pomodoroButton= (Button) findViewById(R.id.pomodoroButton);
        breakButton = (Button) findViewById(R.id.breakButton);
        breakButton.setVisibility(View.INVISIBLE);
        minuteView= (TextView) findViewById(R.id.minuteView);
        messageView = (TextView) findViewById(R.id.messageView);
        pomodoroMessage = (TextView) findViewById(R.id.pomodoroMessage);
        pomodoroCounter = (TextView) findViewById(R.id.pomodoroCounter);
        pomodoroCounter.setVisibility(View.VISIBLE);
        floatingActionButton = (FloatingActionButton) findViewById(R.id.floatingActionButton);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null){
            userID = auth.getCurrentUser().getUid();
            usernameRef = mDatabase.child("UserPomodoroInfo").child(userID); //id for database user.
            usernameRef.addListenerForSingleValueEvent(eventListener); //create database entry if there isn't one for user
            mDatabase.addValueEventListener(eventListener);

        } else {
            userID = null;
        }



        minutePicker = (NumberPicker) findViewById(R.id.minutePicker);
        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(60);
        minutePicker.setValue(25);

        minutePicker.setOnValueChangedListener(onValueChangeListener);


        minuteView.setVisibility(View.INVISIBLE);
        pomodoroButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                startTimer();
            }
        });
        breakButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startBreak();
            }
        });


        String[] projection = {
                MainCP.TITLE,
                MainCP.RRule,
                MainCP.DURATION,
                MainCP.DTSTART,
                MainCP.DTEND,
                MainCP.LAST_DATE};

        String selection = MainCP.DTSTART + " >= ? AND " +
                MainCP.LAST_DATE + " <= ? ";

        Long secondTime = currentTime + 43200000;
        String[] selectionArgs = new String[]{
                currentTime.toString(),
                secondTime.toString()
        };

        Cursor cursor = getContentResolver().query(
                MainCP.CONTENT_URI,
                null,
                null,
                null,
                MainCP.DTSTART

        );

        // Get all events from cursor
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
                    Log.i(TAG, "onCreate: timerActivity event added");
                }while(cursor.moveToNext());
            }
        }

        initRecyclerView();


//        for(Event event : eventArrayList) {
//            Log.i(TAG, "timerActivity: \n" +
//                    "Title = " + event.getTitle() +
//                    "\nStart Date = " + event.getEventStartDate() +
//                    "\nEnd Date = " + event.getEventEndDate() +
//                    "\nStart Time= " + event.getEventStartTime() +
//                    "\nEnd Time = " + event.getEventEndTime());
//        }



    }

    private void initRecyclerView(){
        Log.d(TAG, "initRecyclerView: init recyclerview");
        RecyclerView eventRecyclerView = (RecyclerView) findViewById(R.id.readEventsRecyclerView);
        final EventRecyclerViewAdapter adapter = new EventRecyclerViewAdapter(this, eventArrayList);
        eventRecyclerView.setAdapter(adapter);
        eventRecyclerView.setLayoutManager(new LinearLayoutManager(this));



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
                    Log.i(TAG, "onDismissedBySwipeRight: SavedEvent");
                    eventArrayList.remove(position);
                    adapter.notifyItemRemoved(position);
                }
                adapter.notifyDataSetChanged();
            }
        });
        eventRecyclerView.addOnItemTouchListener(swipeTouchListener);
    }

    private void startTimer(){
        messageView.setVisibility(View.INVISIBLE);
        minuteView.setVisibility(View.VISIBLE);
        minutePicker.setVisibility(View.INVISIBLE);
        mCountDownTimer = new CountDownTimer(mTimeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeft = millisUntilFinished;
                updateCountDownText(mTimeLeft);                }


            @Override
            public void onFinish() {
                messageView.setVisibility(View.VISIBLE);
                messageView.setText("You have stayed focused for " + prevTime/60000 + " minute(s)");
                pomodoroButton.setVisibility(View.INVISIBLE);
                breakButton.setVisibility(View.VISIBLE);
                pCounter++;
                fullCounter++;
                AllTimeP++;
                AllTimeTime += prevTime/60000;

                if(pCounter == 4)
                {
                    AllTimeGP++;
                }

                Log.i(TAG, "All time pp is: " + AllTimeP);

                //STORE DATA TO THE FIREBASE
                if(auth.getCurrentUser() != null)
                {
                    mDatabase.child("UserPomodoroInfo").child(userID).child("overallPomodoro").setValue(AllTimeP);
                    mDatabase.child("UserPomodoroInfo").child(userID).child("goldenTomatoes").setValue(AllTimeGP);
                    mDatabase.child("UserPomodoroInfo").child(userID).child("overallTime").setValue(AllTimeTime);
                }


                floatingActionButton.setTooltipText("Your all time pomodoros is: " + AllTimeP +
                        "\nAll time golden tomatoes is: " + AllTimeGP +
                        "\nTotal minute(s) spent focused: " + AllTimeTime);


                pomodoroCounter.setText(Integer.toString(fullCounter));
            }
        }.start();

        mTimerRunning = true;
    }

    private void startBreak(){
        messageView.setVisibility(View.INVISIBLE);
        if(pCounter == 4)
        {
            breakTimeLeft = 10000;
            pCounter = 0;
        }
        mCountDownTimer = new CountDownTimer(breakTimeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                breakTimeLeft = millisUntilFinished;
                updateCountDownText(breakTimeLeft);
            }

            @Override
            public void onFinish() {
                messageView.setText("5 minute break is done. Press button to start again.");
                pomodoroButton.setVisibility(View.VISIBLE);
                breakButton.setVisibility(View.INVISIBLE);
                minutePicker.setVisibility(View.VISIBLE);

                mTimeLeft = prevTime;
                minutePicker.setOnValueChangedListener(onValueChangeListener);
                breakTimeLeft = breakTime;
                minuteView.setVisibility(View.INVISIBLE);

            }
        }.start();

    }

    private void updateCountDownText(long time){
        int minutes = (int) (time / 1000) / 60;
        int seconds = (int) (time / 1000) % 60;

        String timeLeft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        minuteView.setText(timeLeft);

    }

    NumberPicker.OnValueChangeListener onValueChangeListener =
            new NumberPicker.OnValueChangeListener() {
                @Override
                public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                    Toast.makeText(timerActivity.this,
                            "selected Number is" + picker.getValue(), Toast.LENGTH_SHORT);

                    mTimeLeft = minutePicker.getValue();
                    minutePicker.setValue(picker.getValue());
                    mTimeLeft *= 60000;
                    prevTime = mTimeLeft;

                }
            };

    private void writeData(String username, long OverallTime, long OverallPomodoro, long GoldenTomatoes,
                           long GlobalPomodoro){
        Pomodoros pomodoro = new Pomodoros(username, OverallTime, OverallPomodoro, GoldenTomatoes, GlobalPomodoro);

        mDatabase.child("UserPomodoroInfo").child(username).setValue(pomodoro);
    }

    ValueEventListener eventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            if(!dataSnapshot.exists())
            {

                Log.i(TAG, "no user for this guy yet" + dataSnapshot);
                writeData(userID, 0, 0, 0, 0);
            }
            else
            {
                Object temp = dataSnapshot.child("overallPomodoro").getValue();
//                AllTimeP = Long.parseLong(temp);
                if(temp != null) {
                    String porque = temp.toString();
                    AllTimeP = Long.parseLong(porque);
                    Log.i(TAG, "FUCK " + AllTimeP);
                }
//                Log.i(TAG, "already has user" + dataSnapshot + "plus this shit" + dataSnapshot.child("overallPomodoro").getValue());
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.i(TAG, databaseError.getMessage());
        }
    };

}
