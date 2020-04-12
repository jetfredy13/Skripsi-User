package com.example.psikologiku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.psikologiku.Chat.UserKonseling;
import com.example.psikologiku.Konsultasi.PaketKonsultasi;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TermsAgreement extends AppCompatActivity {
    Psikolog psikolog;
    CheckBox cb_agree;
    Button btn_agree;
    SharedPreferences sp;
    DatabaseReference ref;
    SesiKonseling sesiKonseling;
    PaketKonsultasi paketKonsultasi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        psikolog = (Psikolog) getIntent().getSerializableExtra("Psikolog");
        sp = TermsAgreement.this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String id = sp.getString("id","");
        setContentView(R.layout.activity_terms_agreement);
        sesiKonseling = (SesiKonseling) getIntent().getSerializableExtra("Sesi");
        paketKonsultasi = (PaketKonsultasi) getIntent().getSerializableExtra("Paket");
        cb_agree = findViewById(R.id.cb_agree);
        btn_agree = findViewById(R.id.btn_agree);
        btn_agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(TermsAgreement.this, UserKonseling.class);
                intent.putExtra("Psikolog",psikolog);
                intent.putExtra("Sesi",sesiKonseling);
                startActivity(intent);
                finish();
                String key = id+":"+psikolog.getId();
                ref = FirebaseDatabase.getInstance().getReference("Sesi Konseling").child(key);
                ref.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        ref.child("status_konseling").setValue("Berlangsung");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }
}
