package com.example.ifixit.SERVICE_PROVIDER_FILES.TransactionHistory;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;


public class TransactionViewHolder extends RecyclerView.ViewHolder {

    public TextView service;
    public TextView name;
    public TextView date;
    public TextView price;
    public TextView status;


    public TransactionViewHolder(@NonNull View itemView) {
        super(itemView);

        service = itemView.findViewById(R.id.transactionService);
        name = itemView.findViewById(R.id.transactionName);
        date = itemView.findViewById(R.id.transactionDate);
        price = itemView.findViewById(R.id.transactionTotalPrice);
        status =itemView.findViewById(R.id.transactionStatus);



    }
}
