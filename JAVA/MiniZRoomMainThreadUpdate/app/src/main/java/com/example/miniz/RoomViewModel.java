package com.example.miniz;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class RoomViewModel extends AndroidViewModel {

    private FoodRepository foodRepository;
    private LiveData<List<FoodItem>> allFoods;
    public RoomViewModel(@NonNull Application application) {
        super(application);
        foodRepository = new FoodRepository(application);
        allFoods = foodRepository.getAllFoods();
    }

    LiveData<List<FoodItem>> getAllFoods(){
        return allFoods;
    }

    public void insert(FoodItem item){foodRepository.insert(item);}
}

