package com.example.bmi.Api;

import com.example.bmi.Model.Root;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface ApiInterface  {

    @Headers({
            "x-rapidapi-host: edamam-food-and-grocery-database.p.rapidapi.com",
            "x-rapidapi-key: 7c7ca1a7e5msh290858dea566e62p187250jsn077e532e13c1"
    })
    @GET("parser")
    Call<Root> getSearch(@Query("ingr") String foodName);
}
