/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventaris.model;

import java.sql.Timestamp;
import java.util.Date;


/**
 *
 * @author pnady
 */
public class User {
    private int id_user;
    private String name;
    private String username;
    private String password;
    private String email;
    private String phone;
    private String birthPlace;
    private Date birthDate;
    private String identity_number;
    private String role;
    private String status;
    private String photo;
    private Timestamp createdAt;
    private Timestamp updatedAt;

    public User() {}

    public User(int id_user, String name, String username, String password, String email, 
                String phone, String birthPlace, Date birthDate, String identity_number, String role, String status, String photo){
        this.id_user = id_user;
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.birthPlace = birthPlace;
        this.birthDate = birthDate;
        this.identity_number = identity_number;
        this.role = role;
        this.status = status;
        this.photo = photo;
        
    }

    public User(String name, String username, String password,
                String email, String phone, String birthPlace, Date birthDate, String identity_number,
                String role) {
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.birthPlace = birthPlace;
        this.birthDate = birthDate;
        this.identity_number = identity_number;
        this.role = role;
        this.status = "aktif";
    }

    
    
    public int getIdUser(){
        return id_user;
    }
    
    public void setIdUser(int id_user){
        this.id_user = id_user;
    }
    
    public String getNama(){
        return name;
    }
    
    public void setNama(String name){
        this.name = name;
    }
    
    public String getUsername(){
        return username;
    }
    
    public void setUsername(String username){
        this.username = username;
    }
    
    public String getPassword(){
        return password;
    }
    
    public void setPassword(String password){
        this.password = password;
    }
    
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email = email;
    }
    
    public String getPhone(){
        return phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
    
    public String getPlace(){
        return birthPlace;
    }
    public void setPlace(String birthPlace){
        this.birthPlace = birthPlace;
    }
    
    public Date getBirth(){
        return birthDate;
    }
    public void setBirth(Date birthDate){
        this.birthDate = birthDate;
    }
    
    public String getIdentity(){
        return identity_number;
    }
    public void setIdentity(String identity_number){
        this.identity_number = identity_number;
    }
    
    public String getRole(){
        return role;
    }
    
    public void setRole(String role){
        this.role = role;
    }
    
    public String getStatus(){
        return status;
    }
    
    public void setStatus(String status){
        this.status = status;
    }
    
    public String getPhoto(){
        return photo;
    }
    public void setPhoto(String photo){
        this.photo = photo;
    }
    
    public Timestamp getCreatedAt() { 
        return createdAt; 
    }
    public void setCreatedAt(Timestamp createdAt) { 
        this.createdAt = createdAt; 
    }

    public Timestamp getUpdatedAt() { 
        return updatedAt; 
    }
    public void setUpdatedAt(Timestamp updatedAt) { 
        this.updatedAt = updatedAt; 
    }
    
}
