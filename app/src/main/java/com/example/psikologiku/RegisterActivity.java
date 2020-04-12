package com.example.psikologiku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {
    EditText etName,etEmail,etPassword,etConfirmpass,etNik;
    Button btnRegister;
    TextView tvGotoLogin;
    User usr;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        etName = findViewById(R.id.etNama);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etConfirmpass = findViewById(R.id.etConfirm);
        etNik = findViewById(R.id.etNik);
        btnRegister = findViewById(R.id.btnRegister);
        tvGotoLogin = findViewById(R.id.tvLogin);
        usr = new User();
        ref = FirebaseDatabase.getInstance().getReference().child("User");
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String pesan ="";
                if(etName.equals("") || etEmail.equals("") || etPassword.equals("") || etNik.equals(""))
                {
                   pesan = "Ada data yang masih kosong";
                }
                else if(!etPassword.getText().toString().equals(etConfirmpass.getText().toString()))
                {
                   pesan = "Password dan confirm password harus sama";
                }
                else{
                    usr.setNama(etName.getText().toString());
                    usr.setEmail(etEmail.getText().toString());
                    usr.setPassword(etPassword.getText().toString());
                    usr.setNIK(etNik.getText().toString());
                    usr.setSaldo(0);
                    usr.setTipeUser(0);
                    ref.push().setValue(usr);
                    pesan="Berhasil Register";
                }
                if(!pesan.equals(""))
                {
                   Toast.makeText(RegisterActivity.this,pesan,Toast.LENGTH_SHORT).show();
                }

            }
        });
        tvGotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginIntent = new Intent(RegisterActivity.this,MainActivity.class);
                finish();
                startActivity(loginIntent);
            }
        });
    }
}
