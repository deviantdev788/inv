package com.mycompany.inventaris.view;

import com.mycompany.inventaris.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class SuperAdminPage extends BorderPane {
    
    private User superadmin;
    
    public SuperAdminPage(User superadmin) {
        this.superadmin = superadmin;
        initializeUI();
    }

    private void initializeUI() {
        // SIDEBAR
        VBox sidebar = createSidebar();

        // MAIN CONTENT
        StackPane mainContent = new StackPane();
        mainContent.setStyle("-fx-background-color: #f8fafc;");

        VBox centerBox = new VBox(40);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(60));

        // Title
        Label greeting = new Label("HALO, " + superadmin.getNama().toUpperCase() + "!!");
        greeting.setStyle(
            "-fx-font-size: 48px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #64748b;"
        );

        // Stats Cards
        HBox statsBox = new HBox(30);
        statsBox.setAlignment(Pos.CENTER);

        VBox permintaanCard = createStatCard("Data Permintaan", "0", "#FDE2E4");
        VBox peminjamanCard = createStatCard("Data Peminjaman", "0", "#DBEAFE");
        VBox pengembalianCard = createStatCard("Data Pengembalian", "0", "#FCE7F3");
        VBox replacementCard = createStatCard("Data Replacement", "0", "#BAE6FD");

        statsBox.getChildren().addAll(permintaanCard, peminjamanCard, pengembalianCard, replacementCard);

        centerBox.getChildren().addAll(greeting, statsBox);
        mainContent.getChildren().add(centerBox);

        this.setLeft(sidebar);
        this.setCenter(mainContent);
    }

    private VBox createStatCard(String title, String value, String bgColor) {
        VBox card = new VBox(15);
        card.setPadding(new Insets(30, 40, 30, 40));
        card.setAlignment(Pos.CENTER);
        card.setStyle(
            "-fx-background-color: " + bgColor + "; " +
            "-fx-background-radius: 15; " +
            "-fx-min-width: 200; " +
            "-fx-min-height: 130;"
        );

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-size: 13px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #1e293b;"
        );
        titleLabel.setWrapText(true);
        titleLabel.setAlignment(Pos.CENTER);

        Label valueLabel = new Label(value);
        valueLabel.setStyle(
            "-fx-font-size: 32px; " +
            "-fx-font-weight: bold; " +
            "-fx-text-fill: #1e293b;"
        );

        card.getChildren().addAll(titleLabel, valueLabel);
        return card;
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

        Label nameLabel = new Label(superadmin.getNama());
        nameLabel.setStyle("-fx-font-size: 13px; -fx-font-weight: bold; -fx-text-fill: #1e293b;");
        
        Label roleLabel = new Label(superadmin.getRole().toUpperCase());
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
        Button dashboardBtn = createMenuButton("🏠  Dashboard", true);
        Button userBtn = createMenuButton("👤  User", false);
        Button manageDataBtn = createMenuButton("⚙  Manage Data", false);
        
        // Submenu Laporan (collapsed by default)
        VBox laporanSubmenu = new VBox(5);
        laporanSubmenu.setVisible(false);
        laporanSubmenu.setManaged(false);
        laporanSubmenu.setPadding(new Insets(0, 0, 0, 25));
        
        Button laporanBtn = createMenuButton("📊  Laporan ▼", false);

        // Toggle laporan submenu
        laporanBtn.setOnAction(e -> {
            boolean isVisible = laporanSubmenu.isVisible();
            laporanSubmenu.setVisible(!isVisible);
            laporanSubmenu.setManaged(!isVisible);
            laporanBtn.setText(isVisible ? "📊  Laporan ▼" : "📊  Laporan ▲");
        });

        // Navigation handlers
        userBtn.setOnAction(e -> {
            Stage currentStage = (Stage) userBtn.getScene().getWindow();
            Scene newScene = new Scene(new AdminUserPage(superadmin), 1280, 720);
            currentStage.setScene(newScene);
        });
       
        manageDataBtn.setOnAction(e -> {
            Stage currentStage = (Stage) manageDataBtn.getScene().getWindow();
            Scene newScene = new Scene(new ManageDataPageSuperadmin(superadmin), 1280, 720);
            currentStage.setScene(newScene);
        });

        menuBox.getChildren().addAll(
            dashboardBtn, 
            userBtn, 
            manageDataBtn, 
            laporanBtn, 
            laporanSubmenu
        );

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
}