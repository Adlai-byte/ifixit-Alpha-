package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS.ListViewClasses;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.ifixit.CUSTOMER_FILES.CustomerCheckOutActivity;
import com.example.ifixit.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class ListViewAdapter extends RecyclerView.Adapter<ListViewAdapter.ViewHolder> implements Filterable {

    private List<ListViewItem> originalList;
    private List<ListViewItem> filteredList;
    private Context context;
    private ItemClickListener itemClickListener;

    public ListViewAdapter(Context context, List<ListViewItem> listViewItems) {
        this.context = context;
        this.originalList = listViewItems;
        this.filteredList = listViewItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.customer_list_view_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListViewItem listViewItem = filteredList.get(position);


        holder.nameTextView.setText(listViewItem.getNAME());
        holder.serviceTextView.setText(listViewItem.getSERVICE());
        holder.addressTextView.setText(listViewItem.getADDRESS());
        holder.ratingBar.setRating(listViewItem.getRATING());
        Glide.with(context).load(listViewItem.getProfileImageUrl()).into(holder.profileImageView);

        holder.hireButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String serviceProviderUserId = listViewItem.getUSERID();
                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid().toString();;


                Intent intent  = new Intent(context, CustomerCheckOutActivity.class);
                intent.putExtra("customerUserId",currentUserId );
                intent.putExtra("serviceProviderUserId",serviceProviderUserId);

                context.startActivity(intent);


            }
        });
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView profileImageView;
        TextView nameTextView;
        TextView serviceTextView;
        TextView addressTextView;
        RatingBar ratingBar;
        Button hireButton;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            hireButton = itemView.findViewById(R.id.hireButton);
            profileImageView = itemView.findViewById(R.id.list_view_imageView);
            nameTextView = itemView.findViewById(R.id.list_view_name);
            serviceTextView = itemView.findViewById(R.id.list_view_service);
            addressTextView = itemView.findViewById(R.id.list_view_address);
            ratingBar = itemView.findViewById(R.id.list_view_rating);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (itemClickListener != null) {
                itemClickListener.onItemClick(v, getAdapterPosition());
            }
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String searchQuery = constraint.toString().toLowerCase();

                if (searchQuery.isEmpty()) {
                    filteredList = originalList;
                } else {
                    List<ListViewItem> tempList = new ArrayList<>();
                    for (ListViewItem item : originalList) {
                        if (item.getNAME().toLowerCase().contains(searchQuery) ||
                                item.getSERVICE().toLowerCase().contains(searchQuery) ||
                                item.getADDRESS().toLowerCase().contains(searchQuery)) {
                            tempList.add(item);
                        }
                    }
                    filteredList = tempList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                filteredList = (List<ListViewItem>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
