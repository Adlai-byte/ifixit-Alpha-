package com.example.ifixit.SERVICE_PROVIDER_FILES.SERVICEPROVIDER_FRAGMENTS.OngoingClasses;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;

public class OngoingHolder extends RecyclerView.ViewHolder {

    public TextView nameView;
    public TextView locationView;
    public TextView jobTypeView;
    public TextView totalPriceView;

    public Button completeButton;


    public OngoingHolder(@NonNull View itemView) {
        super(itemView);

        completeButton = itemView.findViewById(R.id.ongoingCompleteButton);
        nameView = itemView.findViewById(R.id.ongoingName);
        locationView = itemView.findViewById(R.id.ongoingLocation);
        jobTypeView = itemView.findViewById(R.id.ongoingTotalPrice);
        totalPriceView = itemView.findViewById(R.id.ongoingTotalPrice);




    }
}
