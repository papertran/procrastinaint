package edu.fsu.cen4020.android.procrastinaint;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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


public class LoginRegisterFragment extends Fragment {
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

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!validateForm()){
                    return;
                }
                else{
                }
            }
        });
        return rootView;
    }

    // Check if email and password fields are filled out
    private boolean validateForm(){
        boolean valid = true;

        email = emailEditText.getText().toString();
        if (TextUtils.isEmpty(email)){
            emailEditText.setError("Required.");
            valid = false;
        } else {
            emailEditText.setError(null);
        }

        password = passwordEditText.getText().toString();
        if(TextUtils.isEmpty(password)){
            passwordEditText.setError("Required.");
            valid = false;
        } else{
            passwordEditText.setError("Required.");
        }

        return valid;
    }

}
