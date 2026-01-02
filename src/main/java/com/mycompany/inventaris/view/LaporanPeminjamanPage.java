package com.mycompany.inventaris.view;

import com.mycompany.inventaris.model.User;
import com.mycompany.inventaris.dao.AuditTrailDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class LaporanPeminjamanPage extends BorderPane {
    
    private TableView<PeminjamanData> table;
    private List<PeminjamanData> allData;
    private User user;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;
    private ComboBox<String> statusFilter;
    
    public LaporanPeminjamanPage(User user) {
        this.user = user;
        allData = new ArrayList<>();
        
        // Dummy data laporan peminjaman
        allData.add(new PeminjamanData("PMJ001", "Ahmad Fauzi", "Mahasiswa", "Laptop Asus", "1", "2025-11-20", "2025-11-25", "Approved", "Dikembalikan"));
        allData.add(new PeminjamanData("PMJ002", "Siti Nurhaliza", "Dosen", "Proyektor", "1", "2025-11-21", "2025-11-23", "Approved", "Dikembalikan"));
        allData.add(new PeminjamanData("PMJ003", "Budi Santoso", "Mahasiswa", "Spidol", "5", "2025-11-22", "2025-11-24", "Rejected", "Ditolak"));
        allData.add(new PeminjamanData("PMJ004", "Rina Kartika", "Mahasiswa", "Kertas HVS", "3", "2025-11-23", "2025-11-26", "Approved", "Dipinjam"));
        allData.add(new PeminjamanData("PMJ005", "Doni Pratama", "Petugas", "Mouse", "2", "2025-11-24", "-", "Pending", "Menunggu"));
        allData.add(new PeminjamanData("PMJ006", "Linda Wijaya", "Dosen", "Webcam", "1", "2025-11-25", "2025-11-28", "Approved", "Dipinjam"));
        allData.add(new PeminjamanData("PMJ007", "Andi Saputra", "Mahasiswa", "Keyboard", "2", "2025-11-26", "-", "Rejected", "Ditolak"));
        allData.add(new PeminjamanData("PMJ008", "Maya Sari", "Mahasiswa", "Penghapus", "10", "2025-11-27", "2025-11-29", "Approved", "Dikembalikan"));
        
        initializeUI();
    }

    private void initializeUI() {
        VBox sidebar = createSidebar();

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30, 40, 30, 40));
        mainContent.setStyle("-fx-background-color: #f8fafc;");

        Label title = new Label("Laporan Peminjaman");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        // Filter Section
        HBox filterBar = new HBox(15);
        filterBar.setAlignment(Pos.CENTER_LEFT);

        // Date Range Filter
        VBox dateRangeBox = new VBox(5);
        Label dateLabel = new Label("Filter Tanggal:");
        dateLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #64748b;");
        
        HBox dateInputs = new HBox(10);
        startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Dari Tanggal");
        startDatePicker.setStyle("-fx-font-size: 12px;");
        
        Label toLabel = new Label("s/d");
        toLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");
        
        endDatePicker = new DatePicker();
        endDatePicker.setPromptText("Sampai Tanggal");
        endDatePicker.setStyle("-fx-font-size: 12px;");
        
        dateInputs.getChildren().addAll(startDatePicker, toLabel, endDatePicker);
        dateRangeBox.getChildren().addAll(dateLabel, dateInputs);

        // Status Filter
        VBox statusBox = new VBox(5);
        Label statusLabel = new Label("Filter Status:");
        statusLabel.setStyle("-fx-font-size: 12px; -fx-font-weight: bold; -fx-text-fill: #64748b;");
        
        statusFilter = new ComboBox<>();
        statusFilter.getItems().addAll("Semua Status", "Approved", "Rejected", "Pending");
        statusFilter.setValue("Semua Status");
        statusFilter.setStyle("-fx-font-size: 12px;");
        
        statusBox.getChildren().addAll(statusLabel, statusFilter);

        Button applyFilterBtn = new Button("Terapkan Filter");
        applyFilterBtn.setStyle(
            "-fx-background-color: #3C4C79; " +
            "-fx-text-fill: white; " +
            "-fx-padding: 10 20; " +
            "-fx-background-radius: 8; " +
            "-fx-font-size: 12px; " +
            "-fx-font-weight: bold; " +
            "-fx-cursor: hand;"
        );
        applyFilterBtn.setOnAction(e -> applyFilters());

        Button resetFilterBtn = new Button("Reset");
        resetFilterBtn.setStyle(
            "-fx-background-color: #3C4C79; " +
            "-fx-text-fill: white; " +
            "-fx-padding: 10 20; " +
            "-fx-background-radius: 8; " +
            "-fx-font-size: 12px; " +
            "-fx-font-weight: bold; " +
            "-fx-cursor: hand;"
        );
        resetFilterBtn.setOnAction(e -> resetFilters());

        filterBar.getChildren().addAll(dateRangeBox, statusBox, applyFilterBtn, resetFilterBtn);

        // Table
        table = new TableView<>();
        table.setStyle("-fx-background-color: white; -fx-background-radius: 10;");
        table.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

        // Apply header styling
        this.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                javafx.application.Platform.runLater(() -> {
                    javafx.scene.Node headerBg = table.lookup(".column-header-background");
                    if (headerBg != null) {
                        headerBg.setStyle("-fx-background-color: #3C4C79;");
                    }
                    table.lookupAll(".column-header").forEach(node -> {
                        node.setStyle("-fx-background-color: #3C4C79;");
                    });
                    table.lookupAll(".column-header > .label").forEach(node -> {
                        node.setStyle("-fx-text-fill: white; -fx-font-weight: bold;");
                    });
                    javafx.scene.Node filler = table.lookup(".filler");
                    if (filler != null) {
                        filler.setStyle("-fx-background-color: #3C4C79;");
                    }
                });
            }
        });

        TableColumn<PeminjamanData, String> noCol = new TableColumn<>("No.");
        noCol.setMinWidth(50);
        noCol.setMaxWidth(50);
        noCol.setStyle("-fx-alignment: CENTER;");
        noCol.setCellValueFactory(data -> 
            new SimpleStringProperty(String.valueOf(table.getItems().indexOf(data.getValue()) + 1)));

        TableColumn<PeminjamanData, String> idCol = new TableColumn<>("ID Peminjaman");
        idCol.setMinWidth(120);
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdPeminjaman()));

        TableColumn<PeminjamanData, String> namaCol = new TableColumn<>("Nama Peminjam");
        namaCol.setMinWidth(150);
        namaCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNamaPeminjam()));

        TableColumn<PeminjamanData, String> roleCol = new TableColumn<>("Role");
        roleCol.setMinWidth(100);
        roleCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRole()));

        TableColumn<PeminjamanData, String> barangCol = new TableColumn<>("Barang");
        barangCol.setMinWidth(150);
        barangCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBarang()));

        TableColumn<PeminjamanData, String> jumlahCol = new TableColumn<>("Jumlah");
        jumlahCol.setMinWidth(80);
        jumlahCol.setMaxWidth(80);
        jumlahCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getJumlah()));

        TableColumn<PeminjamanData, String> tglPinjamCol = new TableColumn<>("Tgl Pinjam");
        tglPinjamCol.setMinWidth(120);
        tglPinjamCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTglPinjam()));

        TableColumn<PeminjamanData, String> tglKembaliCol = new TableColumn<>("Tgl Kembali");
        tglKembaliCol.setMinWidth(120);
        tglKembaliCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTglKembali()));

        // Status Verifikasi column
        TableColumn<PeminjamanData, String> statusVerifCol = new TableColumn<>("Status Verifikasi");
        statusVerifCol.setMinWidth(130);
        statusVerifCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(String status, boolean empty) {
                super.updateItem(status, empty);
                if (empty || status == null) {
                    setGraphic(null);
                } else {
                    Label statusLabel = new Label(status);
                    if (status.equals("Approved")) {
                        statusLabel.setStyle(
                            "-fx-background-color: #dcfce7; " +
                            "-fx-text-fill: #166534; " +
                            "-fx-padding: 5 12; " +
                            "-fx-background-radius: 12; " +
                            "-fx-font-size: 11px; " +
                            "-fx-font-weight: bold;"
                        );
                    } else if (status.equals("Rejected")) {
                        statusLabel.setStyle(
                            "-fx-background-color: #fee2e2; " +
                            "-fx-text-fill: #991b1b; " +
                            "-fx-padding: 5 12; " +
                            "-fx-background-radius: 12; " +
                            "-fx-font-size: 11px; " +
                            "-fx-font-weight: bold;"
                        );
                    } else {
                        statusLabel.setStyle(
                            "-fx-background-color: #fef3c7; " +
                            "-fx-text-fill: #92400e; " +
                            "-fx-padding: 5 12; " +
                            "-fx-background-radius: 12; " +
                            "-fx-font-size: 11px; " +
                            "-fx-font-weight: bold;"
                        );
                    }
                    HBox box = new HBox(statusLabel);
                    box.setAlignment(Pos.CENTER);
                    setGraphic(box);
                }
            }
        });
        statusVerifCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatusVerifikasi()));

        // Status Barang column
        TableColumn<PeminjamanData, String> statusBarangCol = new TableColumn<>("Status Barang");
        statusBarangCol.setMinWidth(120);
        statusBarangCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getStatusBarang()));

        table.getColumns().addAll(noCol, idCol, namaCol, roleCol, barangCol, jumlahCol, 
                                  tglPinjamCol, tglKembaliCol, statusVerifCol, statusBarangCol);
        
        allData.forEach(data -> table.getItems().add(data));

        // Bottom action buttons
        HBox bottomBar = new HBox(15);
        bottomBar.setAlignment(Pos.CENTER_RIGHT);
        bottomBar.setPadding(new Insets(15, 0, 0, 0));

        Label totalLabel = new Label("Total Data: " + allData.size());
        totalLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #64748b;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button exportBtn = new Button("Export ke CSV");
        exportBtn.setStyle(
            "-fx-background-color: #22c55e; " +
            "-fx-text-fill: white; " +
            "-fx-padding: 10 25; " +
            "-fx-background-radius: 20; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: bold; " +
            "-fx-cursor: hand;"
        );
        exportBtn.setOnAction(e -> exportToCSV());

        bottomBar.getChildren().addAll(totalLabel, spacer, exportBtn);

        mainContent.getChildren().addAll(title, filterBar, table, bottomBar);

        this.setLeft(sidebar);
        this.setCenter(mainContent);
    }

    private void applyFilters() {
        List<PeminjamanData> filteredData = new ArrayList<>(allData);

        // Filter by date range
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();
        
        if (startDate != null && endDate != null) {
            filteredData = filteredData.stream()
                .filter(data -> {
                    try {
                        LocalDate tglPinjam = LocalDate.parse(data.getTglPinjam());
                        return !tglPinjam.isBefore(startDate) && !tglPinjam.isAfter(endDate);
                    } catch (Exception e) {
                        return true;
                    }
                })
                .collect(Collectors.toList());
        }

        // Filter by status
        String status = statusFilter.getValue();
        if (!status.equals("Semua Status")) {
            filteredData = filteredData.stream()
                .filter(data -> data.getStatusVerifikasi().equals(status))
                .collect(Collectors.toList());
        }

        table.getItems().clear();
        filteredData.forEach(data -> table.getItems().add(data));
    }

    private void resetFilters() {
        startDatePicker.setValue(null);
        endDatePicker.setValue(null);
        statusFilter.setValue("Semua Status");
        table.getItems().clear();
        allData.forEach(data -> table.getItems().add(data));
    }

    private void exportToCSV() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Export Laporan Peminjaman");
        fileChooser.setInitialFileName("laporan_peminjaman_" + LocalDate.now() + ".csv");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("CSV Files", "*.csv")
        );
        
        File file = fileChooser.showSaveDialog(this.getScene().getWindow());
        
        if (file != null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                // Write header
                writer.write("No,ID Peminjaman,Nama Peminjam,Role,Barang,Jumlah,Tgl Pinjam,Tgl Kembali,Status Verifikasi,Status Barang");
                writer.newLine();
                
                // Write data from current table view (filtered data)
                int no = 1;
                for (PeminjamanData data : table.getItems()) {
                    writer.write(String.format("%d,%s,%s,%s,%s,%s,%s,%s,%s,%s",
                        no++,
                        data.getIdPeminjaman(),
                        data.getNamaPeminjam(),
                        data.getRole(),
                        data.getBarang(),
                        data.getJumlah(),
                        data.getTglPinjam(),
                        data.getTglKembali(),
                        data.getStatusVerifikasi(),
                        data.getStatusBarang()
                    ));
                    writer.newLine();
                }
                
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Export Berhasil");
                alert.setHeaderText(null);
                alert.setContentText("Data berhasil diekspor ke:\n" + file.getAbsolutePath() +
                                   "\n\nTotal: " + table.getItems().size() + " data");
                alert.showAndWait();
                
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Export Gagal");
                alert.setHeaderText(null);
                alert.setContentText("Gagal mengekspor data: " + e.getMessage());
                alert.showAndWait();
            }
        }
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

    if (user.getPhoto() != null && user.getPhoto().length > 0) {
        userPhoto = new Image(
        new java.io.ByteArrayInputStream(user.getPhoto())
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

        Label nameLabel = new Label(user.getNama());
        nameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        
        Label roleLabel = new Label(user.getRole().toUpperCase());
        roleLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #9ca3af;");

        VBox textBox = new VBox(2, nameLabel, roleLabel);
        textBox.setAlignment(Pos.CENTER_LEFT);
        HBox userBox = new HBox(10, userImage, textBox);
        userBox.setAlignment(Pos.CENTER_LEFT);
        userBox.setPadding(new Insets(10, 10, 20, 10));

        // MENU
        VBox menuBox = new VBox(8);

        // MAIN MENU
        Button dashboardBtn = createMenuButton("🏠  Dashboard", false);
        Button verifikasiBtn = createMenuButton("✓  Verifikasi", false);
        Button userBtn = createMenuButton("👤  User", false);
        Button manageDataBtn = createMenuButton("⚙  Manage Data", false);
        Button auditTrailBtn = createMenuButton("📜  Audit Trail", false);
        Button laporanBtn = createMenuButton("📊  Laporan ▼", false);

        // SUB MENU LAPORAN
        VBox laporanSubMenu = new VBox(5);
        laporanSubMenu.setPadding(new Insets(0, 0, 0, 20));
        laporanSubMenu.setVisible(false);
        laporanSubMenu.setManaged(false);
        
        boolean isLaporanPage = true;

        if (isLaporanPage) {
            laporanSubMenu.setVisible(true);
            laporanSubMenu.setManaged(true);
            laporanBtn.setText("📊  Laporan ▲");
        }


        Button laporanPinjamBtn =
                createMenuButton("Laporan Peminjaman", true);

        Button laporanGunaBtn =
                createMenuButton("Laporan Penggunaan", false);

        // ACTION
        dashboardBtn.setOnAction(e -> {
                Stage s = (Stage) sidebar.getScene().getWindow();
            if (user.isSuperAdmin()) {
                s.setScene(new Scene(new SuperAdminPage(user), 1280, 720));
            } else {
                s.setScene(new Scene(new AdminPage(user), 1280, 720));
            }
        });
        
        verifikasiBtn.setOnAction(e -> {
            Stage s = (Stage) verifikasiBtn.getScene().getWindow();
            s.setScene(new Scene(new VerifikasiPage(user), 1280, 720));
        });
        
        userBtn.setOnAction(e -> {
            Stage currentStage = (Stage) userBtn.getScene().getWindow();
            Scene newScene = new Scene(new AdminUserPage(user), 1280, 720);
            currentStage.setScene(newScene);
        });

        manageDataBtn.setOnAction(e -> {
            Stage s = (Stage) manageDataBtn.getScene().getWindow();
            s.setScene(new Scene(new ManageDataPage(user), 1280, 720));
        });
        
        auditTrailBtn.setOnAction(e -> {
            Stage currentStage = (Stage) auditTrailBtn.getScene().getWindow();
            Scene newScene = new Scene(new AuditTrailPage(user), 1280, 720);
            currentStage.setScene(newScene);
        });
        

        laporanGunaBtn.setOnAction(e -> {
            Stage s = (Stage) laporanGunaBtn.getScene().getWindow();
            s.setScene(new Scene(new LaporanPenggunaanPage(user), 1280, 720));
        });

        // TOGGLE LAPORAN ▼ ▲
        laporanBtn.setOnAction(e -> {
            boolean open = laporanSubMenu.isVisible();
            laporanSubMenu.setVisible(!open);
            laporanSubMenu.setManaged(!open);
            laporanBtn.setText(open ? "📊  Laporan ▼" : "📊  Laporan ▲");
        });

        // MASUKKAN SUBMENU
        laporanSubMenu.getChildren().addAll(
                laporanPinjamBtn,
                laporanGunaBtn
        );

        // FINAL ADD
        menuBox.getChildren().add(dashboardBtn);
        
        if (user.isAdmin()) {
            menuBox.getChildren().add(verifikasiBtn);
        }
        
        if (user.isSuperAdmin()) {
            menuBox.getChildren().add(userBtn);
        }
        
        menuBox.getChildren().add(manageDataBtn);
        
        if (user.isSuperAdmin()) {
            menuBox.getChildren().add(auditTrailBtn);
        }
        
        menuBox.getChildren().addAll(laporanBtn, laporanSubMenu);
        
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
        user.getIdUser(),          
        user.getUsername(),         
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

    // Inner class
    public static class PeminjamanData {
        private String idPeminjaman, namaPeminjam, role, barang, jumlah;
        private String tglPinjam, tglKembali, statusVerifikasi, statusBarang;
        
        public PeminjamanData(String idPeminjaman, String namaPeminjam, String role, 
                             String barang, String jumlah, String tglPinjam, String tglKembali,
                             String statusVerifikasi, String statusBarang) {
            this.idPeminjaman = idPeminjaman;
            this.namaPeminjam = namaPeminjam;
            this.role = role;
            this.barang = barang;
            this.jumlah = jumlah;
            this.tglPinjam = tglPinjam;
            this.tglKembali = tglKembali;
            this.statusVerifikasi = statusVerifikasi;
            this.statusBarang = statusBarang;
        }
        
        public String getIdPeminjaman() { return idPeminjaman; }
        public String getNamaPeminjam() { return namaPeminjam; }
        public String getRole() { return role; }
        public String getBarang() { return barang; }
        public String getJumlah() { return jumlah; }
        public String getTglPinjam() { return tglPinjam; }
        public String getTglKembali() { return tglKembali; }
        public String getStatusVerifikasi() { return statusVerifikasi; }
        public String getStatusBarang() { return statusBarang; }
    }
}