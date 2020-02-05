package com.example.apptofragservice;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyFragment extends Fragment implements ResponseListener {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView loadingText;
    private FoodItemAdapter adapter = null;
    boolean isBounded = false;
    private Intent intent;
    private MainActivity activity;
    private Resources resources;
    private String category;
    private OnFragmentInteractionListener listener;

    public MyFragment(MainActivity activity) {
        this.activity = activity;
        resources = activity.getResources();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_layout, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progress_bar);
        loadingText = view.findViewById(R.id.loading_text);
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        adapter = new FoodItemAdapter(getContext(),new ArrayList<FoodItem>());
        recyclerView.setAdapter(adapter);
        ConnectivityManager manager = (ConnectivityManager) activity.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = null;
        if (manager != null) {
            network = manager.getActiveNetworkInfo();
        }
        if (!(network != null && network.isConnected())) {
            loadingText.setText(resources.getString(R.string.no_internet_text));
        }
        intent = new Intent(getContext(), MyService.class);
        activity.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        activity.startService(intent);
        category = getArguments().getString("Category");
        if (listener != null) {
            listener.onFragmentInteraction(category);
        }
    }

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyService.MyLocalBinder binder = (MyService.MyLocalBinder) iBinder;
            MyService boundService = binder.getService();
            isBounded = true;
            boundService.loadDataFromJson(activity.getResources().getString(R.string.query_string)
                    , MyFragment.this);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBounded = false;
        }
    };

    @Override
    public void onResponse(ArrayList<FoodItem> foodArrayList) {
        for(int i=0;i<foodArrayList.size();i++){
            if(!(foodArrayList.get(i).getCategory().equals(category))){
                foodArrayList.remove(foodArrayList.get(i));
                i--;
            }
        }
        adapter = new FoodItemAdapter(getContext(), foodArrayList);
        recyclerView.setAdapter(adapter);
        progressBar.setVisibility(View.INVISIBLE);
        loadingText.setVisibility(View.INVISIBLE);
        activity.stopService(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        activity.unbindService(connection);
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
