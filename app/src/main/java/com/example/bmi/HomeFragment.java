package com.example.bmi;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Locale;
import java.util.zip.DeflaterOutputStream;

public class HomeFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    private View view;
    //private Spinner activitySpinner;
    //private TextView bmrText, totalEnergyText, perdayCalText, todayCalText, activityLevelText;
    private EditText genderText, ageText, weightText, heightText;
    private EditText bmrText, totalEnergyText, perdayCalText, activityLevelText, todayCalText;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference userDataReference, perdayCalorieReference, todayCalorieReference, activityLevelReference;
    private int selectedItem;
    private static String TAG = "Home Fragment : ";
    private String gender, perdayCalorie, todayCalorie, activityLevel;
    private double weight, height;
    private int age;
    private ProgressBar progressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Initialised();

        //userDataReference.keepSynced(true);
        //perdayCalorieReference.keepSynced(true);
        //activityLevelReference.keepSynced(true);
        //todayCalorieReference.keepSynced(true);
        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.activity_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(adapter);
        activitySpinner.setOnItemSelectedListener(this);*/

        RetrieveData();

    }

    public void Initialised() {

        //activitySpinner = view.findViewById(R.id.activitySpinner_home_frag_id);
        activityLevelText = view.findViewById(R.id.activityLevel_home_frag_id);
        bmrText = view.findViewById(R.id.bmr_home_frag_id);
        totalEnergyText = view.findViewById(R.id.totalenergy_home_frag_id);
        progressBar = view.findViewById(R.id.progressBar_home_frag_id);
        genderText = view.findViewById(R.id.gender_home_frag_id);
        ageText = view.findViewById(R.id.age_home_frag_id);
        weightText = view.findViewById(R.id.weight_home_frag_id);
        heightText = view.findViewById(R.id.height_home_frag_id);
        perdayCalText = view.findViewById(R.id.calIntake_home_frag_id);
        todayCalText = view.findViewById(R.id.todayCalIntake_home_frag_id);

        firebaseAuth = FirebaseAuth.getInstance();
        userDataReference = FirebaseDatabase.getInstance().getReference("users_data").child(firebaseAuth.getCurrentUser().getUid());
        perdayCalorieReference = FirebaseDatabase.getInstance().getReference("perday_calorie");
        todayCalorieReference = FirebaseDatabase.getInstance().getReference("today_calorie");
        activityLevelReference = FirebaseDatabase.getInstance().getReference("activity_level");
    }

    public void RetrieveData()
    {
        progressBar.setVisibility(View.VISIBLE);

        activityLevelReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    Log.d(TAG, "In activity level");
                    //activityLevel = dataSnapshot.child("activity").getValue().toString().trim();
                    selectedItem = Integer.parseInt(dataSnapshot.child("activity").getValue().toString().trim());
                    String[] levels = getResources().getStringArray(R.array.activity_array);
                    activityLevel = levels[selectedItem];
                }
                else
                {
                    Log.d(TAG, "In activity level not present");
                    activityLevel = "Not Recorded";
                }
                activityLevelText.setText(activityLevel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        userDataReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    gender = dataSnapshot.child("gender").getValue().toString().trim();
                    weight = Double.parseDouble(dataSnapshot.child("weight").getValue().toString().trim());
                    height = Double.parseDouble(dataSnapshot.child("height").getValue().toString().trim());
                    age = Integer.parseInt(dataSnapshot.child("age").getValue().toString().trim());
                    Log.d(TAG + "Gender  ", gender);
                    Log.d(TAG + "Weight  ", Double.toString(weight));
                    Log.d(TAG + "Height  ", Double.toString(height));
                    Log.d(TAG + "Age  ", Integer.toString(age));

                    double bmr = calculateBMR(gender, weight, height, age);
                    double calorie = calorieNeed(bmr);
                    UpdateUI(gender, age, weight, height, bmr, calorie);
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                progressBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), "No data found for this user", Toast.LENGTH_SHORT).show();
            }
        });

        /*activityLevelReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    Log.d(TAG, "In activity level");
                    //activityLevel = dataSnapshot.child("activity").getValue().toString().trim();
                    int selectedLevel = Integer.parseInt(dataSnapshot.child("activity").getValue().toString().trim());
                    String[] levels = getResources().getStringArray(R.array.activity_array);
                    activityLevel = levels[selectedLevel];
                }
                else
                {
                    Log.d(TAG, "In activity level not present");
                    activityLevel = "Not Recorded";
                }
                activityLevelText.setText(activityLevel);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });*/

        perdayCalorieReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    Log.d(TAG, "In perday calorie intake");
                    perdayCalorie = dataSnapshot.child("calorie").getValue().toString().trim();
                }
                else
                {
                    Log.d(TAG, "In perday calorie intake not present");
                    perdayCalorie = "Not Recorded";
                }
                perdayCalText.setText(perdayCalorie+" kcal");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        todayCalorieReference.child(firebaseAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.exists())
                {
                    todayCalorie = dataSnapshot.child("calorie").getValue().toString().trim();
                }
                else
                {
                    todayCalorie = "Not Recorded";
                }
                todayCalText.setText(todayCalorie+" kcal");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public double calculateBMR(String gender, double weight, double height, int age)
    {
        //Toast.makeText(getContext(), "Before BMR Calculate", Toast.LENGTH_SHORT).show();

        double bmr = 0.0;
        //Log.d(TAG + "BMR After ", Double.toString(bmr));

        /*Log.d(TAG + "Gender : ", gender);
        Log.d(TAG + "Weight : ", Double.toString(weight));
        Log.d(TAG + "Height : ", Double.toString(height));
        Log.d(TAG + "Age : ", Integer.toString(age));*/

        bmr = (10* weight) + (6.25* height) - (5* age);
        if(gender.equals("Male"))
        {
            bmr = bmr - 5;
        }
        else if(gender.equals("Female"))
        {
            bmr = bmr - 161;
        }
        //Toast.makeText(getContext(), "After BMR Calculate "+ bmr, Toast.LENGTH_SHORT).show();
        return bmr;
    }

    public double calorieNeed(double bmr)
    {
        //Toast.makeText(getContext(), "Before Calorie Calculate "+selectedItem, Toast.LENGTH_SHORT).show();
        double activityFactor = 0.0;

        if(selectedItem == 0)
        {
            activityFactor = 1.2;
        }
        else if(selectedItem == 1)
        {
            activityFactor = 1.375;
        }
        else if(selectedItem == 2)
        {
            activityFactor = 1.55;
        }
        else if(selectedItem == 3)
        {
            activityFactor = 1.725;
        }
        else if(selectedItem == 4)
        {
            activityFactor = 1.9;
        }

        double calorie = bmr * activityFactor;
        //Toast.makeText(getContext(), "After BMR Calculate "+ calorie, Toast.LENGTH_SHORT).show();
        return calorie;
    }

    public void UpdateUI(String gender, int age, double weight, double height, double bmr, double calorie)
    {
        //Toast.makeText(getContext(), "Before Update UI", Toast.LENGTH_SHORT).show();
        genderText.setText(gender);
        ageText.setText(String.format("%1$s",age));
        weightText.setText(String.format("%1$sKg", weight));
        heightText.setText(String.format("%1$scm",height));
        bmrText.setText(String.format("%1$s  kcal/day", bmr));
        totalEnergyText.setText(String.format("%1$s kcal/day", calorie));
        //perdayCalText.setText(perdayCalorie);
        //todayCalText.setText(todayCalorie);

        //Toast.makeText(getContext(), "After Update UI", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String activityLevel = parent.getItemAtPosition(position).toString();
        //selectedItem = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
