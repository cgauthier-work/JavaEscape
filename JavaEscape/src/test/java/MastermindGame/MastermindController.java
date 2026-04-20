package MastermindGame;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import java.util.ArrayList;
import java.util.List;

public class MastermindController {
    private final List<Button> selectedButtons = new ArrayList<>();
    private final List<String> selectedColors = new ArrayList<>();

    private int attempts = 0;
    public final int MAX_ATTEMPTS = 10;

    @FXML
    private Label historyLabel;

    @FXML
    private void onColorClick(ActionEvent event) {
        Button sourceButton = (Button) event.getSource();
        String colorId = sourceButton.getId();

        if (selectedButtons.contains(sourceButton)) {
            deselect(sourceButton);
            updateNumbers();
        } else {
            if (selectedColors.size() >= 4) {
                return;
            }
            selectedButtons.add(sourceButton);
            selectedColors.add(colorId);
            select(sourceButton);
        }
    }

    private void select(Button btn) {
        btn.setStyle(btn.getStyle() + "-fx-border-color: black; -fx-border-width: 3; -fx-border-style: solid;");
        btn.setText(String.valueOf(selectedColors.size()));
    }

    private void deselect(Button btn) {
        int index = selectedButtons.indexOf(btn);
        if (index != -1) {
            selectedColors.remove(index);
            selectedButtons.remove(index);
        }

        String cleanedStyle = btn.getStyle()
                .replace("-fx-border-color: black;", "")
                .replace("-fx-border-width: 3;", "")
                .replace("-fx-border-style: solid;", "");
        btn.setStyle(cleanedStyle);
        btn.setText(btn.getId());
    }

    private void updateNumbers() {
        for (int i = 0; i < selectedButtons.size(); i++) {
            selectedButtons.get(i).setText(String.valueOf(i + 1));
        }
    }

    @FXML
    private void onLaunchClick() {
        if (attempts >= MAX_ATTEMPTS) {
            return;
        }

        if (selectedColors.size() == 4) {
            List<String> codeSecret = MastermindGame.getSecretCode();
            attempts++;

            int score = 0;
            for (int i = 0; i < 4; i++) {
                if (selectedColors.get(i).equals(codeSecret.get(i))) {
                    score++;
                }
            }

            String log = "\nEssai " + attempts + " : " + selectedColors + " | Score : " + score + "\n";
            if (historyLabel != null) {
                historyLabel.setText(historyLabel.getText() + log);
            }

            javafx.stage.Stage stage = (javafx.stage.Stage) historyLabel.getScene().getWindow();

            if (score == 4) {
                quizjavaescape.DialogueView.afficher(stage, "VICTOIRE");
            } else if (attempts == MAX_ATTEMPTS) {
                quizjavaescape.DialogueView.afficher(stage, "DEFAITE");
            }

            clearCurrentSelection();
        }
    }

    private void clearCurrentSelection() {
        List<Button> toDeselect = new ArrayList<>(selectedButtons);
        for (Button btn : toDeselect) {
            deselect(btn);
        }
        selectedButtons.clear();
        selectedColors.clear();
    }
}