package com.example.flappybird;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<scoreData> list;
    int i=1;

    public MyAdapter(Context context, ArrayList<scoreData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.item,parent,false);
        return  new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Collections.reverse(list);
        scoreData user = list.get(position);
        holder.adsoyad.setText(user.getKulcad());
        holder.score.setText(String.valueOf(user.getScore()));
        holder.rank.setText(String.valueOf(i));
        i++;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class MyViewHolder extends  RecyclerView.ViewHolder{

        TextView score,adsoyad;
        Button rank;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            rank=itemView.findViewById(R.id.rank);
            score=itemView.findViewById(R.id.txtscorecek);
            adsoyad=itemView.findViewById(R.id.txtda);
        }
    }
}