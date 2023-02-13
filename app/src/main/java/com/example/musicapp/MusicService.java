package com.example.musicapp;

import static com.example.musicapp.ApplicationClass.CHANNEL_ID;
import static com.example.musicapp.PlayerActivity.binding;
import static com.example.musicapp.PlayerActivity.musicArrayListPA;
import static com.example.musicapp.PlayerActivity.musicService;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.support.v4.media.session.MediaSessionCompat;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.example.musicapp.adapter.music;
import java.util.ArrayList;

public class MusicService extends Service {
    IBinder myBinder = new MyBinder();
    MediaPlayer mediaPlayer = null;
    MediaSessionCompat mediaSession;
    ArrayList<music> musicFiles = new ArrayList<>();
    Handler handler = new Handler();
    Runnable runnable;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mediaSession = new MediaSessionCompat(getBaseContext(),"My music");
        return myBinder;
    }
    public class MyBinder extends Binder{
        MusicService currentService(){
            return MusicService.this;
        }
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    public void showNotification(int pasuePlayBtn){

        Intent prevIntent = new Intent(getBaseContext(), NotificationReceiver.class).setAction(ApplicationClass.PREVIOUS);
        PendingIntent prevPendingIntent = PendingIntent.getBroadcast(getBaseContext(),0,prevIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent playIntent = new Intent(getBaseContext(), NotificationReceiver.class).setAction(ApplicationClass.PLAY);
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(getBaseContext(),0,playIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent nextIntent = new Intent(getBaseContext(), NotificationReceiver.class).setAction(ApplicationClass.NEXT);
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(getBaseContext(),0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        Intent exitIntent = new Intent(getBaseContext(), NotificationReceiver.class).setAction(ApplicationClass.EXIT);
        PendingIntent exitPendingIntent = PendingIntent.getBroadcast(getBaseContext(),0,exitIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        //image
        Bitmap thump = null;
        byte[] image = music.getAlbumArt(musicArrayListPA.get(PlayerActivity.song_pos).getPath());
        if(image!=null){
            thump = BitmapFactory.decodeByteArray(image,0,image.length);
        }
        else {
            thump = BitmapFactory.decodeResource(getResources(),R.drawable.album4);
        }
        Notification notification = new NotificationCompat.Builder(getBaseContext(),CHANNEL_ID)
                .setContentTitle(musicArrayListPA.get(PlayerActivity.song_pos).getTitle())
                .setContentText(musicArrayListPA.get(PlayerActivity.song_pos).getTitle())
                .setSmallIcon(R.drawable.ic_baseline_favorite_24)
                .setLargeIcon(thump)
                .setStyle(new androidx.media.app.NotificationCompat.MediaStyle().setMediaSession(mediaSession.getSessionToken()))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setOnlyAlertOnce(true)
                .addAction(R.drawable.ic_baseline_arrow_back_ios_24,"Previous",prevPendingIntent)
                .addAction(pasuePlayBtn,"Play",playPendingIntent)
                .addAction(R.drawable.ic_baseline_arrow_forward_ios_24,"Next",nextPendingIntent)
                .addAction(R.drawable.ic_baseline_close_24,"Exit",exitPendingIntent)
                .build();
        startForeground(13,notification);
    }

    //
    Uri uri;
    void createMediaPlayer(){
        if(musicArrayListPA !=null){
            uri = Uri.parse(musicArrayListPA.get(PlayerActivity.song_pos).getPath());
        }
        if(musicService.mediaPlayer != null){
            if (PlayerActivity.isPlaying) {
                musicService.mediaPlayer.stop();
                musicService.mediaPlayer.release();
                musicService.mediaPlayer = MediaPlayer.create(this,uri);
                musicService.mediaPlayer.start();}
        }
        else{
            musicService.mediaPlayer = MediaPlayer.create(this,uri);
            musicService.mediaPlayer.start();
        }
        PlayerActivity.isPlaying = true;
        binding.timeSeekbar.setProgress(0);
        binding.timeSeekbar.setMax(musicService.mediaPlayer.getDuration());
        PlayerActivity.binding.pausePlay.setIconResource(R.drawable.ic_baseline_pause_24);
        musicService.showNotification(R.drawable.ic_baseline_pause_24);
    }

    //seekbar
    void seekbarSetup(){
        runnable = new Runnable() {
            @Override
            public void run() {
                binding.seekbarStart.setText(music.formatDuration((long) mediaPlayer.getCurrentPosition()));
                binding.timeSeekbar.setProgress(mediaPlayer.getCurrentPosition());

                handler.postDelayed(this,200);
            }
        };
        handler.postDelayed(runnable,0);
    }
}
