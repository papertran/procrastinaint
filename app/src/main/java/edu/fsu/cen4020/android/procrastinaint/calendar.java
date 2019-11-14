package edu.fsu.cen4020.android.procrastinaint;

import android.content.Intent;
import android.os.Bundle;
import android.widget.CalendarView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class calendar extends AppCompatActivity {

    private static final String TAG = "CalendarActivity";

    private CalendarView my_calendarView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        my_calendarView = findViewById(R.id.calendarView);

        my_calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override // i = year, i1 = month, i2 = day
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {


            }
        });
    }
}
