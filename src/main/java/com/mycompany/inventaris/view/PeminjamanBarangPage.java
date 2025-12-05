package com.mycompany.inventaris.view;

import com.mycompany.inventaris.dao.PermintaanDAO;
import com.mycompany.inventaris.model.Permintaan;
import java.util.Date;
import com.mycompany.inventaris.dao.BarangDAO;
import com.mycompany.inventaris.model.Barang;
import com.mycompany.inventaris.model.User;
import javafx.beans.property.SimpleStringProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.util.ArrayList;
import java.util.List;

public class PeminjamanBarangPage extends BorderPane {

    private TableView<BarangRow> table;
    private List<Barang> allData;
    private List<BarangRow> selectedItems = new ArrayList<>();
    private User user;
    private PermintaanDAO permintaanDAO;

    public PeminjamanBarangPage(User user) {
        allData = BarangDAO.getAll();
        permintaanDAO = new PermintaanDAO();
        this.user = user;
        loadStylesheet();
        initializeUI();
    }

    private void loadStylesheet() {
        try {
            String css = getClass().getResource("/css/main.css").toExternalForm();
            this.getStylesheets().add(css);
        } catch (Exception e) {
            System.err.println("Failed to load CSS: " + e.getMessage());
        }
    }

    private void initializeUI() {
        // SIDEBAR
        VBox sidebar = createSidebar();

        // MAIN CONTENT
        VBox mainContent = new VBox(20);
        mainContent.getStyleClass().add("main-content");

        // Header
        Label title = new Label("PEMINJAMAN BARANG");
        title.getStyleClass().add("page-title");

        // Search Bar
        TextField searchField = new TextField();
        searchField.setPromptText("🔍 Cari Barang...");
        searchField.getStyleClass().add("search-field");

        // Top Bar: Dropdown + Button
        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);

        ComboBox<String> kategoriBox = new ComboBox<>();
        kategoriBox.getItems().addAll("Semua Kategori", "Reusable", "Consumable", "Non Consumable");
        kategoriBox.setValue("Semua Kategori");
        kategoriBox.getStyleClass().add("combo-box");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Button meminjamBtn = new Button("Meminjam Barang");
        meminjamBtn.getStyleClass().add("btn-primary");
        meminjamBtn.setOnAction(e -> showFormPopup());

        topBar.getChildren().addAll(kategoriBox, spacer, meminjamBtn);

        // Table
        table = new TableView<>();
        table.getStyleClass().add("table-view");
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<BarangRow, String> noCol = new TableColumn<>("No.");
        noCol.setMinWidth(50);
        noCol.setMaxWidth(50);
        noCol.setCellValueFactory(data -> 
            new SimpleStringProperty(String.valueOf(table.getItems().indexOf(data.getValue()) + 1)));

        TableColumn<BarangRow, String> idCol = new TableColumn<>("ID Barang");
        idCol.setMinWidth(100);
        idCol.setMaxWidth(120);
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().barang.getKode()));

        TableColumn<BarangRow, String> nameCol = new TableColumn<>("Nama Barang");
        nameCol.setMinWidth(200);
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().barang.getNama()));

        TableColumn<BarangRow, String> catCol = new TableColumn<>("Kategori");
        catCol.setMinWidth(150);
        catCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().barang.getKategori()));

        TableColumn<BarangRow, String> stokCol = new TableColumn<>("Stok Barang");
        stokCol.setMinWidth(100);
        stokCol.setMaxWidth(120);
        stokCol.setCellValueFactory(data -> 
            new SimpleStringProperty(data.getValue().barang.getStok() + "pcs"));

        TableColumn<BarangRow, Void> actionCol = new TableColumn<>("Aksi");
        actionCol.setMinWidth(180);
        actionCol.setMaxWidth(200);
        actionCol.setCellFactory(col -> new TableCell<>() {
            private HBox actionBox = new HBox(8);
            private Button plusBtn = new Button("+");
            private Label countLabel = new Label("0");
            private Button minusBtn = new Button("-");

            {
                plusBtn.getStyleClass().add("action-btn-plus");
                minusBtn.getStyleClass().add("action-btn-minus");
                countLabel.getStyleClass().add("action-count-label");
              
                plusBtn.setOnAction(e -> {
                    BarangRow row = getTableView().getItems().get(getIndex());
                    row.quantity++;
                    countLabel.setText(String.valueOf(row.quantity));
                    if (!selectedItems.contains(row)) selectedItems.add(row);
                });

                minusBtn.setOnAction(e -> {
                    BarangRow row = getTableView().getItems().get(getIndex());
                    if (row.quantity > 0) {
                        row.quantity--;
                        countLabel.setText(String.valueOf(row.quantity));
                        if (row.quantity == 0) selectedItems.remove(row);
                    }
                });

                actionBox.getChildren().addAll(plusBtn, countLabel, minusBtn);
                actionBox.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    BarangRow row = getTableView().getItems().get(getIndex());
                    countLabel.setText(String.valueOf(row.quantity));
                    setGraphic(actionBox);
                }
            }
        });

        table.getColumns().addAll(noCol, idCol, nameCol, catCol, stokCol, actionCol);
        
        // Load data
        List<Barang> allData = BarangDAO.getAll();
        for (Barang b : allData) {
            table.getItems().add(new BarangRow(b));
        }

        // Search functionality
        searchField.textProperty().addListener((obs, old, newVal) -> {
            table.getItems().clear();
            if (newVal.isEmpty()) {
                allData.forEach(b -> table.getItems().add(new BarangRow(b)));
            } else {
                String keyword = newVal.toLowerCase();
                allData.stream()
                    .filter(b -> 
                        b.getKode().toLowerCase().contains(keyword) ||
                        b.getNama().toLowerCase().contains(keyword))
                    .forEach(b -> table.getItems().add(new BarangRow(b)));
            }
        });

        // Kategori filter
        kategoriBox.setOnAction(e -> {
            table.getItems().clear();
            String selected = kategoriBox.getValue();
            if (selected.equals("Semua Kategori")) {
                allData.forEach(b -> table.getItems().add(new BarangRow(b)));
            } else {
                allData.stream()
                    .filter(b -> b.getKategori().equals(selected))
                    .forEach(b -> table.getItems().add(new BarangRow(b)));
            }
        });

        mainContent.getChildren().addAll(title, searchField, topBar, table);

        this.setLeft(sidebar);
        this.setCenter(mainContent);
    }

    private VBox createSidebar() {
        VBox sidebar = new VBox(20);
        sidebar.getStyleClass().add("sidebar");
        sidebar.setAlignment(Pos.TOP_CENTER);

        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/assets/logoAsa.png")));
        logo.setFitHeight(80);
        logo.setPreserveRatio(true);
        
        VBox logoBox = new VBox(3, logo);
        logoBox.getStyleClass().add("logo-box");

        // foto user
        Image userPhoto = new Image(getClass().getResourceAsStream("/assets/user.png"));
        ImageView userImage = new ImageView(userPhoto);
        userImage.setFitWidth(45);
        userImage.setFitHeight(45);
        userImage.setPreserveRatio(true);
        userImage.getStyleClass().add("sidebar-user-avatar");

        // bikin foto jadi lingkaran
        Circle clipCircle = new Circle(22.5, 22.5, 22.5);
        userImage.setClip(clipCircle);

        // label "User"
        String fullName = user.getNama();
        String[] parts = fullName.split(" ");
        
        String displayName = parts[0];
        if(parts.length > 1){
            displayName += " " + parts[1];
        }
        Label guestLabel = new Label(displayName.toUpperCase());
        guestLabel.getStyleClass().add("sidebar-user-label");

        // posisi foto kiri - tulisan kanan (gantinya Vbox jadi HBox)
        HBox userBox = new HBox(12, userImage, guestLabel);
        userBox.getStyleClass().add("sidebar-user-box");

        VBox menuBox = new VBox(8);
        menuBox.getStyleClass().add("sidebar-menu");
        Button dashboardBtn = createMenuButton("🏠  Dashboard", false);
        Button riwayatBtn = createMenuButton("🕐  Riwayat", false);
        Button logoutBtn = new Button("↩  Logout");
        logoutBtn.getStyleClass().add("logout-button");
        
        dashboardBtn.setOnAction(e -> {
            Stage currentStage = (Stage) dashboardBtn.getScene().getWindow();
            Scene newScene = new Scene(new UserPage(user), 1280, 720);
            currentStage.setScene(newScene);
        });
        
        riwayatBtn.setOnAction(e -> {
            Stage currentStage = (Stage) riwayatBtn.getScene().getWindow();
            Scene newScene = new Scene(new RiwayatPage(), 1280, 720);
            currentStage.setScene(newScene);
        });
        
        logoutBtn.setOnAction(e -> {
            Stage currentStage = (Stage) logoutBtn.getScene().getWindow();
            Scene newScene = new Scene(new MainPage(currentStage), 1280, 720);
            currentStage.setScene(newScene);
        });
        
        menuBox.getChildren().addAll(dashboardBtn, riwayatBtn);

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sidebar.getChildren().addAll(logoBox, userBox, menuBox, spacer, logoutBtn);
        return sidebar;
    }

    private Button createMenuButton(String text, boolean active) {
        Button btn = new Button(text);
        btn.setMaxWidth(Double.MAX_VALUE);
        if (active) {
            btn.getStyleClass().add("menu-button-active");
        } else {
            btn.getStyleClass().add("menu-button");
        }
        return btn;
    }

    private void showFormPopup() {
        if (selectedItems.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Peringatan");
            alert.setHeaderText(null);
            alert.setContentText("Silakan pilih barang terlebih dahulu!");
            alert.showAndWait();
            return;
        }

        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initStyle(StageStyle.UNDECORATED);

        VBox container = new VBox(12);
        container.setStyle(
            "-fx-background-color: white; " +
            "-fx-padding: 20; " +
            "-fx-background-radius: 12; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 3);"
        );
        container.setPrefWidth(340);
        container.setAlignment(Pos.TOP_CENTER);

        // Logo di tengah atas
        ImageView logo = new ImageView(new Image(getClass().getResourceAsStream("/assets/logoAsa.png")));
        logo.setFitHeight(40);
        logo.setPreserveRatio(true);
        
        StackPane logoBox = new StackPane(logo);
        logoBox.setAlignment(Pos.CENTER);
        
        // Close button
        Button closeBtn = new Button("×");
        closeBtn.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-text-fill: #999; " +
            "-fx-font-size: 24px; " +
            "-fx-cursor: hand; " +
            "-fx-padding: 0;"
        );
        closeBtn.setOnAction(e -> popup.close());
        
        StackPane closeBtnBox = new StackPane(closeBtn);
        closeBtnBox.setAlignment(Pos.TOP_RIGHT);
        StackPane.setMargin(closeBtn, new Insets(-10, -10, 0, 0));

        StackPane headerStack = new StackPane(logoBox, closeBtnBox);

        Label title = new Label("Formulir Peminjaman");
        title.setStyle("-fx-font-size: 14px; -fx-font-weight: bold; -fx-text-fill: #2C3E50; -fx-font-family: 'Poppins';");

        // Form fields
        VBox fields = new VBox(10);

        // Nama Peminjam
        TextField namaPeminjam = new TextField();
        if(user != null){
            namaPeminjam.setText(user.getNama());
        }
        namaPeminjam.setPromptText("Ketik nama disini");
        VBox namaField = createSimpleField("Nama Peminjam", namaPeminjam);

        // Lokasi Pengiriman
        TextField lokasiPengambilan = new TextField();
        lokasiPengambilan.setPromptText("Ketik lokasi disini");
        VBox lokasiField = createSimpleField("Lokasi Pengiriman", lokasiPengambilan);

        // Nama & Kode Barang
        TextField namaKodeBarang = new TextField();
        String barangList = selectedItems.stream()
            .map(r -> r.barang.getNama() + " (" + r.barang.getKode() + ")")
            .reduce((a, b) -> a + ", " + b)
            .orElse("");
        namaKodeBarang.setText(barangList);
        namaKodeBarang.setPromptText("Spidol (RL001)");
        VBox namaKodeField = createSimpleField("Nama & Kode Barang", namaKodeBarang);

        // Jenis Barang
        ComboBox<String> jenisBarang = new ComboBox<>();
        jenisBarang.getItems().addAll("Reusable", "Consumable", "Non Consumable");
        if (!selectedItems.isEmpty()) {
            jenisBarang.setValue(selectedItems.get(0).barang.getKategori());
        }
        jenisBarang.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #ddd; " +
            "-fx-border-radius: 6; " +
            "-fx-background-radius: 6; " +
            "-fx-padding: 7 10; " +
            "-fx-font-size: 11px; " +
            "-fx-pref-width: 300; " +
            "-fx-font-family: 'Poppins';"
        );
        VBox jenisField = createSimpleFieldCombo("Jenis Barang", jenisBarang);

        // Jumlah Barang
        TextField jumlahBarang = new TextField();
        jumlahBarang.setText(String.valueOf(selectedItems.stream().mapToInt(r -> r.quantity).sum()) + " pcs");
        jumlahBarang.setEditable(false);
        VBox jumlahField = createSimpleField("Jumlah Barang", jumlahBarang);

        // Status badge
        Label statusLabel = new Label("Status");
        statusLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #555; -fx-font-weight: bold; -fx-font-family: 'Poppins';");
        
        Label statusBadge = new Label("Tersedia");
        statusBadge.setStyle(
            "-fx-background-color: #22c55e; " +
            "-fx-text-fill: white; " +
            "-fx-padding: 4 16; " +
            "-fx-background-radius: 12; " +
            "-fx-font-size: 10px; " +
            "-fx-font-weight: bold; " +
            "-fx-font-family: 'Poppins';"
        );
        
        VBox statusBox = new VBox(4, statusLabel, statusBadge);

        fields.getChildren().addAll(namaField, lokasiField, namaKodeField, jenisField, jumlahField, statusBox);

        Button submitBtn = new Button("Ajukan Peminjaman");
        submitBtn.setStyle(
            "-fx-background-color: #3C4C79; " +
            "-fx-text-fill: white; " +
            "-fx-padding: 9 0; " +
            "-fx-pref-width: 300; " +
            "-fx-background-radius: 8; " +
            "-fx-font-size: 12px; " +
            "-fx-font-weight: bold; " +
            "-fx-cursor: hand; " +
            "-fx-font-family: 'Poppins';"
        );
        submitBtn.setOnMouseEntered(e -> submitBtn.setStyle(
            "-fx-background-color: #2C3A5F; " +
            "-fx-text-fill: white; " +
            "-fx-padding: 9 0; " +
            "-fx-pref-width: 300; " +
            "-fx-background-radius: 8; " +
            "-fx-font-size: 12px; " +
            "-fx-font-weight: bold; " +
            "-fx-cursor: hand; " +
            "-fx-font-family: 'Poppins';"
        ));
        submitBtn.setOnMouseExited(e -> submitBtn.setStyle(
            "-fx-background-color: #3C4C79; " +
            "-fx-text-fill: white; " +
            "-fx-padding: 9 0; " +
            "-fx-pref-width: 300; " +
            "-fx-background-radius: 8; " +
            "-fx-font-size: 12px; " +
            "-fx-font-weight: bold; " +
            "-fx-cursor: hand; " +
            "-fx-font-family: 'Poppins';"
        ));
        submitBtn.setOnAction(e -> {
    if (namaPeminjam.getText().isEmpty() || lokasiPengambilan.getText().isEmpty()) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText("Semua field harus diisi!");
        alert.showAndWait();
        return;
    }

    try {
        for (BarangRow row : selectedItems) {
            if (row.quantity > 0) {
                String kategori = row.barang.getKategori(); // ambil kategori
                Date now = new Date();

                if (kategori.equalsIgnoreCase("Consumable")) {
                    // Simpan ke table permintaan
                    Permintaan p = new Permintaan();
                    p.setIdUser(user.getIdUser());
                    p.setIdBarang(row.barang.getIdBarang());
                    p.setJumlah(row.quantity);
                    p.setTanggal(now);
                    p.setStatus("Pending");

                    boolean sukses = permintaanDAO.insert(p);
                    if (!sukses) {
                        Alert alert = new Alert(Alert.AlertType.ERROR,
                            "Gagal menyimpan permintaan untuk " + row.barang.getNama());
                        alert.showAndWait();
                        return;
                    }

                } else {
                   
                }
            }
        }
        
        for (BarangRow row : selectedItems) {
        if (row.quantity > 0) {
            if (row.barang.getKategori().equals("Consumable")) {
                permintaanDAO.insert(new Permintaan(user.getIdUser(), row.barang.getIdBarang(), row.quantity, new Date(), "Diproses"));
            } 
            BarangDAO.reduceStok(row.barang.getIdBarang(), row.quantity); // stok berkurang
        }
    }
        popup.close();
        showSuccessPopup();

    } catch (Exception ex) {
        ex.printStackTrace();
        Alert alert = new Alert(Alert.AlertType.ERROR, "Terjadi kesalahan: " + ex.getMessage());
        alert.showAndWait();
    }
});

        container.getChildren().addAll(headerStack, title, fields, submitBtn);

        StackPane root = new StackPane(container);
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);");

        Scene scene = new Scene(root, 420, 540);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        popup.setScene(scene);
        popup.showAndWait();
    }

    private VBox createSimpleField(String labelText, TextField textField) {
        VBox field = new VBox(4);
        
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 10px; -fx-text-fill: #555; -fx-font-weight: bold; -fx-font-family: 'Poppins';");
        
        textField.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #ddd; " +
            "-fx-border-radius: 6; " +
            "-fx-background-radius: 6; " +
            "-fx-padding: 7 10; " +
            "-fx-pref-width: 300; " +
            "-fx-font-size: 11px; " +
            "-fx-font-family: 'Poppins';"
        );
        
        field.getChildren().addAll(label, textField);
        return field;
    }

    private VBox createSimpleFieldCombo(String labelText, ComboBox<String> combo) {
        VBox field = new VBox(4);
        
        Label label = new Label(labelText);
        label.setStyle("-fx-font-size: 10px; -fx-text-fill: #555; -fx-font-weight: bold; -fx-font-family: 'Poppins';");
        
        field.getChildren().addAll(label, combo);
        return field;
    }

    private void showSuccessPopup() {
        Stage popup = new Stage();
        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initStyle(StageStyle.UNDECORATED);

        VBox container = new VBox(20);
        container.setStyle(
            "-fx-background-color: white; " +
            "-fx-padding: 40; " +
            "-fx-background-radius: 15; " +
            "-fx-alignment: center; " +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.2), 15, 0, 0, 3);"
        );
        container.setPrefWidth(380);

        Circle circle = new Circle(45, Color.web("#22c55e"));
        Label checkmark = new Label("✓");
        checkmark.setStyle("-fx-font-size: 45px; -fx-text-fill: white; -fx-font-weight: bold;");
        StackPane icon = new StackPane(circle, checkmark);

        Label title = new Label("Sukses !!");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1e293b; -fx-font-family: 'Poppins';");

        Label message = new Label("Terima kasih telah meminjam barang,\nmohon ditunggu ya !!");
        message.setStyle("-fx-font-size: 13px; -fx-text-fill: #64748b; -fx-text-alignment: center; -fx-font-family: 'Poppins';");
        message.setWrapText(true);

        Button okBtn = new Button("Kembali ke Dashboard");
        okBtn.setStyle(
            "-fx-background-color: #3C4C79; " +
            "-fx-text-fill: white; " +
            "-fx-padding: 10 30; " +
            "-fx-background-radius: 20; " +
            "-fx-font-size: 12px; " +
            "-fx-font-weight: bold; " +
            "-fx-cursor: hand; " +
            "-fx-font-family: 'Poppins';"
        );
        okBtn.setOnAction(e -> {
            popup.close();
            Stage currentStage = (Stage) this.getScene().getWindow();
            Scene newScene = new Scene(new UserPage(user), 1280, 720);
            currentStage.setScene(newScene);
        });

        container.getChildren().addAll(icon, title, message, okBtn);

        StackPane root = new StackPane(container);
        root.setStyle("-fx-background-color: rgba(0, 0, 0, 0.4);");

        Scene scene = new Scene(root, 480, 380);
        scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
        popup.setScene(scene);
        popup.showAndWait();

        selectedItems.clear();
        table.getItems().forEach(row -> row.quantity = 0);
        table.refresh();
    }

    static class BarangRow {
        Barang barang;
        int quantity = 0;

        BarangRow(Barang barang) {
            this.barang = barang;
        }
    }
}