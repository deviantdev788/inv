/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventaris.model;

import java.util.Date;

/**
 *
 * @author pnady
 */
public class Riwayat {
    private String type;
    private String namaBarang;
    private String kodeBarang;
    private int jumlah;
    private Date tanggal_pengajuan;
    private Date tanggal_pengembalian;
    private String status;
    
    public Riwayat(String type,String namaBarang, String kodeBarang, 
            int jumlah, Date tanggal_pengajuan, Date tanggal_pengembalian, String status){
        this.type = type;
        this.namaBarang = namaBarang;
        this.kodeBarang = kodeBarang;
        this.jumlah = jumlah;
        this.tanggal_pengajuan = tanggal_pengajuan;
        this.tanggal_pengembalian = tanggal_pengembalian;
        this.status = status;
    }
    
    public String getType(){
        return type;
    }
    public void setType(String type){
        this.type = type;
    }
    public String getNamaBarang(){
        return namaBarang;
    }
    public void setNamaBarang(String namaBarang){
        this.namaBarang = namaBarang;
    }
    public String getKodeBarang(){
        return kodeBarang;
    }
    public void setKodeBarang(String kodeBarang){
        this.kodeBarang = kodeBarang;
    }
    public int getJumlah(){
        return jumlah;
    }
    public void setJumlah(int jumlah){
        this.jumlah = jumlah;
    }
    public Date getTanggalPengajuan(){
        return tanggal_pengajuan;
    }
    public void setTanggalPengajuan (Date tanggal_pengajuan){
        this.tanggal_pengajuan = tanggal_pengajuan;
    }
    public Date getTanggalPengembalian(){
        return tanggal_pengembalian;
    }
    public void setTanggalPengembalian(Date tanggal_pengembalian){
        this.tanggal_pengembalian = tanggal_pengembalian;
    }
    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
}
