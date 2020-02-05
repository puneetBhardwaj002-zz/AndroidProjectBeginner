package com.example.applicationtofragment;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

public class FragmentVeg extends Fragment {

    private RecyclerView recyclerView;
    private ArrayList<Food> foodArrayList = new ArrayList<>();
    ProgressBar progressBar;
    TextView textView;
    private OnFragmentInteractionListener listener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        if (listener != null) {
            listener.onFragmentInteraction(getResources().getString(R.string.veg));
        }
        recyclerView = rootView.findViewById(R.id.recyclerView);
        progressBar = rootView.findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);
        textView = rootView.findViewById(R.id.loadingText);
        textView.setVisibility(View.VISIBLE);
        textView.setText(R.string.loading_text);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final String queryString = "http://demo9063766.mockable.io/";
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        final FoodAdapter adapter = new FoodAdapter(getContext(), foodArrayList);
        recyclerView.setAdapter(adapter);

        ConnectivityManager connectivityManager = (ConnectivityManager)
                getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connectivityManager != null) {
            networkInfo = connectivityManager.getActiveNetworkInfo();
        }
        if (!(networkInfo != null && networkInfo.isConnected())) {
            textView.setText(R.string.no_internet);
            progressBar.setVisibility(View.INVISIBLE);
        }


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, queryString, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONObject rootJsonObject = new JSONObject(response.toString());
                            JSONArray foodArray = rootJsonObject.getJSONArray("Food");
                            for (int i = 0; i < foodArray.length(); i++) {
                                JSONObject foodObject = foodArray.getJSONObject(i);
                                String nameText = foodObject.getString("FoodName");
                                String priceText = foodObject.getString("FoodPrice");
                                String categoryText = foodObject.getString("FoodCategory");
                                String imageResourceText = foodObject.getString("FoodThumb");
                                //Log.d("JSON Object Parsing",nameText);
                                if (categoryText.equals("veg")) {
                                    foodArrayList.add(new Food(nameText, priceText, imageResourceText));
                                    adapter.notifyDataSetChanged();
                                }
                                progressBar.setVisibility(View.INVISIBLE);
                                textView.setVisibility(View.INVISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getContext());
        requestQueue.add(jsonObjectRequest);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener=null;
    }
}
