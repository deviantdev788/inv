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
public class Permintaan {
    private int id_permintaan;
    private int id_user;
    private int id_barang;
    private int jumlah;
    private Date tanggal;
    private String status;
    
    public Permintaan(int id_permintaan, int id_user, int id_barang, int jumlah, 
            Date tanggal, String status){
        this.id_permintaan = id_permintaan;
        this.id_user = id_user;
        this.id_barang = id_barang;
        this.jumlah = jumlah;
        this.tanggal = tanggal;
        this.status = status;
    }

    public Permintaan() {
        
    }

    public Permintaan(int idUser, int idBarang, int quantity, Date date, String diproses) {
        
    }
    
    public int getIdPermintaan(){
        return id_permintaan;
    }
    public void setIdPermintaan(int id_permintaan){
        this.id_permintaan = id_permintaan;
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
    public int getJumlah(){
        return jumlah;
    }
    public void setJumlah(int jumlah){
        this.jumlah = jumlah;
    }
    public Date getTanggal(){
        return tanggal;
    }
    public void setTanggal(Date tanggal){
        this.tanggal = tanggal;
    }
    public String getStatus(){
        return status;
    }
    public void setStatus(String status){
        this.status = status;
    }
}
