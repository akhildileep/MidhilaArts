package com.wisewolf.midhilaarts.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wisewolf.midhilaarts.R;

import java.util.ArrayList;
import java.util.List;


public class PackSelectAdapter extends  RecyclerView.Adapter<PackSelectAdapter.PackSelectAdapterViewHolder> {


    private List<String>  packFee;
    private RecyclerView recent_add_recyclerView;
    private static int lastClickedPosition = -1;
    private int selectedItem;
    private final  OnItemClickListener listener;

    List<String> duration;
    List<String> price ;
    List<String> disc;



    public PackSelectAdapter(List<String> packFee, RecyclerView pricelist, List<String> price, List<String> duration, List<String> disc, OnItemClickListener onItemClickListener) {
        this.packFee = packFee;
        this.recent_add_recyclerView = recent_add_recyclerView;
        this.listener = onItemClickListener;
        selectedItem = -1;

        this.price = price;
        this.duration = duration;
        this.disc = disc;

    }

    public interface OnItemClickListener {
        void onItemClick(String list,String price,String dur,String disc);
    }

    @NonNull
    @Override
    public PackSelectAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(
            R.layout.packindividual,
            parent,
            false
        );

        PackSelectAdapterViewHolder viewHolder = new PackSelectAdapterViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PackSelectAdapterViewHolder holder, final int position) {

        holder.subj.setBackgroundColor(Color.parseColor("#FFFFFF"));

        if (selectedItem == position) {
            holder.subj.setBackgroundColor(Color.parseColor("#802196F3"));
        }

        holder.set(packFee.get(position));
        holder.bind(packFee.get(position), price.get(position),duration.get(position),disc.get(position),listener, position);

    }

    @Override
    public int getItemCount() {
        return packFee.size();
    }

    public  class PackSelectAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView subj;

        public PackSelectAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            subj=itemView.findViewById(R.id.packselect_line);
        }

        public void set(String s) {
            subj.setText(s);
        }

        public void bind(final String list,final String price,final String dur,final String disc, final OnItemClickListener listeners, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listeners.onItemClick(list,price,dur,disc);
                    int previousItem = selectedItem;
                    selectedItem = position;
                    notifyItemChanged(previousItem);
                    notifyItemChanged(position);


                }
            });
        }
    }



}
