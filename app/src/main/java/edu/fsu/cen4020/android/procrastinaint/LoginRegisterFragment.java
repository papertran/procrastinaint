package edu.fsu.cen4020.android.procrastinaint;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;



public class LoginRegisterFragment extends Fragment implements View.OnClickListener{

    // TODO More options for password not long enough, change UI on signin
    private String TAG = LoginRegisterFragment.class.getCanonicalName();

    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button registerButton;
    private TextView statusTextView;
    private String email;
    private String password;

    // firebase.google.com/docs/auth/android/start
    private FirebaseAuth mAuth;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_login_register, container, false);

        emailEditText = (EditText) rootView.findViewById(R.id.emailEditText);
        passwordEditText = (EditText) rootView.findViewById(R.id.passwordEditText);
        loginButton = (Button) rootView.findViewById(R.id.loginButton);
        registerButton = (Button) rootView.findViewById(R.id.registerButton);
        statusTextView = (TextView) rootView.findViewById(R.id.statusTextView);

        if(mAuth.getCurrentUser() == null){
            statusTextView.setText("No User Signed In");
        }

        loginButton.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        return rootView;
    }

    // Provides the actual onClick Implementation
    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.loginButton){
            if(!validateForm()){
                return;
            }
            else{
                LoginUser();
            }
        }
        else if(view.getId() == R.id.registerButton){
            if(!validateForm()){
                return;
            }
            else{
                RegisterUser();
            }
        }
    }
    // Check if email and password fields are filled out
    private boolean validateForm(){
        boolean valid = true;

        email = emailEditText.getText().toString();
        if (TextUtils.isEmpty(email)){
            emailEditText.setError("Required.");
            valid = false;
        }
        else if(validateEmail(emailEditText) == false){ //error check if email has valid format
            emailEditText.setError("Enter valid email!");
        }
        else {
            emailEditText.setError(null);
        }

        password = passwordEditText.getText().toString();
        if(TextUtils.isEmpty(password)){
            passwordEditText.setError("Required.");
            valid = false;
        } else{
            passwordEditText.setError(null);
        }

        return valid;
    }

    private boolean validateEmail(EditText text) //function that checks for email format
    {
        CharSequence tempEmail = text.getText().toString();
        return(!TextUtils.isEmpty(tempEmail) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }

    private void RegisterUser(){
        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener( (Activity) getContext(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Registration Passed", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                        else{
                            Toast.makeText(getContext(), "Registration Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void LoginUser(){
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener((Activity) getContext(),
                new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getContext(), "Login Passed", Toast.LENGTH_SHORT).show();
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }
                        else{
                            Toast.makeText(getContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void updateUI(FirebaseUser user){
        if (user != null ){
            statusTextView.setText(user.getUid());
        }
        else{
            statusTextView.setText("Not signed in");
        }
    }
}
