package quizjavaescape;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuizView {

    private static final int    SCORE_REQUIS = 5;
    private static final String API_URL      = "https://opentdb.com/api.php?amount=10&type=multiple";

    private static final String[] COULEURS = {
            "#27ae60", "#2980b9", "#e74c3c", "#e67e22"
    };

    public static void afficher(Stage stage, int scoreInitial) {
        new Thread(() -> {
            List<String[]> questions = fetchQuestionsFromAPI();
            Platform.runLater(() -> {
                if (questions.isEmpty()) { afficherErreur(stage, scoreInitial); return; }
                lancerJeu(stage, questions, scoreInitial);
            });
        }).start();
    }

    public static void afficher(Stage stage) {
        afficher(stage, 0);
    }

    private static void lancerJeu(Stage stage, List<String[]> questions, int scoreInitial) {

        int[] bonnesReponses = {scoreInitial};
        int[] indexQuestion  = {0};

        Label labelTitre = new Label("💣 JAVA ESCAPE – Quiz");
        labelTitre.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #e0e0e0;");

        Label labelScore = new Label("Score : " + bonnesReponses[0] + " / " + SCORE_REQUIS);
        labelScore.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-text-fill: #27ae60;" +
                "-fx-background-color: #0d2d1a; -fx-padding: 6 18 6 18; -fx-background-radius: 20;");

        Label labelQuestion = new Label("Chargement…");
        labelQuestion.setStyle("-fx-font-size: 17px; -fx-font-weight: bold; -fx-text-fill: #e0e0e0;" +
                "-fx-background-color: #16213e; -fx-padding: 18 22 18 22; -fx-background-radius: 10;" +
                "-fx-border-color: #4a90d9; -fx-border-width: 1; -fx-border-radius: 10;" +
                "-fx-text-alignment: center;");
        labelQuestion.setWrapText(true);
        labelQuestion.setPrefWidth(640);
        labelQuestion.setMinHeight(100);
        labelQuestion.setAlignment(Pos.CENTER);

        Button[] boutons = new Button[4];
        GridPane grille = new GridPane();
        grille.setHgap(15); grille.setVgap(15); grille.setAlignment(Pos.CENTER);
        for (int i = 0; i < 4; i++) {
            boutons[i] = new Button();
            boutons[i].setWrapText(true);
            boutons[i].setMinWidth(290); boutons[i].setMaxWidth(290); boutons[i].setMinHeight(60);
            styleBouton(boutons[i], COULEURS[i], 1.0);
            grille.add(boutons[i], i % 2, i / 2);
        }

        Label labelFeedback = new Label();
        labelFeedback.setVisible(false);

        Button boutonSuivant = new Button("Question suivante ▶");
        styleBoutonNav(boutonSuivant, "#4a90d9");
        boutonSuivant.setVisible(false);

        Button boutonRecommencer = new Button("🔄  Recommencer");
        styleBoutonNav(boutonRecommencer, "#27ae60");
        boutonRecommencer.setVisible(false);
        boutonRecommencer.setOnAction(e -> afficher(stage, 0));

        VBox root = new VBox(18, labelTitre, labelScore, labelQuestion, grille, labelFeedback, boutonSuivant, boutonRecommencer);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #1a1a2e;");
        stage.setScene(new Scene(root, 900, 640));

        Runnable[] ref = {null};
        ref[0] = () -> {
            labelFeedback.setVisible(false);
            boutonSuivant.setVisible(false);
            boutonRecommencer.setVisible(false);

            String[] q = questions.get(indexQuestion[0]);
            labelQuestion.setText("Q" + (indexQuestion[0] + 1) + ".  " + q[0]);
            final String bonneReponse = q[1];

            List<String> reponses = new ArrayList<>(Arrays.asList(q[1], q[2], q[3], q[4]));
            Collections.shuffle(reponses);

            for (int i = 0; i < 4; i++) {
                final String texte = reponses.get(i);
                final int idx = i;

                boutons[i].setText(texte);
                boutons[i].setDisable(false);
                styleBouton(boutons[i], COULEURS[i], 1.0);

                boutons[i].setOnAction(e -> {
                    for (Button b : boutons) b.setDisable(true);
                    boolean correct = texte.equals(bonneReponse);

                    if (correct) {
                        for (int j = 0; j < 4; j++) if (j != idx) styleBouton(boutons[j], COULEURS[j], 0.3);
                        bonnesReponses[0]++;
                        labelScore.setText("Score : " + bonnesReponses[0] + " / " + SCORE_REQUIS);
                        labelFeedback.setText("✅  Correct !");
                        labelFeedback.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #27ae60; -fx-background-color: #0d2d1a; -fx-padding: 8 16 8 16; -fx-background-radius: 8;");
                    } else {
                        for (int j = 0; j < 4; j++) {
                            if (boutons[j].getText().equals(bonneReponse)) styleBouton(boutons[j], "#27ae60", 1.0);
                            else if (j == idx) styleBouton(boutons[j], "#e74c3c", 1.0);
                            else styleBouton(boutons[j], COULEURS[j], 0.3);
                        }
                        labelFeedback.setText("❌  Incorrect ! Bonne réponse : " + bonneReponse);
                        labelFeedback.setStyle("-fx-font-size: 15px; -fx-font-weight: bold; -fx-text-fill: #e74c3c; -fx-background-color: #2d0d0d; -fx-padding: 8 16 8 16; -fx-background-radius: 8;");
                    }
                    labelFeedback.setVisible(true);

                    if (bonnesReponses[0] >= SCORE_REQUIS) {
                        boutonSuivant.setText("Continuer l'aventure ▶");
                        boutonSuivant.setOnAction(ev -> DialogueView.afficher(stage, "AVANT_MASTERMIND"));
                        boutonSuivant.setVisible(true);
                        boutonRecommencer.setVisible(true);
                    } else {
                        boutonSuivant.setText("Question suivante ▶");
                        boutonSuivant.setOnAction(ev -> {
                            indexQuestion[0]++;

                            if (indexQuestion[0] >= questions.size()) {
                                afficher(stage, bonnesReponses[0]);
                            } else {
                                ref[0].run();
                            }
                        });
                        boutonSuivant.setVisible(true);
                    }
                });
            }
        };
        ref[0].run();
    }

    private static void afficherErreur(Stage stage, int scoreActuel) {
        Label msg = new Label("⚠  Impossible de charger les questions.\nVérifie ta connexion internet.");
        msg.setStyle("-fx-font-size: 18px; -fx-text-fill: #e74c3c; -fx-text-alignment: center;");
        msg.setWrapText(true); msg.setAlignment(Pos.CENTER);
        Button retry = new Button("🔄  Réessayer");
        styleBoutonNav(retry, "#4a90d9");
        retry.setOnAction(e -> afficher(stage, scoreActuel));
        VBox root = new VBox(30, msg, retry);
        root.setAlignment(Pos.CENTER); root.setPadding(new Insets(60));
        root.setStyle("-fx-background-color: #1a1a2e;");
        stage.setScene(new Scene(root, 900, 400));
    }

    private static void styleBouton(Button b, String couleur, double opacity) {
        b.setStyle("-fx-background-color: " + couleur + "; -fx-text-fill: white; -fx-font-size: 14px; -fx-font-weight: bold; -fx-background-radius: 10; -fx-cursor: hand;");
        b.setOpacity(opacity);
    }

    private static void styleBoutonNav(Button b, String couleur) {
        b.setStyle("-fx-background-color: " + couleur + "; -fx-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold; -fx-padding: 10 28 10 28; -fx-background-radius: 8; -fx-cursor: hand;");
    }

    private static List<String[]> fetchQuestionsFromAPI() {
        List<String[]> list = new ArrayList<>();
        try {
            URL url = new URL(API_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5000);
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) return chargerQuestionsDeSecours();

            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = rd.readLine()) != null) sb.append(line);
            rd.close();

            Pattern p = Pattern.compile("\"question\":\"(.*?)\",\"correct_answer\":\"(.*?)\",\"incorrect_answers\":\\[\"(.*?)\",\"(.*?)\",\"(.*?)\"\\]");
            Matcher m = p.matcher(sb.toString());
            while (m.find()) {
                list.add(new String[]{ decodeHTML(m.group(1)), decodeHTML(m.group(2)), decodeHTML(m.group(3)), decodeHTML(m.group(4)), decodeHTML(m.group(5)) });
            }
        } catch (Exception e) { return chargerQuestionsDeSecours(); }
        return list;
    }

    private static List<String[]> chargerQuestionsDeSecours() {
        return new ArrayList<>(Arrays.asList(
                new String[]{"Backup: Which planet is known as the Red Planet?", "Mars", "Venus", "Jupiter", "Saturn"},
                new String[]{"Backup: What is the largest mammal in the world?", "Blue Whale", "Elephant", "Giraffe", "Orca"}
        ));
    }

    private static String decodeHTML(String str) {
        if (str == null) return "";
        return str.replace("&quot;", "\"").replace("&#039;", "'").replace("&amp;", "&").replace("&eacute;", "é").replace("&rsquo;", "'").replace("&shy;", "");
    }
}