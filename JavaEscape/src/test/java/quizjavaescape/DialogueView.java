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

public class DialogueView {

    private static final String IMAGE_CHEF = "/quizjavaescape/chef.png";

    private static final String[] TEXTES_INTRO = {
            "Écoute-moi bien. Une bombe a été placée quelque part en ville, et tout repose sur toi. Nous n'avons pas de temps à perdre. Chaque seconde compte.",
            "Voici la situation : tu vas devoir résoudre une série d'énigmes. Chacune te donnera des indices pour localiser la bombe.",
            "Je sais que ce n'est pas facile, mais je crois en toi. On compte sur toi. La ville compte sur toi."
    };

    private static final String[] TEXTES_AVANT_MASTERMIND = {
            "Tu as trouvé l'emplacement de la bombe. C'est un soulagement, mais ne te repose pas encore. Le plus difficile reste à venir.",
            "Nous savons maintenant où elle se trouve, mais il faut encore la désamorcer. C'est une course contre la montre.",
            "Si tu échoues à désactiver la bombe, tout est fini. Mais je sais que tu as ce qu'il faut pour y arriver.",
            "Bonne chance. Et rappelle-toi, le destin de tout le monde est entre tes mains."
    };

    private static final String[] TEXTES_VICTOIRE = {
            "Tu l'as fait... Tu as réussi à désamorcer la bombe et à sauver la ville. Je savais que tu en étais capable.",
            "Grâce à toi, des vies ont été sauvées aujourd'hui. Tu as fait preuve de courage, d'intelligence et de détermination.",
            "Bien joué, vraiment. Tu as prouvé qu'il n'y a rien que tu ne puisses accomplir. Je n'oublierai jamais ce jour."
    };

    private static final String[] TEXTES_DEFAITE = {
            "L'échec est total. La bombe a explosé.",
            "Le Chef, qui avait placé toute sa confiance en toi, est mort dans l'explosion.",
            "La ville a été détruite. Des vies ont été perdues. Tout est fini.",
            "On est tous très déçus..."
    };

    public static void afficher(Stage stage, String type) {
        String[] textes;
        switch (type) {
            case "AVANT_MASTERMIND": textes = TEXTES_AVANT_MASTERMIND; break;
            case "VICTOIRE": textes = TEXTES_VICTOIRE; break;
            case "DEFAITE": textes = TEXTES_DEFAITE; break;
            default: textes = TEXTES_INTRO; break;
        }

        VBox root = new VBox(25);
        root.setAlignment(Pos.CENTER);
        root.setPadding(new Insets(40));
        root.setStyle("-fx-background-color: #1a1a2e;");

        Label titre = new Label("💣 JAVA ESCAPE");
        titre.setStyle("-fx-font-size: 26px; -fx-font-weight: bold; -fx-text-fill: #e0e0e0;");

        ImageView photoChef = new ImageView();
        try {
            photoChef.setImage(new Image(DialogueView.class.getResourceAsStream(IMAGE_CHEF)));
        } catch (Exception e) {}
        photoChef.setFitWidth(220);
        photoChef.setPreserveRatio(true);

        VBox conteneurImage = new VBox(photoChef);
        conteneurImage.setStyle("-fx-border-color: #4a90d9; -fx-border-width: 3; -fx-border-radius: 15;");

        Label labelNom = new Label("👮 CHEF");
        labelNom.setStyle("-fx-font-size: 20px; -fx-font-weight: bold; -fx-text-fill: #4a90d9;");

        Label labelTexte = new Label();
        labelTexte.setStyle("-fx-font-size: 18px; -fx-text-fill: #e0e0e0;");
        labelTexte.setWrapText(true);
        labelTexte.setMinHeight(120);

        VBox zoneTexte = new VBox(15, labelNom, labelTexte);
        HBox.setHgrow(zoneTexte, Priority.ALWAYS);

        HBox boiteDialogue = new HBox(30, conteneurImage, zoneTexte);
        boiteDialogue.setAlignment(Pos.CENTER);
        boiteDialogue.setStyle("-fx-background-color: #16213e; -fx-border-color: #4a90d9; -fx-border-width: 2; -fx-border-radius: 15; -fx-padding: 30;");
        boiteDialogue.setMaxWidth(850);

        Button boutonSuivant = new Button("Suivant ▶");
        boutonSuivant.setStyle("-fx-background-color: #4a90d9; -fx-text-fill: white; -fx-font-weight: bold; -fx-padding: 10 25; -fx-background-radius: 8;");
        boutonSuivant.setVisible(false);

        root.getChildren().addAll(titre, boiteDialogue, boutonSuivant);

        int[] indexDialogue = {0};
        Timeline[] timeline = {null};

        Runnable avancer = () -> {
            if (timeline[0] != null && timeline[0].getStatus() == Timeline.Status.RUNNING) {
                timeline[0].stop();
                labelTexte.setText(textes[indexDialogue[0]]);
                boutonSuivant.setVisible(true);
            } else {
                indexDialogue[0]++;
                if (indexDialogue[0] < textes.length) {
                    lancerAnimation(textes[indexDialogue[0]], labelTexte, boutonSuivant, timeline);
                } else {
                    redirectionFinDialogue(stage, type);
                }
            }
        };

        boutonSuivant.setOnAction(e -> avancer.run());
        Scene scene = new Scene(root, 950, 650);
        scene.setOnKeyPressed(e -> { if (e.getCode() == KeyCode.SPACE) avancer.run(); });

        stage.setScene(scene);
        lancerAnimation(textes[0], labelTexte, boutonSuivant, timeline);
    }

    private static void lancerAnimation(String texte, Label label, Button btn, Timeline[] tl) {
        label.setText("");
        btn.setVisible(false);
        final int[] i = {0};
        tl[0] = new Timeline(new KeyFrame(Duration.millis(30), e -> {
            label.setText(texte.substring(0, i[0] + 1));
            i[0]++;
        }));
        tl[0].setCycleCount(texte.length());
        tl[0].setOnFinished(e -> btn.setVisible(true));
        tl[0].play();
    }

    private static void redirectionFinDialogue(Stage stage, String type) {
        if (type.equals("INTRO")) {
            QuizView.afficher(stage, 0);
        } else if (type.equals("AVANT_MASTERMIND")) {
            try {
                new MastermindGame.MastermindGame().start(stage);
            } catch (Exception e) { e.printStackTrace(); }
        } else if (type.equals("VICTOIRE") || type.equals("DEFAITE")) {
            afficherEcranFinal(stage, type.equals("VICTOIRE"));
        }
    }

    private static void afficherEcranFinal(Stage stage, boolean estVictoire) {
        VBox root = new VBox(30);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: " + (estVictoire ? "#1a1a2e;" : "#2e1a1a;"));

        Label emoji = new Label(estVictoire ? "🎉" : "💥");
        emoji.setStyle("-fx-font-size: 100px;");

        Label titre = new Label(estVictoire ? "MISSION RÉUSSIE !" : "GAME OVER");
        titre.setStyle("-fx-font-size: 50px; -fx-font-weight: bold; -fx-text-fill: " + (estVictoire ? "#00ff00;" : "#ff4d4d;"));

        Label message = new Label(estVictoire ? "Vous avez sauvé la ville !" : "La bombe a explosé... Tout est perdu.");
        message.setStyle("-fx-font-size: 20px; -fx-text-fill: white;");

        Button btnQuitter = new Button("Quitter le jeu");
        btnQuitter.setStyle("-fx-background-color: #e94560; -fx-text-fill: white; -fx-padding: 10 30;");
        btnQuitter.setOnAction(e -> stage.close());

        root.getChildren().addAll(emoji, titre, message, btnQuitter);
        stage.setScene(new Scene(root, 950, 650));
    }
}