/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventaris.dao;

import com.mycompany.inventaris.model.User;
import com.mycompany.inventaris.Koneksi;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class UserAdminDAO {
    private Connection conn;
    
    public UserAdminDAO(){
        conn = Koneksi.getKoneksi();
    }
    
    //insert user
    public boolean insert(User user){
        String sql = "insert into user (name, username, password, email, phone, birthPlace,"
                + "birthDate, identity_number, role, status)"
                + "values (?,?,?,?,?,?,?,?,?,?)";
        try{
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getNama());
            ps.setString(2, user.getUsername());
            ps.setString(3, user.getPassword());
            ps.setString(4, user.getEmail());
            ps.setString(5, user.getPhone());
            ps.setString(6, user.getPlace());
            ps.setDate(7, new java.sql.Date(user.getBirth().getTime()));
            ps.setString(8, user.getIdentity());
            ps.setString(9, user.getRole());
            ps.setString(10, user.getStatus());
            
            return ps.executeUpdate() > 0;
        }catch(Exception e){
            System.out.println("Insert User Error: " + e.getMessage());
            return false;
        }
    }
    //get all user
    public List<User> getAll(){
        List<User> list = new ArrayList<>();
        String sql = "Select*from user order by created_at desc";
        try{
            Connection conn = Koneksi.getKoneksi();
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            
           while (rs.next()) {
                User u = new User();
                u.setIdUser(rs.getInt("id_user"));
                u.setNama(rs.getString("name"));
                u.setUsername(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                u.setPhone(rs.getString("phone"));
                u.setIdentity(rs.getString("identity_number"));
                u.setRole(rs.getString("role"));
                u.setStatus(rs.getString("status"));
                list.add(u);
            }

        }catch (Exception e) {
            System.out.println("Get All User Error: " + e.getMessage());
        }
        return list;
    }
    //delete user
    public boolean delete(int id_user){
        String sql = "delete from user where id_user=?";
        try{
            Connection conn = Koneksi.getKoneksi();
            PreparedStatement ps = conn.prepareStatement(sql);
            
            ps.setInt(1, id_user);
            ps.executeUpdate();
            return true;
        }catch(Exception e){
            return false;
        }
    }
}
