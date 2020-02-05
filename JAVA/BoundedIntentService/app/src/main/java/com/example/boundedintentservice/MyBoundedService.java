package com.example.boundedintentservice;

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

public class MyBoundedService extends Service {
    private final IBinder binder = new MyLocalBinder();
    private ArrayList<Food> foodArrayList = new ArrayList<>();

    public MyBoundedService(){
        Log.d("MyBoundService","MyBoundedService constructor called");
    }
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(getPackageName(),"onBind called");
        return binder;
    }

    public class MyLocalBinder extends Binder {
        MyBoundedService getService(){
            Log.d(getPackageName(),"getService called");
            return MyBoundedService.this;
        }
    }

    public void loadDataFromJson(String queryString, final MainActivity mainActivity){
        Log.d(getPackageName(),"loadDataFromJson called");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,queryString,
                null, new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONObject rootJsonObject = new JSONObject(response.toString());
                    JSONArray foodArray = rootJsonObject.getJSONArray("Food");
                    for(int i=0;i<foodArray.length();i++){
                        JSONObject foodObject = foodArray.getJSONObject(i);
                        String nameText = foodObject.getString("FoodName");
                        String priceText = foodObject.getString("FoodPrice");
                        String categoryText = foodObject.getString("FoodCategory");
                        String imageResourceText = foodObject.getString("FoodThumb");
                        Log.d("JSON Object Parsing",nameText);
                        foodArrayList.add(new Food(nameText,priceText,categoryText,imageResourceText));
                        mainActivity.onResponse(foodArrayList);
                    }
                    Log.d(getPackageName(),"Returning from onResponse");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),error.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonObjectRequest);
        Log.d(getPackageName(),"returning list");
    }

}
