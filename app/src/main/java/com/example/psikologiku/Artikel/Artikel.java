package com.example.psikologiku.Artikel;

import java.io.Serializable;

public class Artikel implements Serializable {
    private String judul;
    private String isi_artikel;
    private String tanggal_artikel;
    private String penulis;
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getIsi_artikel() {
        return isi_artikel;
    }

    public void setIsi_artikel(String isi_artikel) {
        this.isi_artikel = isi_artikel;
    }

    public String getTanggal_artikel() {
        return tanggal_artikel;
    }

    public void setTanggal_artikel(String tanggal_artikel) {
        this.tanggal_artikel = tanggal_artikel;
    }
    public String getPenulis() {
        return penulis;
    }

    public void setPenulis(String penulis) {
        this.penulis = penulis;
    }
}
