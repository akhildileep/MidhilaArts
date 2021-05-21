package com.wisewolf.midhilaarts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.vimeo.networking.model.Video;
import com.wisewolf.midhilaarts.Global.GlobalData;
import com.wisewolf.midhilaarts.Model.Pack;

import java.util.ArrayList;

public class PackageVideoAdapter extends RecyclerView.Adapter<PackageVideoAdapter.PackageVideoAdapterViewHolder> {


    public PackageVideoAdapter(ArrayList videoList, RecyclerView videoList_recyclerView, int imageWidth, int pageLength, OnItemClickListener itemClickListner) {
        this.videoList = videoList;
        this.videoList_recyclerView = videoList_recyclerView;
        this.imageWidth = imageWidth;
        this.pageLength = pageLength;
        this.itemClickListner = itemClickListner;
    }

    public interface OnItemClickListener {
        void onItemClick(Video item) throws Exception;
    }

    private ArrayList videoList;
    private RecyclerView videoList_recyclerView;
    private OnItemClickListener itemClickListner;
    int imageWidth, pageLength;


    @NonNull
    @Override
    public PackageVideoAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(
            R.layout.packagevideo,
            parent,
            false
        );

        PackageVideoAdapterViewHolder viewHolder = new PackageVideoAdapterViewHolder(v, itemClickListner);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PackageVideoAdapterViewHolder holder, int position) {

        holder.set((Video) videoList.get(position));
        holder.bind(videoList.get(position), itemClickListner, position);


    }

    @Override
    public int getItemCount() {
        return videoList.size();
    }

    class PackageVideoAdapterViewHolder extends RecyclerView.ViewHolder {

        AutoResizeTextView head, subhead;
        ImageView thumb,watch;
        CardView cardWidth;

        public PackageVideoAdapterViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
            super(itemView);
            head = itemView.findViewById(R.id.head);
            subhead = itemView.findViewById(R.id.subHead);
            thumb = itemView.findViewById(R.id.thumbnail);
            cardWidth = itemView.findViewById(R.id.pack_card);
            watch = itemView.findViewById(R.id.watched);
           // setWidth();
        }

        public void set(Video video) {

            try {
                if (video.description == null) {
                    head.setText(video.name);

                } else {
                    String[] x = video.description.split("-");
                    head.setText(video.name);
                    subhead.setText(x[1]);
                }
                int i = video.pictures.sizes.size();
                if (i< 3){Picasso.get().load((video.pictures.sizes.get(2).link)).into(thumb);}
                else if (i< 6){Picasso.get().load((video.pictures.sizes.get(5).link)).into(thumb);}
                else if (i< 9){Picasso.get().load((video.pictures.sizes.get(5).link)).into(thumb);}
                else if (i< 12){Picasso.get().load((video.pictures.sizes.get(5).link)).into(thumb);}
                else {Picasso.get().load((video.pictures.sizes.get(i - 1).link)).into(thumb);}



                String[] videoNum = video.description.split("-");
                int uservideNum = Integer.valueOf(GlobalData.planTypeMod.getVideoNo());
                int VimeoVideoNum = Integer.valueOf(videoNum[0]);
                if (VimeoVideoNum<=uservideNum &&uservideNum!=0){
                    watch.setVisibility(View.VISIBLE);
                }
                else watch.setVisibility(View.GONE);


            } catch (Exception e) {

            }


        }

        public void bind(final Object o, final OnItemClickListener listener, final int position) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        listener.onItemClick((Video) o);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            });


        }

        private void setWidth() {
            ViewTreeObserver viewTreeObserver = cardWidth.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        cardWidth.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        //cardWidth.setLayoutParams(new LinearLayout.LayoutParams((int) (imageWidth / 1.3), (int) (pageLength / 3.5)));
                        cardWidth.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, (int) (pageLength / 3.5)));
                    }
                });
            }


        }
    }

    private class OnItemClickListner {
    }


}


