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


public class MediaListAdapter extends RecyclerView.Adapter<MediaListAdapter.MyViewHolder> {

    Context c;
    ArrayList<Media> media;

    public MediaListAdapter(Context c, ArrayList<Media> media) {
        this.c = c;
        this.media = media;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(c).inflate(R.layout.model,parent,false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        Media s= media.get(position);

        holder.nameTxt.setText(s.getName());
        Picasso.with(c).load(s.getUri()).placeholder(R.drawable.placeholder).into(holder.img);
    }

    @Override
    public int getItemCount(){
        return media.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTxt;
        ImageView img;

        public MyViewHolder(View itemView) {
            super(itemView);

            nameTxt= (TextView) itemView.findViewById(R.id.file_name_txt);
            img= (ImageView) itemView.findViewById(R.id.uploadedImg);

        }
    }
}
