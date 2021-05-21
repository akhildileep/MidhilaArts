package com.wisewolf.midhilaarts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wisewolf.midhilaarts.Model.Pack;

import java.util.List;

public class Package_Adapter extends
        RecyclerView.Adapter<Package_Adapter.PackageViewHolder>{
    private List<Pack> packages;
    private RecyclerView package_recyclerView;
    private  OnItemClickListener itemClickListner;

    public Package_Adapter(List<Pack> packages, RecyclerView package_recyclerView,OnItemClickListener itemClickListner) {
        this.packages = packages;
        this.package_recyclerView = package_recyclerView;
        this.itemClickListner = itemClickListner;
    }

    @NonNull
    @Override
    public PackageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(
                R.layout.package_card,
                parent,
                false
        );

        PackageViewHolder viewHolder = new PackageViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PackageViewHolder holder, int position) {
        holder.set(packages.get(position));
        holder.bind(packages.get(position),itemClickListner,position);
    }
    public interface OnItemClickListener {
        void onItemClick(Pack item);
    }
    @Override
    public int getItemCount() {
        return packages.size();
    }

    class  PackageViewHolder extends  RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView add_image;
        TextView package_heading,total_rateings,price;
        TextView standard;




        public PackageViewHolder(View itemView) {
            super(itemView);
            add_image=itemView.findViewById(R.id.add_image);


            package_heading=itemView.findViewById(R.id.package_heading);
            total_rateings=itemView.findViewById(R.id.total_rateings);
            price=itemView.findViewById(R.id.price);
            standard=itemView.findViewById(R.id.standard);



        }
        public void set(Pack packages){
            try {
                package_heading.setText(packages.getCourseName());
                total_rateings.setText(packages.getAgeGroup());
                price.setText("Rs "+ packages.getPrice().get(0).getDiscount());
                Picasso.get().load(packages.getThumbnail()).into(add_image);
                standard.setText("G");


            }catch (Exception e)
            {
                e.printStackTrace();
                package_heading.setText("Pencil Drawing");
              //  Picasso.get().load(packages.getThumbnail()).into(add_image);
                price.setText("Rs "+ 400);
                total_rateings.setText("up to Age 20");

                standard.setText("G");
            }

        }

        @Override
        public void onClick(View v) {

        }

        public void bind(final Object o, final OnItemClickListener listener, final int position) {

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listener.onItemClick((Pack) o);

                }
            });


        }
    }


}

