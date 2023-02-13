package com.example.musicapp;

import static com.example.musicapp.PlayerActivity.binding;
import static com.example.musicapp.PlayerActivity.musicService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.bumptech.glide.Glide;
import com.example.musicapp.adapter.music;

import java.io.IOException;

public class NotificationReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        switch (intent.getAction()){
            case ApplicationClass.PREVIOUS:{
                prevNextSong(false, context.getApplicationContext());
                break;
            }
            case ApplicationClass.PLAY:{
                if (PlayerActivity.isPlaying) {
                    pauseMusic();
                }
                else {
                    playMusic();
                }
                break;
            }
            case ApplicationClass.NEXT:{
                prevNextSong(true, context.getApplicationContext());
                break;
            }
            case ApplicationClass.EXIT:{
                musicService.stopForeground(true);
                musicService = null;
                System.exit(0);
                break;
            }
        }

    }
    private void playMusic(){
        PlayerActivity.isPlaying = true;
        musicService.mediaPlayer.start();
        musicService.showNotification(R.drawable.ic_baseline_pause_24);
        binding.pausePlay.setIconResource(R.drawable.ic_baseline_pause_24);
    }
    private void pauseMusic(){
        PlayerActivity.isPlaying = false;
        musicService.mediaPlayer.pause();
        musicService.showNotification(R.drawable.ic_baseline_play_arrow_24);
        binding.pausePlay.setIconResource(R.drawable.ic_baseline_play_arrow_24);
    }
    private void prevNextSong(Boolean increment, Context context){
        PlayerActivity.setSongPosition(increment);
        musicService.createMediaPlayer();
        //set layout
        byte[] image = music.getAlbumArt(PlayerActivity.musicArrayListPA.get(PlayerActivity.song_pos).getPath());
        if (image != null) {
            Glide.with(context.getApplicationContext()).asBitmap().load(image).into(binding.album);
        }
        else {
            Glide.with(context).load(R.drawable.album4).into(binding.album);
        }
        binding.songName.setText(PlayerActivity.musicArrayListPA.get(PlayerActivity.song_pos).getTitle());
        playMusic();
    }

}
