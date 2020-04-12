package com.example.psikologiku;

public class User {
    private  String Nama,Email,Password,NIK,id;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private  int tipeUser,saldo;
    private  String ImageUrl;

    public int getSaldo() {
        return saldo;
    }

    public void setSaldo(int saldo) {
        this.saldo = saldo;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
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
