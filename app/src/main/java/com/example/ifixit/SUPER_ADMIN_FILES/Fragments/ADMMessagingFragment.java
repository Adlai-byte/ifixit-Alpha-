package com.example.ifixit.SUPER_ADMIN_FILES.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;
import com.example.ifixit.SUPER_ADMIN_FILES.Fragments.UsersRecyclerView.CMAdapter;
import com.example.ifixit.SUPER_ADMIN_FILES.Fragments.UsersRecyclerView.UsersItemView;

import java.util.List;

public class ADMMessagingFragment extends Fragment {

    //------Recycler Variables----
    private RecyclerView recyclerView;
    private List<UsersItemView> usersItemViewList;
    private CMAdapter cmAdapter;
    //------------------------


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.adm_messaging_fragment, container, false);





        return rootView;
    }
}
