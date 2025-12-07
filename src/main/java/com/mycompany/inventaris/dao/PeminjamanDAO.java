package com.mycompany.inventaris.dao;

import com.mycompany.inventaris.Koneksi;
import com.mycompany.inventaris.model.Peminjaman;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class PeminjamanDAO {

    private Connection conn;

    public PeminjamanDAO() {
        conn = Koneksi.getKoneksi();
    }

    // INSERT PEMINJAMAN
    public boolean insert(Peminjaman pn) {
    String sql = "INSERT INTO peminjaman (id_user, id_barang, jumlah, tanggal_peminjaman, tanggal_kembali, status) "
               + "VALUES (?, ?, ?, ?, ?, ?)";
    String updateStok = "UPDATE barang SET stok = stok - ? WHERE id_barang = ?";

    try {
        if (conn.getAutoCommit()) conn.setAutoCommit(false);

        // insert peminjaman
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setInt(1, pn.getIdUser());
        ps.setInt(2, pn.getIdBarang());
        ps.setInt(3, pn.getJumlah());
        ps.setDate(4, new java.sql.Date(pn.getTanggalPeminjaman().getTime()));
        if (pn.getTanggalKembali() != null) ps.setDate(5, new java.sql.Date(pn.getTanggalKembali().getTime()));
        else ps.setNull(5, Types.DATE);
        ps.setString(6, pn.getStatus());

        ps.executeUpdate();  // <-- HARUS ADA INI

        // update stok
        PreparedStatement psStock = conn.prepareStatement(updateStok);
        psStock.setInt(1, pn.getJumlah());
        psStock.setInt(2, pn.getIdBarang());
        psStock.executeUpdate();

        conn.commit();
        conn.setAutoCommit(true);
        return true;

    } catch (Exception e) {
        try { conn.rollback(); } catch(Exception ex){ ex.printStackTrace(); }
        System.out.println("Insert Peminjaman Error: " + e.getMessage());
        return false;
    }
}


    // GET PEMINJAMAN BY USER
    public List<Peminjaman> getByUser(int id_user) {
        List<Peminjaman> list = new ArrayList<>();
        String sql = """
        SELECT p.id_peminjaman, p.id_user, p.id_barang,
               b.nama_barang, b.kode_barang,
               p.jumlah,
               p.tanggal_peminjaman,
               p.tanggal_kembali,
               p.status
        FROM peminjaman p
        JOIN barang b ON p.id_barang = b.id_barang
        WHERE p.id_user = ?
        AND p.status = 'Dipinjam'
    """;
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id_user);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                list.add(new Peminjaman(
                    rs.getInt("id_peminjaman"),
                    rs.getInt("id_user"),
                    rs.getInt("id_barang"),
                    rs.getString("nama_barang"),
                    rs.getString("kode_barang"),
                    rs.getInt("jumlah"),
                    rs.getDate("tanggal_peminjaman"),
                    rs.getDate("tanggal_kembali"),
                    rs.getString("status")
                ));
            }
        } catch (Exception e) {
            System.out.println("Get Peminjaman Error: " + e.getMessage());
        }
        return list;
    }
}
