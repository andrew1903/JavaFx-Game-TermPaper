package com.example.kursach;

import com.example.kursach.map.Map;
import com.example.kursach.players.Authority;
import com.example.kursach.players.FighterImpl;
import com.example.kursach.players.Veteran;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CaptainGeneratorController implements Initializable {

    @FXML
    public ComboBox<String> fCaptain;
    public ComboBox<String> sCaptain;
    public ComboBox<String> tCaptain;

    private Map map;
    private ArrayList<FighterImpl> fighterImplArray;

    public void setter(ArrayList<FighterImpl> fighterImplArray, Map map) {
        fighterImplArray
                .forEach(x-> fCaptain.getItems().add(String.valueOf(x)));

        fighterImplArray
                .forEach(x-> sCaptain.getItems().add(String.valueOf(x)));

        fighterImplArray
                .forEach(x-> tCaptain.getItems().add(String.valueOf(x)));

        this.fighterImplArray = fighterImplArray;
        this.map = map;
    }

    @FXML
    public void closeWindow(InputEvent event) {
        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    private void error(String messageError) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(messageError);
        alert.showAndWait();
    }

    @FXML
    public void run(InputEvent event){

        if (fCaptain.getValue() == null || fCaptain.getValue().isEmpty())
            error("Виберіть капітана першої команди!");
        else if (sCaptain.getValue() == null || sCaptain.getValue().isEmpty())
            error("Виберіть капітана другої команди!");
        else if (tCaptain.getValue() == null || tCaptain.getValue().isEmpty())
            error("Виберіть капітана третьої команди!");
        else {
            FighterImpl fighter_Impl_1 = fighterImplArray.stream()
                    .filter(x -> x.toString()
                            .equals(fCaptain.getValue()))
                    .findAny()
                    .get();

            Veteran fighter_2 = (Veteran) fighterImplArray.stream()
                    .filter(x -> x.toString()
                            .equals(sCaptain.getValue()))
                    .findAny()
                    .get();

            Authority fighter_3 = (Authority) fighterImplArray.stream()
                    .filter(x -> x.toString()
                            .equals(tCaptain.getValue()))
                    .findAny()
                    .get();

            if (fighter_Impl_1.equals(fighter_2) ||
                    fighter_2.equals(fighter_3) ||
                    fighter_Impl_1.equals(fighter_3)) {
                error("Виберіть різні мікрооб'єкти!");
            } else if (!(fighter_3 instanceof Authority)) {
                error("Капітаном третьої команди повинен бути об'єкт класу Authority!");
            } else if (!(fighter_2 instanceof Veteran)) {
                error("Капітаном другої команди повинен бути об'єкт класу Veteran!");
            } else if (!(fighter_Impl_1 instanceof FighterImpl)) {
                error("Капітаном першої команди повинен бути об'єкт класу Figther!");
            } else {
                fighter_Impl_1.setCaptain();
                fighter_2.setCaptain();
                fighter_3.setCaptain();

                final Node source = (Node) event.getSource();
                final Stage stage = (Stage) source.getScene().getWindow();
                stage.close();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
