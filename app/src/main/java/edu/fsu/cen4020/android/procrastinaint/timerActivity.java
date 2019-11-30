package edu.fsu.cen4020.android.procrastinaint;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.CountDownTimer;
import android.widget.NumberPicker;

import android.os.Bundle;
import android.widget.Toast;

import org.w3c.dom.Text;

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

        minutePicker = (NumberPicker) findViewById(R.id.minutePicker);

        minutePicker.setMinValue(0);
        minutePicker.setMaxValue(60);
        minutePicker.setValue(25);

        minutePicker.setOnValueChangedListener(onValueChangeListener);

//        Log.i(TAG, "prevTime is in millisecs and minutes: " + prevTime + prevTime/60000);

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
                Log.i(TAG, "pCounter: " + pCounter);
                pomodoroCounter.setText(Integer.toString(pCounter));
            }
        }.start();

        mTimerRunning = true;
    }

    private void startBreak(){
        messageView.setVisibility(View.INVISIBLE);
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
}
