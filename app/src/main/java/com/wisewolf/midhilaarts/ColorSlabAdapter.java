package com.wisewolf.midhilaarts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wisewolf.midhilaarts.Model.Pack;

import java.util.List;

public class ColorSlabAdapter extends RecyclerView.Adapter<ColorSlabAdapter.ColorSlabAdapterViewHolder>{
    private List<String> packages;
    private RecyclerView searchItemListButton;


    @NonNull
    @Override
    public ColorSlabAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ColorSlabAdapterViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.colorslab,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull ColorSlabAdapterViewHolder holder, int position) {

holder.settext(packages.get(position),position);
    }

    @Override
    public int getItemCount() {
        return packages.size();
    }

    public ColorSlabAdapter(List<String> packages, RecyclerView searchItemListButton) {
        this.packages = packages;
        this.searchItemListButton = searchItemListButton;
    }


    class  ColorSlabAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView searchCat;

        public ColorSlabAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            searchCat = itemView.findViewById(R.id.recent_search_button);
        }

        void settext(String packages, int position){



            searchCat.setText(packages);
        }

    }
}
