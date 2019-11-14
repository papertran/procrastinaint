package edu.fsu.cen4020.android.procrastinaint;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;

public class EventAdderActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private String TAG = EventAdderActivity.class.getCanonicalName();
    private String CdateSolo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_adder);

        final Button datePicker = (Button) findViewById(R.id.Date_picker_nonreoccurring);
        datePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment date = new DatePickerFragment();
                date.show(getSupportFragmentManager(), "date pciker");
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

        CdateSolo = currentDateString;
        Toast.makeText(getApplicationContext(), CdateSolo, Toast.LENGTH_LONG).show();

    }
}
