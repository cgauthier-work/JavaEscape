package SceneHandler;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import java.io.IOException;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onPlayButtonCLick() {
        try {
            Stage stage = (Stage) welcomeText.getScene().getWindow();
            MastermindScene gameScene = new MastermindScene();
            gameScene.start(stage);
        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
    }

    @FXML
    protected void onQuitButtonClick() {
        System.exit(0);
    }

    @FXML
    protected void onReplayButtonClick() {
        System.out.println("Reprendre Button");
    }
}