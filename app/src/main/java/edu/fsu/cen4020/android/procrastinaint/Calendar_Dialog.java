package edu.fsu.cen4020.android.procrastinaint;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Calendar_Dialog extends AppCompatDialogFragment {
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.calendar_dialog, null);

        Bundle bundle = getArguments();
        final Long dateEpoch = bundle.getLong("TIME");
        String date = ReadCalendarActivity.epochToDate(dateEpoch);


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
