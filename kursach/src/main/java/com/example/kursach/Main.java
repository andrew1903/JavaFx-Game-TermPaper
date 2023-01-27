package com.example.kursach;

import com.example.kursach.map.Map;
import com.example.kursach.map.MapPoint;
import com.example.kursach.players.*;
import javafx.animation.AnimationTimer;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.application.Application;
import java.awt.Robot;
import java.awt.*;
import java.io.*;
import java.util.*;

public class Main extends Application {

    final KeyCombination FullScreenKeyCombo =
            new KeyCodeCombination(KeyCode.ENTER, KeyCombination.ALT_ANY);
    private final Map map = new Map(3);
    private final GamePanel gamePanel = new GamePanel(map);
    private Group visibleAreaMiniMap;
    private ScrollPane scrollPane;
    private VBox vBox;
    private Scene scene;
    private AnchorPane anchorPane;

    private final HashMap<FighterImpl, Rectangle> mapMiniObj = new HashMap<>();
    private final HashMap<MapPoint, Rectangle> mapMacroObj = new HashMap<>();

    private void initialMiniMapMiniObj(AnchorPane pane) {

        for (FighterImpl x: gamePanel.getFighters()) {

            Rectangle box = new Rectangle(10, 10);
            box.setFill(Color.YELLOW);
            pane.getChildren().add(box);
            mapMiniObj.put(x, box);
        }
    }

    public void addMiniMapMiniObj(AnchorPane pane, FighterImpl fighterImpl) {

        Rectangle box = new Rectangle(10, 10);
        box.setFill(Color.YELLOW);
        pane.getChildren().add(box);
        mapMiniObj.put(fighterImpl, box);
    }

    public void removeMiniMapMiniObj(AnchorPane pane, FighterImpl fighterImpl) {

        Rectangle box = mapMiniObj.get(fighterImpl);
//        box.setVisible(false);
        pane.getChildren().remove(box);
        mapMiniObj.remove(fighterImpl);

    }

    public void initialMiniMapMacroObj(AnchorPane pane) {

        for (MapPoint x: gamePanel.getPointMaps()) {

            Rectangle box = new Rectangle(20, 20);
            if (x.getStatus().equals(Status.RED))
                box.setFill(Color.RED);
            if (x.getStatus().equals(Status.BLUE))
                box.setFill(Color.BLUE);
            if (x.getStatus().equals(Status.NEUTRAL))
                box.setFill(Color.GREY);
            pane.getChildren().add(box);
            mapMacroObj.put(x, box);
        }
    }

    public void addMacroMapObj(AnchorPane pane, MapPoint mapPoint) {

        Rectangle box = new Rectangle(20, 20);
        if (mapPoint.getStatus().equals(Status.RED))
            box.setFill(Color.RED);
        if (mapPoint.getStatus().equals(Status.BLUE))
            box.setFill(Color.BLUE);
        if (mapPoint.getStatus().equals(Status.NEUTRAL))
            box.setFill(Color.GREY);
        pane.getChildren().add(box);
        mapMacroObj.put(mapPoint, box);
    }

    public void removeMacroMapMacroObj(AnchorPane pane, MapPoint mapPoint) {

        Rectangle box = mapMacroObj.get(mapPoint);
        pane.getChildren().remove(box);
        mapMacroObj.remove(mapPoint);
    }

    @Override
    public void start(Stage stage) {
        anchorPane = null;
        System.out.println("Початок роботи");

        try {
            FXMLLoader loader = new FXMLLoader(new File("src/main/java/com/example/kursach/Sample.fxml").toURI().toURL());
            Pane root = loader.load();

            MenuBar menuBar = (MenuBar) root.getChildren().get(3);

            Menu menu1 = menuBar.getMenus().get(0);
            MenuItem item1 = menu1.getItems().get(0);
            MenuItem item2 = menu1.getItems().get(1);
            MenuItem item3 = menu1.getItems().get(2);

            Menu menu2 = menuBar.getMenus().get(1);
            MenuItem item4 = menu2.getItems().get(0);
            MenuItem item5 = menu2.getItems().get(1);


            scrollPane = (ScrollPane) root.getChildren().get(0);
            scrollPane.setContent(Map.getRoot());
            vBox = (VBox) root.getChildren().get(2);
            anchorPane = (AnchorPane) root.getChildren().get(1);
            Rectangle rectangle = (Rectangle) anchorPane.getChildren().get(0);
            rectangle.setFill(new ImagePattern(Map.MAP_IMAGE));
            Line leftBorder = new Line();
            leftBorder.setStroke(Color.YELLOW);
            leftBorder.setStartX(0);
            leftBorder.setStartY(0);
            leftBorder.setEndX(0);
            leftBorder.setStartY(GamePanel.SCREEN_HEIGHT * GamePanel.SCALE);
            Line topBorder = new Line();
            topBorder.setStroke(Color.YELLOW);
            topBorder.setStartX(0);
            topBorder.setStartY(0);
            topBorder.setEndX(GamePanel.SCREEN_WIDTH * GamePanel.SCALE);
            topBorder.setEndY(0);
            Line rightBorder = new Line();
            rightBorder.setStroke(Color.YELLOW);
            rightBorder.setStartX(GamePanel.SCREEN_WIDTH * GamePanel.SCALE);
            rightBorder.setStartY(0);
            rightBorder.setEndX(GamePanel.SCREEN_WIDTH * GamePanel.SCALE);
            rightBorder.setEndY(GamePanel.SCREEN_HEIGHT * GamePanel.SCALE);
            Line bottomBorder = new Line();
            bottomBorder.setStroke(Color.YELLOW);
            bottomBorder.setStartX(0);
            bottomBorder.setStartY(GamePanel.SCREEN_HEIGHT * GamePanel.SCALE);
            bottomBorder.setEndX(GamePanel.SCREEN_WIDTH * GamePanel.SCALE);
            bottomBorder.setEndY(GamePanel.SCREEN_HEIGHT * GamePanel.SCALE);
            visibleAreaMiniMap = new Group(leftBorder,topBorder,rightBorder,bottomBorder);
            anchorPane.getChildren().add(visibleAreaMiniMap);

            item1.setOnAction(event-> saveGame());
            item2.setOnAction(event-> openSave(this));
            item3.setOnAction(event-> System.exit(0));
            item4.setOnAction(event-> {
                try {
                    Robot r = new Robot();

                    r.keyPress(java.awt.event.KeyEvent.VK_INSERT);
                    r.keyRelease(java.awt.event.KeyEvent.VK_INSERT);
                } catch (AWTException e) {
                    e.printStackTrace();
                }
            });
            item5.setOnAction(event-> {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("INFO ABOUT FIGHTERS");
                alert.setHeaderText("In this window you can see information about the fighters sorted by number of lives.");
                map.getFighters().sort(FighterComparator.healthComparator);
                StringBuilder sb = new StringBuilder();
                map.getFighters().forEach(x-> sb.append(x.toStringg()).append("\n"));
                alert.setContentText(String.valueOf(sb));
                alert.getDialogPane().setMinWidth(800);
                alert.show();
            });

            rectangle.setOnMouseClicked(event -> {
                scrollPane.setHvalue((event.getX() - (GamePanel.SCREEN_WIDTH * GamePanel.SCALE)/2)/(GamePanel.SCALE*GamePanel.MAP_WIDTH- (GamePanel.SCREEN_WIDTH * GamePanel.SCALE)));
                scrollPane.setVvalue((event.getY() - (GamePanel.SCREEN_HEIGHT * GamePanel.SCALE)/2)/(GamePanel.SCALE*GamePanel.MAP_HEiGHT- (GamePanel.SCREEN_HEIGHT * GamePanel.SCALE)));
            });
            scene = new Scene(root);
        } catch (IOException ex) {
            System.out.println("Error" + ex);
        }

        scene.setOnKeyPressed(new KeyPressedHandler());

        stage.setMaximized(true);
        stage.setScene(scene);
        stage.setTitle("Grand Bazar");

        stage.setFullScreen(true);
        stage.setResizable(false);
        stage.setFullScreenExitKeyCombination(KeyCombination.keyCombination("ALT+ENTER"));

        initialMiniMapMacroObj(anchorPane);
        initialMiniMapMiniObj(anchorPane);

        AnchorPane finalAnchorPane = anchorPane;
        scene.addEventHandler(KeyEvent.KEY_PRESSED, event ->{
//            if(FullScreenKeyCombo.match(event)) {
//                stage.setFullScreen(!stage.isFullScreen());
//            }
            FighterImpl fighterImpl;
            switch (event.getCode()) {
                case DIGIT1 -> {
                    fighterImpl = new FighterImpl();
                    assert finalAnchorPane != null;
                    addMiniMapMiniObj(finalAnchorPane, fighterImpl);
                    map.addNewFighter(fighterImpl);
                }
                case DIGIT2 -> {
                    fighterImpl = new Veteran();
                    assert finalAnchorPane != null;
                    addMiniMapMiniObj(finalAnchorPane, fighterImpl);
                    map.addNewFighter(fighterImpl);
                }
                case DIGIT3 -> {
                    fighterImpl = new Authority();
                    assert finalAnchorPane != null;
                    addMiniMapMiniObj(Objects.requireNonNull(finalAnchorPane), fighterImpl);
                    map.addNewFighter(fighterImpl);
                }
                case DIGIT4 -> {
                    MapPoint mapPoint = new MapPoint(
                            new Random().nextInt(GamePanel.SCREEN_WIDTH),
                            new Random().nextInt(GamePanel.SCREEN_HEIGHT));
                    addMacroMapObj(finalAnchorPane, mapPoint);
                    map.addNewMapPoint(mapPoint);
                }
                case DIGIT5 -> {
                    try {
                        int rnd = new Random().nextInt(gamePanel.getFighters().size());
                        assert finalAnchorPane != null;
                        removeMiniMapMiniObj(finalAnchorPane, gamePanel.getFighters().get(rnd));
                        map.deleteFighter(gamePanel.getFighters().get(rnd));
                    } catch (IllegalArgumentException ex) {
                        System.out.println("Всі гравці видалені!");
                    }
                }
                case DIGIT6 -> {
                    try {
                        if (map.getMapPoints().size() == 2) throw new IllegalArgumentException();
                        int rnd = 0;
                        assert finalAnchorPane != null;
                        removeMacroMapMacroObj(finalAnchorPane, gamePanel.getPointMaps().get(rnd));
                        map.deleteMapPoint(gamePanel.getPointMaps().get(rnd));

                    } catch (IllegalArgumentException ex) {
                        System.out.println("Всі точки видалені!");
                    }
                }
                case F -> map.getFighters().forEach(AbstractFighter::setToBase);
            }
        });

        new AnimationTimer() {
            @Override
            public void handle(long now) {

                mapMiniObj.forEach((fighter,rectangle) -> {
                    if (!fighter.isAlive()){
                        rectangle.setLayoutX(0);
                        rectangle.setLayoutY(0);
                        rectangle.setVisible(false);
                        return;
                    }

                    if (fighter.getSide().equals(Status.RED))
                        rectangle.setFill(Color.RED);
                    if (fighter.getSide().equals(Status.BLUE))
                        rectangle.setFill(Color.BLUE);

                    rectangle.setLayoutX(fighter.getPosX() * GamePanel.SCALE + 1099);
                    rectangle.setLayoutY(fighter.getPosY() * GamePanel.SCALE + 5);
                });

                mapMacroObj.forEach((mapPoint,rectangle) -> {
                    if (mapPoint.getStatus().equals(Status.RED))
                        rectangle.setFill(Color.RED);
                    if (mapPoint.getStatus().equals(Status.BLUE))
                        rectangle.setFill(Color.BLUE);
                    if (mapPoint.getStatus().equals(Status.NEUTRAL))
                        rectangle.setFill(Color.GREY);

                    rectangle.setLayoutX(mapPoint.getPosX() * GamePanel.SCALE + 1099);
                    rectangle.setLayoutY(mapPoint.getPosY() * GamePanel.SCALE + 5);
                });

                int blueTeamCounter = 0;
                int redTeamCounter = 0;
                int bluePointsCounter = 0;
                int redPointsCounter = 0;
                for (FighterImpl x: map.getFighters()) {
                    if (x.getSide().equals(Status.RED)) redTeamCounter++;
                    if (x.getSide().equals(Status.BLUE)) blueTeamCounter++;
                }

                for (MapPoint x: map.getMapPoints()) {
                    if (x.getStatus().equals(Status.RED)) redPointsCounter++;
                    if (x.getStatus().equals(Status.BLUE)) bluePointsCounter++;
                }

                Label label1 = (Label) vBox.getChildren().get(0);
                label1.setText(String.format("Players BLUE Team: %d", blueTeamCounter));
                Label label2 = (Label) vBox.getChildren().get(1);
                label2.setText(String.format("Players RED Team: %d", redTeamCounter));
                Label label3 = (Label) vBox.getChildren().get(2);
                label3.setText(String.format("Points BLUE Team: %d", bluePointsCounter));
                Label label4 = (Label) vBox.getChildren().get(3);
                label4.setText(String.format("Points RED Team: %d", redPointsCounter));

                gamePanel.update(map);

                visibleAreaMiniMap.setLayoutX((scrollPane.getHvalue() * GamePanel.MAP_WIDTH * GamePanel.SCALE - (scrollPane.getHvalue() * (GamePanel.SCREEN_WIDTH * GamePanel.SCALE))) + GamePanel.SCREEN_WIDTH + 20);
                visibleAreaMiniMap.setLayoutY((scrollPane.getVvalue() * GamePanel.MAP_HEiGHT * GamePanel.SCALE - (scrollPane.getVvalue() * (GamePanel.SCREEN_HEIGHT * GamePanel.SCALE))) + 5);

            }
        }.start();
        stage.show();
    }

    private void saveGame() {
        SavedGame.serializeNow(getScene().getWindow(), map);
    }

    private void openSave(Main main) {
        SavedGame.deserializeNow(getScene().getWindow(), map, main);
    }

    private class KeyPressedHandler implements EventHandler<KeyEvent> {
        @Override
        public void handle(KeyEvent event) {
            // Actions
            switch (event.getCode()) {
                case W -> gamePanel.getFighters()
                        .stream()
                        .filter(x -> !x.getIsActive())
                        .forEach(FighterImpl::moveUp);
                case A -> gamePanel.getFighters()
                        .stream()
                        .filter(x -> !x.getIsActive())
                        .forEach(FighterImpl::moveLeft);
                case S -> gamePanel.getFighters()
                        .stream()
                        .filter(x -> !x.getIsActive())
                        .forEach(FighterImpl::moveDown);
                case D -> gamePanel.getFighters()
                        .stream()
                        .filter(x -> !x.getIsActive())
                        .forEach(FighterImpl::moveRight);
                case ESCAPE -> gamePanel.getFighters()
                        .stream()
                        .filter(x -> !x.getIsActive())
                        .forEach(FighterImpl::switchActivation);
                case DELETE -> gamePanel.getFighters()
                        .stream()
                        .filter(x -> !x.getIsActive())
                        .forEach(FighterImpl::switchAlive);
                case ENTER -> gamePanel.getFighters()
                        .forEach(FighterImpl::switchActivation);
                case INSERT -> {
                    gamePanel.getFighters()
                            .stream()
                            .filter(FighterImpl::getIsActive)
                            .forEach(FighterImpl::switchActivation);
                    newWindow();
                }
                case K -> {
                    gamePanel.getFighters()
                            .stream()
                            .filter(FighterImpl::getIsActive)
                            .forEach(FighterImpl::switchActivation);
                    captainWindow();
                }
            }
        }

        private void captainWindow() {
            try {
                FXMLLoader loader = new FXMLLoader(new File("src/main/java/com/example/kursach/CaptainGenerator.fxml").toURI().toURL());
                Pane root = loader.load();
                CaptainGeneratorController fxg = loader.getController();
                fxg.setter(gamePanel.getFighters(), gamePanel.getMap());
                Scene scene = new Scene(root);
                Stage window = new Stage();
                window.setTitle("Вибір капітанів");
                window.setScene(scene);
                window.show();
            } catch (IOException ex) {
                System.out.println("Error" + ex);
            }

        }

        public void newWindow() {
            try {
                FXMLLoader loader = new FXMLLoader(new File("src/main/java/com/example/kursach/FightersGenerator.fxml").toURI().toURL());
                Pane root = loader.load();
                FightersGeneratorController fxg = loader.getController();
                fxg.setter(gamePanel.getPointMaps(), gamePanel.getMap());
                Scene scene = new Scene(root);
                Stage window = new Stage();
                window.setTitle("Створенння бійця");
                window.setScene(scene);
                window.show();
            } catch (IOException ex) {
                System.out.println("Error" + ex);
            }
        }
    }

    public AnchorPane getAnchorPane() {
        return anchorPane;
    }

    public Scene getScene() {
        return scene;
    }

    public static void main(String[] args) {
        launch();
    }
}