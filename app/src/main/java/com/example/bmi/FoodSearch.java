package com.example.bmi;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import com.example.bmi.Api.ApiClient;
import com.example.bmi.Api.ApiInterface;
import com.example.bmi.Model.Hints;
import com.example.bmi.Model.Root;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class FoodSearch extends AppCompatActivity implements SelectedFoodFragment.FragmentSelectedListener, SearchedFoodFragment.FragmentSearchedListener{

    private static final String TAG = "Food Search ";
    private SearchView searchView;
    private Button searchButton;
    private Bundle bundle;
    private ArrayList<String> selectedFoodIName;
    private ArrayList<String> selectedFoodCal;
    private ArrayList<String> foodItemName;
    private ArrayList<String> foodItemCal;
    private SelectedFoodFragment selectedFrag;
    private SearchedFoodFragment searchedFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_search);

        getSupportActionBar().hide();

        Log.d(TAG, "Start Food Search");
        Initialised();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String foodName = searchView.getQuery().toString();
                Log.d(TAG, "Name : "+foodName);
                getFood(foodName);
                /*getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frameLayout_foodSearch_id, searchedFrag)
                        .commit();*/
            }
        });
        //getFood("apple");
        Log.d(TAG, "Start");
        //getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_foodSearch_id, new SelectedFoodFragment()).commit();
        Log.d(TAG, "End");
        //getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout_foodSearch_id, new FoodFragment()).commit();

    }

    public void Initialised()
    {
        searchView = findViewById(R.id.searchView_foodSearch_id);
        searchButton = findViewById(R.id.searchButton_foodSearch_id);
        selectedFrag = new SelectedFoodFragment();
        searchedFrag = new SearchedFoodFragment();
        foodItemName = new ArrayList<String>();
        foodItemCal = new ArrayList<String>();
        selectedFoodIName = new ArrayList<String>();
        selectedFoodCal = new ArrayList<String >();
        bundle = new Bundle();
    }

    public void getFood(String foodName) {

        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Root> call = apiInterface.getSearch(foodName);
        call.enqueue(new Callback<Root>() {
            @Override
            public void onResponse(Call<Root> call, retrofit2.Response<Root> response) {

                if(response.isSuccessful()) {
                    Root result = response.body();
                    Log.d(TAG, "Response : "+result.getText());
                    List<Hints> hints = result.getHints();
                    Log.d(TAG, hints.toString());
                    for (int i=0; i<hints.size();i++)
                    {
                        Log.d(TAG, "Position : "+i+" : "+ hints.get(i).getFood().getLabel());
                        foodItemName.add(hints.get(i).getFood().getLabel());
                        foodItemCal.add(Double.toString(hints.get(i).getFood().getNutrients().getENERC_KCAL()));
                    }
                    bundle.putStringArrayList("foodItemName", foodItemName);
                    bundle.putStringArrayList("foodItemCal", foodItemCal);
                    //searchedFrag.getFoodItems(foodItemName, foodItemCal);
                    searchedFrag.setArguments(bundle);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frameLayout_foodSearch_id, searchedFrag)
                            .commit();
                } else {
                    System.out.println(response.errorBody());
                    Log.d(TAG, "Error : "+response.message());
                }
            }

            @Override
            public void onFailure(Call<Root> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void getSelectedFrag()
    {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameLayout_foodSearch_id, selectedFrag, "secondFragmentTag")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void selectedFood(String foodName, String calorie) {
        selectedFoodIName.add(foodName);
        selectedFoodCal.add(calorie);
        Log.d(TAG, "Selected Food Name : " +foodName + ". Size : "+selectedFoodIName.size());
        Log.d(TAG, "Selected Food Cal : " +calorie+ ". Size : "+selectedFoodCal.size());
        getSelectedFrag();
        selectedFrag.updateList(selectedFoodIName, selectedFoodCal);
    }

    @Override
    public void calorieSum(String sum) {
        Intent intent = new Intent(FoodSearch.this, MainActivity.class);
        intent.putExtra("sum", sum);
        startActivity(intent);
    }
}