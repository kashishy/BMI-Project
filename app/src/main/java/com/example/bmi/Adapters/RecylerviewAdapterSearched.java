package com.example.bmi.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bmi.Holders.ViewHolderSearched;
import com.example.bmi.Holders.ViewHolderSelected;
import com.example.bmi.Model.Food;
import com.example.bmi.Model.Hints;
import com.example.bmi.Model.Nutrients;
import com.example.bmi.R;
import com.example.bmi.SearchedFoodFragment;

import java.util.ArrayList;
import java.util.List;

public class RecylerviewAdapterSearched extends RecyclerView.Adapter<ViewHolderSearched> {

    public SearchedFoodFragment.FragmentSearchedListener listener;
    private List<Hints> hints;
    static final String TAG="Searched Adapter";
    public ArrayList<String> foodName = new ArrayList<String>();
    public ArrayList<String> foodCal = new ArrayList<String>();

    public RecylerviewAdapterSearched(ArrayList<String> foodName, ArrayList<String> foodCal, SearchedFoodFragment.FragmentSearchedListener listener) {
        this.foodName = foodName;
        Log.d(TAG, "Size of FoodName : "+foodName.size());
        this.foodCal = foodCal;
        this.listener = listener;
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
                listener.selectedFood(foodName.get(position), foodCal.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodName.size();
    }
}
