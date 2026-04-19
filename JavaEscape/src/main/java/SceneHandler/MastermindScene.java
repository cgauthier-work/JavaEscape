package SceneHandler;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class MastermindScene {
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("MastermindScene.fxml"));

        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Mastermind");
        stage.setScene(scene);
        stage.show();
        stage.setMaximized(true);
    }
}