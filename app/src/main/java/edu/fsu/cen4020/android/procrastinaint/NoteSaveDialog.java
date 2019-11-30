package edu.fsu.cen4020.android.procrastinaint;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class NoteSaveDialog extends AppCompatDialogFragment {

    private EditText editTextFilename;
    private NoteSaveDialogListener listener;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_note_dialog, null);

        builder.setView(view)
                .setTitle("Enter Filename")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String filename = editTextFilename.getText().toString();
                        listener.applyText(filename);
                        //NoteEditorActivity.save();
                    }

                });
        editTextFilename = view.findViewById(R.id.edit_filename);
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (NoteSaveDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must use NoteSaveDialogListener");
        }
    }

    public interface NoteSaveDialogListener{
        void applyText(String filename);
    }
}
