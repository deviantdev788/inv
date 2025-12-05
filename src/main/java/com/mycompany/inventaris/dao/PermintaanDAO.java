/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventaris.dao;

import com.mycompany.inventaris.Koneksi;
import com.mycompany.inventaris.model.Permintaan;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
public class PermintaanDAO {
    private Connection conn;
    
    public PermintaanDAO(){
        conn = Koneksi.getKoneksi();
    }
    
    //insert
    public boolean insert(Permintaan p){
        String sql = "insert into permintaan (id_user, id_barang, jumlah, tanggal, status)"
                + "values (?,?,?,?,?)";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, p.getIdUser());
            ps.setInt(2, p.getIdBarang());
            ps.setInt(3, p.getJumlah());
            ps.setDate(4, new java.sql.Date(p.getTanggal().getTime()));
            ps.setString(5, p.getStatus());
            
            return ps.executeUpdate() > 0;
        }catch(Exception e){
            System.out.println("Insert Permintaan Error: " + e.getMessage());
            return false;
        }
    }
    
    //get permintan
    public List<Permintaan> getByUser(int id_user) {
        List<Permintaan> list = new ArrayList<>();
        String sql = "select*from permintaan where id_user=? order by tanggal desc";
        
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id_user);
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()){
                list.add(new Permintaan(
                       rs.getInt("id_permintaan"),
                       rs.getInt("id_user"),
                       rs.getInt("id_barang"),
                       rs.getInt("jumlah"),
                       rs.getDate("tanggal"),
                       rs.getString("status")
                ));
            }
        }catch(Exception e){
           System.out.println("Get Permintaan Error: " + e.getMessage());
        }
        return list;
    }
}
