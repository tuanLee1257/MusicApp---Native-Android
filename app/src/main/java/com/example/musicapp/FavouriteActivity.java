package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.musicapp.databinding.ActivityMainBinding;
import com.example.musicapp.databinding.FavouriteBinding;

public class FavouriteActivity extends AppCompatActivity {
    FavouriteBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = FavouriteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}