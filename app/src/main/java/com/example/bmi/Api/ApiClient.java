package com.example.bmi.Api;

import android.util.Log;

import com.example.bmi.Model.Root;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient implements Callback<Root> {

    static final String TAG = "ApiClient  ";
    public static final String BASE_URL = "https://edamam-food-and-grocery-database.p.rapidapi.com/";
    public static Retrofit retrofit;
    public static Retrofit getApiClient(){

        if(retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    @Override
    public void onResponse(Call<Root> call, Response<Root> response) {
        if(response.isSuccessful()) {
            Root changesList = response.body();
            Log.d(TAG, "Response : "+changesList.getText());
        } else {
            System.out.println(response.errorBody());
            Log.d(TAG, "Error : "+response.message());
        }
    }

    @Override
    public void onFailure(Call<Root> call, Throwable t) {
        t.printStackTrace();
    }
}
