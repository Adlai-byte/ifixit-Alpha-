package com.example.ifixit.SERVICE_PROVIDER_FILES.SERVICEPROVIDER_FRAGMENTS.OngoingClasses;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
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
    public TextView dateView;

    public LinearLayout validation;
    public Button completeStatusButton;
    public RadioGroup radioGroup;
    public Button ongoingCancelledButton;

    public OngoingHolder(@NonNull View itemView) {
        super(itemView);


        radioGroup = itemView.findViewById(R.id.completeStatusradioGroup);
        completeButton = itemView.findViewById(R.id.ongoingCompletedButton);
        nameView = itemView.findViewById(R.id.ongoingName);
        locationView = itemView.findViewById(R.id.ongoingLocation);
        jobTypeView = itemView.findViewById(R.id.ongoingJobType);
        totalPriceView = itemView.findViewById(R.id.ongoingTotalPrice);
        dateView = itemView.findViewById(R.id.ongoingDate);
        ongoingCancelledButton = itemView.findViewById(R.id.ongoingCancelledButton);
        validation = itemView.findViewById(R.id.completeStatusLayout);
        completeStatusButton = itemView.findViewById(R.id.completeStatusButton);


    }
}