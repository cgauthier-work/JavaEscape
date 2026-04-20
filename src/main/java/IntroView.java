package com.example.quizjavaescape;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class IntroView {

    public static void afficher(Stage stage) {

        Label emoji = new Label("💣");
        emoji.setStyle("-fx-font-size: 90px;");

        Label titre = new Label("JAVA ESCAPE");
        titre.setStyle(
                "-fx-font-size: 42px; -fx-font-weight: bold; -fx-text-fill: #e0e0e0;" +
                        "-fx-effect: dropshadow(gaussian, #4a90d9, 12, 0, 0, 0);"
        );

        Label sousTitre = new Label("Désamorce la bombe. Sauve la ville.");
        sousTitre.setStyle("-fx-font-size: 17px; -fx-text-fill: #888; -fx-font-style: italic;");

        Button boutonJouer = new Button("▶  Jouer");
        boutonJouer.setStyle(
                "-fx-background-color: #4a90d9; -fx-text-fill: white;" +
                        "-fx-font-size: 18px; -fx-font-weight: bold;" +
                        "-fx-padding: 14 50 14 50; -fx-background-radius: 10; -fx-cursor: hand;"
        );
        boutonJouer.setOnAction(e -> DialogueView.afficher(stage, false));

        VBox root = new VBox(22, emoji, titre, sousTitre, boutonJouer);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(60));
        root.setStyle("-fx-background-color: #1a1a2e;");

        stage.setScene(new Scene(root, 900, 600));
    }
}