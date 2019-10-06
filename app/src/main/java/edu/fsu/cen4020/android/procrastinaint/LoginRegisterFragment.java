package edu.fsu.cen4020.android.procrastinaint;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;



public class LoginRegisterFragment extends Fragment {


    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login_register, container, false);

        emailEditText = (EditText) rootView.findViewById(R.id.emailEditText);
        passwordEditText = (EditText) rootView.findViewById(R.id.passwordEditText);
        loginButton = (Button) rootView.findViewById(R.id.loginButton);
        registerButton = (Button) rootView.findViewById(R.id.registerButton);


        return rootView;
    }
}
