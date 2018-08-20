package com.example.sofi.mediauploader.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.sofi.mediauploader.R;
import com.example.sofi.mediauploader.data.Media;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MediaListAdapter extends RecyclerView.Adapter<MediaListAdapter.MyViewHolder> {

    Context c;
    ArrayList<Media> mediaList;

    public MediaListAdapter(Context c, ArrayList<Media> mediaList) {
        this.c = c;
        this.mediaList = mediaList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.model,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Media media= mediaList.get(position);

        holder.nameTxt.setText(media.getName());
        Picasso.with(c).load(media.getUri()).placeholder(R.drawable.placeholder).into(holder.img);
    }

    @Override
    public int getItemCount(){
        return mediaList.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.file_name_txt)
        TextView nameTxt;
        @BindView(R.id.uploadedImg)
        ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }
}
