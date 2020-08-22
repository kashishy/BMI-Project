package com.example.bmi.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bmi.BmiFragment;
import com.example.bmi.Holders.ViewHolderSearched;
import com.example.bmi.Holders.ViewHolderSelected;
import com.example.bmi.Model.Food;
import com.example.bmi.Model.Hints;
import com.example.bmi.Model.Nutrients;
import com.example.bmi.R;
import com.example.bmi.SearchedFoodFragment;

import java.util.ArrayList;
import java.util.List;

public class SearchedAdapter extends RecyclerView.Adapter<ViewHolderSearched> {

    static final String TAG="Searched Adapter";
    public ArrayList<String> foodName;
    public ArrayList<String> foodCal;
    public ArrayList<String> selectedFoodName;
    public ArrayList<String> selectedFoodCal;
    public RecyclerView selectedRecyle;
    public LinearLayout searchedLayout;
    public LinearLayout selectedLayout;

    public SearchedAdapter(ArrayList<String> foodName, ArrayList<String> foodCal, ArrayList<String> selectedFoodName,
                           ArrayList<String> selectedFoodCal, RecyclerView selectedRecyle,
                           LinearLayout searchedLayout, LinearLayout selectedLayout) {
        this.foodName = foodName;
        Log.d(TAG, "Size of FoodName : "+foodName.size());
        this.foodCal = foodCal;
        this.selectedFoodName = selectedFoodName;
        this.selectedFoodCal = selectedFoodCal;
        this.selectedRecyle = selectedRecyle;
        this.searchedLayout = searchedLayout;
        this.selectedLayout = selectedLayout;
    }

    @NonNull
    @Override
    public ViewHolderSearched onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.searched_item, parent, false);
        ViewHolderSearched rootViewHolder = new ViewHolderSearched(view);
        return rootViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSearched holder, final int position) {
        holder.food_name.setText(foodName.get(position));
        holder.food_calorie.setText(foodCal.get(position) + " Kcl");
        holder.add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedFoodName.add(foodName.get(position));
                selectedFoodCal.add(foodCal.get(position));
                Log.d(TAG, "Size of selectedFoodName : "+selectedFoodName.size());
                searchedLayout.setVisibility(View.GONE);
                selectedLayout.setVisibility(View.VISIBLE);
                BmiFragment bmiFragment = new BmiFragment();
                bmiFragment.showSelectedItem(selectedFoodName, selectedFoodCal, selectedRecyle);
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodName.size();
    }
}
