package com.example.psikologiku.Konsultasi;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psikologiku.JadwalKonseling;
import com.example.psikologiku.Psikolog;
import com.example.psikologiku.R;
import com.example.psikologiku.SesiKonseling;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class PaketKonsultasiAdapter extends RecyclerView.Adapter<PaketKonsultasiAdapter.PaketKonsultasiViewHolder> {
    private List<PaketKonsultasi> list_paketkonsultasi;
    private Psikolog psikolog;
    DatabaseReference reference;
    SharedPreferences sp;
    private JadwalKonseling jadwalKonseling;
    private List<JadwalKonseling> list_jadwal;
    private SesiKonseling sesiKonseling;
    private Context context;

    public PaketKonsultasiAdapter(List<PaketKonsultasi> list_paketkonsultasi,Psikolog psikologi, Context context) {
        this.list_paketkonsultasi = list_paketkonsultasi;
        this.context = context;
        this.psikolog = psikologi;
    }

    @NonNull
    @Override
    public PaketKonsultasiViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.paket_konseling,parent,false);
        return new PaketKonsultasiAdapter.PaketKonsultasiViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PaketKonsultasiViewHolder holder, final int position) {
        sp = context.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        String id = sp.getString("id","");
        final String nama = sp.getString("username","");
        final PaketKonsultasi paket = list_paketkonsultasi.get(position);
        sesiKonseling = new SesiKonseling();
        holder.nama_paket.setText(paket.getNama_paket());
        holder.jumlah_sesi.setText("Jumlah Sesi :"+paket.getJumlah_sesi());
        holder.harga.setText("Rp "+paket.getHarga());
        holder.btn_belipaket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference = FirebaseDatabase.getInstance().getReference("Sesi Konseling");
                sesiKonseling.setNama_user(nama);
                sesiKonseling.setNama_psikolog(psikolog.getNama());
                String key = reference.push().getKey();
                reference.child(key).setValue(sesiKonseling);
                reference = FirebaseDatabase.getInstance().getReference("Sesi Konseling").child(key);
                reference.child("Paket Konsultasi").setValue(list_paketkonsultasi.get(position));
                int count = Integer.parseInt(list_paketkonsultasi.get(position).getJumlah_sesi());
                reference = FirebaseDatabase.getInstance().getReference("Sesi Konseling").child(key).child("Jadwal Sesi");
                for (int i=0 ; i<count ; i++)
                {
                    jadwalKonseling = new JadwalKonseling();
                    jadwalKonseling.setHari("");
                    jadwalKonseling.setJam("");
                    jadwalKonseling.setTanggal("");
                    reference.child(i+1+"").setValue(jadwalKonseling);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list_paketkonsultasi.size();
    }

    public class PaketKonsultasiViewHolder extends RecyclerView.ViewHolder{
      TextView nama_paket,harga,jumlah_sesi;
      Button btn_belipaket;
      public PaketKonsultasiViewHolder(@NonNull View itemView) {
          super(itemView);
          btn_belipaket = itemView.findViewById(R.id.btn_beli);
          nama_paket = itemView.findViewById(R.id.nama_paket);
          harga = itemView.findViewById(R.id.harga_paket);
          jumlah_sesi = itemView.findViewById(R.id.jumlah_sesi);
      }
  }
}
