package com.example.kursach;

import com.example.kursach.map.Map;
import com.example.kursach.map.MapPoint;
import com.example.kursach.players.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.InputEvent;
import javafx.stage.Stage;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;
import java.util.ResourceBundle;

public class FightersGeneratorController implements Initializable {

    @FXML
    public TextField name;
    public TextField posY;
    public TextField posX;
    public Button create;
    public Button cancel;
    public ComboBox<String> level;
    public ComboBox<String> points;
    public ComboBox<String> status;
    public CheckBox isActive;

    private Map map;
    private ArrayList<MapPoint> mapPointArray;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        level.getItems().addAll("Fighter", "Veteran", "Authority");
        status.getItems().addAll("RED", "BLUE");
    }

    @FXML
    public void closeWindow(InputEvent event) {
        final Node source = (Node) event.getSource();
        final Stage stage = (Stage) source.getScene().getWindow();
        stage.close();
    }

    public void setter(ArrayList<MapPoint> mapPointArray, Map map) {
        mapPointArray
                .forEach(x-> points.getItems().add(String.valueOf(x)));

        this.mapPointArray = mapPointArray;
        this.map = map;
    }

    @FXML
    public void print(InputEvent event){

        if (name.getText() == null || name.getText().isEmpty()) {
            error("Вкажіть ім'я бійця!");
        } else if (this.status.getValue() == null || status.getValue().isEmpty()) {
            error("Вкажіть команду бійця!");
        } else if (level.getValue() == null || level.getValue().isEmpty()) {
            error("Вкажіть левел бійця!");
        } else if (points.getValue() == null || points.getValue().isEmpty()) {
            error("Вкажіть базу бійця бійця!");
        } else {

            String Name = name.getText();
            String X = posX.getText();
            String Y = posY.getText();

            int posX, posY;
            boolean isActive;

            Status side = Status.get(this.status.getValue()).get();

            try {
                posX = Integer.parseInt(X);
            } catch (NumberFormatException e) {
                posX = new Random().nextInt(4000);
            }
            try {
                posY = Integer.parseInt(Y);
            } catch (NumberFormatException e) {
                posY = new Random().nextInt(2000);
            }
            isActive = this.isActive.isSelected();
            if (name.getText().isEmpty()) {
                Name = Map.names[new Random().nextInt(Map.names.length)];
            }
            MapPoint point = mapPointArray.stream()
                    .filter(x -> x.toString()
                            .equals(points.getValue()))
                    .findAny()
                    .get();

//        for (int i = 0; i<count; ++i){
            FighterImpl newFighterImpl = null;

            switch (level.getValue()) {
                case "Fighter" -> newFighterImpl = new FighterImpl(side, point, Name, posX, posY, isActive);
                case "Veteran" -> newFighterImpl = new Veteran(side, point, Name, posX, posY, isActive);
                case "Authority" -> newFighterImpl = new Authority(side, point, Name, posX, posY, isActive);
            }
            map.addNewInsertFighter(newFighterImpl);
            point.addFighter(newFighterImpl);

            final Node source = (Node) event.getSource();
            final Stage stage = (Stage) source.getScene().getWindow();
            stage.close();
        }
    }


    private void error(String messageError) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("ERROR");
        alert.setHeaderText(messageError);
        alert.showAndWait();
    }

}
