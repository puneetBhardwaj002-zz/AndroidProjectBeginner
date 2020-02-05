package com.example.miniz;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;


public class MyFragment extends Fragment{

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private TextView loadingText;
    private FoodItemAdapter adapter = null;
    private MainActivity activity;
    private RoomViewModel viewModel;
    private int type;

    static MyFragment newInstance(int num) {
        MyFragment fragment = new MyFragment();
        Bundle args = new Bundle();
        args.putInt("type", num);
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
        adapter = new FoodItemAdapter(getContext(), new ArrayList<FoodItem>());
        recyclerView.setAdapter(adapter);
        if (getActivity() != null) {
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
        if(getArguments()!=null){
            type=getArguments().getInt("type");
        }
        viewModel = ViewModelProviders.of(this).get(RoomViewModel.class);
        viewModel.getAllFoods().observe(this, new Observer<List<FoodItem>>() {
            @Override
            public void onChanged(List<FoodItem> foodItems) {
                adapter.setFoods(foodItems,type);
                if(foodItems.size() > 0){
                    progressBar.setVisibility(View.INVISIBLE);
                    loadingText.setVisibility(View.INVISIBLE);
                }
            }
        });
    }
}
