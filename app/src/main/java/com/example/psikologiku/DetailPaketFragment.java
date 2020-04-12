package com.example.psikologiku;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.psikologiku.Konsultasi.KonsultasiHarian;
import com.example.psikologiku.Konsultasi.KonsultasiHarianAdapter;
import com.example.psikologiku.Konsultasi.PaketKonsultasi;
import com.example.psikologiku.Konsultasi.PaketKonsultasiAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class DetailPaketFragment extends Fragment {
    RecyclerView recview_konsulharian,recview_paket;
    Button beli_paket;
    DatabaseReference reference;
    List<KonsultasiHarian> ListKonsultasi;
    List<PaketKonsultasi> ListPaketKonsultasi;
    PaketKonsultasiAdapter paketKonsulAdapter;
    KonsultasiHarianAdapter konsulAdapter;
    SharedPreferences sp;
    Psikolog psikolog;
    User user;
    public DetailPaketFragment(Psikolog psikolog) {
        this.psikolog = psikolog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_detail_psikolog_paket, container, false);
        // Inflate the layout for this fragment
        beli_paket = v.findViewById(R.id.btn_beli);
        DetailPsikolog detail = (DetailPsikolog) getActivity();
        psikolog = detail.getPsikolog();
        recview_konsulharian = v.findViewById(R.id.jadwal_harian);
        recview_paket = v.findViewById(R.id.rec_view_paket);
        recview_konsulharian.setHasFixedSize(true);
        recview_paket.setHasFixedSize(true);
        recview_konsulharian.setLayoutManager(new LinearLayoutManager(getContext()));
        recview_paket.setLayoutManager(new LinearLayoutManager(getContext()));
        getUser();
        getJadwalHarian();
        loadPaket();
        return v;
    }
    private void getUser()
    {
        sp = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String id = sp.getString("id","");
        reference = FirebaseDatabase.getInstance().getReference("User").child(id);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(User.class);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void getJadwalHarian()
    {
        ListKonsultasi = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Psikolog").child(psikolog.getId()).child("KonsultasiHarian");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    for(DataSnapshot snapshot : dataSnapshot.getChildren())
                    {
                        KonsultasiHarian konsultasiHarian = snapshot.getValue(KonsultasiHarian.class);
                        ListKonsultasi.add(konsultasiHarian);
                    }
                    konsulAdapter = new KonsultasiHarianAdapter(ListKonsultasi,getContext());
                    recview_konsulharian.setAdapter(konsulAdapter);
                    konsulAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void loadPaket() {
        ListPaketKonsultasi = new ArrayList<>();
        reference = FirebaseDatabase.getInstance().getReference("Psikolog").child(psikolog.getId()).child("Paket Konseling");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snap : dataSnapshot.getChildren()) {
                        PaketKonsultasi konsultasi = snap.getValue(PaketKonsultasi.class);
                        ListPaketKonsultasi.add(konsultasi);
                    }
                    paketKonsulAdapter = new PaketKonsultasiAdapter(ListPaketKonsultasi,psikolog,user, getContext());
                    recview_paket.setAdapter(paketKonsulAdapter);
                    paketKonsulAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
