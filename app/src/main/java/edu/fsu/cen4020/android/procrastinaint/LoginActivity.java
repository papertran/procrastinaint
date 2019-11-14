package edu.fsu.cen4020.android.procrastinaint;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    // firebase.google.com/docs/auth/android/start
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private Button redirectSignUpButton;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        // If user is signed in then end this activity
        if(auth.getCurrentUser() != null){
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }

        setContentView(R.layout.activity_login);


        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        loginButton = (Button) findViewById(R.id.loginButton);
        redirectSignUpButton = (Button) findViewById(R.id.redirectSignUpButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login(view);
            }
        });

        redirectSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });

    }

    private void login(View view){
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        if(TextUtils.isEmpty(email)){
            emailEditText.setError("Enter email address!");
            return;
        }

        if(TextUtils.isEmpty(password)){
            passwordEditText.setError("Enter password!");
            return;
        }

        if(password.length() < 6){
            passwordEditText.setError("Minimum 6 characters!");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        // Login the user
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                // If sign in fails display message, else sign in
                progressBar.setVisibility(View.GONE);
                if(task.isSuccessful()) {
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
