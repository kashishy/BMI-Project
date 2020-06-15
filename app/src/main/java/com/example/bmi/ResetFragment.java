package com.example.bmi;

import android.content.Intent;
import android.os.Bundle;
import android.os.strictmode.WebViewMethodCalledOnWrongThreadViolation;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ResetFragment extends Fragment {

    private View view;
    private FirebaseAuth firebaseAuth;
    private EditText emailText;
    private TextView registerText;
    private Button sendButton;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_passowrd_reset, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Initialised();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkDataEntered())
                {
                    progressBar.setVisibility(View.VISIBLE);
                    String email = emailText.getText().toString().toLowerCase().trim();

                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if(task.isSuccessful())
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), "Password reset link sent to your email address.", Toast.LENGTH_LONG).show();
                                Intent intent=new Intent(getContext(),LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK |Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                            else
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(),task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
                }
            }
        });

        registerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    public void Initialised()
    {
        emailText = view.findViewById(R.id.email_passwordReset_frag_id);
        sendButton = view.findViewById(R.id.sendButton_passwordReset_frag_id);
        progressBar = view.findViewById(R.id.progressBar_reset_frag_id);
        registerText = view.findViewById(R.id.registerText_reset_frag_id);

        firebaseAuth = FirebaseAuth.getInstance();
    }

    boolean isEmail(EditText text){
        CharSequence email=text.getText().toString().trim();
        return (!TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches());
    }
    boolean isEmpty(EditText text){
        CharSequence string =text.getText().toString().trim();
        return (TextUtils.isEmpty(string));
    }
    boolean checkDataEntered()
    {
        if(!isEmail(emailText))
        {
            emailText.setError("Incorrect Email");
            return false;
        }
        else
        {
            return true;
        }
    }
}
