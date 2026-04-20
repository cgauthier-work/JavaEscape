package SceneHandler;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MastermindGame {
    private static List<String> secretCode = new ArrayList<>();
    private final String[] availableColors = {"Rouge", "Bleu", "Vert", "Jaune", "Orange", "Violet"};

    public void start(Stage stage) throws IOException {
        generateSecretCode();

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MastermindScene.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        stage.setTitle("Mastermind");
        stage.setScene(scene);
        stage.show();
        stage.setMaximized(true);
    }

    public void generateSecretCode() {
        List<String> deck = new ArrayList<>(List.of(availableColors));
        Collections.shuffle(deck);
        secretCode = new ArrayList<>(deck.subList(0, 4));
    }

    public static List<String> getSecretCode() {
        return secretCode;
    }
}