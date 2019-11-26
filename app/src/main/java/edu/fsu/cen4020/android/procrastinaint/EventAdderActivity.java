package edu.fsu.cen4020.android.procrastinaint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.TableRow;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class EventAdderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private String TAG = EventAdderActivity.class.getCanonicalName();
    private String CdateSolo="";   //This contains the date of the solo date of the event
    private String CdateRepeatStart;    // Contains the starting date
    private String CdateRepeatEnd;      // Contains the Ending date
    private String StartTimeHour="";
    private String StartTimeMin="";
    private String EndTimeHour="";
    private String EndTimeMin="";
    public int datepick = 0;   // This is used to determine which date picker was last clicked
    private int start_or_end = 0;
    private Button startButton;
    private Button endButton;
    private Button datePicker;
    private Button startDate;
    private Button endDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_adder);

        // This button is for the Solo date picker
        datePicker = (Button) findViewById(R.id.Date_picker_nonreoccurring);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datepick = 0;
                DialogFragment date = new DatePickerFragment();
                date.show(getSupportFragmentManager(), "date picker");
            }
        });

        startDate = (Button) findViewById(R.id.Date_picker_reoccurring_start);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datepick = 1;
                DialogFragment date = new DatePickerFragment();
                date.show(getSupportFragmentManager(), "date picker start");
            }
        });

        endDate = (Button) findViewById(R.id.Date_picker_reoccurring_end);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datepick = 2;
                DialogFragment date = new DatePickerFragment();
                date.show(getSupportFragmentManager(), "date picker end");
            }
        });

        startButton = (Button) findViewById(R.id.start_time);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_or_end = 0;
                DialogFragment startTimePicker = new TimePickerFragment();
                startTimePicker.show(getSupportFragmentManager(), "time picker");

            }
        });

        endButton = (Button) findViewById(R.id.end_time);
        endButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                start_or_end = 1;
                DialogFragment startTimePicker = new TimePickerFragment();
                startTimePicker.show(getSupportFragmentManager(), "time picker");

            }
        });



    }

    @Override
    public void onDateSet(DatePicker view, int year , int month, int day){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        String currentDateString = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        String tempStr = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());
        if (datepick == 0) {
            CdateSolo = currentDateString;
            datePicker = (Button) findViewById(R.id.Date_picker_nonreoccurring);
            datePicker.setText(tempStr);
            Toast.makeText(getApplicationContext(), CdateSolo, Toast.LENGTH_LONG).show();
        }

        else if (datepick == 1){
            CdateRepeatStart = currentDateString;
            startDate = (Button) findViewById(R.id.Date_picker_reoccurring_start);
            startDate.setText(tempStr);
            Toast.makeText(getApplicationContext(), CdateRepeatStart, Toast.LENGTH_LONG).show();

        }

        else if (datepick == 2){
            CdateRepeatEnd = currentDateString;
            endDate = (Button) findViewById(R.id.Date_picker_reoccurring_end);
            endDate.setText(tempStr);
            Toast.makeText(getApplicationContext(), CdateRepeatEnd, Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {

        if (start_or_end == 0) {
            StartTimeHour = Integer.toString(hour);
            StartTimeMin = Integer.toString(minute);
            endButton = (Button) findViewById(R.id.end_time);
            String tempStr = StartTimeHour + ":" + StartTimeMin;
            startButton.setText(tempStr);
            Toast.makeText(getApplicationContext(), "Start Time " + StartTimeHour + ":" + StartTimeMin, Toast.LENGTH_LONG).show();
        }

        else{
            EndTimeHour= Integer.toString(hour);
            EndTimeMin= Integer.toString(minute);
            endButton = (Button) findViewById(R.id.end_time);
            String tempStr = EndTimeHour + ":" + EndTimeMin;
            endButton.setText(tempStr);
            Toast.makeText(getApplicationContext(), "End Time " + EndTimeHour + ":" + EndTimeMin, Toast.LENGTH_LONG).show();

        }


    }

    public void onCheckboxClicked(View view) {
        // Is the view now checked?
        boolean checked = ((CheckBox) view).isChecked();

        // Check which checkbox was clicked
        switch(view.getId()) {
            case R.id.reoccurringCheckBox:
            // This section handles the reoccurring checkbox visibility

                TableRow nonRe = (TableRow) findViewById(R.id.nonreoccurringDatePickerRow);
                TableRow reStart = (TableRow) findViewById(R.id.reoccurringDatePickerStart);
                TableRow reEnd = (TableRow) findViewById(R.id.reoccurringDatePickerEnd);

                TableRow repeat = (TableRow) findViewById(R.id.reoccurringDatePickerText);
                TableRow sunday = (TableRow) findViewById(R.id.sundayRow);
                TableRow monday = (TableRow) findViewById(R.id.mondayRow);
                TableRow tuesday = (TableRow) findViewById(R.id.tuesdayRow);
                TableRow wednesday = (TableRow) findViewById(R.id.wednesdayRow);
                TableRow thursday = (TableRow) findViewById(R.id.thursdayRow);
                TableRow friday = (TableRow) findViewById(R.id.fridayRow);
                TableRow saturday = (TableRow) findViewById(R.id.saturdayRow);

                if (checked) {
                    nonRe.setVisibility(View.GONE);
                    repeat.setVisibility(View.VISIBLE);
                    sunday.setVisibility(View.VISIBLE);
                    monday.setVisibility(View.VISIBLE);
                    tuesday.setVisibility(View.VISIBLE);
                    wednesday.setVisibility(View.VISIBLE);
                    thursday.setVisibility(View.VISIBLE);
                    friday.setVisibility(View.VISIBLE);
                    saturday.setVisibility(View.VISIBLE);
                    reStart.setVisibility(View.VISIBLE);
                    reEnd.setVisibility(View.VISIBLE);

                }
                else {
                    nonRe.setVisibility(View.VISIBLE);
                    repeat.setVisibility(View.GONE);
                    sunday.setVisibility(View.GONE);
                    monday.setVisibility(View.GONE);
                    tuesday.setVisibility(View.GONE);
                    wednesday.setVisibility(View.GONE);
                    thursday.setVisibility(View.GONE);
                    friday.setVisibility(View.GONE);
                    saturday.setVisibility(View.GONE);
                    reStart.setVisibility(View.GONE);
                    reEnd.setVisibility(View.GONE);
                }
            case R.id.firebase_upload:
            // This case is for the firebase upload

                if (checked)
                break;
            else
                break;
        }
    }
}
