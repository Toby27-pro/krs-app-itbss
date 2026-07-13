package com.mycompany.krs_sistem.admin;

import com.mycompany.krs_sistem.config.*;
import com.mycompany.krs_sistem.ui.*;


public class Matakuliah {
    private int mk_id;
    private String kode_mk;
    private String nama_mk;
    private int sks;
    private int semester;
    private int prodi_id;

    public Matakuliah(int mk_id, String kode_mk, String nama_mk, int sks, int semester, int prodi_id) {
        this.mk_id = mk_id;
        this.kode_mk = kode_mk;
        this.nama_mk = nama_mk;
        this.sks = sks;
        this.semester = semester;
        this.prodi_id = prodi_id;
    }

    public int getMkId() { return mk_id; }
    public void setMkId(int mk_id) { this.mk_id = mk_id; }

    public String getKodeMk() { return kode_mk; }
    public void setKodeMk(String kode_mk) { this.kode_mk = kode_mk; }

    public String getNamaMk() { return nama_mk; }
    public void setNamaMk(String nama_mk) { this.nama_mk = nama_mk; }

    public int getSks() { return sks; }
    public void setSks(int sks) { this.sks = sks; }

    public int getSemester() { return semester; }
    public void setSemester(int semester) { this.semester = semester; }

    public int getProdiId() { return prodi_id; }
    public void setProdiId(int prodi_id) { this.prodi_id = prodi_id; }
}