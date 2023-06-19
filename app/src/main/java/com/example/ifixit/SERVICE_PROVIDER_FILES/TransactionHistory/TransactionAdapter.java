package com.example.ifixit.SERVICE_PROVIDER_FILES.TransactionHistory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionViewHolder> {

        private List<TransactionItem> transactionItemList;
        private Context context;

    public TransactionAdapter(Context context,List<TransactionItem> transactionItemList) {
        this.context = context;
        this.transactionItemList = transactionItemList;

    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.transaction_item,parent,false);
        return new TransactionViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {

        TransactionItem transactionItem = transactionItemList.get(position);

        holder.date.setText(transactionItem.getDate());
        holder.service.setText(transactionItem.getService());
        holder.name.setText(transactionItem.getName());
        holder.price.setText(transactionItem.getTotal());
        holder.status.setText(transactionItem.getStatus());



    }

    @Override
    public int getItemCount() {
        return transactionItemList.size();
    }
}
