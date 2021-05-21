package com.wisewolf.midhilaarts.Adapters;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.wisewolf.midhilaarts.Model.Transactions;
import com.wisewolf.midhilaarts.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.TransactionAdapterViewHolder>{
    List<String> messages;List<String> dates; List<String> uname; List<String> images;
    private RecyclerView commentList;
    Context context;


    public interface OnItemClickListener {
        void onItemClick(String s, int position);
    }


    @NonNull
    @Override
    public TransactionAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionAdapterViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.comment_card,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapterViewHolder holder, int position) {

      holder.settext(messages.get(position),position);

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    public CommentAdapter(List<String> messages, List<String> dates, List<String> uname, List<String> images, RecyclerView commentList, Context context) {
        this.messages = messages;
        this.dates = dates;
        this.uname = uname;
        this.images = images;
        this.commentList = commentList;
        this.context = context;

    }


    class  TransactionAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView unameT,date,message;
        ImageView image;

        public TransactionAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            unameT = itemView.findViewById(R.id.comment_uname);
            date = itemView.findViewById(R.id.comment_date);
            message = itemView.findViewById(R.id.comment_message);
            image = itemView.findViewById(R.id.comment_image);

            image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert(getAdapterPosition());
                }
            });


        }

        void settext(String s, int position) {


            try {
                unameT.setText(uname.get(position));
                date.setText(dates.get(position));
                message.setText(messages.get(position));
                if (!images.get(position).equals("")){
                    Picasso.get().load(images.get(position)).into(image);
                }
                else {
                    image.setVisibility(View.GONE);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }



    }

    private void alert(int adapterPosition) {
        // create an alert builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);

        builder.setIcon(R.drawable.login);
        // set the custom layout
        final View customLayout = LayoutInflater.from(context).inflate(R.layout.alertimage, null);

        ImageView cam =  customLayout.findViewById(R.id.previewImage);
        Picasso.get().load(images.get(adapterPosition)).into(cam);



        builder.setView(customLayout);
        // add a button
        AlertDialog dialogComment ;
        // create and show the alert dialog
        dialogComment= builder.create();
        dialogComment.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogComment.show();
    }
}
