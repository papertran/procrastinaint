package edu.fsu.cen4020.android.procrastinaint;

import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class HelperEventNagvigatorTimeActivityInterface extends AppCompatActivity {

    private Button addButton;
    private EditText name;
    private EditText description;
    private Button startDate;
    private Button endRange;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helper_event_nagvigator_time_interface);

        addButton = (Button) findViewById(R.id.AddEvent);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {


            }
        });

        




    }

}
