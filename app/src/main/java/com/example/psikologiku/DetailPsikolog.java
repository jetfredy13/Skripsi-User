package com.example.psikologiku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.psikologiku.Chat.UserKonseling;
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

public class DetailPsikolog extends AppCompatActivity {
    Psikolog psikolog;
    ImageView image_profile;
    TextView nama_psikolog;
    RecyclerView recview_konsulharian,recview_paket;
    Button chat,beli_paket;
    SharedPreferences sp;
    DatabaseReference reference;
    List<KonsultasiHarian> ListKonsultasi;
    List<PaketKonsultasi> ListPaketKonsultasi;
    PaketKonsultasiAdapter paketKonsulAdapter;
    KonsultasiHarianAdapter konsulAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_psikolog);
        psikolog =(Psikolog) getIntent().getSerializableExtra("Psikolog");
        recview_konsulharian = findViewById(R.id.jadwal_harian);
        recview_paket = findViewById(R.id.rec_view_paket);
        image_profile = findViewById(R.id.profile_psikolog);
        recview_konsulharian.setHasFixedSize(false);
        recview_paket.setHasFixedSize(false);
        recview_konsulharian.setLayoutManager(new LinearLayoutManager(this));
        recview_paket.setLayoutManager(new LinearLayoutManager(this));
        chat = findViewById(R.id.chat);
        beli_paket = findViewById(R.id.btn_beli);
        nama_psikolog = findViewById(R.id.nama_user);
        nama_psikolog.setText(psikolog.getNama());
        if(psikolog.getImage_url().equals("default"))
        {
            image_profile.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            Glide.with(getApplicationContext()).load(psikolog.getImage_url()).into(image_profile);
        }
        getJadwalHarian();
        loadPaket();
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailPsikolog.this, UserKonseling.class);
                intent.putExtra("id",psikolog.getId());
                intent.putExtra("key",psikolog.getEmail());
                startActivity(intent);
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
                        Toast.makeText(DetailPsikolog.this,konsultasiHarian.getHari(),Toast.LENGTH_SHORT).show();
                        ListKonsultasi.add(konsultasiHarian);
                    }
                    konsulAdapter = new KonsultasiHarianAdapter(ListKonsultasi,getBaseContext());
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
                        Toast.makeText(DetailPsikolog.this,konsultasi.getNama_paket(),Toast.LENGTH_SHORT).show();
                        ListPaketKonsultasi.add(konsultasi);
                    }
                    paketKonsulAdapter = new PaketKonsultasiAdapter(ListPaketKonsultasi,psikolog, getBaseContext());
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
