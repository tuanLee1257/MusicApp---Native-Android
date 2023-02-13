package com.example.musicapp.adapter;

import android.content.Context;
import android.content.Intent;
import android.media.MediaMetadataRetriever;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.PlayerActivity;
import com.example.musicapp.R;
import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

public class music_adapter extends RecyclerView.Adapter<music_adapter.MyViewHolder> {
    Context context;
    ArrayList<music> musicArrayList;


    public music_adapter(Context context, ArrayList<music> musicArrayList) {
        this.context = context;
        this.musicArrayList = musicArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.music_view,parent,false);

        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        music music = musicArrayList.get(position);

        holder.songName.setText(music.getTitle());
//        duration =
        String duration = music.formatDuration(music.getDuration());
        holder.duration.setText(duration);
        byte[] image = music.getAlbumArt(music.getPath());
        if(image!=null){
            Glide.with(context).asBitmap().load(image).into(holder.pic);
        }
        else {
            Glide.with(context).load(R.drawable.album4).into(holder.pic);
        }

        // click
        holder.music_view.setOnClickListener(view -> {
            Intent intent =  new Intent(context, PlayerActivity.class);
            intent.putExtra("pos",position);
            intent.putExtra("class","adapter");
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return musicArrayList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        RelativeLayout music_view;
        TextView songName;
        ShapeableImageView pic;
        TextView duration;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setLongClickable(true);
            music_view = itemView.findViewById(R.id.music_view);
            songName = itemView.findViewById(R.id.music_name);
            pic = itemView.findViewById(R.id.music_img);
            duration = itemView.findViewById(R.id.duration);
        }
    }
}
