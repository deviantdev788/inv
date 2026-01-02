/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventaris.view;

/**
 *
 * @author Amy
 */

import com.mycompany.inventaris.model.User;
import com.mycompany.inventaris.dao.AuditTrailDAO;
import java.io.File;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;

public class VerifikasiPage extends BorderPane {
    
    private TableView<PermintaanData> table;
    private List<PermintaanData> allData;
    private User admin;
    
    public VerifikasiPage(User admin) {    
        this.admin = admin;
        allData = new ArrayList<>();
        
        // Dummy data permintaan dari user
        allData.add(new PermintaanData("Medi Pribadi", "26/11/2025", "Spidol (RL001)", "1 pcs", "Lab SI & TI"));
        allData.add(new PermintaanData("Medi Pribadi", "26/11/2025", "Penghapus Papan Tulis (RL002)", "1 pcs", "Lab SI & TI"));
        allData.add(new PermintaanData("Ahmad Fauzi", "27/11/2025", "Laptop (NC001)", "1 pcs", "Lab Umum"));
        allData.add(new PermintaanData("Siti Nurhaliza", "27/11/2025", "Proyektor (NC002)", "1 pcs", "Ruang 105"));
        allData.add(new PermintaanData("Budi Santoso", "28/11/2025", "Webcam (NC003)", "2 pcs", "Lab SI"));
        
        initializeUI();
    }

    private void initializeUI() {
        VBox sidebar = createSidebar();

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30, 40, 30, 40));
        mainContent.setStyle("-fx-background-color: #f8fafc;");

        Label title = new Label("Verifikasi");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("🔍  Pencarian");
        searchField.setPrefWidth(300);
        searchField.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #e5e7eb; " +
            "-fx-border-radius: 8; " +
            "-fx-background-radius: 8; " +
            "-fx-padding: 8 15;"
        );

        ComboBox<String> kategoriBox = new ComboBox<>();
        kategoriBox.getItems().addAll("Semua Kategori", "Pending", "Approved", "Rejected");
        kategoriBox.setValue("Semua Kategori");
        kategoriBox.setStyle("-fx-font-size: 13px; -fx-padding: 6;");

        topBar.getChildren().addAll(searchField, kategoriBox);

        // Table
        table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        // Apply RED header
        this.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                javafx.application.Platform.runLater(() -> {
                    javafx.scene.Node headerBg = table.lookup(".column-header-background");
                    if (headerBg != null) {
                        headerBg.setStyle("-fx-background-color: #B71C1C;");
                    }
                    table.lookupAll(".column-header").forEach(node -> {
                        node.setStyle("-fx-background-color: #B71C1C;");
                    });
                    table.lookupAll(".column-header > .label").forEach(node -> {
                        node.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
                    });
                    javafx.scene.Node filler = table.lookup(".filler");
                    if (filler != null) {
                        filler.setStyle("-fx-background-color: #B71C1C;");
                    }
                });
            }
        });

        TableColumn<PermintaanData, String> noCol = new TableColumn<>("No.");
        noCol.setMinWidth(50);
        noCol.setMaxWidth(50);
        noCol.setStyle("-fx-alignment: CENTER;");
        noCol.setCellValueFactory(data -> 
            new SimpleStringProperty(String.valueOf(table.getItems().indexOf(data.getValue()) + 1)));

        TableColumn<PermintaanData, String> namaCol = new TableColumn<>("Nama Pengguna");
        namaCol.setMinWidth(150);
        namaCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaPengguna()));

        TableColumn<PermintaanData, String> tanggalCol = new TableColumn<>("Tanggal Peminjaman");
        tanggalCol.setMinWidth(150);
        tanggalCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTanggalPeminjaman()));

        TableColumn<PermintaanData, String> barangCol = new TableColumn<>("Nama & Kode Barang");
        barangCol.setMinWidth(200);
        barangCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaKodeBarang()));

        TableColumn<PermintaanData, String> jumlahCol = new TableColumn<>("Jumlah Barang");
        jumlahCol.setMinWidth(120);
        jumlahCol.setMaxWidth(120);
        jumlahCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJumlahBarang()));

        TableColumn<PermintaanData, String> ruangCol = new TableColumn<>("Ruang");
        ruangCol.setMinWidth(120);
        ruangCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRuang()));

        // Aksi column dengan approve & reject button
        TableColumn<PermintaanData, Void> aksiCol = new TableColumn<>("Aksi");
        aksiCol.setMinWidth(150);
        aksiCol.setMaxWidth(150);
        aksiCol.setCellFactory(col -> new TableCell<>() {
            private HBox actionBox = new HBox(8);
            private Button approveBtn = new Button("✓");
            private Button rejectBtn = new Button("✕");
            private Button menuBtn = new Button("⋮");

            {
                approveBtn.setStyle(
                    "-fx-background-color: #22c55e; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 16px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-padding: 5 12; " +
                    "-fx-background-radius: 50; " +
                    "-fx-cursor: hand;"
                );
                
                rejectBtn.setStyle(
                    "-fx-background-color: #dc2626; " +
                    "-fx-text-fill: white; " +
                    "-fx-font-size: 16px; " +
                    "-fx-font-weight: bold; " +
                    "-fx-padding: 5 12; " +
                    "-fx-background-radius: 50; " +
                    "-fx-cursor: hand;"
                );
                
                menuBtn.setStyle(
                    "-fx-background-color: transparent; " +
                    "-fx-text-fill: #64748b; " +
                    "-fx-font-size: 18px; " +
                    "-fx-cursor: hand;"
                );

                approveBtn.setOnAction(e -> {
                    PermintaanData data = getTableView().getItems().get(getIndex());
                    handleApprove(data);
                });

                rejectBtn.setOnAction(e -> {
                    PermintaanData data = getTableView().getItems().get(getIndex());
                    handleReject(data);
                });

                menuBtn.setOnAction(e -> {
                    PermintaanData data = getTableView().getItems().get(getIndex());
                    showDetailPopup(data);
                });

                actionBox.getChildren().addAll(approveBtn, rejectBtn, menuBtn);
                actionBox.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty ? null : actionBox);
            }
        });

        table.getColumns().addAll(noCol, namaCol, tanggalCol, barangCol, jumlahCol, ruangCol, aksiCol);
        allData.forEach(data -> table.getItems().add(data));

        // Search functionality
        searchField.textProperty().addListener((obs, old, newVal) -> {
            table.getItems().clear();
            if (newVal.isEmpty()) {
                allData.forEach(data -> table.getItems().add(data));
            } else {
                String keyword = newVal.toLowerCase();
                allData.stream()
                    .filter(data -> 
                        data.getNamaPengguna().toLowerCase().contains(keyword) ||
                        data.getNamaKodeBarang().toLowerCase().contains(keyword))
                    .forEach(data -> table.getItems().add(data));
            }
        });

        mainContent.getChildren().addAll(title, topBar, table);

        this.setLeft(sidebar);
        this.setCenter(mainContent);
    }

    private void handleApprove(PermintaanData data) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Approve");
        alert.setHeaderText(null);
        alert.setContentText("Apakah Anda yakin ingin menyetujui permintaan dari " + data.getNamaPengguna() + "?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Berhasil");
                success.setHeaderText(null);
                success.setContentText("Permintaan telah disetujui!");
                success.showAndWait();
                
                // TODO: Update database status jadi Approved
                // allData.remove(data);
                // table.getItems().remove(data);
            }
        });
    }

    private void handleReject(PermintaanData data) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Konfirmasi Reject");
        alert.setHeaderText(null);
        alert.setContentText("Apakah Anda yakin ingin menolak permintaan dari " + data.getNamaPengguna() + "?");
        
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Alert success = new Alert(Alert.AlertType.INFORMATION);
                success.setTitle("Berhasil");
                success.setHeaderText(null);
                success.setContentText("Permintaan telah ditolak!");
                success.showAndWait();
                
                // TODO: Update database status jadi Rejected
                // allData.remove(data);
                // table.getItems().remove(data);
            }
        });
    }

    private void showDetailPopup(PermintaanData data) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Detail Permintaan");
        alert.setHeaderText("Informasi Lengkap");
        alert.setContentText(
            "Nama: " + data.getNamaPengguna() + "\n" +
            "Tanggal: " + data.getTanggalPeminjaman() + "\n" +
            "Barang: " + data.getNamaKodeBarang() + "\n" +
            "Jumlah: " + data.getJumlahBarang() + "\n" +
            "Ruang: " + data.getRuang()
        );
        alert.showAndWait();
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(15);
        sidebar.setPadding(new Insets(20, 10, 20, 10));
        sidebar.setAlignment(Pos.TOP_LEFT);
        sidebar.setPrefWidth(200);
        sidebar.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-width: 0 1 0 0; " +
            "-fx-border-color: #e5e7eb;"
        );

        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/assets/logoAsa.png")));
        logo.setFitHeight(70);
        logo.setPreserveRatio(true);
        VBox logoBox = new VBox(logo);
        logoBox.setAlignment(Pos.TOP_LEFT);

         Image userPhoto;

    if (admin.getPhoto() != null && admin.getPhoto().length > 0) {
        userPhoto = new Image(
        new java.io.ByteArrayInputStream(admin.getPhoto())
        );
    } else {
        userPhoto = new Image(
        getClass().getResourceAsStream("/assets/user.png")
    );
    }
        ImageView userImage = new ImageView(userPhoto);
        userImage.setFitWidth(40);
        userImage.setFitHeight(40);
        userImage.setPreserveRatio(true);
        Circle clipCircle = new Circle(20, 20, 20);
        userImage.setClip(clipCircle);

        Label nameLabel = new Label(admin.getNama());
        nameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        
        Label roleLabel = new Label(admin.getRole().toUpperCase());
        roleLabel.setStyle(
                "-fx-font-size: 10px;" +
                "-fx-text-fill: #9ca3af;" +
                "-fx-font-weight: normal;"
        );

        VBox textBox = new VBox(2, nameLabel, roleLabel);
        textBox.setAlignment(Pos.CENTER_LEFT);
        HBox userBox = new HBox(10, userImage, textBox);
        userBox.setAlignment(Pos.CENTER_LEFT);
        userBox.setPadding(new Insets(10, 10, 20, 10));

        VBox menuBox = new VBox(8);
        Button dashboardBtn = createMenuButton("🏠  Dashboard", false);
        Button verifikasiBtn = createMenuButton("✓  Verifikasi", true);
        Button manageDataBtn = createMenuButton("⚙  Manage Data", false);
        Button laporanBtn = createMenuButton("📊  Laporan ▼", false);
        
        VBox laporanSubMenu = new VBox(5);
        laporanSubMenu.setPadding(new Insets(0, 0, 0, 20));
        laporanSubMenu.setVisible(false);
        laporanSubMenu.setManaged(false);

        Button laporanPinjamBtn =
                createMenuButton("Laporan Peminjaman", false);

        Button laporanGunaBtn =
                createMenuButton("Laporan Penggunaan", false);
        
        dashboardBtn.setOnAction(e -> {
            Stage currentStage = (Stage) dashboardBtn.getScene().getWindow();
            Scene newScene = new Scene(new AdminPage(admin), 1280, 720);
            currentStage.setScene(newScene);
        });
        
        manageDataBtn.setOnAction(e -> {
            Stage currentStage = (Stage) manageDataBtn.getScene().getWindow();
            Scene newScene = new Scene(new ManageDataPage(admin), 1280, 720);
            currentStage.setScene(newScene);
        });
        
        laporanPinjamBtn.setOnAction(e -> {
            Stage s = (Stage) laporanBtn.getScene().getWindow();
            s.setScene(new Scene(new LaporanPeminjamanPage(admin), 1280, 720));
        });

        laporanGunaBtn.setOnAction(e -> {
            Stage s = (Stage) laporanGunaBtn.getScene().getWindow();
            s.setScene(new Scene(new LaporanPenggunaanPage(admin), 1280, 720));
        });
        
        laporanBtn.setOnAction(e -> {
            boolean open = laporanSubMenu.isVisible();
            laporanSubMenu.setVisible(!open);
            laporanSubMenu.setManaged(!open);
            laporanBtn.setText(open ? "📊  Laporan ▼" : "📊  Laporan ▲");
        });

        laporanSubMenu.getChildren().addAll(
                laporanPinjamBtn,
                laporanGunaBtn
        );
        
        menuBox.getChildren().addAll(dashboardBtn, verifikasiBtn, manageDataBtn, laporanBtn, laporanSubMenu);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button logoutBtn = new Button("↩  Logout");
        logoutBtn.setAlignment(Pos.CENTER_LEFT);
        logoutBtn.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-font-size: 13px; " +
            "-fx-text-fill: #475569; " +
            "-fx-padding: 12 10; " +
            "-fx-font-weight: bold; " +
            "-fx-cursor: hand;"
        );
      logoutBtn.setOnAction(e -> {
    String ip = "UNKNOWN";
    try {
        ip = java.net.InetAddress.getLocalHost().getHostAddress();
    } catch (Exception ex) {
        ex.printStackTrace();
    }

    AuditTrailDAO.log(
        admin.getIdUser(),          
        admin.getUsername(),         
        "LOGOUT",
        "Pengguna keluar dari sistem",
        ip,
        "BERHASIL"
    );

    Stage currentStage = (Stage) logoutBtn.getScene().getWindow();
    Scene newScene = new Scene(new MainPage(currentStage), 1280, 720);
    currentStage.setScene(newScene);
});

        sidebar.getChildren().addAll(logoBox, userBox, menuBox, spacer, logoutBtn);
        return sidebar;
    }

    // Inner class for PermintaanData
    public static class PermintaanData {
        private String namaPengguna;
        private String tanggalPeminjaman;
        private String namaKodeBarang;
        private String jumlahBarang;
        private String ruang;
        
        public PermintaanData(String namaPengguna, String tanggalPeminjaman, String namaKodeBarang, 
                             String jumlahBarang, String ruang) {
            this.namaPengguna = namaPengguna;
            this.tanggalPeminjaman = tanggalPeminjaman;
            this.namaKodeBarang = namaKodeBarang;
            this.jumlahBarang = jumlahBarang;
            this.ruang = ruang;
        }
        
        public String getNamaPengguna() { return namaPengguna; }
        public String getTanggalPeminjaman() { return tanggalPeminjaman; }
        public String getNamaKodeBarang() { return namaKodeBarang; }
        public String getJumlahBarang() { return jumlahBarang; }
        public String getRuang() { return ruang; }
    }
    
    private Button createMenuButton(String text, boolean isActive) {
        Button btn = new Button(text);
        
        btn.setWrapText(true);
        btn.setTextAlignment(javafx.scene.text.TextAlignment.LEFT);
        btn.setAlignment(Pos.CENTER_LEFT);
        btn.setMaxWidth(Double.MAX_VALUE);
        btn.setPrefHeight(Region.USE_COMPUTED_SIZE);
    
        if (isActive) {
            btn.setStyle(
                    "-fx-background-color: rgba(164,35,35,0.10);" +
                    "-fx-font-weight: bold;" +
                    "-fx-text-fill: #111827;" +
                    "-fx-padding: 10 15;" +
                    "-fx-background-radius: 6;" +
                    "-fx-font-size: 13px;" +
                    "-fx-alignment: center-left;" +
                    "-fx-cursor: hand;"
            );
        } else {
            btn.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-font-size: 13px;" +
                    "-fx-text-fill: #475569;" +
                    "-fx-padding: 10 15;" +
                    "-fx-font-weight: bold;" +
                    "-fx-alignment: center-left;" +
                    "-fx-background-radius: 6;" +
                    "-fx-cursor: hand;"
            );
        }

        return btn;
    }
}