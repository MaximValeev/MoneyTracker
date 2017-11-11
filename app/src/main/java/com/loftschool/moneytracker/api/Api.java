package com.loftschool.moneytracker.api;

import com.loftschool.moneytracker.Item;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Max on 10.11.2017.
 */

public interface Api {

    @GET("items")
    Call<List<Item>> items(@Query("type") String type);

    @GET("items/add")
    Call<Item> add(@Query("name")String name, @Query("price")int price, @Query("type")String type);

}
