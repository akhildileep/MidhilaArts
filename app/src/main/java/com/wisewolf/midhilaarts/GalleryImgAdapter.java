package com.wisewolf.midhilaarts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class GalleryImgAdapter extends RecyclerView.Adapter<GalleryImgAdapter.GalleryImgAdapterViewHolder>{
    List<String> gallery;
    private RecyclerView searchItemListButton;
    private final  OnItemClickListener listener;
    int pageWidth, pageLength;

    public interface OnItemClickListener {
        void onItemClick(String s);
    }


    @NonNull
    @Override
    public GalleryImgAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GalleryImgAdapterViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.gallery_image,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryImgAdapterViewHolder holder, int position) {

holder.settext(gallery.get(position),position);
        holder.bind(gallery.get(position), listener,position);
    }

    @Override
    public int getItemCount() {
        return gallery.size();
    }

    public GalleryImgAdapter(int pageWidth, int pageLength, List<String> gallery, RecyclerView searchItemListButton, OnItemClickListener listener) {
        this.gallery = gallery;
        this.searchItemListButton = searchItemListButton;
        this.listener = listener;
        this.pageWidth = pageWidth;
        this.pageLength = pageLength;
    }


    class  GalleryImgAdapterViewHolder extends RecyclerView.ViewHolder {
        private ImageView galleryImg;
        CardView imgCard;


        public GalleryImgAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            galleryImg = itemView.findViewById(R.id.galleryImg);

            imgCard = itemView.findViewById(R.id.imgCard);

        }

        void settext(String gallery, int position){
           // setWidth();
           // imgCard.setLayoutParams(new LinearLayout.LayoutParams((int) (p , pageLength / 2));
           // imgCard.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) (pageLength / 3.5)));
            Picasso.get().load(gallery).into(galleryImg);
        }

        private void setWidth() {
            ViewTreeObserver viewTreeObserver = imgCard.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        imgCard.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                        imgCard.setLayoutParams(new LinearLayout.LayoutParams((int) (pageWidth / 1.5), pageLength / 2));


                    }
                });
            }


        }

        public void bind(final String s, final OnItemClickListener listeners, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listeners.onItemClick(s);



                }
            });
        }

    }
}
