package com.mytask.fetchingfiles.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;

import android.os.Bundle;

import com.mytask.fetchingfiles.R;
import com.mytask.fetchingfiles.ui.fragments.MainFragment;

public class MainActivity extends AppCompatActivity {

    private MainFragment mainFragment;
    NavController navController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setTitle("YOUR TITLE HERE");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}