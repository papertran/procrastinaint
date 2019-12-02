package edu.fsu.cen4020.android.procrastinaint;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

import java.util.Calendar;
import java.util.TimeZone;

public class calendar extends AppCompatActivity {

    //calendar activity referenced from youtube video
    private static final String TAG = "CalendarActivity";
    private CalendarView my_calendarView;
    private FirebaseAuth auth;
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate and add items to actionbar
        getMenuInflater().inflate(R.menu.drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                startActivity(new Intent(getApplicationContext(), calendar.class));
                return true;
            case R.id.nav_login:
                startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                return true;
            case R.id.nav_logout:
                if(auth.getCurrentUser() != null){
                    auth.signOut();
                }
                return true;
            case R.id.nav_newevent:
                startActivity(new Intent(getApplicationContext(), EventAdderActivity.class));
                return true;
            case R.id.nav_read_cal:
                startActivity(new Intent(getApplicationContext(), ReadCalendarActivity.class));
                return true;
            case R.id.nav_hentimer:
                startActivity(new Intent(getApplicationContext(), HelperEventNagvigatorTimeActivityInterface.class));
                return true;
            case R.id.nav_timer:
                startActivity(new Intent(getApplicationContext(), timerActivity.class));
                return true;
            case R.id.nav_notes:
                startActivity(new Intent(getApplicationContext(), NotesActivity.class));
                return true;
            case R.id.nav_write_cal:
                startActivity(new Intent(getApplicationContext(), WriteCalendar.class));
                return true;
            default:
                    return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        auth = FirebaseAuth.getInstance();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calendar_layout);
        setTitle("Home");
        my_calendarView = findViewById(R.id.calendarView);
//        menuButton = (ImageButton) findViewById(R.id.menuButton);
        my_calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override // i = year, i1 = month, i2 = day
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int i, int i1, int i2) {

                Calendar c = Calendar.getInstance();
                c.setTimeZone(TimeZone.getTimeZone("America/New_York"));
                c.setTimeInMillis(18000000);
                c.set(Calendar.YEAR, i);
                c.set(Calendar.MONTH, i1);
                c.set(Calendar.DAY_OF_MONTH, i2);
                Long time = c.getTimeInMillis();
                openDialog(time);
            }
        });

//        menuButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(calendar.this, DrawerActivity.class);
//                startActivity(intent);
//            }
//        });


    }
    public void openDialog(Long time){
        Calendar_Dialog dialog = new Calendar_Dialog();
        Bundle args = new Bundle();
        args.putLong("TIME", time);
        dialog.setArguments(args);
        dialog.show(getSupportFragmentManager(), "Calendar Dialog");
    }
}
