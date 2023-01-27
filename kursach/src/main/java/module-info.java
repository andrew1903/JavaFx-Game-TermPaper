module com.example.kursach {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;


    opens com.example.kursach to javafx.fxml;
    exports com.example.kursach;
    exports com.example.kursach.players;
    opens com.example.kursach.players to javafx.fxml;
    exports com.example.kursach.map;
    opens com.example.kursach.map to javafx.fxml;
}