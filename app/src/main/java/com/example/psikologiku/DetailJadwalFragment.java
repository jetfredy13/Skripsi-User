package com.example.psikologiku;

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


public class DetailJadwalFragment extends Fragment {
    Psikolog psikolog;
    TextView jam_konsultasi,tanggal_konsultasi;
    DatabaseReference ref;
    private List<SesiKonseling> listSesiKonseling;
    SharedPreferences sp;
    SesiKonseling sesiKonseling;
    public DetailJadwalFragment(Psikolog psikolog) {
        this.psikolog = psikolog;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View v = inflater.inflate(R.layout.fragment_detail_jadwal, container, false);
        jam_konsultasi = v.findViewById(R.id.jam_konsultasi);
        tanggal_konsultasi = v.findViewById(R.id.tanggal_konsultasi);
        jam_konsultasi.setVisibility(View.INVISIBLE);
        loadPaket();
        return v;
    }
    private void loadPaket()
    {
        sp = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String id = sp.getString("id","");
        String key = id+":"+psikolog.getId();
        listSesiKonseling = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("Sesi Konseling").child(key);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    sesiKonseling = dataSnapshot.getValue(SesiKonseling.class);
                    if(sesiKonseling.getStatus_konseling().equals("Berlangsung"))
                    {
                        String tanggal = dataSnapshot.child("Jadwal Sesi").child(sesiKonseling.getSesi_sekarang())
                                .child("tanggal").getValue(String.class);
                        String jam = dataSnapshot.child("Jadwal Sesi").child(sesiKonseling.getSesi_sekarang()).child("jam")
                                .getValue(String.class);
                        if(tanggal.equals(""))
                        {
                            tanggal_konsultasi.setText("Belum menentukan jadwal untuk konseling");
                        }
                        else{
                            tanggal_konsultasi.setText("Tanggal : "+tanggal);
                            jam_konsultasi.setVisibility(View.VISIBLE);
                            jam_konsultasi.setText("Jam : " +jam);
                        }
                    }
                    else if(sesiKonseling.getStatus_konseling().equals("Selesai"))
                    {
                        tanggal_konsultasi.setText("Konsultasi sudah selesai");
                    }
                    else{
                        tanggal_konsultasi.setText("Belum menentukan jadwal untuk konseling");
                    }

                }
                else{
                    tanggal_konsultasi.setText("Anda tidak mendaftar sesi");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
