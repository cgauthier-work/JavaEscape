module com.example.javaescape {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires java.compiler;

    opens MastermindGame to javafx.fxml;
    exports MastermindGame;
    opens quizjavaescape to javafx.fxml;
    exports quizjavaescape;

}