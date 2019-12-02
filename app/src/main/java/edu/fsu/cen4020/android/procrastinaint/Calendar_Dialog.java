package edu.fsu.cen4020.android.procrastinaint;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class Calendar_Dialog extends AppCompatDialogFragment {

    private ListView mListView;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.calendar_dialog, null);

        Bundle bundle = getArguments();
        final Long dateEpoch = bundle.getLong("TIME");
        String date = ReadCalendarActivity.epochToDate(dateEpoch);


        mListView = (ListView) view.findViewById(R.id.calendar_lv);


        final Cursor mCursor = getContext().getContentResolver().query(MainCP.CONTENT_URI,null,null,
                null, MainCP.DTSTART);

        final List<String> list = new ArrayList<String>();

        while(mCursor.moveToNext()){
            if (mCursor.getLong(5) > dateEpoch && mCursor.getLong(5) < dateEpoch + 86340000){
                String finished = "\n";
                finished += mCursor.getString(1);
                finished += "\n\n";
                finished += ReadCalendarActivity.epochToTime( mCursor.getLong(5));
                finished += " - ";
                finished += ReadCalendarActivity.epochToTime(mCursor.getLong(6));
                Log.i("LOL", "Description is here");
                if (mCursor.getString(2) != null) {
                    finished += "\n\n";
                    finished += mCursor.getString(2);
                }
                finished += "\n";


                list.add(finished);

            }
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getContext().getApplicationContext(),
                android.R.layout.simple_list_item_1,
                list );

        mListView.setAdapter(arrayAdapter);

        builder.setView(view)
                .setTitle(date)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
        return builder.create();

    }
}
