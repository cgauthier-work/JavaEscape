package SceneHandler;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Button;

public class MastermindController {
    private final List<String> selectedColors = new ArrayList<>();

    @FXML
    private void onColorClick(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String color = sourceButton.getText();


        if (sourceButton.getStyle().contains("-fx-border-color: black;")) {
            deselect(sourceButton);
            selectedColors.remove(color);
        } else {
            if (selectedColors.size() >= 4) {
                return;
            }
            select(sourceButton);
            selectedColors.add(color);
        }
    }

    private void select(Button btn) {
        btn.setStyle(btn.getStyle() + "-fx-border-color: black; -fx-border-width: 3; -fx-border-style: solid;");
    }

    private void deselect(Button btn) {
        String cleanedStyle = btn.getStyle()
                .replace("-fx-border-color: black;", "")
                .replace("-fx-border-width: 3;", "")
                .replace("-fx-border-style: solid;", "");
        btn.setStyle(cleanedStyle);
    }
}