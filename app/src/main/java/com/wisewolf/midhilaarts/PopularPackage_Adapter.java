package com.wisewolf.midhilaarts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import com.wisewolf.midhilaarts.Model.Pack;

import java.util.List;

public class PopularPackage_Adapter extends
        RecyclerView.Adapter<PopularPackage_Adapter.PackageViewHolder>{
    private List<String> link;
    private RecyclerView package_recyclerView;
    private  OnItemClickListner itemClickListner;
    int length;
    int imageHeight;


    public PopularPackage_Adapter(List<String> link, RecyclerView package_recyclerView, int imageHeight) {
        this.link = link;
        this.package_recyclerView = package_recyclerView;
        this.imageHeight=imageHeight;

    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(
                R.layout.packagecart_long,
                parent,
                false
        );

        PackageViewHolder viewHolder = new PackageViewHolder(v,itemClickListner);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        holder.set(link.get(position));

    }

    @Override
    public int getItemCount() {
        return link.size();
    }

    class  PackageViewHolder extends  RecyclerView.ViewHolder{
        ImageView event;

        ConstraintLayout constraintLayout;




        public PackageViewHolder(View itemView, OnItemClickListner itemClickListner) {
            super(itemView);
            event=itemView.findViewById(R.id.special_event);

             constraintLayout=itemView.findViewById(R.id.constraintheight);




        }
        public void set(String link){
            Picasso.get().load(link).into(event);
        }

    }

    private class OnItemClickListner {
    }
}

