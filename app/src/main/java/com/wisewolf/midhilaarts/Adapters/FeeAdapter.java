package com.wisewolf.midhilaarts.Adapters;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wisewolf.midhilaarts.R;

import java.util.List;


public class FeeAdapter extends  RecyclerView.Adapter<FeeAdapter.PackSelectAdapterViewHolder> {


    private List<String>  duration;
    private List<String>  fee;
    private List<String>  disc;
    private RecyclerView feelist;
    private static int lastClickedPosition = -1;
    private int selectedItem;



    public FeeAdapter(List<String> duration,List<String> fee,List<String> disc, RecyclerView feelist  ) {
        this.duration = duration;
        this.fee = fee;
        this.disc = disc;
        this.feelist = feelist;

    }


    @NonNull
    @Override
    public PackSelectAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(
            R.layout.fee_struct,
            parent,
            false
        );

        PackSelectAdapterViewHolder viewHolder = new PackSelectAdapterViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final PackSelectAdapterViewHolder holder, final int position) {



        holder.set(duration.get(position),fee.get(position),disc.get(position));


    }

    @Override
    public int getItemCount() {
        return fee.size();
    }

    public  class PackSelectAdapterViewHolder extends RecyclerView.ViewHolder{
        TextView duration,fees,disc;

        public PackSelectAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            duration=itemView.findViewById(R.id.duration);
            fees=itemView.findViewById(R.id.price);
            disc=itemView.findViewById(R.id.disc);
        }

        public void set(String dur, String fee, String desc) {
            duration.setText(new StringBuilder().append(dur).append(" Month").toString());
            fees.setText(new StringBuilder().append("   ").append(new StringBuilder().append("Rs ").append(fee).toString()).append("  ").toString());
            disc.setText(new StringBuilder().append("Rs ").append(desc).toString());
        }


    }


}
