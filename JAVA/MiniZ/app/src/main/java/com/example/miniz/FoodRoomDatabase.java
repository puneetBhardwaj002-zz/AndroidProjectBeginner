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
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static FoodRoomDatabase.Callback roomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    c.getResources().getString(R.string.query_string),
                    null, new Response.Listener<JSONObject>() {

                @Override
                public void onResponse(JSONObject response) {
                    Log.e("TEST", response.toString());
                    new PopulateAsyncTask(INSTANCE).execute(response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(c, error.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
            Log.e("TEST ABC", jsonObjectRequest.toString());
            RequestQueue requestQueue = Volley.newRequestQueue(c);
            requestQueue.add(jsonObjectRequest);

        }
    };

    static class PopulateAsyncTask extends AsyncTask<JSONObject, Void, Void> {
        private final FoodDao dao;

        PopulateAsyncTask(FoodRoomDatabase instance) {
            dao = instance.foodDao();
        }

        @Override
        protected Void doInBackground(JSONObject... jsonObjects) {
            try {
                JSONObject rootJsonObject = new JSONObject(jsonObjects[0].toString());
                JSONArray foodArray = rootJsonObject.getJSONArray("Food");
                dao.deleteAll();
                for (int i = 0; i < foodArray.length(); i++) {
                    JSONObject foodObject = foodArray.getJSONObject(i);
                    String nameText = foodObject.getString("FoodName");
                    String priceText = foodObject.getString("FoodPrice");
                    String imageResourceText = foodObject.getString("FoodThumb");
                    String categoryText = foodObject.getString("FoodCategory");
                    Log.d("JSON Parsing", nameText);
                    dao.insert(new FoodItem(nameText, priceText, categoryText, imageResourceText));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}