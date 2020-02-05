package com.example.miniz;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface FoodDao {

    @Insert
    void insert(FoodItem item);

    @Query("DELETE FROM food_table")
    void deleteAll();

    @Query("SELECT * from food_table ORDER BY name ASC")
    LiveData<List<FoodItem>> getAllFoods();
}
