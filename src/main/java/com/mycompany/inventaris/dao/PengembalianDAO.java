/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventaris.dao;

import com.mycompany.inventaris.Koneksi;
import com.mycompany.inventaris.model.Pengembalian;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
public class PengembalianDAO {
    private Connection conn;
    public PengembalianDAO(){
        conn = Koneksi.getKoneksi();
    }
    
    public boolean insert(Pengembalian p){
        String insert = "insert into pengembalian(id_peminjaman, id_user, id_barang, lokasi, jumlah, "
                + "tanggal_kembali, status) values (?,?,?,?,?,?,?)";
        
        String update = "update peminjaman set status = 'dikembalikan', tanggal_kembali=?"
                + "where id_peminjaman=?";
        
        String stock = "update barang set stok = stok + ? where id_barang = ?";
        
        try{
            conn.setAutoCommit(false);
            PreparedStatement ps1 = conn.prepareStatement(insert);
            ps1.setInt(1, p.getIdPeminjaman());
            ps1.setInt(2, p.getIdUser());
            ps1.setInt(3, p.getIdBarang());
            ps1.setString(4, p.getLokasi());
            ps1.setInt(5, p.getJumlah());
            ps1.setDate(6, new java.sql.Date(p.getTanggalKembali().getTime()));
            ps1.setString(7, p.getStatus());
            ps1.executeUpdate();
            
            //update
            PreparedStatement ps2 = conn.prepareStatement(update);
            ps2.setDate(1, new java.sql.Date(p.getTanggalKembali().getTime()));
            ps2.setInt(2, p.getIdPeminjaman());
            ps2.executeUpdate();
            
            //update stock
            PreparedStatement ps3 = conn.prepareStatement(stock);
            ps3.setInt(1, p.getJumlah());
            ps3.setInt(2, p.getIdBarang());
            ps3.executeUpdate();

            conn.commit();
            return true;
        }catch (Exception e){
            System.out.println("Insert Pengembalian Error: " + e.getMessage());
            return false;
        }
    }
}