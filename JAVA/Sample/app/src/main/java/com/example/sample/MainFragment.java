package com.example.sample;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MainFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("Test","inside Fragment onCreateView");
        return inflater.inflate(R.layout.fragment_main,container,false);
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d("Test","inside Fragment onStop");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d("Test","inside Fragment onPause");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d("Test","inside Fragment onStart");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d("Test","inside Fragment onDestroy");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d("Test","inside Fragment onSaveInstanceState");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("Test","inside Fragment onResume");
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d("Test","inside Fragment onAttach");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Test","inside Fragment onCreate");
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d("Test","inside Fragment onActivityCreated");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d("Test","inside Fragment onDestroyView");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d("Test","inside Fragment onDetach");
    }
}
