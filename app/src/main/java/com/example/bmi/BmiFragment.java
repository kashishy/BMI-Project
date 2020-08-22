package com.example.bmi;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bmi.Adapters.SearchedAdapter;
import com.example.bmi.Adapters.SelectedAdapter;
import com.example.bmi.Api.ApiClient;
import com.example.bmi.Api.ApiInterface;
import com.example.bmi.Model.Hints;
import com.example.bmi.Model.Root;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Console;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class BmiFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    //private Spinner activitySpinner;
    private EditText avgCalorie, todayCalorie;
    private Button updateButton;
    private DatabaseReference perdayCalRef, todayCalRef, activityLevelRef;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private int selectedItem;
    private String perdayCal;
    private String todayCal;
    private static final String TAG = "BMI Fragment : ";
    public ArrayList<String> selectedFoodIName;
    public ArrayList<String> selectedFoodCal;
    private ArrayList<String> foodItemName;
    private ArrayList<String> foodItemCal;
    private Button searchButton;
    private LinearLayout searchedLayout;
    private LinearLayout selectedLayout;
    private RecyclerView searchedRecycle;
    private RecyclerView selectedRecycle;
    private View view;
    private Double sum;

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

        Log.d(TAG, "In start function");
        //activityLevelRef.keepSynced(true);
        todayCalRef.keepSynced(true);
        //perdayCalRef.keepSynced(true);
        /*ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.activity_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(adapter);
        activitySpinner.setOnItemSelectedListener(this);*/

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*getActivity().getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                 */
                String foodName = todayCalorie.getText().toString();
                todayCalorie.setText("");
                getFood(foodName);
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkDataEntered())
                {
                    /*selectedItem = selectedItem - 1;
                    final String activityLevel = Integer.toString(selectedItem);*/
                    Double sum=0.0;
                    Log.d(TAG, "Before sum size : "+selectedFoodCal.size() +"    "+selectedFoodCal.get(0));
                    for (int i=0;i<selectedFoodCal.size();i++)
                    {
                        sum = sum + Double.parseDouble(selectedFoodCal.get(i));
                    }
                    final String todayCal = Double.toString(sum);
                    perdayCal = todayCal;
                    Log.d(TAG, "Perday cal before average : " +perdayCal);
                    perdayCalRef.child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists())
                            {
                                perdayCal = dataSnapshot.child("calorie").getValue().toString().trim();
                                double x = Double.parseDouble(perdayCal);
                                double y = Double.parseDouble(todayCal);
                                double ans = (x + y) / 2.0;
                                perdayCal = Double.toString(ans);
                                Log.d(TAG, "After update avergae " + perdayCal);
                                Log.d(TAG, "Just after average" +perdayCal );
                                progressBar.setVisibility(View.VISIBLE);

                                try
                                {
                                    Log.d(TAG, " In try update" + perdayCal);
                                    perdayCalRef.child(firebaseAuth.getCurrentUser().getUid()).child("calorie").setValue(perdayCal);
                                    //activityLevelRef.child(firebaseAuth.getCurrentUser().getUid()).child("activity").setValue(activityLevel);
                                    todayCalRef.child(firebaseAuth.getCurrentUser().getUid()).child("calorie").setValue(todayCal).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                    });;
                                    /*activityLevelRef.child(firebaseAuth.getCurrentUser().getUid()).child("activity").setValue(activityLevel).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                    });*/
                                }
                                catch (Exception e)
                                {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Log.d(TAG, "Just after average" +perdayCal );
                                progressBar.setVisibility(View.VISIBLE);

                                try
                                {
                                    Log.d(TAG, " In try update" + perdayCal);
                                    //activityLevelRef.child(firebaseAuth.getCurrentUser().getUid()).child("activity").setValue(activityLevel)
                                    perdayCalRef.child(firebaseAuth.getCurrentUser().getUid()).child("calorie").setValue(perdayCal);
                                    todayCalRef.child(firebaseAuth.getCurrentUser().getUid()).child("calorie").setValue(todayCal).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                    /*activityLevelRef.child(firebaseAuth.getCurrentUser().getUid()).child("activity").setValue(activityLevel).addOnCompleteListener(new OnCompleteListener<Void>() {
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
                                    });*/
                                }
                                catch (Exception e)
                                {
                                    progressBar.setVisibility(View.INVISIBLE);
                                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    public void Initialise()
    {
        //activitySpinner = view.findViewById(R.id.activityLevel_bmi_frag_id);
        todayCalorie = view.findViewById(R.id.todayCalIntake_bmi_frag_id);
        updateButton = view.findViewById(R.id.updateButton_bmi_frag_id);
        progressBar = view.findViewById(R.id.progressBar_bmi_frag_id);
        foodItemName = new ArrayList<String>();
        foodItemCal = new ArrayList<String>();
        selectedFoodIName = new ArrayList<String>();
        selectedFoodCal = new ArrayList<String >();
        searchButton = view.findViewById(R.id.searchButton_BMIFrag_id);
        searchedLayout = view.findViewById(R.id.searchedItem_layout_BMIFrag_id);
        selectedLayout = view.findViewById(R.id.selectedItem_layout_BMIFrag_id);
        searchedRecycle = view.findViewById(R.id.searchedRecyle_BMIFrag_id);
        searchedRecycle.setHasFixedSize(true);
        searchedRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));
        selectedRecycle = view.findViewById(R.id.selectedRecyle_BMIFrag_id);
        selectedRecycle.setHasFixedSize(true);
        selectedRecycle.setLayoutManager(new LinearLayoutManager(getActivity()));

        firebaseAuth = FirebaseAuth.getInstance();
        perdayCalRef = FirebaseDatabase.getInstance().getReference("perday_calorie");
        todayCalRef = FirebaseDatabase.getInstance().getReference("today_calorie");
        //activityLevelRef = FirebaseDatabase.getInstance().getReference("activity_level");
    }

    public void getFood(String foodName) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Root> call = apiInterface.getSearch(foodName);
        progressBar.setVisibility(View.VISIBLE);
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, retrofit2.Response<Root> response) {

                if(response.isSuccessful()) {
                    Root result = response.body();
                    Log.d(TAG, "Response : "+result.getText());
                    List<Hints> hints = result.getHints();
                    Log.d(TAG, hints.toString());
                    foodItemCal.clear();
                    foodItemName.clear();
                    for (int i=0; i<hints.size();i++)
                    {
                        Log.d(TAG, "Position : "+i+" : "+ hints.get(i).getFood().getLabel());
                        foodItemName.add(hints.get(i).getFood().getLabel());
                        foodItemCal.add(Double.toString(hints.get(i).getFood().getNutrients().getENERC_KCAL()));
                    }
                    progressBar.setVisibility(View.INVISIBLE);
                    SearchedAdapter adapter1 = new SearchedAdapter(foodItemName, foodItemCal,
                            selectedFoodIName, selectedFoodCal, selectedRecycle, searchedLayout, selectedLayout );
                    searchedRecycle.setAdapter(adapter1);
                    searchedLayout.setVisibility(View.VISIBLE);
                } else {
                    progressBar.setVisibility(View.INVISIBLE);
                    System.out.println(response.errorBody());
                    Log.d(TAG, "Error : "+response.message());
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                t.printStackTrace();
                progressBar.setVisibility(View.INVISIBLE);
            }
        });
    }

    public void update(){
        SelectedAdapter adapter2 = new SelectedAdapter(selectedFoodIName, selectedFoodCal);
        selectedRecycle.setAdapter(adapter2);
    }

    public void showSelectedItem(ArrayList<String> foodName, ArrayList<String> foodCal, RecyclerView selectedRecycle){
        Log.d(TAG, "Show Selected Food Size : "+foodName.size());
        this.selectedFoodIName = foodName;
        this.selectedFoodCal = foodCal;
        this.selectedRecycle = selectedRecycle;
        /*Double sum = 0.0;
        for (int i=0;i<selectedFoodCal.size();i++)
        {
            sum = sum + Double.parseDouble(selectedFoodCal.get(i));
        }
        this.sum = sum;*/
        update();
    }

    public void getCalSum(String sum)
    {
        todayCal = sum;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String activityLevel = parent.getItemAtPosition(position).toString();
        selectedItem = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public boolean isEmpty(EditText text){
        //Toast.makeText(SignUpActivity.this,text.getText().toString(), Toast.LENGTH_SHORT).show();
        CharSequence string = text.getText().toString().trim();
        return (TextUtils.isEmpty(string));
    }
    public boolean checkDataEntered()
    {
        /*if(selectedItem == 0)
        {
            Toast.makeText(getContext(), "Choose Activity Level", Toast.LENGTH_SHORT).show();
            return false;
        }
        else*/ if(selectedFoodIName.size()==0){
            Toast.makeText(getContext(), "Select atleast one food item", Toast.LENGTH_SHORT).show();
            return false;
        }
        else
        {
            return true;
        }
    }
}
