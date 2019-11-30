package edu.fsu.cen4020.android.procrastinaint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class EventAdderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private String TAG = EventAdderActivity.class.getCanonicalName();
    private Long CdateSolo;   //This contains the date of the solo date of the event
    private Long CdateRepeatStart;    // Contains the starting date
    private Long CdateRepeatEnd;      // Contains the Ending date
    private String StartTimeHour="";
    private String StartTimeMin="";
    private String EndTimeHour="";
    private String EndTimeMin="";
    private Long StartMilli;
    private Long EndMilli;
    public int datepick = 0;   // This is used to determine which date picker was last clicked
    private int start_or_end = 0;
    private boolean startTimeSelected = false;
    private boolean endTimeSelected = false;
    private boolean dateSoloSelected = false;
    private boolean dateRepeatStartSelected = false;
    private boolean dateRepeatEndSelected = false;
    private Button startButton;
    private Button endButton;
    private Button datePicker;
    private Button startDate;
    private Button endDate;
    private Button addButton;
    private EditText TITLE;
    private EditText Description;
    private Boolean Reoccurr_or_not=false;
    private Boolean Firebase = false;
    private CheckBox sunday;
    private CheckBox monday;
    private CheckBox tuesday;
    private CheckBox wednesday;
    private CheckBox thursday;
    private CheckBox friday;
    private CheckBox saturday;


    FirebaseAuth auth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_adder);


        auth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

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

        // This section is for when we add the event

        TITLE = (EditText) findViewById(R.id.EventTitle);
        Description = (EditText) findViewById(R.id.Description);

        sunday = (CheckBox) findViewById(R.id.sundayCheckBox);
        monday = (CheckBox) findViewById(R.id.mondayCheckBox);
        tuesday = (CheckBox) findViewById(R.id.tuesdayCheckBox);
        wednesday = (CheckBox) findViewById(R.id.wednesdayCheckBox);
        thursday = (CheckBox) findViewById(R.id.thursdayCheckBox);
        friday = (CheckBox) findViewById(R.id.fridayCheckBox);
        saturday = (CheckBox) findViewById(R.id.saturdayCheckBox);


        addButton = (Button) findViewById(R.id.AddEvent);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {

                Boolean errorCheck = false;
                String title = "";
                String description = "";

                if (TITLE.getText().toString() == "")
                {
                    errorCheck = true;
                    TITLE.setError("Title is empty");
                }
                else {
                    title = TITLE.getText().toString();
                }
                if (Description.getText().toString() != "") {
                    description = Description.getText().toString();
                }
                // Add Firebase thingy here

                if (!Reoccurr_or_not){
                    Long startEpoch = Long.valueOf(0);
                    Long endEpoch = Long.valueOf(0);


                    if (!startTimeSelected || !endTimeSelected || !dateSoloSelected) {
                        errorCheck = true;
                        Log.i("LOL", "Made it here");
                        Toast.makeText(getApplicationContext(), "Error, The times and dates are not filled out.", Toast.LENGTH_LONG).show();


                    }
                    else {
                        startEpoch = CdateSolo + StartMilli;
                        endEpoch = CdateSolo + EndMilli;
                        if (endEpoch < startEpoch){
                            Toast.makeText(getApplicationContext(), "Error, The end time is before the start time.", Toast.LENGTH_LONG).show();

                        }
                    }

                    // Add to ContentProvider
                    if (!errorCheck) {
                        ContentValues mNewValues = new ContentValues();
                        mNewValues.put(MainCP.TITLE, title);
                        mNewValues.put(MainCP.DESCRIPTION, description);
                        mNewValues.put(MainCP.DTSTART, startEpoch);
                        mNewValues.put(MainCP.DTEND, endEpoch);
                        mNewValues.put(MainCP.LAST_DATE, endEpoch);
                        mNewValues.put(MainCP.NEW, 1);
                        getContentResolver().insert(MainCP.CONTENT_URI, mNewValues);
                        if (Firebase) {
                            uploadEventToFirebase(mNewValues);
                        }
                       //TODO Go back to main activity?

                    }


                }
                else{
                    // If reoccuring it needs to not have the seconds added
                    String repeatRule = "";
                    String duration = "";
                    Long startEpoch = Long.valueOf(0);
                    Long endDate = Long.valueOf(0);
                    if (dateRepeatStartSelected && dateRepeatEndSelected && startTimeSelected && endTimeSelected) {
                        startEpoch = CdateRepeatStart + StartMilli;
                        repeatRule = "FREQ=WEEKLY;UNTIL=" + CdateRepeatEnd + ";WKST=SU;BYDAY=";
                        endDate = CdateRepeatEnd;

                        if (endDate < startEpoch){
                            errorCheck = true;
                            Toast.makeText(getApplicationContext(), "Error, The end date is before the start date.", Toast.LENGTH_LONG).show();
                        }


                        Long seconds = (EndMilli - StartMilli) / 1000;
                        if (seconds < 0)
                        {
                            errorCheck = true;
                            Toast.makeText(getApplicationContext(), "Error, The end time is before the start time.", Toast.LENGTH_LONG).show();
                        }

                        duration = "P" + seconds.toString() + "S";
                        Log.i("LOL", duration);
                    }
                    else{
                        errorCheck = true;
                        Toast.makeText(getApplicationContext(), "Error, The times and dates are not filled out.", Toast.LENGTH_LONG).show();

                    }
                    boolean dateSelected = false;

                    if (friday.isChecked()){
                        repeatRule +="FR,";
                        dateSelected = true;
                    }

                    if (monday.isChecked()) {
                        repeatRule += "MO,";
                        dateSelected = true;

                    }

                    if (saturday.isChecked()){
                        repeatRule +="SA,";
                        dateSelected = true;

                    }

                    if (sunday.isChecked()){
                        repeatRule +="SU,";
                        dateSelected = true;

                    }

                    if (thursday.isChecked()) {
                        repeatRule += "TH,";
                        dateSelected = true;

                    }

                    if (tuesday.isChecked()){
                        repeatRule += "TU,";
                        dateSelected = true;

                    }

                    if (wednesday.isChecked()){
                        repeatRule += "WE,";
                        dateSelected = true;

                    }

                    if (!dateSelected)
                    {
                        errorCheck = true;
                        Toast.makeText(getApplicationContext(), "Error, no days are selected.", Toast.LENGTH_LONG).show();
                    }else{
                        String temp = "";

                        for (int x = 0; x <  repeatRule.length()-1; x++){
                            temp += repeatRule.charAt(x);
                        }
                        repeatRule = temp;
                    }

                    String temp = "";

                    for (int x = 0; x <  repeatRule.length()-1; x++){
                        temp += repeatRule.charAt(x);
                    }
                    repeatRule = temp;



                    if (!errorCheck) {
                        ContentValues mNewValues = new ContentValues();
                        mNewValues.put(MainCP.TITLE, title);
                        mNewValues.put(MainCP.DESCRIPTION, description);
                        mNewValues.put(MainCP.RRule, repeatRule);
                        mNewValues.put(MainCP.DURATION, duration);
                        mNewValues.put(MainCP.DTSTART, startEpoch);
                        mNewValues.put(MainCP.LAST_DATE, endDate);
                        mNewValues.put(MainCP.NEW, 1);
                        getContentResolver().insert(MainCP.CONTENT_URI,mNewValues);

                        if (Firebase) {
                            uploadEventToFirebase(mNewValues);
                        }

                    }

                }


            }
        });



    }

    @Override
    public void onDateSet(DatePicker view, int year , int month, int day){
        Calendar c = Calendar.getInstance();
        c.setTimeZone(TimeZone.getTimeZone("America/New_York"));
        c.setTimeInMillis(18000000);
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, day);
        Long time = c.getTimeInMillis();
        Log.i(TAG, "onDateSet: calander = " + c.getTimeInMillis());
        Log.i(TAG, "onDateSet: corrected time = " + ReadCalendarActivity.epochToDate(time));
        Log.i(TAG, "onDateSet: corrected time time = " + ReadCalendarActivity.epochToTime(time));


        String currentDateString = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());

        String tempStr = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());
        if (datepick == 0) {
            CdateSolo = time;
            datePicker = (Button) findViewById(R.id.Date_picker_nonreoccurring);
            datePicker.setText(tempStr);
            dateSoloSelected = true;
        }

        else if (datepick == 1){
            CdateRepeatStart = time;
            startDate = (Button) findViewById(R.id.Date_picker_reoccurring_start);
            startDate.setText(tempStr);
            dateRepeatStartSelected = true;

        }

        else if (datepick == 2){
            CdateRepeatEnd = time;
            endDate = (Button) findViewById(R.id.Date_picker_reoccurring_end);
            endDate.setText(tempStr);
            dateRepeatEndSelected = true;
        }

    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hour, int minute) {

        int milli = hour*3600000+ minute *60000;

        if (start_or_end == 0) {
            StartTimeHour = Integer.toString(hour);
            if (minute < 10){
                StartTimeMin = "0";
                StartTimeMin += Integer.toString(minute);
            }
            else {
                StartTimeMin = Integer.toString(minute);
            }
            endButton = (Button) findViewById(R.id.end_time);
            String tempStr = StartTimeHour + ":" + StartTimeMin;
            startButton.setText(tempStr);
            StartMilli = Long.valueOf(milli);
            startTimeSelected = true;

        }

        else{
            EndTimeHour= Integer.toString(hour);
            if ( minute < 10)
            {
                EndTimeMin = "0";
                EndTimeMin +=Integer.toString(minute);
            }
            else {
                EndTimeMin = Integer.toString(minute);
            }
            endButton = (Button) findViewById(R.id.end_time);
            String tempStr = EndTimeHour + ":" + EndTimeMin;
            endButton.setText(tempStr);
            EndMilli = Long.valueOf(milli);
            endTimeSelected = true;

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
                    Reoccurr_or_not = true;

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
                    Reoccurr_or_not = false;
                }
            case R.id.firebase_upload:
            // This case is for the firebase upload

            if (checked){
                Firebase = true;
            } else{
                Firebase = false;
            }


        }
    }

    private void uploadEventToFirebase(ContentValues value){
        if (auth.getCurrentUser() == null){
            Toast.makeText(this, "Must be signed in to upload to events to firebase", Toast.LENGTH_SHORT).show();
        } else{


            Event event = new Event();

            event.setTitle(value.getAsString(MainCP.TITLE));
            event.setDescription(value.getAsString(MainCP.DESCRIPTION));

            try {
                event.setRRULE(value.getAsString(MainCP.RRule));
            } catch (Exception e){
                event.setRRULE(null);
            }

            try {
                event.setDTSTART(value.getAsLong(MainCP.DTSTART));
            } catch (Exception e){
                event.setDTSTART(null);
            }

            try {
                event.setDTEND(value.getAsLong(MainCP.DTEND));
            } catch (Exception e){
                event.setDTEND(null);
            }


            try {
                event.setLAST_DATE(value.getAsLong(MainCP.LAST_DATE));
            } catch (Exception e){
                event.setLAST_DATE(null);
            }

            //TODO upload to firebase
            mDatabase.child("Events").child(event.getTitle()).setValue(event);

        }
    }
}
