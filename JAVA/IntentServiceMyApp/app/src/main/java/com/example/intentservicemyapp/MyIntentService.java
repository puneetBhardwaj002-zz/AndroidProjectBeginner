package com.example.intentservicemyapp;

import android.app.IntentService;
import android.content.Intent;
import android.widget.Toast;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

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

public class MyIntentService extends IntentService {
    final static String FOOD_INFO = "food_info";
    ArrayList<Food> foodList = new ArrayList<>();
    public MyIntentService() {
        super("MyIntentServices");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String queryString = intent.getStringExtra("QueryUrl");
        /*
        *   Fetching data using URL string by traditional method of connecting to URL, getting input
        *   stream and then parsing the JSON object
        *
        BufferedReader bufferedReader=null;
        HttpURLConnection urlConnection=null;
        try {

            //Create URL from query string and make a HTTP connection
            URL url = new URL(queryString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();

            //Read from input stream and convert it into a JSON string
            InputStream inputStream = urlConnection.getInputStream();
            bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder builder = new StringBuilder();
            String text = bufferedReader.readLine();
            while (text != null) {
                builder.append(text);
                text = bufferedReader.readLine();
            }

            //Parse data from JSON string and store it into an ArrayList of object type Food
            JSONObject rootJsonObject = new JSONObject(builder.toString());
            JSONArray foodArray = rootJsonObject.getJSONArray("Food");
            for (int i = 0; i < foodArray.length(); i++) {
                JSONObject foodObject = foodArray.getJSONObject(i);
                String name = foodObject.getString("FoodName");
                String price = foodObject.getString("FoodPrice");
                String category = foodObject.getString("FoodCategory");
                String imageResource = foodObject.getString("FoodThumb");
                foodList.add(new Food(name, price, category, imageResource));
            }

        } catch (IOException | JSONException e) {
            e.printStackTrace();
        } finally {
            if(bufferedReader!=null){
                try {
                    bufferedReader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(urlConnection!=null){
                urlConnection.disconnect();
            }
        }
        Intent intent1 = new Intent(FOOD_INFO);
        intent1.putParcelableArrayListExtra("Data", foodList);
        Log.d("onHandleIntent","Sent broadcast intent");
        //TODO:BoundService
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent1);
        */

        /*
        * Fetching data from URL string using Volley library.
        * //TODO: Sending data back to activity has to be completed and UI has to be updated.
         */

        ///*
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
                        //Log.d("JSON Object Parsing",nameText);
                        foodList.add(new Food(nameText,priceText,categoryText,imageResourceText));
                    }
                    //Log.d("onHandleIntent","Sending... broadcast intent");
                    Intent intentVolley = new Intent(FOOD_INFO);
                    intentVolley.putParcelableArrayListExtra("Data",foodList);
                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intentVolley);
                    stopSelf();
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

        //*/
    }
}
