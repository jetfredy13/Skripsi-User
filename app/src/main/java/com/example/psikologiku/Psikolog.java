package com.example.psikologiku;

import android.widget.Toast;

import java.io.Serializable;

public class Psikolog implements Serializable {
    private  String Nama;
    private  String Email;
    private  String Password;
    private  String NIK;
    private  String Bidang;
    private  String id;
    private  String tanggal_berakhir;
    private  int tipeUser;
    private  String image_url;

    public String getTanggal_berakhir() {
        return tanggal_berakhir;
    }

    public void setTanggal_berakhir(String tanggal_berakhir) {
        this.tanggal_berakhir = tanggal_berakhir;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String getBidang() {
        return Bidang;
    }

    public void setBidang(String bidang) {
        Bidang = bidang;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getTipeUser() {
        return tipeUser;
    }

    public void setTipeUser(int tipeUser) {
        this.tipeUser = tipeUser;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getNIK() {
        return NIK;
    }

    public void setNIK(String NIK) {
        this.NIK = NIK;
    }
}