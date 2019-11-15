package edu.fsu.cen4020.android.procrastinaint;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class calendar extends AppCompatActivity {

    //calendar activity referenced from youtube video
    private static final String TAG = "CalendarActivity";
    private CalendarView my_calendarView;
    private ImageButton menuButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        my_calendarView = findViewById(R.id.calendarView);
        menuButton = (ImageButton) findViewById(R.id.menuButton);
        my_calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override // i = year, i1 = month, i2 = day
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {


            }
        });

        menuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(calendar.this, DrawerActivity.class);
                startActivity(intent);
            }
        });
    }
}
