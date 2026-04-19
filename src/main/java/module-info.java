module com.example.quizjavaescape {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.quizjavaescape to javafx.fxml;
    exports com.example.quizjavaescape;
}