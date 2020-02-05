package com.example.miniz_2;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

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

public class MyService extends Service {

    private final IBinder binder = new MyLocalBinder();

    public MyService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public class MyLocalBinder extends Binder {
        MyService getService() {
            return MyService.this;
        }
    }

    public void loadDataFromJson(String queryString, final MyFragment fragment) {
        final ArrayList<FoodItem> foodArrayList = new ArrayList<>();

        Log.d(getPackageName(), "loadDataFromJson called");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, queryString,
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
                        foodArrayList.add(new FoodItem(nameText, priceText, categoryText, imageResourceText));
                        Log.d("JSON Parsing", nameText);
                    }
                    fragment.onResponse(foodArrayList);
                    //stopSelf();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
    }
}
