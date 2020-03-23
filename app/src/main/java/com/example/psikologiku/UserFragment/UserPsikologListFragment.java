package com.example.psikologiku.UserFragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psikologiku.Psikolog;
import com.example.psikologiku.PsikologAdapter;
import com.example.psikologiku.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserPsikologListFragment extends Fragment {
    private RecyclerView recView;
    private PsikologAdapter rvAdapter;
    private List<Psikolog> psikologList;
    DatabaseReference ref;
    SharedPreferences sp;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.user_fragment_psikolog_list,container,false);
        recView = v.findViewById(R.id.recView);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(new LinearLayoutManager(getContext()));
        getData();
        return v;

    }

    private void getData()
    {
        psikologList = new ArrayList<>();
        sp = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String currUser = sp.getString("username","");
        ref = FirebaseDatabase.getInstance().getReference("Psikolog");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Psikolog psikolog = ds.getValue(Psikolog.class);
                    psikolog.setId(ds.getKey());
                    psikologList.add(psikolog);
                }
                rvAdapter = new PsikologAdapter(getContext(),psikologList);
                recView.setAdapter(rvAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
