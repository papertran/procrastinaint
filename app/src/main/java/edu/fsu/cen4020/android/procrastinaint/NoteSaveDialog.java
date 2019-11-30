package edu.fsu.cen4020.android.procrastinaint;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class NoteSaveDialog extends AppCompatDialogFragment {

    private EditText editTextFilename;
    private NoteSaveDialogListener listener;
    private EditText mEditText = NoteEditorActivity.mEditText;
    private String FILE_NAME = NoteEditorActivity.FILE_NAME;

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
                        FILE_NAME = filename + ".txt";
                        listener.applyText(filename);
                        dialogSave(mEditText, FILE_NAME);
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

    public void dialogSave(EditText mEditText, String FILE_NAME){
        //Converts mEditText it to a string saving it to "text"
        String text = mEditText.getText().toString();
        FileOutputStream fos = null;

        try {


            //This changes the default save location of the FileOutputStream
            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);

            dir.mkdirs();

            //Creates a file output stream "dir" to write to the file "FILE_NAME"
            fos = new FileOutputStream(new File(dir, FILE_NAME));

            //Saves the content of "fos" to the internal storage
            fos.write(text.getBytes());

            //Prints a message to the user where the note was saved
            Toast.makeText(getContext(),"Saved to " + dir + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
