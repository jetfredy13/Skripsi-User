package com.example.psikologiku.UserFragment;

import android.content.Context;
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
import com.example.psikologiku.Chat.UserMessage;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserListKonselingFragment extends Fragment {
    private RecyclerView rec_view_list_konseling;
    private PsikologAdapter adapter;
    DatabaseReference reference;
    private List<String> uList;
    private List<Psikolog> ListPsikolog;
    SharedPreferences sp;
    String currUser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.user_fragment_konseling,container,false);
        rec_view_list_konseling = view.findViewById(R.id.rec_view_list_user_chat);
        rec_view_list_konseling.setHasFixedSize(true);
        rec_view_list_konseling.setLayoutManager(new LinearLayoutManager(getContext()));
        uList = new ArrayList<>();
        sp = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        currUser = sp.getString("username","");
        reference = FirebaseDatabase.getInstance().getReference("Konseling");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                uList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    UserMessage msg = snapshot.getValue(UserMessage.class);
                    if(msg.getCurentUser().equals(currUser)){
                        uList.add(msg.getSended());
                    }
                    if(msg.getSended().equals(currUser))
                    {
                        uList.add(msg.getCurentUser());
                    }
                }
                readUser();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        return view;
    }
    public void readUser(){
        ListPsikolog = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("User");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ListPsikolog.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Psikolog psikolog = snapshot.getValue(Psikolog.class);
                    for(String id : uList){
                        if(psikolog.getNama().equals(id)){
                            if(ListPsikolog.size() > 0 ){
                                for(Psikolog psikolog1 : ListPsikolog){
                                    if(!psikolog.getNama().equals(psikolog1.getNama())){
                                        ListPsikolog.add(psikolog);
                                    }
                                }
                            } else {
                                ListPsikolog.add(psikolog);
                            }
                        }
                    }
                }
                adapter = new PsikologAdapter(getContext(),ListPsikolog);
                rec_view_list_konseling.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
