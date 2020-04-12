package com.example.psikologiku.Chat;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;

import com.example.psikologiku.JadwalKonseling;
import com.example.psikologiku.Konsultasi.PaketKonsultasi;
import com.example.psikologiku.MessageListAdapter;
import com.example.psikologiku.Psikolog;
import com.example.psikologiku.R;
import com.example.psikologiku.SesiKonseling;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class UserKonseling extends AppCompatActivity {
    ImageButton btnSend,attach_file;
    private RecyclerView recView;
    private MessageListAdapter mlAdapter;
    ImageView psikolog_profile;
    TextView nama_psikolog;
    long t;
    private static final int AUDIO_REQUEST = 1;
    Psikolog psikolog;
    ArrayList<UserMessage> listMessage;
    EditText message;
    Dialog dialog;
    Date curTime;
    MediaRecorder recorder;
    TextView recording_status;
    DatabaseReference ref;
    SharedPreferences sp;
    String fileName;
    StorageReference storageReference;
    SesiKonseling sesiKonseling;
    JadwalKonseling jadwalKonseling;
    PaketKonsultasi paketKonsultasi;
    private Uri audioUrl;
    private StorageTask uploadTask;
    private static final int REQUEST_RECORD_AUDIO_PERMISSION = 200;
    private boolean permissionToRecordAccepted = false;
    private String [] permissions = {Manifest.permission.RECORD_AUDIO};

    String currUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_konseling);
        message = findViewById(R.id.text_send);
        curTime =  new Date();
        psikolog = (Psikolog) getIntent().getSerializableExtra("Psikolog");
        sesiKonseling = (SesiKonseling) getIntent().getSerializableExtra("Sesi");
        paketKonsultasi = (PaketKonsultasi) getIntent().getSerializableExtra("Paket");
        nama_psikolog = findViewById(R.id.nama_psikolog);
        psikolog_profile = findViewById(R.id.psikolok_profile);
        recording_status = findViewById(R.id.status_record);
        recording_status.setVisibility(View.INVISIBLE);
        dialog = new Dialog(this);
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
        final String id = sp.getString("id","");
        currUser = sp.getString("username","");
        LinearLayoutManager lm = new LinearLayoutManager(getApplicationContext());
        lm.setStackFromEnd(true);
        fileName = getExternalCacheDir().getAbsolutePath();
        ActivityCompat.requestPermissions(this, permissions, REQUEST_RECORD_AUDIO_PERMISSION);
        recView.setHasFixedSize(true);
        recView.setLayoutManager(lm);
        ref = FirebaseDatabase.getInstance().getReference("Psikolog").child(psikolog.getId());
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
        nama_psikolog.setText(psikolog.getNama());
        btnSend = findViewById(R.id.btn_send);
        attach_file = findViewById(R.id.attachment);
        /*
        attach_file.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                long duration = motionEvent.getEventTime() - motionEvent.getDownTime();
                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN  ){
                    //System.out.println("Time : " + duration +"");
                    startRecording();
                }
                else if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                {
                    stopRecord();

                    /*if(duration<1000)
                    {
                        Toast.makeText(UserKonseling.this,"Hold this button to record",Toast.LENGTH_SHORT).show();
                    }
                    else{

                        shortpressed = false;
                    }

                }
                return  false;
            }

        });*/

        attach_file.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uploadSound();
            }
        });
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               sendChat(psikolog.getId(),id,message.getText().toString(),"text");
            }
        });
        konfirmasi_sesi_selesai();
        loadChat(id,psikolog.getId());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_RECORD_AUDIO_PERMISSION:
                permissionToRecordAccepted  = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                break;
        }
        if (!permissionToRecordAccepted ) finish();
    }

    private void startRecording()
    {
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String id = sp.getString("id","");
        fileName += "/"+System.currentTimeMillis()+":"+id;
        recorder.setOutputFile(fileName);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        try{
            recorder.prepare();
        }catch (IOException e)
        {
            System.out.println("prepare () Failed");
        }
        recorder.start();
        recording_status.setVisibility(View.VISIBLE);
        recording_status.setText("Starting Recording ");
    }
    private void stopRecord(){
        recording_status.setVisibility(View.INVISIBLE);
        recorder.stop();
        recorder.reset();
        recorder.release();
        recorder = null;
    }

    private void uploadSound()
    {
        Intent intent = new Intent();
        intent.setType("audio/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,AUDIO_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == AUDIO_REQUEST && resultCode == RESULT_OK && data != null && data .getData() !=null){
            audioUrl = data.getData();
            if(uploadTask!=null && uploadTask.isInProgress())
            {
                Toast.makeText(UserKonseling.this,"Upload in progress",Toast.LENGTH_SHORT).show();
            }
            else{
                uploadVoiceMail();
            }
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = UserKonseling.this.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void uploadVoiceMail() {
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String id = sp.getString("id","");
       final ProgressDialog pd = new ProgressDialog(UserKonseling.this);
        pd.setMessage("Uploading Audio ...");
        pd.show();
        if(audioUrl != null){
            final StorageReference stReference = FirebaseStorage.getInstance().getReference("Voice Mail").child(System.currentTimeMillis()+":"+id);
            /*stReference.putFile(audioUrl).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pd.dismiss();
                    Toast.makeText(UserKonseling.this,"Uploading Finished",Toast.LENGTH_SHORT).show();
                }
            });*/
            uploadTask = stReference.putFile(audioUrl);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task <UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return stReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();
                        sendChat(psikolog.getId(),id,mUri,"audio");
                        //reference = FirebaseDatabase.getInstance().getReference("Psikolog").child(id);
                        //HashMap<String,Object> map = new HashMap<>();
                        //map.put("image_url",mUri);
                        //reference.updateChildren(map);
                        pd.dismiss();
                    }
                    else{
                        Toast.makeText(UserKonseling.this,"Failed !",Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UserKonseling.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                    pd.dismiss();
                }
            });
        } else{
            Toast.makeText(UserKonseling.this,"No Audio Selected",Toast.LENGTH_SHORT).show();
        }
    }

    private void konfirmasi_sesi_selesai()
    {
        if(sesiKonseling.getSesi_sekarang()!=null)
        {
            sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            final String id = sp.getString("id","");
            final String key = id+":"+psikolog.getId();
            /*ref = FirebaseDatabase.getInstance().getReference("Sesi Konseling").child(key);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sesiKonseling = dataSnapshot.getValue(SesiKonseling.class);
            */
            ref = FirebaseDatabase.getInstance().getReference("Sesi Konseling").child(key).child("Jadwal Sesi").child(sesiKonseling.getSesi_sekarang());
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    jadwalKonseling = dataSnapshot.getValue(JadwalKonseling.class);
                    if(jadwalKonseling.getStatus().equals("Selesai") && sesiKonseling.getStatus_konseling().equalsIgnoreCase("Berlangsung"))
                    {
                        show_konfirmasi();
                    }
                    else{

                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
    private void show_konfirmasi()
    {
        final Button btn_belum,btn_selesai;
        sp = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        final String id = sp.getString("id","");
        final String key = id+":"+psikolog.getId();
        dialog.setContentView(R.layout.custompopup);
        btn_belum = dialog.findViewById(R.id.btn_belum);
        btn_selesai = dialog.findViewById(R.id.btn_selesai);
        /*ref = FirebaseDatabase.getInstance().getReference("Sesi Konseling").child(key);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sesiKonseling = dataSnapshot.getValue(SesiKonseling.class);

         */
                btn_selesai.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int sesi_sekarang = Integer.parseInt(sesiKonseling.getSesi_sekarang());
                        int konsultasi_akhir = 0;
                        if(paketKonsultasi!=null)
                        {
                            konsultasi_akhir = Integer.parseInt(paketKonsultasi.getJumlah_sesi());
                        }
                        if(sesi_sekarang == konsultasi_akhir)
                        {
                            FirebaseDatabase.getInstance().getReference("Sesi Konseling").child(key).child("status_konseling").setValue("Selesai");
                            sesiKonseling.setSesi_sekarang(sesi_sekarang+"");
                        }
                        else{
                            sesi_sekarang+=1;
                            FirebaseDatabase.getInstance().getReference("Sesi Konseling").child(key).child("sesi_sekarang").setValue(sesi_sekarang+"");
                            sesiKonseling.setSesi_sekarang(sesi_sekarang+"");
                        }
                        dialog.dismiss();
                    }
                });
                btn_belum.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        FirebaseDatabase.getInstance().getReference("Sesi Konseling").child(key)
                                .child("Jadwal Sesi").child(sesiKonseling.getSesi_sekarang()).child("status").setValue("Belum");
                        dialog.dismiss();
                    }
                });

                //}
            /*}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/
        dialog.show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void sendChat(String user,String currUser,String text,String type){
        t = curTime.getTime();
        ref = FirebaseDatabase.getInstance().getReference().child("Konseling");
        UserMessage msg = new UserMessage(currUser,text,user,t,type);
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
                    mlAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
