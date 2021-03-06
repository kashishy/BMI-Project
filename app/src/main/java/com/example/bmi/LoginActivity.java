package com.example.bmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.ContentLoadingProgressBar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "Login : ";
    private FirebaseAuth mAuth;
    private EditText userEmail;
    private EditText userPassword;
    private Button loginButton;
    private ProgressBar progressBar;
    private TextView signupText, passwordResetText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        userEmail = findViewById(R.id.user_email_id);
        userPassword = findViewById(R.id.user_password_id);
        loginButton = findViewById(R.id.loginButton_id);
        progressBar = findViewById(R.id.progressBar_id);
        signupText = findViewById(R.id.signup_login_id);
        passwordResetText = findViewById(R.id.passwordReset_login_id);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean DataCheck = checkDataEntered(userEmail, userPassword);

                if(DataCheck) {
                    progressBar.setVisibility( View.VISIBLE);
                    mAuth.signInWithEmailAndPassword(userEmail.getText().toString().toLowerCase().trim(), userPassword.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {

                                        progressBar.setVisibility(View.INVISIBLE);
                                        // Sign in success, update UI with the signed-in user's information
                                        Log.d(TAG, "signInWithEmail:success");
                                        Toast.makeText(LoginActivity.this, "Authentication successful.",
                                                Toast.LENGTH_SHORT).show();
                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        //updateUI(user);
                                    } else {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        // If sign in fails, display a message to the user.
                                        Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        Toast.makeText(LoginActivity.this, "Authentication failed. " +task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                        //updateUI(null);
                                    }

                                    // ...
                                }
                            });
                }
            }
        });

        signupText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });

        passwordResetText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout_login_id, new ResetFragment()).commit();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(mAuth.getCurrentUser()!=null){
            finish();
            Intent intent=new Intent(LoginActivity.this,MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        }
    }

    boolean isEmail(EditText text){
        CharSequence email=text.getText().toString().trim();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
    boolean isEmpty(EditText text){
        CharSequence string =text.getText().toString().trim();
        return (TextUtils.isEmpty(string));
    }
    boolean checkDataEntered(EditText UserEmail, EditText UserPassword)
    {
        if(!isEmail(UserEmail))
        {
            UserEmail.setError("Incorrect Email");
            return false;
        }
        else if(isEmpty(UserPassword))
        {
            UserPassword.setError("Enter Password");
            return false;
        }
        else if(UserPassword.length()<6){
            UserPassword.setError("Minimum length of password is 6");
            return  false;
        }
        else
        {
            return true;
        }
    }


}
