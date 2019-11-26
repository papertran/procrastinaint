package edu.fsu.cen4020.android.procrastinaint;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.os.CountDownTimer;

import android.os.Bundle;

import java.util.Locale;

public class timerActivity extends AppCompatActivity {

    private static final long START_MILLIS = 1500000;
//    private static final long START_MILLIS = 3000;
    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeft = START_MILLIS;
    private long breakTime = 300000;
//    private long breakTime = 5000;
    private long breakTimeLeft = breakTime;

    public int counter = 25;
    Button pomodoroButton;
    Button breakButton;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer);
        pomodoroButton= (Button) findViewById(R.id.pomodoroButton);
        breakButton = (Button) findViewById(R.id.breakButton);
        breakButton.setVisibility(View.INVISIBLE);
        textView= (TextView) findViewById(R.id.textView);
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
        mCountDownTimer = new CountDownTimer(mTimeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeft = millisUntilFinished;
                updateCountDownText(mTimeLeft);                }

            @Override
            public void onFinish() {
                textView.setText("You have stayed focused for 25 minutes!");
                pomodoroButton.setVisibility(View.INVISIBLE);
                breakButton.setVisibility(View.VISIBLE);
                mTimeLeft = START_MILLIS;
//                startBreak();
            }
        }.start();

        mTimerRunning = true;
    }

    private void startBreak(){
        mCountDownTimer = new CountDownTimer(breakTimeLeft, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                breakTimeLeft = millisUntilFinished;
                updateCountDownText(breakTimeLeft);
            }

            @Override
            public void onFinish() {
                textView.setText("5 minute break is done. Press button to start again.");
                pomodoroButton.setVisibility(View.VISIBLE);
                breakButton.setVisibility(View.INVISIBLE);
                breakTimeLeft = breakTime;

            }
        }.start();

    }

    private void updateCountDownText(long time){
        int minutes = (int) (time / 1000) / 60;
        int seconds = (int) (time / 1000) % 60;

        String timeLeft = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        textView.setText(timeLeft);

    }
}
