package edu.fsu.cen4020.android.procrastinaint;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity implements MainFragment.MainFragmentListener {

    private String TAG = MainActivity.class.getCanonicalName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainFragment fragment = new MainFragment();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment)
                .addToBackStack(null)
                .commit();

    }

    @Override
    public void onLoginButtonClicked() {
        LoginFragment fragment = new LoginFragment();
        String tag = LoginFragment.class.getCanonicalName();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment, tag)
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void onRegisterButtonClicked() {
        RegisterFragment fragment = new RegisterFragment();
        String tag = RegisterFragment.class.getCanonicalName();
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main_container, fragment, tag)
                .addToBackStack(null)
                .commit();
    }
}
