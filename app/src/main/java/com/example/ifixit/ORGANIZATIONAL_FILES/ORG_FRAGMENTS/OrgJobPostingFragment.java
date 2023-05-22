package com.example.ifixit.ORGANIZATIONAL_FILES.ORG_FRAGMENTS;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ifixit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class OrgJobPostingFragment extends Fragment {

    private LinearLayout postLayout;
    private Switch postSwitch;

    private EditText jobType;
    private EditText minRate, maxRate;
    private EditText Address;
    private EditText Duration;
    private EditText jobDecription;

    private Button createButton;

    private RecyclerView recyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.org_job_posting_fragment, container, false);

        //-----Current User id ----
        String currentUid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        String currentUserName;

        postLayout = rootView.findViewById(R.id.createPostLayout);
        postSwitch = rootView.findViewById(R.id.switch1);

        //---- EditText ----
        jobType = rootView.findViewById(R.id.postingJobtype);
        minRate = rootView.findViewById(R.id.postingMinRate);
        maxRate = rootView.findViewById(R.id.postingMaxRate);
        Address = rootView.findViewById(R.id.postingAddress);
        Duration = rootView.findViewById(R.id.postingDuration);
        jobDecription = rootView.findViewById(R.id.postingDescription);
        //------------------
        //---- Button ------
        createButton = rootView.findViewById(R.id.postingButton);
        //---- Recycler View ----
        recyclerView = rootView.findViewById(R.id.recycler_view);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String jobtype = jobType.getText().toString();
                final String minrate = minRate.getText().toString();
                final String maxrate = maxRate.getText().toString();
                final String address = Address.getText().toString();
                final String duration = Duration.getText().toString();
                final String jobdescription = jobDecription.getText().toString();

                if (TextUtils.isEmpty(jobtype) ||
                        TextUtils.isEmpty(minrate) ||
                        TextUtils.isEmpty(maxrate) ||
                        TextUtils.isEmpty(address) ||
                        TextUtils.isEmpty(duration) ||
                        TextUtils.isEmpty(jobdescription)

                ) {
                    Toast.makeText(getContext(), "Fill up all the neccessary information", Toast.LENGTH_SHORT).show();
                } else {


                    DatabaseReference currentUserRef = FirebaseDatabase.getInstance().getReference()
                            .child("organizational");



                    currentUserRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists()){
                                String currentUserName = snapshot.child("name").getValue(String.class);
                                String orgName = snapshot.child("orgname").getValue(String.class);

                                DatabaseReference jobPostingRef = FirebaseDatabase.getInstance().getReference()
                                        .child("job-posting");




                                Map jobInfo = new HashMap();
                                jobInfo.put("orgname",orgName);
                                jobInfo.put("userid",currentUid);
                                jobInfo.put("name", currentUserName);
                                jobInfo.put("jobtype", jobtype);
                                jobInfo.put("minrate", minrate);
                                jobInfo.put("maxrate", maxrate);
                                jobInfo.put("address", address);
                                jobInfo.put("description", jobdescription);

                                jobPostingRef.push().setValue(jobInfo);
                                Toast.makeText(getContext(), "Job Posted Succesfully", Toast.LENGTH_SHORT).show();

                                postSwitch.setChecked(false);

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });



                }

            }
        });


        postSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) {
                    postLayout.setVisibility(View.VISIBLE);
                } else {
                    postLayout.setVisibility(View.GONE);
                }
            }
        });

        return rootView;
    }
}