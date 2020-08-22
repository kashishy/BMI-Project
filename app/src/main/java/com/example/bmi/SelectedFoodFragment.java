package com.example.bmi;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.bmi.Adapters.RecyleviewAdapterSelected;

import java.util.ArrayList;

public class SelectedFoodFragment extends Fragment {

    private FragmentSelectedListener listener;
    private static final String TAG = "Selected Food ";
    private Button saveButton;
    private View view;
    private RecyclerView recyclerView;
    private ArrayList<String> selectedFoodItemName;
    private ArrayList<String> selectedFoodItemCal;
    private Double sum;

    public interface FragmentSelectedListener {
        public void calorieSum(String sum);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_selected_food, container, false);
        Initialised();
        Log.d(TAG, "Create. Size of List : "+selectedFoodItemName.size());
        if (selectedFoodItemName.size()!=0)
        {
            RecyleviewAdapterSelected adapterSelected = new RecyleviewAdapterSelected(selectedFoodItemName, selectedFoodItemCal, listener);
            recyclerView.setAdapter(adapterSelected);
        }
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int length = selectedFoodItemCal.size();
                sum=0.0;
                if(length>0)
                {
                    for (int i=0;i<length;i++)
                    {
                        sum = sum+Double.parseDouble(selectedFoodItemCal.get(i));
                    }
                    listener.calorieSum(Double.toString(sum));
                }
            }
        });
        return view;
    }

    public void updateList(ArrayList<String> foodName, ArrayList<String> foodCal)
    {
        Log.d(TAG, "Size of List : "+foodCal.size());
        selectedFoodItemName = new ArrayList<String>();
        selectedFoodItemCal = new ArrayList<String>();
        this.selectedFoodItemName = foodName;
        this.selectedFoodItemCal = foodCal;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof  FragmentSelectedListener)
        {
            listener = (FragmentSelectedListener) context;
        }
        else
        {
            throw new RuntimeException(context.toString() + " must implements FragmentSelected Listener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    public void Initialised()
    {
        recyclerView = view.findViewById(R.id.foodItemsList_foodSelected_id);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        saveButton = view.findViewById(R.id.saveButton_foodSearch_id);
    }
}
