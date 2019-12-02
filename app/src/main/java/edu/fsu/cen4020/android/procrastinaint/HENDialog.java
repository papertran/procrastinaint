package edu.fsu.cen4020.android.procrastinaint;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatDialogFragment;

public class HENDialog extends AppCompatDialogFragment {

    TextView timeString;
    EditText hours;
    EditText minutes;
    Spinner AM_PM;



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.hen_dialog, null);

        Bundle bundle = getArguments();
        final Long start = bundle.getLong("START");
        final Long end = bundle.getLong("END");
        final Long duration = bundle.getLong("DURATION");
        final String name = bundle.getString("NAME");
        final String description = bundle.getString("DESCRIPTION");

        Log.i("LOL", "Duration is " + ReadCalendarActivity.epochToTime(duration));


        Long end_minus_duration = end - duration;

        String start_time = ReadCalendarActivity.epochToTime(start);
        String end_time = ReadCalendarActivity.epochToTime(end_minus_duration);

        timeString = (TextView) view.findViewById(R.id.time_str);
        timeString.setText(start_time + " - " + end_time);


        final Spinner spinner = (Spinner) view.findViewById(R.id.AM_PM);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.PM_AM, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        builder.setView(view)
                .setTitle("Confirm")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                })
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        hours = (EditText) view.findViewById(R.id.hour_id);
                        minutes = (EditText) view.findViewById(R.id.min_id);
                        boolean errorCheck = false;


                        if (hours.getText().toString().matches("") || minutes.getText().toString().matches("")){
                            errorCheck = true;
                            Toast.makeText(view.getContext(), "Please enter the time.", Toast.LENGTH_LONG).show();

                        }
                        else {

                            String temp = hours.getText().toString();
                            int hours_given = Integer.parseInt(temp);
                            temp = minutes.getText().toString();

                            int minutes_given = Integer.parseInt(temp);

                            if (hours_given > 12 || 0 > hours_given) {
                                errorCheck = true;
                                Toast.makeText(view.getContext(), "The hour given is not correct", Toast.LENGTH_LONG).show();
                            }

                            if (minutes_given > 59 || minutes_given < 0) {
                                errorCheck = true;
                                Toast.makeText(view.getContext(), "The minutes given is not correct", Toast.LENGTH_LONG).show();
                            }

                            if (!errorCheck) {

                                Long startOfDayTimes = start / 86400000;
                                startOfDayTimes = startOfDayTimes * 86400000;
                                startOfDayTimes = startOfDayTimes + 18000000;


                                String text = spinner.getSelectedItem().toString();

                                if (text.matches("AM"))
                                {
                                    if (hours_given == 12) {
                                        hours_given = hours_given - 12;
                                    }
                                }
                                else{
                                    if (hours_given != 12) {
                                        hours_given = hours_given + 12;
                                    }
                                }



                                startOfDayTimes = startOfDayTimes + hours_given * 3600000 + minutes_given * 60000;

                                Log.i("LOL", "" + ReadCalendarActivity.epochToDate(startOfDayTimes) + " " + ReadCalendarActivity.epochToTime(startOfDayTimes));



                                if (!errorCheck){
                                    ContentValues mNewValues = new ContentValues();
                                    mNewValues.put(MainCP.TITLE, name);
                                    mNewValues.put(MainCP.DESCRIPTION, description);
                                    mNewValues.put(MainCP.DTSTART, startOfDayTimes);
                                    mNewValues.put(MainCP.DTEND, startOfDayTimes+ duration);
                                    mNewValues.put(MainCP.LAST_DATE, startOfDayTimes + duration);
                                    mNewValues.put(MainCP.NEW, 1);
                                    getContext().getContentResolver().insert(MainCP.CONTENT_URI, mNewValues);
                                }
                            }
                        }



                    }
                });
        return builder.create();
    }
}
