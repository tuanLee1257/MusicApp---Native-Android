package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.musicapp.adapter.music;
import com.example.musicapp.adapter.music_adapter;
import com.example.musicapp.databinding.ActivityMainBinding;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ActionBarDrawerToggle toggle;
    private ActivityMainBinding binding;
    private music_adapter music_adapter;
    static ArrayList<music> musicArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initLayout();

        binding.sufferBtn.setOnClickListener(view -> {
            Intent intent =  new Intent(this, PlayerActivity.class);
            intent.putExtra("pos",0);
            intent.putExtra("class","MainActivity");
            startActivity(intent);
        });
        binding.favrBtn.setOnClickListener(view -> {
            Intent inten = new Intent(this,FavouriteActivity.class);
            startActivity(inten);
        });
        binding.playListBtn.setOnClickListener(view -> {
            Intent inten = new Intent(this,PlayListActivity.class);
            startActivity(inten);
        });

        // navigation click
        binding.navView.setNavigationItemSelectedListener(view -> {
            switch (view.getItemId()){
                case R.id.feedback_nav:{
                    Toast.makeText(this, "feedback clicked", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.setting_nav:{
                    Toast.makeText(this, "setting clicked", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.about_nav:{
                    Toast.makeText(this, "about clicked", Toast.LENGTH_SHORT).show();
                    break;
                }
                case R.id.exit_nav:{
                    System.exit(0);
                    break;
                }

            }
            return true;
        });
    }

    private void initLayout() {
        requestRuntimePermission();

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // navigation bar
        toggle = new ActionBarDrawerToggle(this,binding.getRoot(),R.string.open,R.string.close);
        binding.getRoot().addDrawerListener(toggle);
        toggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Music
        musicArrayList = getAudio(this);
        music_adapter = new music_adapter(this,musicArrayList);
        binding.musicList.setLayoutManager(new LinearLayoutManager(this));
        binding.musicList.setHasFixedSize(true);
        binding.musicList.setAdapter(music_adapter);
        music_adapter.notifyDataSetChanged();

    }

    //cap quyen truy cap file
    private void requestRuntimePermission(){
        if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},13);
        }
    }
    @Override
    public int checkSelfPermission(String permission) {
        return super.checkSelfPermission(permission);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 13 ){
            if(grantResults != null && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();
            }
            else {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},13);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        return super.onOptionsItemSelected(item);
    }

    // lay file mp3
    public static ArrayList<music> getAudio(Context context){
        ArrayList<music> tempMusiclist = new ArrayList<>();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {
                MediaStore.Audio.Media.ALBUM,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA,
                MediaStore.Audio.Media.ARTIST,
        };
        Cursor cursor = context.getContentResolver().query(uri,projection,null,null,null);
        if(cursor != null){
            while ((cursor.moveToNext())){
                String album = cursor.getString(0);
                String title = cursor.getString(1);
                String duration = cursor.getString(2);
                String path = cursor.getString(3);
                String artist = cursor.getString(4);

                long Longduration = Long.parseLong(duration);

                music music = new music(path,title,artist,album,Longduration);
                //
                Log.e("path" + path, "album: "+album );
                tempMusiclist.add(music);
            }
            cursor.close();
        }
        return tempMusiclist;
    }


}