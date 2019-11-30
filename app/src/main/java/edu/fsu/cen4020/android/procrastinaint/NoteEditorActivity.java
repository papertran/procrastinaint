package edu.fsu.cen4020.android.procrastinaint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;

public class NoteEditorActivity extends AppCompatActivity  {

//    int noteId;
//    private String FILE_NAME = "example.txt";
//    public EditText mEditText;
//    private Button button;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_note_editor);
//
//        //Reference: https://www.youtube.com/watch?v=ARezg1D9Zd0
//        button = (Button) findViewById(R.id.dialog_button);
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openDialog();
//            }
//        });
//
//        //Text for the note that the user types
//        EditText editText = (EditText) findViewById(R.id.editText);
//
//        Intent intent = getIntent();
//        noteId = intent.getIntExtra("noteId", -1);
//
//        //The default value above make sure that we dont get passed an invalid noteId
//        if(noteId != -1 ) {
//            editText.setText(NotesActivity.notes.get(noteId));
//        }
//        else{
//            NotesActivity.notes.add("");
//            noteId = NotesActivity.notes.size()-1;
//            NotesActivity.arrayAdapter.notifyDataSetChanged();
//        }
//
//        editText.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//                NotesActivity.notes.set(noteId, String.valueOf(s));
//                NotesActivity.arrayAdapter.notifyDataSetChanged();
//
//                SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("edu.fsu.cen4020.android.procrastinaint", Context.MODE_PRIVATE);
//                HashSet<String> set = new HashSet(NotesActivity.notes);
//                sharedPreferences.edit().putStringSet("notes", set).apply();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });
//
//        //Saves context of note to mEditText
//        mEditText = findViewById(R.id.editText);
//    }
//
//    public void openDialog(){
//        NoteSaveDialog noteSaveDialog = new NoteSaveDialog();
//        noteSaveDialog.show(getSupportFragmentManager(), "example dialog");
//    }
//
//    //Reference: https://www.youtube.com/watch?v=EcfUkjlL9RI
//    public void save(View v) {
//
//        //Converts mEditText it to a string saving it to "text"
//        String text = mEditText.getText().toString();
//        FileOutputStream fos = null;
//
//        try {
//
//
//            //This changes the default save location of the FileOutputStream
//            File dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS);
//
//            dir.mkdirs();
//
//            //Creates a file output stream "dir" to write to the file "FILE_NAME"
//            fos = new FileOutputStream(new File(dir, FILE_NAME));
//
//            //Saves the content of "fos" to the internal storage
//            fos.write(text.getBytes());
//
//            //Prints a message to the user where the note was saved
//            Toast.makeText(this, "Saved to " + dir + "/" + FILE_NAME, Toast.LENGTH_LONG).show();
//        } catch (FileNotFoundException e){
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } finally {
//            if(fos != null){
//                try {
//                    fos.close();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//    }
//
//    @Override
//    public void applyText(String filename) {
//        FILE_NAME = filename;
//    }
}
