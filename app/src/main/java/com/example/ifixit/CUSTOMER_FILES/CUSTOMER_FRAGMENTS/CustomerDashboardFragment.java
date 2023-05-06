package com.example.ifixit.CUSTOMER_FILES.CUSTOMER_FRAGMENTS;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.ifixit.R;

public class CustomerDashboardFragment extends Fragment {
    private CardView plumberCard;
    private CardView electricianCard;
    private CardView masonryCard;
    private CardView gardenerCard;
    private CardView computerTechnicianCard;
    private CardView carpenterCard;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.customer_fragment_dashboard, container, false);

        plumberCard = (CardView) rootView.findViewById(R.id.plumberCard);
        masonryCard = (CardView) rootView.findViewById(R.id.masonryCard);
        gardenerCard = (CardView) rootView.findViewById(R.id.gardenerCard);
        electricianCard = (CardView) rootView.findViewById(R.id.electricianCard);
        carpenterCard = (CardView) rootView.findViewById(R.id.carpentryCard);
        computerTechnicianCard = (CardView) rootView.findViewById(R.id.computerTechnicianCard);


        return rootView;




    }
}
