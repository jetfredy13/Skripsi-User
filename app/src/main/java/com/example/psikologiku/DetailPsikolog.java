package com.example.psikologiku;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
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
import com.example.psikologiku.UserFragment.MainUserFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.sql.Time;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.example.psikologiku.AppChannel.CHANNEL_1_ID;

public class DetailPsikolog extends AppCompatActivity {
    Psikolog psikolog;
    ImageView image_profile;
    TextView nama_psikolog;
    Button chat;
    SharedPreferences sp;
    Dialog dialog;
    DatabaseReference reference;
    PaketKonsultasi paketKonsultasi;
    JadwalKonseling jadwalKonseling;
    SesiKonseling sesiKonseling;
    private NotificationManagerCompat notificationManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_psikolog);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_nav_detail);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
        psikolog =(Psikolog) getIntent().getSerializableExtra("Psikolog");
        image_profile = findViewById(R.id.profile_psikolog);
        sesiKonseling = new SesiKonseling();
        paketKonsultasi = new PaketKonsultasi();
        dialog = new Dialog(this);
        chat = findViewById(R.id.chat);
        nama_psikolog = findViewById(R.id.nama_user);
        nama_psikolog.setText(psikolog.getNama());
        notificationManager = NotificationManagerCompat.from(this);
        if(psikolog.getImage_url().equals("default"))
        {
            image_profile.setImageResource(R.mipmap.ic_launcher);
        }
        else {
            Glide.with(getApplicationContext()).load(psikolog.getImage_url()).into(image_profile);
        }
        if (savedInstanceState == null)
        {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_detail,
                    new DetailPaketFragment(psikolog)).commit();
        }
        checkjadwal();
        chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                konseling_cek();
            }
        });
    }
    private void checkjadwal(){
        sp = DetailPsikolog.this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String id = sp.getString("id","");
        String key = id+":"+psikolog.getId();
        reference = FirebaseDatabase.getInstance().getReference("Sesi Konseling").child(key).child("Jadwal Sesi");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                showNotification();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
    private void showNotification() {
        Intent activityIntent = new Intent(this, DetailPsikolog.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, activityIntent, 0);
        Intent broadcastIntent = new Intent(this, BroadCast.class);
        String message = "Psikolog anda menambahkan jadwal baru mohon cek aplikasi anda";
        broadcastIntent.putExtra("toastMessage", message);
        PendingIntent actionIntent = PendingIntent.getBroadcast(this,
                0, broadcastIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification notification = new NotificationCompat.Builder(this, CHANNEL_1_ID)
                    .setSmallIcon(R.drawable.ic_android)
                    .setContentTitle("Psikologiku")
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                    .setColor(Color.BLUE)
                    .setContentIntent(contentIntent)
                    .setAutoCancel(true)
                    .setOnlyAlertOnce(true)
                    .addAction(R.mipmap.ic_launcher, "Toast", actionIntent)
                    .build();
            notificationManager.notify(1, notification);
    }
    private BottomNavigationView.OnNavigationItemSelectedListener navListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Fragment selectedFragment = null;
            switch (menuItem.getItemId())
            {
                case R.id.nav_paket:
                    selectedFragment = new DetailPaketFragment(psikolog);
                    break;
                case R.id.nav_jadwal:
                    selectedFragment = new DetailJadwalFragment(psikolog);
                    break;
            }
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_detail,selectedFragment).commit();
            return true;
        }
    };

    private void konseling_cek()
    {
        sp = DetailPsikolog.this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String id = sp.getString("id","");
        String key = id+":"+psikolog.getId();
        reference = FirebaseDatabase.getInstance().getReference("Sesi Konseling").child(key);
        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists() )
                {
                    sesiKonseling = dataSnapshot.getValue(SesiKonseling.class);
                    String sesi_sekarang = sesiKonseling.getSesi_sekarang();
                    jadwalKonseling = dataSnapshot.child("Jadwal Sesi").child(sesi_sekarang).getValue(JadwalKonseling.class);
                    paketKonsultasi = dataSnapshot.child("Paket Konsultasi").getValue(PaketKonsultasi.class);
                    if(!jadwalKonseling.getJam().equals(""))
                    {
                        Calendar c = Calendar.getInstance();
                        String[] waktu = jadwalKonseling.getJam().split(":");
                        String[] tanggal = jadwalKonseling.getTanggal().split("-");
                        int jam  = Integer.parseInt(waktu[0]);
                        int menit = Integer.parseInt(waktu[1]);
                        int hari = Integer.parseInt(tanggal[0]);
                        int bulan = Integer.parseInt(tanggal[1]);
                        int tahun = Integer.parseInt(tanggal[2]);
                        if(c.get(Calendar.DATE)== hari && c.get(Calendar.MONTH)+1 == bulan && c.get(Calendar.YEAR)==tahun &&
                                sesiKonseling.getStatus_konseling().equals("Belum") && c.get(Calendar.HOUR_OF_DAY)>=jam
                                && c.get(Calendar.MINUTE)>=menit)
                        {
                            Intent intent = new Intent(DetailPsikolog.this, TermsAgreement.class);
                            intent.putExtra("Psikolog",psikolog);
                            intent.putExtra("Sesi",sesiKonseling);
                            intent.putExtra("Paket",paketKonsultasi);
                            startActivity(intent);
                        }
                        else{
                            masukchat();
                        }
                    }
                    else{
                        masukchat();
                    }
                }
                else{
                    masukchat();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    public Psikolog getPsikolog()
    {
        return (Psikolog) getIntent().getSerializableExtra("Psikolog");
    }
    private void masukchat()
    {
        Intent intent = new Intent(DetailPsikolog.this, UserKonseling.class);
        intent.putExtra("Psikolog",psikolog);
        intent.putExtra("Sesi",sesiKonseling);
        intent.putExtra("Paket",paketKonsultasi);
        startActivity(intent);
        finish();
    }

}
