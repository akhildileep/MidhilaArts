package com.wisewolf.midhilaarts;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ViewPagerAdapter extends RecyclerView.Adapter<ViewPagerAdapter.ViewPagerAdapterHolder> {
    private List<Model_viewPager> bannerItems;
    private ViewPager2 viewPager2;

    @NonNull
    @Override
    public ViewPagerAdapterHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewPagerAdapterHolder(
            LayoutInflater.from(parent.getContext()).inflate(
                R.layout.viewpager_content,
                parent,
                false
            )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPagerAdapterHolder holder, int position) {
        holder.setImage(bannerItems.get(position));
    }

    @Override
    public int getItemCount() {
        return bannerItems.size();
    }

    public   ViewPagerAdapter(List<Model_viewPager> bannerItems, ViewPager2 viewPager2) {
        this.bannerItems = bannerItems;
        this.viewPager2 = viewPager2;
    }

    static class  ViewPagerAdapterHolder extends  RecyclerView.ViewHolder{
        private ImageView imageView;

        ViewPagerAdapterHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.viewpager_content);
        }
        void setImage(Model_viewPager bannerItems){

            Picasso.get().load(bannerItems.getImage()).into(imageView);
            //imageView.setImageResource(R.drawable.profilepic);
        }


    }
}
