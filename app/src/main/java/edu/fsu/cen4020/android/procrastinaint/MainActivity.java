package edu.fsu.cen4020.android.procrastinaint;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements MainFragment.MainFragmentListener {

    private String TAG = MainActivity.class.getCanonicalName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // On startup it will inflate this fragment into view
        MainFragment fragment = new MainFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onLoginRegisterButtonClicked() {
        // Test change
        LoginRegisterFragment fragment = new LoginRegisterFragment();
        String tag = LoginRegisterFragment.class.getCanonicalName();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    public void onEventAdderButtonClicked(){
        EventAdderFragment fragment = new EventAdderFragment();
        String tag = LoginRegisterFragment.class.getCanonicalName();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment, tag)
                .addToBackStack(null)
                .commit();
    }



}
