package edu.fsu.cen4020.android.procrastinaint;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;


//  replacement drawer implementation from: https://stackoverflow.com/questions/19442378/navigation-drawer-to-switch-activities-instead-of-fragments

public class BaseDrawerActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{

    DrawerLayout drawerLayout;
    Toolbar toolbar;
    FrameLayout frameLayout;
    NavigationView navigationView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_base_drawer);;

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        frameLayout = (FrameLayout) findViewById(R.id.content_frame);

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        //to prevent current item select over and over
        if (item.isChecked()){
            drawerLayout.closeDrawer(GravityCompat.START);
            return false;
        }

        if (id == R.id.nav_home) {
            // Handle home
            startActivity(new Intent(getApplicationContext(), calendar.class));
        } else if (id == R.id.nav_login) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        } else if (id == R.id.nav_newevent) {
            startActivity(new Intent(getApplicationContext(), Event.class));
        } else if (id == R.id.nav_read_cal) {
            startActivity(new Intent(getApplicationContext(), ReadCalendarActivity.class));
        } else if (id == R.id.nav_export) {
            startActivity(new Intent(getApplicationContext(), EventAdderActivity.class));
        } else if (id == R.id.nav_timer) {
            startActivity(new Intent(getApplicationContext(), Pomodoros.class));
        } else if (id == R.id.nav_notes) {
            startActivity(new Intent(getApplicationContext (), NotesActivity.class));
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}