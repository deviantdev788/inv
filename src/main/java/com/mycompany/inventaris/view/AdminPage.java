/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.inventaris.view;

import com.mycompany.inventaris.model.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

/**
 *
 * @author pnady
 */
public class AdminPage extends BorderPane {

    private Stage stage;
    private User user;
    
    public AdminPage(User user){
        this.user = user;
        initializeUI();
    }
    
    private void initializeUI() {

        // ===================== SIDEBAR =====================
        VBox sidebar = new VBox(10);
        sidebar.setPadding(new Insets(20, 10, 20, 10));
        sidebar.setAlignment(Pos.TOP_LEFT);
        sidebar.setPrefWidth(200);
        sidebar.setStyle(
                "-fx-background-color: white;" +
                "-fx-padding: 20 10;" +
                "-fx-border-width: 0 1 0 0;" +
                "-fx-border-color: #e5e7eb;"
        );


        // Logo
        ImageView logo = new ImageView(
                new Image(getClass().getResourceAsStream("/assets/logoAsa.png"))
        );
        logo.setFitHeight(70);
        logo.setPreserveRatio(true);

        VBox logoBox = new VBox(logo);
        logoBox.setAlignment(Pos.TOP_LEFT);
        logoBox.setPadding(new Insets(0,0,0,0));


        // ===================== USER PROFILE =====================
        Image userPhoto = new Image(getClass().getResourceAsStream("/assets/user.png"));
        ImageView userImage = new ImageView(userPhoto);
        userImage.setFitWidth(40);
        userImage.setFitHeight(40);
        userImage.setPreserveRatio(true);

        Circle clipCircle = new Circle(20, 20, 20);
        userImage.setClip(clipCircle);

        String fullName = user.getNama();
        String[] parts = fullName.split(" ");
        
        String displayName = parts[0];
        if(parts.length> 1){
            displayName += " " + parts[1];
        }
        Label nameLabel = new Label(displayName.toUpperCase());
        nameLabel.setStyle(
                "-fx-font-size: 14px;" +
                "-fx-font-weight: bold;" +
                "-fx-text-fill: #1e293b;"
        );
        
        Label roleLabel = new Label(user.getRole().toUpperCase());
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


        // ===================== MENU =====================
        VBox menuBox = new VBox(8);
        menuBox.setAlignment(Pos.TOP_CENTER);

        Button dashboardBtn = createMenuButton("🏠  Dashboard", true);
        Button pembelianBtn = createMenuButton("📊  Pembelian", false);
        Button stockBtn = createMenuButton("🕐  Kartu Stock", false);

        pembelianBtn.setOnAction(e -> {
            Scene newScene = new Scene(new StatusPage(user), 1280, 720);
            Stage currentStage = (Stage) pembelianBtn.getScene().getWindow();
            currentStage.setScene(newScene);
        });
        
        stockBtn.setOnAction(e -> {
            Scene newScene = new Scene(new RiwayatPage(user), 1280, 720);
            Stage currentStage = (Stage) stockBtn.getScene().getWindow();
            currentStage.setScene(newScene);
        });

        menuBox.getChildren().addAll(dashboardBtn, pembelianBtn, stockBtn);


        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        Button logoutBtn = new Button("↩ Logout");
        logoutBtn.setAlignment(Pos.CENTER_LEFT);
        logoutBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: #475569;" +
                "-fx-padding: 12 10;" +
                "-fx-font-weight: bold;" +
                "-fx-cursor: hand;"
        );
        logoutBtn.setOnAction(e -> {
            Stage currentStage = (Stage) logoutBtn.getScene().getWindow();
            Scene newScene = new Scene(new MainPage(currentStage), 1280, 720);
            currentStage.setScene(newScene);
        });

        sidebar.getChildren().addAll(logoBox, userBox, menuBox, spacer, logoutBtn);
        
        // ===================== MAIN CONTENT =====================
        StackPane mainContent = new StackPane();
        mainContent.setStyle("-fx-background-color: #f8fafc;");

        VBox centerBox = new VBox(40);
        centerBox.setAlignment(Pos.CENTER);
        centerBox.setPadding(new Insets(60));

        Label halo = new Label("Halo, " + fullName + " !!");
        halo.setStyle(
            "-fx-font-size: 40px;" +
            "-fx-font-weight: bold;" +
            "-fx-text-fill: #334155;"
        );

        // ===================== CARD CONTENT =====================
        HBox cardBox = new HBox(20);
        cardBox.setAlignment(Pos.CENTER);

        VBox cardPeminjaman = createStatCard("Data Peminjaman", "3", "#fee2e2");
        VBox cardPermintaan = createStatCard("Data Permintaan", "3", "#e0f2fe");
        VBox cardPengembalian = createStatCard("Data Pengembalian", "3", "#fee2e2");
        VBox cardReplacement = createStatCard("Data Replacement", "3", "#e0f2fe");

        cardBox.getChildren().addAll(
            cardPeminjaman,
            cardPermintaan,
            cardPengembalian,
            cardReplacement
        );

        centerBox.getChildren().addAll(halo, cardBox);
        mainContent.getChildren().add(centerBox);

        this.setLeft(sidebar);
        this.setCenter(mainContent);
    }
    private VBox createStatCard(String title, String value, String bgColor) {

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
                "-fx-font-size: 18px;" +
//                "-fx-text-fill: #64748b;" +
                "-fx-font-weight: bold;" 
        );

        Label valueLabel = new Label(value);
        valueLabel.setStyle(
                "-fx-font-size: 28px;" +
                "-fx-font-weight: bold;"
        );

        VBox card = new VBox(8, titleLabel, valueLabel);
        card.setAlignment(Pos.CENTER);
        card.setPadding(new Insets(20));
        card.setPrefWidth(220);
        card.setStyle(
                "-fx-background-color: " + bgColor + ";" +
                "-fx-background-radius: 14;" +
                "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.08), 10, 0, 0, 4);"
        );

        return card;
    }

    private Button createMenuButton(String text, boolean isActive) {
        Button btn = new Button(text);

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

        btn.setMaxWidth(Double.MAX_VALUE);
        return btn;
    }
}
