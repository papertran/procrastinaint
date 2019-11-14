package edu.fsu.cen4020.android.procrastinaint;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class NoteEditorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_editor);

        //Text for the note that the user types
        EditText editText = (EditText) findViewById(R.id.editText);

        Intent intent = getIntent();
        final int noteId = intent.getIntExtra("noteId", -1);

        //The default value above make sure that we dont get passed an invalid noteId
        if(noteId != -1 ) {
            editText.setText(NotesActivity.notes.get(noteId));
        }

        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                NotesActivity.notes.set(noteId, String.valueOf(s));
                NotesActivity.arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
