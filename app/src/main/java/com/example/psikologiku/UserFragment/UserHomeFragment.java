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

import com.example.psikologiku.Artikel.Artikel;
import com.example.psikologiku.Artikel.ArtikelAdapter;
import com.example.psikologiku.Psikolog;
import com.example.psikologiku.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class UserHomeFragment extends Fragment {
    private RecyclerView rec_view_artikel;
    private ArtikelAdapter artikel_adapter;
    private List<Artikel> list_artikel;
    private List<Psikolog> list_psikolog;
    DatabaseReference ref;
    SharedPreferences sp;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.user_fragment_home,container,false);
        rec_view_artikel = view.findViewById(R.id.rec_view_artikel);
        rec_view_artikel.setHasFixedSize(true);
        rec_view_artikel.setLayoutManager(new LinearLayoutManager(getContext()));
        sp = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String id = sp.getString("id","");
        loadArtikel();
        return view;
    }
    private void loadArtikel()
    {
        list_artikel = new ArrayList<>();
        list_psikolog = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("Artikel");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_artikel.clear();
                for(DataSnapshot snap : dataSnapshot.getChildren())
                {
                    Artikel art = snap.getValue(Artikel.class);
                    art.setId(snap.getKey());
                    list_artikel.add(art);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        ref = FirebaseDatabase.getInstance().getReference("Psikolog");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list_psikolog.clear();
                for(int i =0; i<list_artikel.size() ; i++)
                {
                    for(DataSnapshot snap : dataSnapshot.getChildren())
                    {
                        if(snap.getKey().equals(list_artikel.get(i).getPenulis()))
                        {
                            Psikolog psikolog = snap.getValue(Psikolog.class);
                            list_psikolog.add(psikolog);
                        }
                    }
                }
                artikel_adapter = new ArtikelAdapter(getContext(),list_artikel,list_psikolog);
                rec_view_artikel.setAdapter(artikel_adapter);
                artikel_adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
