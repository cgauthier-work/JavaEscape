package com.example.quizjavaescape;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Java Escape 💣");
        stage.setResizable(false);
        IntroView.afficher(stage);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}