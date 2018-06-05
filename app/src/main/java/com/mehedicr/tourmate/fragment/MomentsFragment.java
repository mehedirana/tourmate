package com.mehedicr.tourmate.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mehedicr.tourmate.Model.Event;
import com.mehedicr.tourmate.Model.Moment;
import com.mehedicr.tourmate.R;
import com.mehedicr.tourmate.adapter.MomentListAdapter;


import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MomentsFragment extends Fragment {

    private RecyclerView momentListRecyclerView;
    private LinearLayoutManager layoutManager;
    private MomentListAdapter adapter;
    private List<Moment> momentList = new ArrayList<>();

    private FirebaseAuth firebaseAuth;
    private FirebaseUser user;
    private DatabaseReference rootRef;
    private DatabaseReference userRef;
    private DatabaseReference eventRef;
    private DatabaseReference momentRef;

    private String eventKey;

    private Event event;


    public MomentsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_moments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        momentListRecyclerView = view.findViewById(R.id.momentListRecyclerView);

        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        rootRef = FirebaseDatabase.getInstance().getReference().child("Tour Mate");
        userRef = rootRef.child(user.getUid());
        eventRef = userRef.child("Event");
        layoutManager = new LinearLayoutManager(getContext());
        momentListRecyclerView.setHasFixedSize(true);
        momentListRecyclerView.setLayoutManager(layoutManager);

        Bundle bundle = getArguments();
        if (bundle!=null){
            event = (Event) bundle.getSerializable("event");
            eventKey = event.getKey();
        }
        momentRef = eventRef.child(eventKey).child("Moment");
        momentRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                momentList.clear();
                for (DataSnapshot postData : dataSnapshot.getChildren()){
                    Moment moment = postData.getValue(Moment.class);
                    momentList.add(moment);
                    if (momentList.size()>0){
                        adapter = new MomentListAdapter(getContext(),momentList);
                        momentListRecyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });



    }
}
