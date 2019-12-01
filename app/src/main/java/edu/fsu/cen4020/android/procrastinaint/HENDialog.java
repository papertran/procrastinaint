package edu.fsu.cen4020.android.procrastinaint;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatDialogFragment;

public class HENDialog extends AppCompatDialogFragment {
    public SeekBar mSeekBar;




    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        AlertDialog.Builder builder  = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.hen_dialog, null);

        mSeekBar = view.findViewById(R.id.seek_bar);

        Bundle bundle = getArguments();
        Long start = bundle.getLong("START");
        Long end = bundle.getLong("END");
        Long duration = bundle.getLong("DURATION");
        String name = bundle.getString("NAME");
        String description = bundle.getString("DESCRIPTION");




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

                    }
                });
        return builder.create();
    }
}
