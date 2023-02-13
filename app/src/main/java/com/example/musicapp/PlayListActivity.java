package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.musicapp.databinding.ActivityMainBinding;
import com.example.musicapp.databinding.PlaylistBinding;

public class PlayListActivity extends AppCompatActivity {
    PlaylistBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = PlaylistBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}