package com.mycompany.inventaris.view;

import com.mycompany.inventaris.model.User;
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

public class AdminKartuStockPage extends BorderPane {
    
    private TableView<StockData> table;
    private List<StockData> allData;
    private User admin;
    
    public AdminKartuStockPage(User admin) {
        this.admin = admin;
        allData = new ArrayList<>();
        // Dummy data
        allData.add(new StockData("NC001", "Computer", "40"));
        allData.add(new StockData("NC001", "Spidol", "10"));
        allData.add(new StockData("NC001", "Penghapus Papan Tulis", "20"));
        allData.add(new StockData("NC001", "Kertas Folio", "100"));
        allData.add(new StockData("NC001", "AC", "20"));
        allData.add(new StockData("NC001", "Kertas HVS", "50"));
        allData.add(new StockData("NC001", "Webcam", "5"));
        allData.add(new StockData("NC001", "Keyboard", "40"));
        allData.add(new StockData("NC001", "Mouse", "40"));
        allData.add(new StockData("NC001", "Proyektor", "20"));
        
        initializeUI();
    }

    private void initializeUI() {
        VBox sidebar = createSidebar();

        VBox mainContent = new VBox(20);
        mainContent.setPadding(new Insets(30, 40, 30, 40));
        mainContent.setStyle("-fx-background-color: #f8fafc;");

        Label title = new Label("Kartu Stock");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");

        HBox topBar = new HBox(15);
        topBar.setAlignment(Pos.CENTER_LEFT);

        TextField searchField = new TextField();
        searchField.setPromptText("🔍  Cari Nama / ID barang");
        searchField.setPrefWidth(350);
        searchField.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #e5e7eb; " +
            "-fx-border-radius: 25; " +
            "-fx-background-radius: 25; " +
            "-fx-padding: 8 20;"
        );

        topBar.getChildren().add(searchField);

        // Table Container with simple border
        VBox tableContainer = new VBox();
        tableContainer.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #e5e7eb; " +
            "-fx-border-width: 1; " +
            "-fx-border-radius: 10; " +
            "-fx-background-radius: 10;"
        );

        // Table
        table = new TableView<>();
        table.setStyle(
            "-fx-background-color: transparent; " +
            "-fx-table-cell-border-color: #e5e7eb;"
        );
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn<StockData, String> idCol = new TableColumn<>("ID Barang");
        idCol.setMinWidth(150);
        idCol.setStyle("-fx-alignment: CENTER;");
        idCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIdBarang()));

        TableColumn<StockData, String> nameCol = new TableColumn<>("Barang");
        nameCol.setMinWidth(300);
        nameCol.setStyle("-fx-alignment: CENTER;");
        nameCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getBarang()));

        TableColumn<StockData, String> qtyCol = new TableColumn<>("Qty");
        qtyCol.setMinWidth(150);
        qtyCol.setStyle("-fx-alignment: CENTER;");
        qtyCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getQty()));

        table.getColumns().addAll(idCol, nameCol, qtyCol);
        allData.forEach(data -> table.getItems().add(data));

        // Apply clean header styling - NO RED BACKGROUND
        this.sceneProperty().addListener((observable, oldScene, newScene) -> {
            if (newScene != null) {
                javafx.application.Platform.runLater(() -> {
                    javafx.scene.Node headerBg = table.lookup(".column-header-background");
                    if (headerBg != null) {
                        headerBg.setStyle("-fx-background-color: white;");
                    }
                    table.lookupAll(".column-header").forEach(node -> {
                        node.setStyle("-fx-background-color: white; -fx-border-color: #e5e7eb; -fx-border-width: 0 0 1 0;");
                    });
                    table.lookupAll(".column-header > .label").forEach(node -> {
                        node.setStyle("-fx-text-fill: #1e293b; -fx-font-weight: bold; -fx-font-size: 13px;");
                    });
                    javafx.scene.Node filler = table.lookup(".filler");
                    if (filler != null) {
                        filler.setStyle("-fx-background-color: white;");
                    }
                });
            }
        });

        // Search functionality
        searchField.textProperty().addListener((obs, old, newVal) -> {
            table.getItems().clear();
            if (newVal.isEmpty()) {
                allData.forEach(data -> table.getItems().add(data));
            } else {
                String keyword = newVal.toLowerCase();
                allData.stream()
                    .filter(data -> 
                        data.getIdBarang().toLowerCase().contains(keyword) ||
                        data.getBarang().toLowerCase().contains(keyword))
                    .forEach(data -> table.getItems().add(data));
            }
        });

        tableContainer.getChildren().add(table);

        // Bottom buttons and pagination
        HBox bottomBar = new HBox(15);
        bottomBar.setAlignment(Pos.CENTER_RIGHT);
        bottomBar.setPadding(new Insets(15, 0, 0, 0));

        Button printBtn = new Button("Print");
        printBtn.setStyle(
            "-fx-background-color: #3C4C79; " +
            "-fx-text-fill: white; " +
            "-fx-padding: 10 30; " +
            "-fx-background-radius: 20; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: bold; " +
            "-fx-cursor: hand;"
        );

        Button exportBtn = new Button("Export");
        exportBtn.setStyle(
            "-fx-background-color: #3C4C79; " +
            "-fx-text-fill: white; " +
            "-fx-padding: 10 30; " +
            "-fx-background-radius: 20; " +
            "-fx-font-size: 13px; " +
            "-fx-font-weight: bold; " +
            "-fx-cursor: hand;"
        );

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        // Pagination
        HBox pagination = new HBox(10);
        pagination.setAlignment(Pos.CENTER_RIGHT);
        
        Label pageInfo = new Label("Showing 1-10 from 100 data");
        pageInfo.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");
        
        Button prevBtn = createPaginationButton("◀");
        Button page1Btn = createPaginationButton("1");
        page1Btn.setStyle(page1Btn.getStyle() + "-fx-background-color: #3C4C79; -fx-text-fill: white;");
        Button page2Btn = createPaginationButton("2");
        Button page3Btn = createPaginationButton("3");
        Button nextBtn = createPaginationButton("▶");
        
        pagination.getChildren().addAll(pageInfo, prevBtn, page1Btn, page2Btn, page3Btn, nextBtn);

        bottomBar.getChildren().addAll(printBtn, exportBtn, spacer, pagination);

        mainContent.getChildren().addAll(title, topBar, tableContainer, bottomBar);

        this.setLeft(sidebar);
        this.setCenter(mainContent);
    }

    private Button createPaginationButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color: white; " +
            "-fx-border-color: #e5e7eb; " +
            "-fx-border-radius: 6; " +
            "-fx-background-radius: 6; " +
            "-fx-padding: 8 12; " +
            "-fx-font-size: 12px; " +
            "-fx-cursor: hand;"
        );
        btn.setMinWidth(40);
        return btn;
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

        Image userPhoto = new Image(getClass().getResourceAsStream("/assets/user.png"));
        ImageView userImage = new ImageView(userPhoto);
        userImage.setFitWidth(40);
        userImage.setFitHeight(40);
        userImage.setPreserveRatio(true);
        Circle clipCircle = new Circle(20, 20, 20);
        userImage.setClip(clipCircle);

        Label nameLabel = new Label(admin.getNama());
        nameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        
        Label emailLabel = new Label("nadia@gmail.com");
        emailLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: #9ca3af;");

        VBox textBox = new VBox(2, nameLabel, emailLabel);
        textBox.setAlignment(Pos.CENTER_LEFT);
        HBox userBox = new HBox(10, userImage, textBox);
        userBox.setAlignment(Pos.CENTER_LEFT);
        userBox.setPadding(new Insets(10, 10, 20, 10));

        VBox menuBox = new VBox(8);
        Button dashboardBtn = createMenuButton("🏠  Dashboard", false);
        Button userBtn = createMenuButton("👤  User", false);
        Button pembelianBtn = createMenuButton("🛒  Pembelian", false);
        Button kartuStockBtn = createMenuButton("📋  Kartu Stock", true);
        Button manageDataBtn = createMenuButton("⚙  Manage Data", false);
        Button laporanBtn = createMenuButton("📊  Laporan ▼", false);
        Button specialRequestBtn = createMenuButton("❗ Special Request", false);

        dashboardBtn.setOnAction(e -> {
            Stage currentStage = (Stage) dashboardBtn.getScene().getWindow();
            Scene newScene = new Scene(new SuperAdminPage(admin), 1280, 720);
            currentStage.setScene(newScene);
        });

        userBtn.setOnAction(e -> {
            Stage currentStage = (Stage) userBtn.getScene().getWindow();
            Scene newScene = new Scene(new AdminUserPage(admin), 1280, 720);
            currentStage.setScene(newScene);
        });

        menuBox.getChildren().addAll(dashboardBtn, userBtn, pembelianBtn, kartuStockBtn, manageDataBtn, laporanBtn, specialRequestBtn);

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
            Stage currentStage = (Stage) logoutBtn.getScene().getWindow();
            Scene newScene = new Scene(new MainPage(currentStage), 1280, 720);
            currentStage.setScene(newScene);
        });

        sidebar.getChildren().addAll(logoBox, userBox, menuBox, spacer, logoutBtn);
        return sidebar;
    }

    private Button createMenuButton(String text, boolean isActive) {
        Button btn = new Button(text);
        if (isActive) {
            btn.setStyle(
                "-fx-background-color: rgba(164,35,35,0.10); " +
                "-fx-font-weight: bold; " +
                "-fx-text-fill: #111827; " +
                "-fx-padding: 10 15; " +
                "-fx-background-radius: 6; " +
                "-fx-font-size: 13px; " +
                "-fx-alignment: center-left; " +
                "-fx-cursor: hand;"
            );
        } else {
            btn.setStyle(
                "-fx-background-color: transparent; " +
                "-fx-font-size: 13px; " +
                "-fx-text-fill: #475569; " +
                "-fx-padding: 10 15; " +
                "-fx-font-weight: bold; " +
                "-fx-alignment: center-left; " +
                "-fx-background-radius: 6; " +
                "-fx-cursor: hand;"
            );
        }
        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }

    // Inner class for StockData
    public static class StockData {
        private String idBarang;
        private String barang;
        private String qty;
        
        public StockData(String idBarang, String barang, String qty) {
            this.idBarang = idBarang;
            this.barang = barang;
            this.qty = qty;
        }
        
        public String getIdBarang() { return idBarang; }
        public String getBarang() { return barang; }
        public String getQty() { return qty; }
    }
}