package com.example.bmi.Holders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bmi.R;

public class ViewHolderSelected extends RecyclerView.ViewHolder {

    public TextView food_name;
    public TextView food_calorie;
    public Button delete_button;

    public ViewHolderSelected(@NonNull View itemView) {
        super(itemView);
        food_name = itemView.findViewById(R.id.foodNameText_selected_item_id);
        food_calorie = itemView.findViewById(R.id.foodCalorieText_selected_item_id);
        delete_button = itemView.findViewById(R.id.deleteButton_selected_item_id);
    }
}
