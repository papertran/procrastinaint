package edu.fsu.cen4020.android.procrastinaint;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

public class HENTimePicker extends AppCompatActivity {

    private String name;
    private String description;
    private Long duration;
    private Long startDate;
    private Long endDate;

    //The next strings are used to get from the intent
    public String NAME = "NAME";
    public String DESCRIPTION = "DESCRIPTION";
    public String TIME = "TIME";
    public String STARTDATE = "STARTDATE";
    public String ENDDATE = "ENDDATE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hentime_picker);

        Bundle extras = getIntent().getExtras();

        name = extras.getString(NAME);
        description = extras.getString(DESCRIPTION);
        duration = extras.getLong(TIME);
        startDate = extras.getLong(STARTDATE);
        endDate = extras.getLong(ENDDATE);


    }
}
