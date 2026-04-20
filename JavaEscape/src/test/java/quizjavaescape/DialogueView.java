package quizjavaescape;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import quizjavaescape.IntroView;

public class DialogueView {

    private static final String IMAGE_NAME = "chef.png";

    private static final String[] TEXTES_INTRO = {
            "Écoute-moi bien. Une bombe a été placée quelque part en ville, et tout repose sur toi. " +
                    "Nous n'avons pas de temps à perdre. Chaque seconde compte.",
            "Voici la situation : tu vas devoir résoudre une série d'énigmes. " +
                    "Le temps presse, mais nous avons encore une chance si tu agis avec précision.",
            "Je sais que ce n'est pas facile, mais je crois en toi. On compte sur toi. La ville compte sur toi."
    };

    private static final String[] TEXTES_INTERMEDIAIRE = {
            "Bien joué. Tu as résolu toutes les énigmes, et maintenant, " +
                    "nous avons une meilleure idée de l'endroit où la bombe pourrait être.",
            "Trouve cette bombe, et sauve tout le monde. Allez, il ne reste plus beaucoup de temps !"
    };

    public static void afficher(Stage stage, boolean intermediaire) {

        String[] textes = intermediaire ? TEXTES_INTERMEDIAIRE : TEXTES_INTRO;

        Label titre = new Label("💣 JAVA ESCAPE");
        titre.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #e0e0e0;");


        ImageView photoChef = new ImageView();
        try {

            Image img = new Image(DialogueView.class.getResourceAsStream(IMAGE_NAME));
            if (img.isError()) {
                // Si ça rate, on tente le chemin absolu depuis resources
                img = new Image(DialogueView.class.getResourceAsStream("/quizjavaescape/" + IMAGE_NAME));
            }
            photoChef.setImage(img);
        } catch (Exception e) {
            System.out.println("L'image est introuvable ou mal placée.");
        }

        photoChef.setFitWidth(220);
        photoChef.setPreserveRatio(true);

        VBox conteneurImage = new VBox(photoChef);
        conteneurImage.setStyle("-fx-border-color: #4a90d9; -fx-border-width: 3; -fx-border-radius: 15;");
        conteneurImage.setMinWidth(220);
        conteneurImage.setMinHeight(220);
        // ----------------------

        Label labelNom = new Label("👮 CHEF DE POLICE");
        labelNom.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #4a90d9;");

        Label labelTexte = new Label();
        labelTexte.setStyle("-fx-font-size: 18px; -fx-text-fill: #e0e0e0;");
        labelTexte.setWrapText(true);
        labelTexte.setMinHeight(120);

        VBox zoneTexte = new VBox(15, labelNom, labelTexte);
        zoneTexte.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(zoneTexte, Priority.ALWAYS);

        HBox boiteDialogue = new HBox(30, conteneurImage, zoneTexte);
        boiteDialogue.setAlignment(Pos.CENTER);
        boiteDialogue.setStyle("-fx-background-color: #16213e; -fx-border-color: #4a90d9; -fx-border-width: 2; -fx-border-radius: 15; -fx-padding: 30;");
        boiteDialogue.setMaxWidth(850);

        Button boutonSuivant = new Button("Suivant ▶");
        boutonSuivant.setStyle("-fx-background-color: #4a90d9; -fx-text-fill: white; -fx-font-size: 16px; -fx-font-weight: bold; -fx-padding: 12 35; -fx-background-radius: 10;");
        boutonSuivant.setVisible(false);

        VBox root = new VBox(25, titre, boiteDialogue, boutonSuivant);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #1a1a2e;");

        Scene scene = new Scene(root, 950, 650);

        int[] indexDialogue = {0};
        int[] indexLettre = {0};
        boolean[] animFinie = {false};
        String[] texteComplet = {""};
        Timeline[] timeline = {null};

        Runnable lancerAnimation = () -> {
            if (indexDialogue[0] >= textes.length) {
                if (intermediaire) afficherFin(stage);
                else QuizView.afficher(stage, 0);
                return;
            }
            texteComplet[0] = textes[indexDialogue[0]];
            indexLettre[0] = 0;
            animFinie[0] = false;
            labelTexte.setText("");
            boutonSuivant.setVisible(false);

            if (timeline[0] != null) timeline[0].stop();
            timeline[0] = new Timeline(new KeyFrame(Duration.millis(30), e -> {
                if (indexLettre[0] < texteComplet[0].length()) {
                    labelTexte.setText(texteComplet[0].substring(0, indexLettre[0] + 1));
                    indexLettre[0]++;
                }
            }));
            timeline[0].setCycleCount(texteComplet[0].length());
            timeline[0].setOnFinished(e -> {
                animFinie[0] = true;
                boutonSuivant.setVisible(true);
            });
            timeline[0].play();
        };

        Runnable avancer = () -> {
            if (!animFinie[0]) {
                if (timeline[0] != null) timeline[0].stop();
                labelTexte.setText(texteComplet[0]);
                animFinie[0] = true;
                boutonSuivant.setVisible(true);
            } else {
                indexDialogue[0]++;
                lancerAnimation.run();
            }
        };

        boutonSuivant.setOnAction(e -> avancer.run());
        scene.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.SPACE) avancer.run(); });

        stage.setScene(scene);
        lancerAnimation.run();
    }

    private static void afficherFin(Stage stage) {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(50));
        root.setStyle("-fx-background-color: #1a1a2e;");

        Label emoji = new Label("🎉");
        emoji.setStyle("-fx-font-size: 80px;");

        Label bravo = new Label("FÉLICITATIONS !");
        bravo.setStyle("-fx-font-size: 45px; -fx-font-weight: bold; -fx-text-fill: #00ff00;");

        Label message = new Label("Tu as désamorcé la bombe et sauvé la ville !");
        message.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

        Button btnRecommencer = new Button("🔄 Recommencer");
        btnRecommencer.setStyle("-fx-background-color: #4a90d9; -fx-text-fill: white; -fx-padding: 10 25; -fx-background-radius: 10;");
        btnRecommencer.setOnAction(e -> IntroView.afficher(stage));

        Button btnQuitter = new Button("❌ Quitter");
        btnQuitter.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-padding: 10 25; -fx-background-radius: 10;");
        btnQuitter.setOnAction(e -> stage.close());

        HBox boutons = new HBox(20, btnRecommencer, btnQuitter);
        boutons.setAlignment(Pos.CENTER);

        root.getChildren().addAll(emoji, bravo, message, boutons);
        stage.setScene(new Scene(root, 950, 650));
    }
}