package com.example.miniz;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Database(entities = {FoodItem.class}, version = 1, exportSchema = false)
public abstract class FoodRoomDatabase extends RoomDatabase {

    public abstract FoodDao foodDao();
    private static FoodRoomDatabase INSTANCE;
    private static Context c;

    static FoodRoomDatabase getDatabase(final Context context) {
        c = context;
        if (INSTANCE == null) {
            synchronized (FoodRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FoodRoomDatabase.class, "word_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(roomDatabaseCallback)
                            .allowMainThreadQueries()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static FoodRoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateDbAsyncTask(INSTANCE).execute();
        }
    };


    private static class PopulateDbAsyncTask extends AsyncTask<Void,Void,Void> {
        private List<FoodItem> list= new ArrayList<>();
        private final FoodDao dao;
        PopulateDbAsyncTask(FoodRoomDatabase instance) {
            dao = instance.foodDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            dao.deleteAll();
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    c.getResources().getString(R.string.query_string),
                    null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject rootJsonObject = new JSONObject(response.toString());
                        JSONArray foodArray = rootJsonObject.getJSONArray("Food");
                        for (int i = 0; i < foodArray.length(); i++) {
                            JSONObject foodObject = foodArray.getJSONObject(i);
                            String nameText = foodObject.getString("FoodName");
                            String priceText = foodObject.getString("FoodPrice");
                            String imageResourceText = foodObject.getString("FoodThumb");
                            String categoryText = foodObject.getString("FoodCategory");
                            list.add(new FoodItem(nameText, priceText, categoryText, imageResourceText));
                            Log.d("JSON Parsing", nameText);
                            dao.insert(new FoodItem(nameText, priceText, categoryText, imageResourceText));
                        }
                        //stopSelf();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(c, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            RequestQueue requestQueue = Volley.newRequestQueue(c);
            requestQueue.add(jsonObjectRequest);
            return null;
            //TODO: Implement interface to return data to insert into database
        }
    }
}