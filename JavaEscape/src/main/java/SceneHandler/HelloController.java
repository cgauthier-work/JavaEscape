package SceneHandler;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class HelloController {
    @FXML
    private Label welcomeText;

    @FXML
    protected void onPlayButtonCLick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
    @FXML
    protected void onQuitButtonClick(){
        System.exit(0);
    }
    @FXML
    protected void onReplayButtonClick(){
        System.out.print("Reprendre Button");
    }
}
