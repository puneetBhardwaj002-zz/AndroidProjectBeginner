package com.example.miniz;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class FoodRepository {
    private FoodDao mFoodDao;
    private LiveData<List<FoodItem>> mAllFoods;

    FoodRepository(Application application){
        FoodRoomDatabase db = FoodRoomDatabase.getDatabase(application);
        mFoodDao = db.foodDao();
        mAllFoods = mFoodDao.getAllFoods();
    }

    LiveData<List<FoodItem>> getAllFoods(){
        return mAllFoods;
    }
    public void insert(FoodItem item){
        new insertAsyncTask(mFoodDao).execute(item);
    }

    private static class insertAsyncTask extends AsyncTask<FoodItem,Void,Void>{

        private FoodDao mAsyncTaskDao;

        insertAsyncTask(FoodDao foodDao){
            this.mAsyncTaskDao = foodDao;
        }

        @Override
        protected Void doInBackground(FoodItem... foodItems) {
            mAsyncTaskDao.insert(foodItems[0]);
            return null;
        }
    }
}
