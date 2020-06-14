package com.example.bmi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BmiFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private Spinner activitySpinner;
    private EditText avgCalorieText, todayCalorieText;
    private Button updateButton;
    private DatabaseReference perdayCalRef, todayCalRef, activityLevelRef;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private int selectedItem;

    private View view;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_bmi, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Initialise();

        activityLevelRef.keepSynced(true);
        todayCalRef.keepSynced(true);
        perdayCalRef.keepSynced(true);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.activity_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(adapter);
        activitySpinner.setOnItemSelectedListener(this);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //String activityLevel = activitySpinner.getSelectedItem().toString().trim();
                String activityLevel = Integer.toString(selectedItem);
                String perdayCalorie = avgCalorieText.getText().toString().trim();
                String todayCalorie = todayCalorieText.getText().toString().trim();

                progressBar.setVisibility(View.VISIBLE);

                try
                {
                    perdayCalRef.child(firebaseAuth.getCurrentUser().getUid()).child("calorie").setValue(perdayCalorie);
                    todayCalRef.child(firebaseAuth.getCurrentUser().getUid()).child("calorie").setValue(todayCalorie);
                    activityLevelRef.child(firebaseAuth.getCurrentUser().getUid()).child("activity").setValue(activityLevel).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), "Successfully Updated", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                progressBar.setVisibility(View.INVISIBLE);
                                Toast.makeText(getContext(), task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                catch (Exception e)
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                //progressBar.setVisibility(View.INVISIBLE);
            }
        });

    }

    public void Initialise()
    {
        activitySpinner = view.findViewById(R.id.activityLevel_bmi_frag_id);
        avgCalorieText = view.findViewById(R.id.calIntake_bmi_frag_id);
        todayCalorieText = view.findViewById(R.id.todayCalIntake_bmi_frag_id);
        updateButton = view.findViewById(R.id.updateButton_bmi_frag_id);
        progressBar = view.findViewById(R.id.progressBar_bmi_frag_id);

        firebaseAuth = FirebaseAuth.getInstance();
        perdayCalRef = FirebaseDatabase.getInstance().getReference("perday_calorie");
        todayCalRef = FirebaseDatabase.getInstance().getReference("today_calorie");
        activityLevelRef = FirebaseDatabase.getInstance().getReference("activity_level");
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String activityLevel = parent.getItemAtPosition(position).toString();
        selectedItem = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
