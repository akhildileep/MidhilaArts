package com.wisewolf.midhilaarts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GalleryHead extends RecyclerView.Adapter<GalleryHead.GalleryHeadViewHolder>{
    private List<Gallery> gallery;
    private RecyclerView searchItemListButton;
    private final  OnItemClickListener listener;
    int pageWidth, pageLength;

    public interface OnItemClickListener {
        void onItemClick(String s, int position);
    }


    @NonNull
    @Override
    public GalleryHeadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GalleryHeadViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.colorslab,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryHeadViewHolder holder, int position) {

holder.settext(gallery.get(position),position);
        holder.bind(gallery.get(position), listener,position);
    }

    @Override
    public int getItemCount() {
        return gallery.size();
    }

    public GalleryHead(List<Gallery> gallery, RecyclerView searchItemListButton, int pageWidth, int pageLength, OnItemClickListener listener) {
        this.gallery = gallery;
        this.searchItemListButton = searchItemListButton;
        this.listener = listener;
        this.pageWidth = pageWidth;
        this.pageLength = pageLength;
    }


    class  GalleryHeadViewHolder extends RecyclerView.ViewHolder {
        private TextView searchCat;

        public GalleryHeadViewHolder(@NonNull View itemView) {
            super(itemView);
            searchCat = itemView.findViewById(R.id.recent_search_button);

        }

        void settext(Gallery gallery, int position){
            setWidth();
            searchCat.setText(gallery.getId());
        }

        private void setWidth() {
            ViewTreeObserver viewTreeObserver = searchCat.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        searchCat.getViewTreeObserver().removeOnGlobalLayoutListener(this);


                        searchCat.setLayoutParams(new LinearLayout.LayoutParams(pageWidth / 3, LinearLayout.LayoutParams.WRAP_CONTENT));


                    }
                });
            }


        }

        public void bind(final Gallery s, final OnItemClickListener listeners, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listeners.onItemClick(s.getId(),position);



                }
            });
        }

    }
}
