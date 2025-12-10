/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventaris.dao;
import com.mycompany.inventaris.Koneksi;
import com.mycompany.inventaris.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author pnady
 */
public class LoginDAO {
    public static User login(String username, String password){
        String sql = "select*from user where username=? and password=?";
        try{
            Connection conn = Koneksi.getKoneksi();
            PreparedStatement ps = conn.prepareCall(sql);
            
            ps.setString(1, username);
            ps.setString(2, password);
            
            ResultSet rs = ps.executeQuery();
            
            if(rs.next()){
                return new User(
                    rs.getInt("id_user"),
                    rs.getString("name"),
                    rs.getString("username"),
                    rs.getString("password"),
                    rs.getString("email"),
                    rs.getString("phone"),
                    rs.getString("birthPlace"),
                    rs.getDate("birthDate"),
                    rs.getString("identity_number"),
                    rs.getString("role"),
                    rs.getString("status"),
                    rs.getString("photo")
                );
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
