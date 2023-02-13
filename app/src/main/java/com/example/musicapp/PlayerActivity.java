package com.example.musicapp;

import static com.example.musicapp.MainActivity.musicArrayList;
import static com.example.musicapp.R.color.black;
import static com.example.musicapp.R.color.pink;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.SeekBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.example.musicapp.adapter.music;
import com.example.musicapp.adapter.music_adapter;
import com.example.musicapp.databinding.ActivityPlayerBinding;

import java.util.ArrayList;
import java.util.Collections;

public class PlayerActivity extends AppCompatActivity implements ServiceConnection, MediaPlayer.OnCompletionListener {

    private Uri uri;
    music_adapter music_adapter;
    String intentOpt;
    // static
    static Boolean repeat = false;
    static ArrayList<music> musicArrayListPA = new ArrayList<>();
    static boolean isPlaying = false;
    static MusicService musicService;
    static int song_pos = -1;
    @SuppressLint("StaticFieldLeak")
    static ActivityPlayerBinding binding;
    //
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Starting service
        Intent intent = new Intent(this,MusicService.class);
        bindService(intent,this,BIND_AUTO_CREATE);
        startService(intent);

        // Music player
        initSong();
        binding.pausePlay.setOnClickListener(view -> {
            if(!isPlaying) playMusic();
            else stopMusic();
        });

        binding.previousSong.setOnClickListener(view -> {prevNextSong(false);});
        binding.nextSong.setOnClickListener(view -> {prevNextSong(true);});
        binding.repeatBtn.setOnClickListener(view -> {
            if(!repeat){
                repeat = true;
                binding.repeatBtn.setColorFilter(ContextCompat.getColor(this,pink));
            }
            else{
                repeat = false;
                binding.repeatBtn.setColorFilter(ContextCompat.getColor(this,black));
            }
        });
        binding.timeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b){
                  musicService.mediaPlayer.seekTo(i);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    private void createMediaPlayer(){

        if(musicArrayListPA !=null){
            uri = Uri.parse(musicArrayListPA.get(song_pos).getPath());
        }
        if(musicService.mediaPlayer != null){
            if (isPlaying) {
                musicService.mediaPlayer.stop();
                musicService.mediaPlayer.release();
                musicService.mediaPlayer = MediaPlayer.create(this,uri);
                musicService.mediaPlayer.start();}
        }
        else{
            musicService.mediaPlayer = MediaPlayer.create(this,uri);
            musicService.mediaPlayer.start();
        }
        isPlaying = true;
        binding.pausePlay.setIconResource(R.drawable.ic_baseline_pause_24);
        musicService.showNotification(R.drawable.ic_baseline_pause_24);
        setLayout();
        musicService.mediaPlayer.setOnCompletionListener(this);
    }
    private void initSong(){
        Intent intent = getIntent();
        song_pos = intent.getIntExtra("pos",-1);
        intentOpt = intent.getStringExtra("class");
        switch(intentOpt){
            case "MainActivity":{
                musicArrayListPA.addAll(musicArrayList);
                Collections.shuffle(musicArrayListPA);
                break;
            }
            case "adapter":{
                musicArrayListPA.addAll(musicArrayList);
                break;
            }
        }
    }
    private void setLayout(){
        byte[] image = music.getAlbumArt(musicArrayListPA.get(song_pos).getPath());
        if(image!=null){
            Glide.with(this).asBitmap().load(image).into(binding.album);
        }
        else {
            Glide.with(this).load(R.drawable.album4).into(binding.album);
        }
        binding.songName.setText(musicArrayListPA.get(song_pos).getTitle());

        // seekbar
        binding.timeSeekbar.setProgress(0);
        binding.timeSeekbar.setMax(musicService.mediaPlayer.getDuration());
        binding.seekbarStart.setText(music.formatDuration((long) musicService.mediaPlayer.getCurrentPosition()));
        binding.seekbarEnd.setText(music.formatDuration((long) musicService.mediaPlayer.getDuration()));
        if(repeat){
            binding.repeatBtn.setColorFilter(ContextCompat.getColor(this,pink));
        }
    }
    private void playMusic(){
        musicService.mediaPlayer.start();
        binding.pausePlay.setIconResource(R.drawable.ic_baseline_pause_24);
        musicService.showNotification(R.drawable.ic_baseline_pause_24);
        isPlaying = true;
    }
    private void stopMusic(){
        musicService.mediaPlayer.pause();
        binding.pausePlay.setIconResource(R.drawable.ic_baseline_play_arrow_24);
        musicService.showNotification(R.drawable.ic_baseline_play_arrow_24);
        isPlaying = false;
    }
    private void prevNextSong(Boolean increment){
        setSongPosition(increment);
        setLayout();
        createMediaPlayer();
    }
    static void setSongPosition(Boolean increment){
        if(!PlayerActivity.repeat){
            if (increment){
                if (musicArrayListPA.size() - 1 == song_pos){
                    song_pos = 0;
                }
                else ++song_pos;
            }
            else {
                if (song_pos==0){
                    song_pos = musicArrayListPA.size()-1;
                }
                else --song_pos;
            }
        }
    }
    @Override
    public void onServiceConnected(ComponentName componentName, IBinder service) {
        MusicService.MyBinder myBinder = (MusicService.MyBinder) service;
        musicService = myBinder.currentService();
        createMediaPlayer();
        musicService.seekbarSetup();
    }

    @Override
    public void onServiceDisconnected(ComponentName componentName) {
        musicService = null;
    }

    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {
        setSongPosition(true);
        createMediaPlayer();

    }
}