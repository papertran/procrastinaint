package edu.fsu.cen4020.android.procrastinaint;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getCanonicalName();

    private Button loginRegisterButton;
    private Button EventAdderButton;
    private Button notesButton;
    private Button rwCalendarButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // This assigns the loginButton and assigns a listener for when the user clicks on it
        loginRegisterButton = (Button) findViewById(R.id.loginRegisterButton);
        loginRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick: Login Button Called");
                onLoginRegisterButtonClicked();

            }
        });

        EventAdderButton = (Button) findViewById(R.id.AddEventButton);
        EventAdderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)  {
                Log.i(TAG, "onClick: Event Add Button Called");
                onEventAdderButtonClicked();
            }
        });

        notesButton = (Button) findViewById(R.id.notesButton);
        notesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onNotesButtonClicked();
            }
        });


        rwCalendarButton = (Button) findViewById(R.id.rwCalendarButton);
        rwCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRWCalendarButtonClicked();
            }
        });
    }

    public void onLoginRegisterButtonClicked(){

    }

    public void onEventAdderButtonClicked(){

    }

    public void onNotesButtonClicked(){

    }

    public void onRWCalendarButtonClicked(){

    }


}
