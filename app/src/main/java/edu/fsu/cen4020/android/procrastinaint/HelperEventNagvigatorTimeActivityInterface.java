package edu.fsu.cen4020.android.procrastinaint;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.TimeZone;

public class HelperEventNagvigatorTimeActivityInterface extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private Button addButton;
    private EditText name;
    private EditText description;
    private EditText hours;
    private EditText minutes;
    private Button startDate;
    private Button endDate;
    private int whichDateIsSelected=0;
    private Long startRange;
    private Long endRange;
    private boolean startRangeSelected = false;
    private boolean endRangeSelected = false;

    // these strings are used for the intent
    public String NAME = "NAME";
    public String DESCRIPTION = "DESCRIPTION";
    public String TIME = "TIME";
    public String STARTDATE = "STARTDATE";
    public String ENDDATE = "ENDDATE";

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
            case R.id.nav_write_to_firebase:
                startActivity(new Intent(getApplicationContext(), AddUploadedEventsActivity.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_helper_event_nagvigator_time_interface);

        addButton = (Button) findViewById(R.id.AddEvent);
        hours = (EditText) findViewById(R.id.hours);
        minutes = (EditText) findViewById(R.id.mins_wack);
        name = (EditText) findViewById(R.id.Name);
        description = (EditText) findViewById(R.id.Description);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                Boolean errorCheck = false;
                String nameStr = "";
                String descriptionStr = "";
                Long startDate = Long.valueOf(0);
                Long endDate = Long.valueOf(0);
                Long duration = Long.valueOf(0);

                //error checking section
                if (name.getText().toString().matches(""))
                {
                    errorCheck = true;
                    Toast.makeText(getApplicationContext(), "Error, the name is empty", Toast.LENGTH_LONG).show();
                }
                else {
                    nameStr = name.getText().toString();
                }

                if (!description.getText().toString().matches("")) {
                    descriptionStr = description.getText().toString();
                }

                if (!startRangeSelected || !endRangeSelected){
                    errorCheck = true;
                    Toast.makeText(getApplicationContext(), "Error, the dates are not filled out.", Toast.LENGTH_LONG).show();
                }
                else{

                    startDate = startRange;
                    endDate = endRange;
                    if (endDate < startDate){
                        errorCheck = true;
                        Toast.makeText(getApplicationContext(), "Error, the end date is before the start time.", Toast.LENGTH_LONG).show();
                    }
                }

                if (hours.getText().toString().matches("") || minutes.getText().toString().matches(""))
                {

                    errorCheck = true;
                    Toast.makeText(getApplicationContext(), "Error, the duration is empty.", Toast.LENGTH_LONG).show();
                }
                else{

                        //turns hours and minutes into milliseconds
                        duration += Long.parseLong(hours.getText().toString())*3600000;


                        duration += Long.parseLong(minutes.getText().toString())*60000;

                }

                if (!errorCheck){
                    //If all goes well send the info to the next page

                    Intent I = new Intent(getApplicationContext(), HENTimePicker.class );
                    I.putExtra(NAME, nameStr);
                    I.putExtra(DESCRIPTION, descriptionStr);
                    I.putExtra(TIME, duration);
                    I.putExtra(STARTDATE, startDate);
                    I.putExtra(ENDDATE, endDate);
                    startActivity(I);
                }

            }
        });

        // These are for the date pickers
        startDate = (Button) findViewById(R.id.start_date);
        startDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                whichDateIsSelected = 0;
                DialogFragment date = new DatePickerFragment();
                date.show(getSupportFragmentManager(), "date picker");
            }
        });

        endDate = (Button) findViewById(R.id.end_date);
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                whichDateIsSelected = 1;
                DialogFragment date = new DatePickerFragment();
                date.show(getSupportFragmentManager(), "date picker");
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
        String currentDateString = DateFormat.getDateInstance(DateFormat.MEDIUM).format(c.getTime());

        // 0 for start 1 for end
        if (whichDateIsSelected == 0){
            startDate = (Button) findViewById(R.id.start_date);
            startDate.setText(currentDateString);
            startRange = time;
            startRangeSelected = true;
        }
        else {
            endDate = (Button) findViewById(R.id.end_date);
            endDate.setText(currentDateString);
            endRange = time;
            endRangeSelected = true;
        }

    }
}
