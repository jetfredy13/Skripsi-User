package com.example.psikologiku;

import com.example.psikologiku.Konsultasi.PaketKonsultasi;

import java.util.List;

public class SesiKonseling {
    String nama_user,nama_psikolog,id_sesi;
    private List<PaketKonsultasi> paketKonsultasi;
    private List<JadwalKonseling> jadwalKonseling;

    public String getId_sesi() {
        return id_sesi;
    }

    public void setId_sesi(String id_sesi) {
        this.id_sesi = id_sesi;
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
