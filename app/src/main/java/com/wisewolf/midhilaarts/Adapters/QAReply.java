package com.wisewolf.midhilaarts.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wisewolf.midhilaarts.R;
import com.wisewolf.midhilaarts.qa_model.QAModel;

import java.util.List;

public class QAReply extends RecyclerView.Adapter<QAReply.QAReplyViewHolder>{
    private List<QAModel> dailyTask;
    private RecyclerView dailyTask_recyclerView;


    public interface OnItemClickListener {
        void onItemClick(QAModel s);
    }

    public QAReply(List<QAModel> dailyTask, RecyclerView dailyTask_recyclerViewr) {
        this.dailyTask = dailyTask;
        this.dailyTask_recyclerView = dailyTask_recyclerView;

    }

    @NonNull
    @Override
    public QAReplyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(
            R.layout.replycard,
            parent,
            false
        );

         QAReplyViewHolder viewHolder = new  QAReplyViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull QAReplyViewHolder holder, int position) {




        holder.set(dailyTask.get(position));


    }

    @Override
    public int getItemCount() {
        return dailyTask.size();
    }

    class  QAReplyViewHolder extends RecyclerView.ViewHolder {

        TextView user_feed,njms_reply;
        public QAReplyViewHolder(@NonNull View itemView) {
            super(itemView);
            user_feed=itemView.findViewById(R.id.user_feed);
            njms_reply=itemView.findViewById(R.id.njms_reply);

        }

        public void set(QAModel o) {

            user_feed.setText(o.getQuestion());
            if (o.getAnswer().equals("")){
                njms_reply.setText("Not yet answered");
            }
            else {
                njms_reply.setText(o.getAnswer());
            }




        }


    }

}
