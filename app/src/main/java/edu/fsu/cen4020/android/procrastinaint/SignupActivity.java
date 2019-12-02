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

public class SignupActivity extends AppCompatActivity {


    // firebase.google.com/docs/auth/android/start
    private EditText emailEditText;
    private EditText passwordEditText;
    private Button signUpButton;
    private Button redirectLoginButton;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_signup_1);


        // Get Firebase Auth instance
        auth = FirebaseAuth.getInstance();

        // Register Elements
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        passwordEditText = (EditText) findViewById(R.id.passwordEditText);
        signUpButton = (Button) findViewById(R.id.signUpButton);
        redirectLoginButton = (Button) findViewById(R.id.redirectLoginButton);


        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp(view);
            }
        });

        redirectLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                // TODO Redirect to Login Activity
            }
        });
    }

    private void signUp(View view){
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


        // Create the user
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(
                SignupActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(SignupActivity.this, "createUserWithEmail:onComplete:"
                        + task.isSuccessful(), Toast.LENGTH_SHORT).show();

                        // If it fails then tell user it failed, else sign in user

                        if(task.isSuccessful()){
                            startActivity(new Intent(SignupActivity.this, MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

}
