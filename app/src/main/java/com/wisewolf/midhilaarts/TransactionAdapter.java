package com.wisewolf.midhilaarts;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wisewolf.midhilaarts.Model.Transactions;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionAdapterViewHolder>{
    private List<Transactions> transactions;
    private RecyclerView searchItemListButton;
    private final  OnItemClickListener listener;
    Context context;
    int length, width;

    public interface OnItemClickListener {
        void onItemClick(String s, int position);
    }


    @NonNull
    @Override
    public TransactionAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionAdapterViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.transaction_card,
                        parent,
                        false
                )
        );
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionAdapterViewHolder holder, int position) {

holder.settext(transactions.get(position),position);
        holder.bind(transactions.get(position), listener,position);
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public TransactionAdapter(int length, int width, List<Transactions> transactions, RecyclerView searchItemListButton, Context context, OnItemClickListener listener) {
        this.transactions = transactions;
        this.length = length;
        this.width = width;
        this.searchItemListButton = searchItemListButton;
        this.listener = listener;
        this.context=context;
    }


    class  TransactionAdapterViewHolder extends RecyclerView.ViewHolder {
        private TextView paymentHead,done_fail,transaction_id,amount_details,packHead;

        public TransactionAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            paymentHead = itemView.findViewById(R.id.paymentHead);
            done_fail = itemView.findViewById(R.id.done_fail);
            transaction_id = itemView.findViewById(R.id.transaction_id);
            amount_details = itemView.findViewById(R.id.amount_details);
            packHead = itemView.findViewById(R.id.packHead);


        }

        void settext(Transactions transaction, int position){

          if (transaction.getStatus().equals("success")){
              try {
                  paymentHead.setText(R.string.paymentdone);
                  done_fail.setText(R.string.done);
                  packHead.setText("Transaction Id :"+transaction.getId());
                  amount_details.setText("Transfered  Rs "+transaction.getAmount()+" Via "+transaction.getPaymentMode());
                  transaction_id.setText("Pack Purchased - "+transaction.getPackName());


              }catch (Exception c){
                  Toast.makeText(context, String.valueOf(c), Toast.LENGTH_SHORT).show();
              }

          }
          else {
              try {
                  paymentHead.setText(R.string.transaction);
                  done_fail.setText(R.string.failed);
                  packHead.setText("Transaction Id :"+transaction.getId());
                  amount_details.setText("Transfer for Rs "+transaction.getAmount()+" Via "+transaction.getPaymentMode()+" was failed");
                  transaction_id.setText("Pack Purchased - "+transaction.getPackName());


              }
              catch (Exception e){
                  Toast.makeText(context, String.valueOf(e), Toast.LENGTH_SHORT).show();
              }

          }
        }


        public void bind(final Transactions s, final OnItemClickListener listeners, final int position) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
                    listeners.onItemClick(String.valueOf(s.getId()),position);



                }
            });
        }

    }
}
