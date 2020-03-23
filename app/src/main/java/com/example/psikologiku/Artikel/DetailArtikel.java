package com.example.psikologiku.Artikel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.psikologiku.Psikolog;
import com.example.psikologiku.R;

public class DetailArtikel extends AppCompatActivity {
    Artikel artikel;
    Psikolog psikolog;
    TextView isi_artikel,judul_artikel,nama_psikolog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_artikel);
        artikel = (Artikel) getIntent().getSerializableExtra("Artikel");
        psikolog = (Psikolog) getIntent().getSerializableExtra("Psikolog");
        isi_artikel = findViewById(R.id.detail_artikel);
        judul_artikel = findViewById(R.id.judul_detail_artikel);
        nama_psikolog = findViewById(R.id.nama_psikolog);
        nama_psikolog.setText(psikolog.getNama() + " \n" + psikolog.getBidang());
        judul_artikel.setText(artikel.getJudul());
        isi_artikel.setText(artikel.getIsi_artikel());

    }
}
