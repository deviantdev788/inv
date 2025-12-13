/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package com.mycompany.inventaris;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class Koneksi {
    static Connection connDb;
    
    public static Connection getKoneksi(){
        if(connDb==null){
            String url = "jdbc:mysql://3.0.41.12:3306/db_inventaris";
            String username = "inv";
            String password = "kosong";
            
            try{
                Class.forName("com.mysql.cj.jdbc.Driver");
                connDb = DriverManager.getConnection(url, username, password);
                System.out.println("Connection Succesful");
            }catch (ClassNotFoundException | SQLException e){
                System.out.println("Connection Failed: " + e.getMessage());
            }
        }
        return connDb;
    }
    public static void main(String[] args) {
        getKoneksi();
    }
    
}
