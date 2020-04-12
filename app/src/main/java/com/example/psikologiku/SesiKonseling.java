package com.example.psikologiku;

import com.example.psikologiku.Konsultasi.PaketKonsultasi;

import java.io.Serializable;
import java.util.List;

public class SesiKonseling implements Serializable {
    String nama_user,nama_psikolog,sesi_sekarang,id_user,id_psikolog,id_users,status_konseling;
    private List<PaketKonsultasi> paketKonsultasi;
    private List<JadwalKonseling> jadwalKonseling;

    public String getId_users() {
        return id_users;
    }

    public void setId_users(String id_users) {
        this.id_users = id_users;
    }

    public String getStatus_konseling() {
        return status_konseling;
    }

    public void setStatus_konseling(String status_konseling) {
        this.status_konseling = status_konseling;
    }

    public String getSesi_sekarang() {
        return sesi_sekarang;
    }

    public void setSesi_sekarang(String sesi_sekarang) {
        this.sesi_sekarang = sesi_sekarang;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }

    public String getId_psikolog() {
        return id_psikolog;
    }

    public void setId_psikolog(String id_psikolog) {
        this.id_psikolog = id_psikolog;
    }

    public String getNama_user() {
        return nama_user;
    }

    public void setNama_user(String nama_user) {
        this.nama_user = nama_user;
    }

    public String getNama_psikolog() {
        return nama_psikolog;
    }

    public void setNama_psikolog(String nama_psikolog) {
        this.nama_psikolog = nama_psikolog;
    }

    public List<PaketKonsultasi> getPaketKonsultasi() {
        return paketKonsultasi;
    }

    public void setPaketKonsultasi(List<PaketKonsultasi> paketKonsultasi) {
        this.paketKonsultasi = paketKonsultasi;
    }

    public List<JadwalKonseling> getJadwalKonseling() {
        return jadwalKonseling;
    }

    public void setJadwalKonseling(List<JadwalKonseling> jadwalKonseling) {
        this.jadwalKonseling = jadwalKonseling;
    }
}
