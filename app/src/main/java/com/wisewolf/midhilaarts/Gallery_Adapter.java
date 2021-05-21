package com.wisewolf.midhilaarts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class Gallery_Adapter extends
        RecyclerView.Adapter<Gallery_Adapter.Gallery_AdapterViewHolder>{
    private List<Model_viewPager> model_viewPager;
    private RecyclerView package_recyclerView;
    private  OnItemClickListner itemClickListner;

    public Gallery_Adapter(List<Model_viewPager> model_viewPager, RecyclerView package_recyclerView ) {
        this.model_viewPager = model_viewPager;
        this.package_recyclerView = package_recyclerView;
    }

    @NonNull
    @Override
    public Gallery_AdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(
                R.layout.gallery_picture,
                parent,
                false
        );

        Gallery_AdapterViewHolder viewHolder = new Gallery_AdapterViewHolder(v,itemClickListner);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull Gallery_AdapterViewHolder holder, int position) {
        holder.set(model_viewPager.get(position));
    }

    @Override
    public int getItemCount() {
        return model_viewPager.size();
    }

    class  Gallery_AdapterViewHolder extends  RecyclerView.ViewHolder{
        ImageView add_image;





        public Gallery_AdapterViewHolder(View itemView, OnItemClickListner itemClickListner) {
            super(itemView);
            add_image=itemView.findViewById(R.id.gallery_image);




        }
        public void set(Model_viewPager packages){
            try {

                notice(packages.getImage());




            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }

        private void notice(String image) {
            Picasso.get().load(image).into(add_image);
        }

    }

    private class OnItemClickListner {
    }

}

