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
public class Pengembalian {
    private int id_pengembalian;
    private int id_peminjaman;
    private int id_user;
    private int id_barang;
    private String lokasi;
    private int jumlah;
    private Date tanggal_kembali;
    private String status;
    
    public Pengembalian(int id_pengembalian, int id_peminjaman, int id_user, String lokasi, int id_barang, Date tanggal_kembali, String status){
        this.id_pengembalian = id_pengembalian;
        this.id_peminjaman = id_peminjaman;
        this.id_user = id_user;
        this.id_barang = id_barang;
        this.lokasi = lokasi;
        this.jumlah = jumlah;
        this.tanggal_kembali = tanggal_kembali;
        this.status = status;
    }
    
    public Pengembalian(int idPeminjaman, int idBarang, int jumlah1, Date date) {
        
    }

    public Pengembalian() {
    
    }
   
    public int getIdPengembalian(){
        return id_pengembalian;
    }
    public void setIdPengembalian(int id_pengembalian){
        this.id_pengembalian = id_pengembalian;
    }
    public int getIdPeminjaman(){
        return id_peminjaman;
    }
    public void setIdPeminjaman(int id_peminjaman){
        this.id_peminjaman = id_peminjaman;
    }
    public int getIdUser(){
        return id_user;
    }
    public void setIdUser(int id_user){
        this.id_user = id_user;
    }
    public int getIdBarang(){
        return id_barang;
    }
    public void setIdBarang(int id_barang){
        this.id_barang = id_barang;
    }
    public String getLokasi(){
        return lokasi;
    }
    public void setLokasi(String lokasi){
        this.lokasi = lokasi;
    }
    public int getJumlah(){
        return jumlah;
    }
    public void setJumlah(int jumlah){
        this.jumlah = jumlah;
    }
    public Date getTanggalKembali(){
        return tanggal_kembali;
    }
    public void setTanggalKembali(Date tanggal_kembali){
        this.tanggal_kembali = tanggal_kembali;
    }
    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
}