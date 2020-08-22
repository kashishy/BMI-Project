package com.example.bmi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner genderSpinner, activitySpinner;
    private EditText userName, userEmail, userMobile, userAge, userPassword, userConfirmPassword, userWeight, userHeight;
    private TextView loginText;
    private Button registerButton;
    private ProgressBar progressBar;
    FirebaseAuth firebaseAuth;
    DatabaseReference databaseReference;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        genderSpinner = findViewById(R.id.gender_spinner_id);
        userName = findViewById(R.id.name_signup_id);
        userEmail = findViewById(R.id.email_signup_id);
        userAge = findViewById(R.id.age_signup_id);
        userMobile = findViewById(R.id.mobile_signup_id);
        userPassword = findViewById(R.id.password_signup_id);
        userConfirmPassword = findViewById(R.id.confirm_password_signup_id);
        userWeight = findViewById(R.id.weight_signup_id);
        userHeight = findViewById(R.id.height_signup_id);
        loginText = findViewById(R.id.login_signup_id);
        registerButton = findViewById(R.id.register_button_id);
        progressBar = findViewById(R.id.progressBar_singup_id);
        activitySpinner = findViewById(R.id.activity_spinner_id);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.gender_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genderSpinner.setAdapter(adapter);
        genderSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this,
                R.array.activity_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(adapter1);
        activitySpinner.setOnItemSelectedListener(this);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference= FirebaseDatabase.getInstance().getReference("users_data");
        databaseReference.keepSynced(true);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (checkDataEntered()) {

                    progressBar.setVisibility( View.VISIBLE);

                    firebaseAuth.createUserWithEmailAndPassword(userEmail.getText().toString().trim(), userPassword.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        String name, email, mobile, age, gender, weight, height, activity;
                                        name = userName.getText().toString().trim();
                                        email = userEmail.getText().toString().trim();
                                        mobile = userMobile.getText().toString().trim();
                                        age = userAge.getText().toString().trim();
                                        gender = genderSpinner.getSelectedItem().toString().trim();
                                        weight = userWeight.getText().toString().trim();
                                        height = userHeight.getText().toString().trim();
                                        activity = activitySpinner.getSelectedItem().toString().trim();


                                        UserData userData = new UserData(name, email, mobile, age, gender, weight, height, activity);

                                        try {
                                            firebaseUser = firebaseAuth.getCurrentUser();
                                            databaseReference.child(firebaseAuth.getCurrentUser().getUid()).setValue(userData);
                                        }
                                        catch (Exception e)
                                        {
                                            Toast.makeText(SignUpActivity.this, e.getMessage(),Toast.LENGTH_SHORT).show();
                                        }
                                        UserProfileChangeRequest userProfileChangeRequest = new UserProfileChangeRequest.Builder()
                                                .setDisplayName(userName.getText().toString().trim())
                                                .build();
                                        firebaseUser.updateProfile(userProfileChangeRequest).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    // Toast.makeText(registeractivity.this, "Upload Successfull", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                        progressBar.setVisibility(View.INVISIBLE);
                                        finish();
                                        Toast.makeText(SignUpActivity.this, "Registration Successfull", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                                        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(intent);
                                    } else {
                                        progressBar.setVisibility(View.INVISIBLE);
                                        Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        loginText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    boolean isEmail(EditText text){
        CharSequence email = text.getText().toString().trim();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
    boolean isEmpty(EditText text){
        //Toast.makeText(SignUpActivity.this,text.getText().toString(), Toast.LENGTH_SHORT).show();
        CharSequence string = text.getText().toString().trim();
        return (TextUtils.isEmpty(string));
    }
    boolean checkDataEntered()
    {
        if(isEmpty(userName))
        {
            userName.setError("Enter Name");
            userName.requestFocus();
            return false;
        }
        if(!isEmail(userEmail))
        {
            userEmail.setError("Incorrect Email");
            userEmail.requestFocus();
            return false;
        }
        else if(isEmpty(userMobile)){
            userMobile.setError("Enter Mobile Number");
            userMobile.requestFocus();
            return false;
        }
        else if(userMobile.getText().toString().length()!=10){

            userMobile.setError("Must 10 Digits" +userMobile.toString().trim().length());
            userMobile.requestFocus();
            return false;
        }
        else if(isEmpty(userAge)){
            userAge.setError("Enter Age");
            userAge.requestFocus();
            return false;
        }
        else if(isEmpty(userPassword))
        {
            userPassword.setError("Enter Password");
            userPassword.requestFocus();
            return false;
        }
        else if(userPassword.length()<6){
            userPassword.setError("Minimum length of password is 6");
            userPassword.requestFocus();
            return  false;
        }
        else if(!userPassword.getText().toString().contentEquals(userConfirmPassword.getText().toString().trim()))
        {
            userConfirmPassword.setError("Password does not match");
            userConfirmPassword.requestFocus();
            return false;
        }
        else
        {
            return true;
        }
    }
}
