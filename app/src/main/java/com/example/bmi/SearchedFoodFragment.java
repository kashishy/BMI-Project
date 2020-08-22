package com.example.bmi;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bmi.Adapters.RecylerviewAdapterSearched;
import com.example.bmi.Model.Hints;

import java.util.ArrayList;
import java.util.List;

public class SearchedFoodFragment extends Fragment {

    public FragmentSearchedListener listener;
    private static final String TAG = "Searched Food ";
    private View view;
    private RecyclerView recyclerView;
    private ArrayList<String> foodName;
    private ArrayList<String> foodCal;
    RecylerviewAdapterSearched recylerviewAdapterSearched;

    public interface FragmentSearchedListener {
        public void selectedFood(String foodName, String calorie);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_searched_food, container, false);
        Initialised();
        foodName = getArguments().getStringArrayList("foodItemName");
        foodCal = getArguments().getStringArrayList("foodItemCal");
        Log.d(TAG, "FoodName" + foodName.size());
        Log.d(TAG, "FoodCal "+foodCal.size());
        recylerviewAdapterSearched = new RecylerviewAdapterSearched(foodName, foodCal, listener);
        recyclerView.setAdapter(recylerviewAdapterSearched);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentSearchedListener)
        {
            listener = (FragmentSearchedListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + " must implements FragmentSearched Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void Initialised() {
        recyclerView = view.findViewById(R.id.foodItemsList_foodSearched_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        /*foodName = getArguments().getStringArrayList("foodItemName");
        foodCal = getArguments().getStringArrayList("foodItemCal");*/
    }

    public void getFoodItems(ArrayList<String> foodName, ArrayList<String> foodCal)
    {
        //Log.d(TAG, "Get : " +hints.get(0).getFood().getLabel());
        recylerviewAdapterSearched = new RecylerviewAdapterSearched(foodName, foodCal, listener);
        recyclerView.setAdapter(recylerviewAdapterSearched);
    }

}
