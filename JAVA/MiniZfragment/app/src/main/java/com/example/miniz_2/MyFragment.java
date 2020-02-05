package com.example.miniz_2;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
    private String category;

    static MyFragment newInstance(int num){
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putInt("type",num);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
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
        if(getActivity()!=null){
            activity = (MainActivity) getActivity();
        }
        ConnectivityManager manager = (ConnectivityManager) activity.
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo network = null;
        if (manager != null) {
            network = manager.getActiveNetworkInfo();
        }
        if (!(network != null && network.isConnected())) {
            loadingText.setText(activity.getResources().getString(R.string.no_internet_text));
        }
        intent = new Intent(getContext(), MyService.class);
        activity.bindService(intent, connection, Context.BIND_AUTO_CREATE);
        activity.startService(intent);
        int type;
        if(getArguments()!=null){
            type = getArguments().getInt("type");
            if(type == 0){
                category = "veg";
            }else {
                category = "non-veg";
            }
        }
        Log.d("MyFragment",category + " fragment created");
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


}
