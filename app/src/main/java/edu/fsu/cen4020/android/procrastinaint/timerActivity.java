package edu.fsu.cen4020.android.procrastinaint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.widget.NumberPicker;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.EventListener;
import java.util.Locale;

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


    private DatabaseReference mDatabase;
    private DatabaseReference usernameRef;
    private FirebaseAuth auth;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String TEXT = "text";
    public static final String PCOUNT = "pCount";

    private String text;
    private long storeCounter;


    private String userID;
    NumberPicker minutePicker;
    Button pomodoroButton;
    Button breakButton;
    TextView minuteView;
    TextView messageView;
    TextView pomodoroMessage;
    TextView pomodoroCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        pomodoroButton= (Button) findViewById(R.id.pomodoroButton);
        breakButton = (Button) findViewById(R.id.breakButton);
        breakButton.setVisibility(View.INVISIBLE);
        minuteView= (TextView) findViewById(R.id.minuteView);
        messageView = (TextView) findViewById(R.id.messageView);
        pomodoroMessage = (TextView) findViewById(R.id.pomodoroMessage);
        pomodoroCounter = (TextView) findViewById(R.id.pomodoroCounter);
        pomodoroCounter.setVisibility(View.VISIBLE);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() != null){
            userID = auth.getCurrentUser().getUid();
        } else {
            userID = null;
        }

        usernameRef = mDatabase.child("UserPomodoroInfo").child(userID);

        usernameRef.addListenerForSingleValueEvent(eventListener);

        Log.i(TAG, "THIS BIDDIE" + usernameRef);

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

                if(pCounter == 4)
                {
                    saveData();
                }

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



    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putString(TEXT, userID);
        editor.putLong(PCOUNT, pCounter);

        editor.apply();

        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();
    }

    private void writeData(String username, long OverallTime, long OverallPomodoro, long GoldenTomatoes,
                           long GlobalPomodoro){
        Pomodoros pomodoro = new Pomodoros(username, OverallTime, OverallPomodoro, GoldenTomatoes, GlobalPomodoro);

        mDatabase.child("UserPomodoroInfo").child(username).setValue(pomodoro);
    }
    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        text = sharedPreferences.getString(TEXT, "");
        storeCounter = sharedPreferences.getLong(PCOUNT, 0);
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
                Log.i(TAG, "already has user" + dataSnapshot);
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {
            Log.i(TAG, databaseError.getMessage());
        }
    };



}
