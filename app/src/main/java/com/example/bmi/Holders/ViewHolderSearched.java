package com.example.bmi.Holders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bmi.R;

public class ViewHolderSearched extends RecyclerView.ViewHolder {

    public TextView food_name;
    public TextView food_calorie;
    public Button add_button;
    public ViewHolderSearched(@NonNull View itemView) {
        super(itemView);
        food_name = itemView.findViewById(R.id.foodNameText_searched_item_id);
        food_calorie = itemView.findViewById(R.id.foodCalorieText_searched_item_id);
        add_button = itemView.findViewById(R.id.addButton_searched_item_id);
    }
}
