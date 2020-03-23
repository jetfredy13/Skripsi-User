package com.example.psikologiku.Chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.psikologiku.MessageListAdapter;
import com.example.psikologiku.Psikolog;
import com.example.psikologiku.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;

public class UserKonseling extends AppCompatActivity {
    ImageButton btnSend;
    private RecyclerView recView;
    private MessageListAdapter mlAdapter;
    ImageView psikolog_profile;
    TextView nama_psikolog;
    long t;
    Psikolog psikolog;
    ArrayList<UserMessage> listMessage;
    EditText message;
    Date curTime;
    Intent intent;
    DatabaseReference ref;
    SharedPreferences sp;
    String currUser;
    String nama;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_konseling);
        message = findViewById(R.id.text_send);
        curTime =  new Date();
        nama_psikolog = findViewById(R.id.nama_psikolog);
        psikolog_profile = findViewById(R.id.psikolok_profile);
        intent = getIntent();
        Toolbar tool = findViewById(R.id.toolbar);
        setSupportActionBar(tool);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        recView = findViewById(R.id.rec_view_chat);
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        currUser = sp.getString("username","");
        Log.d("Current User",currUser);
        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
        lm.setStackFromEnd(true);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(lm);
        ref = FirebaseDatabase.getInstance().getReference("Psikolog").child(intent.getStringExtra("id"));
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    listMessage.add(ds.child("Konseling").getValue(UserMessage.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        psikolog = new Psikolog();
        nama = intent.getStringExtra("id").toString();
        nama_psikolog.setText(nama);
        psikolog.setNama(nama);
        Toast.makeText(UserKonseling.this,currUser,Toast.LENGTH_SHORT).show();
        btnSend = findViewById(R.id.btn_send);
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendChat(nama,currUser);
            }
        });
        loadChat(currUser,nama);
    }
    public void sendChat(String user, String currUser){

        t = curTime.getTime();
        ref = FirebaseDatabase.getInstance().getReference().child("Konseling");
        UserMessage msg = new UserMessage(currUser,message.getText().toString(),user,t);
        ref.push().setValue(msg);
        message.setText("");
    }
    public void loadChat(final String currUser, final String psikolog){
        listMessage = new ArrayList<>();
        ref = FirebaseDatabase.getInstance().getReference("Konseling");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listMessage.clear();
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    UserMessage msg = ds.getValue(UserMessage.class);
                    if(msg.getCurentUser().equals(currUser) && msg.getSended().equals(psikolog) ||
                    msg.getCurentUser().equals(psikolog) && msg.getSended().equals(currUser)){
                        listMessage.add(msg);
                    }
                    mlAdapter = new MessageListAdapter(getApplicationContext(),listMessage,"");
                    recView.setAdapter(mlAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
