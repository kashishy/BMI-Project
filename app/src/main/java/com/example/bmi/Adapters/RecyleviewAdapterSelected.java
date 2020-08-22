package com.example.bmi.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bmi.Holders.ViewHolderSelected;
import com.example.bmi.Model.Food;
import com.example.bmi.Model.Hints;
import com.example.bmi.Model.Nutrients;
import com.example.bmi.Model.Root;
import com.example.bmi.R;
import com.example.bmi.SelectedFoodFragment;

import java.util.ArrayList;
import java.util.List;

public class RecyleviewAdapterSelected extends RecyclerView.Adapter<ViewHolderSelected> {

    private static final String TAG = "Adapter Selected";
    private SelectedFoodFragment.FragmentSelectedListener listener;
    private ArrayList<String> selectedFoodName = new ArrayList<String>();
    private ArrayList<String> selectedFoodCal = new ArrayList<String>();

    public RecyleviewAdapterSelected(ArrayList<String> foodName, ArrayList<String> foodCal, SelectedFoodFragment.FragmentSelectedListener listener) {
        this.selectedFoodName = foodName;
        this.selectedFoodCal = foodCal;
        this.listener = listener;
        Log.d(TAG, "Size of List : "+selectedFoodName.size());
    }


    @NonNull
    @Override
    public ViewHolderSelected onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.selected_item, parent, false);
        ViewHolderSelected rootViewHolder = new ViewHolderSelected(view);
        Log.d(TAG, "Create Size of List : "+selectedFoodName.size());
        return rootViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderSelected holder, int position) {
        holder.food_name.setText(selectedFoodName.get(position));
        holder.food_calorie.setText(selectedFoodCal.get(position));
        holder.delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return selectedFoodName.size();
    }
}
