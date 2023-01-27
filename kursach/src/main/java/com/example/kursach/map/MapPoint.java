package com.example.kursach.map;

import com.example.kursach.GamePanel;
import com.example.kursach.players.*;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;

import static java.lang.Math.pow;

public class MapPoint {

    private static int numberOfPoints = 0;

    private static final Image POINT_IMAGE_RED = new Image(new File("src/main/java/images/MapPoint0002.png").toURI().toString());
    private static final Image POINT_IMAGE_BLUE = new Image(new File("src/main/java/images/MapPoint0022.png").toURI().toString());
    private static final Image POINT_IMAGE_GREY = new Image(new File("src/main/java/images/MapPoint0023.png").toURI().toString());

    private boolean isBase;

    private int health = 1000;

    protected int posX;
    protected int posY;

    protected Map map;
    protected Status status;

    protected TitledPane titledPane;
    protected VBox content;
    protected Rectangle rectangle;
    protected Group group;
    protected ImageView pointImage = new ImageView(POINT_IMAGE_RED);

    private int counter1;

    private HashSet<FighterImpl> myFighterImpls;

    {
        myFighterImpls = new HashSet<>();
    }

    public MapPoint() {
        numberOfPoints++;

        this.setPosX(new Random().nextInt(Map.getRootWidth()));
        this.setPosY(new Random().nextInt(Map.getRootHeight()));
        getImage();
        this.status = Status.NEUTRAL;
        draw();
        this.isBase = false;
    }

    public MapPoint(int posX, int posY) {
        numberOfPoints++;

        this.setPosX(posX);
        this.setPosY(posY);
        getImage();
        this.status = Status.NEUTRAL;
        draw();
        this.isBase = false;
    }

    public MapPoint(Status status, Map map) {
        numberOfPoints++;
        this.status = status;

        if (status == Status.RED) {
            this.posX = (int)Map.getBadBasePoint().getX();
            this.posY = (int)Map.getBadBasePoint().getY();
        }
        if (status == Status.BLUE) {
            this.posX = (int)Map.getGoodBasePoint().getX();
            this.posY = (int)Map.getGoodBasePoint().getY();
        }
        getImage();

        draw();
        setHealth(10_000);
        this.isBase = true;
        this.map = map;
        this.myFighterImpls = new HashSet<>();
    }

    public void addFighter(FighterImpl fighterImpl) {
        myFighterImpls.add(fighterImpl);
        fighterImpl.setPos(new Point2D(posX, posY));
    }

    public void delFighter(FighterImpl fighterImpl) {
        myFighterImpls.remove(fighterImpl);
        fighterImpl.setPos(new Point2D(posX, posY));
    }

    public void getImage() {
        if (Status.RED.equals(status))
            pointImage.setImage(POINT_IMAGE_RED);
        else if (Status.BLUE.equals(status))
            pointImage.setImage(POINT_IMAGE_BLUE);
        else
            pointImage.setImage(POINT_IMAGE_GREY);

        pointImage.setPreserveRatio(true);
        pointImage.setFitHeight(200);
    }

    public void draw() {

        Rectangle aura = new Rectangle(200, 200);
        aura.setFill(Color.LIGHTGREEN);
        this.rectangle = aura;

        titledPane = new TitledPane();

        content = new VBox();

        titledPane.setContent(content);
        titledPane.setMinWidth(100);
        titledPane.setMinHeight(50);
        titledPane.setExpanded(false);

        VBox root= new VBox();
        root.setPadding(new Insets(5));
        root.getChildren().add(titledPane);

        this.group = new Group(rectangle, pointImage, root);

        root.setLayoutX(47);
        root.setLayoutY(135);

        root.setOnMouseEntered(event -> titledPane.setExpanded(true));
        root.setOnMouseExited(event -> titledPane.setExpanded(false));

        rectangle.relocate(0, 0);
        group.setLayoutX(posX);
        group.setLayoutY(posY);
    }

    public void update(GamePanel gp) {
        counter1 = myFighterImpls.size() / 2;
        gp.getFighters()
                .stream()
                .filter(x-> !x.isAlive())
                .forEach(x-> myFighterImpls.remove(x));

        capture(gp.getFighters());
//        support();
    }

    public void capture(ArrayList<FighterImpl> fighterImpls) {
        content.getChildren().removeAll(content.getChildren());

        int counter = 0;
        for(FighterImpl x: fighterImpls) {

            if (pow(x.getPosX() - this.posX, 2) + pow(x.getPosY() - this.posY, 2)
                    <= pow(x.getFist().getDistanceToAttack(), 2)) {

                counter++;
                if (content.getChildren().stream().noneMatch(x::equals)) {
                    content.getChildren().add(x.getNameText());
                }

                if (status != x.getSide()) {
                    health -= x.getFist().getDamageGive();

                    if (health <= 0) {
                        x.getSide().setExperience(x.getSide().getExperience() + 500);
                        this.status = x.getSide();
                        this.health = 1000;
                        getImage();
                    }
                } else {
                    if (status == x.getSide() && health <= 1000) health += x.getFist().getDamageGive();
                }
            }
        }
        titledPane.setText("Гравців: " + counter);
    }

    public void support() {
        FighterImpl temp;
        if (isBase && status.getExperience() >= 2000) {
            if (status.equals(Status.RED)) {
                temp = new Authority(Status.BLUE);
                status.setExperience(Status.BLUE.getExperience() - 2000);
            } else {
                temp = new Authority(Status.RED);
                status.setExperience(Status.RED.getExperience() - 2000);
            }
            map.addNewFighter(temp);
        }
        if (isBase && status.getExperience() >= 500  && counter1 > 0) {
            counter1--;
            if (status.equals(Status.RED)) {
                temp = new Veteran(Status.BLUE);
                status.setExperience(Status.BLUE.getExperience() - 500);
            } else {
                temp = new Veteran(Status.RED);
                status.setExperience(Status.RED.getExperience() - 500);
            }
            map.addNewFighter(temp);
        }
    }

    public Status getStatus() {
        return status;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public void setPosX(int posX) {
        this.posX = posX;
    }

    public void setPosY(int posY) {
        this.posY = posY;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public Group getGroup() {
        return group;
    }

    public boolean isBase() {
        return isBase;
    }

    public static int getNumberOfPoints() {
        return numberOfPoints;
    }

    public static void setNumberOfPoints(int numberOfShips) {
        MapPoint.numberOfPoints = numberOfShips;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + getStatus();
    }

    public void saveData(FileWriter fileWriter) {

        try {
            fileWriter.write(Double.toString(health));
            fileWriter.write("\n");
            fileWriter.write(Status.toString(status));
            fileWriter.write("\n");
            fileWriter.write(Double.toString(posX));
            fileWriter.write("\n");
            fileWriter.write(Double.toString(posY));
            fileWriter.write("\n");
            fileWriter.write(Boolean.toString(isBase));
            fileWriter.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openData(BufferedReader bufferedReader) throws IOException {
        String text;

        text = bufferedReader.readLine();
        setHealth((int)Double.parseDouble(text));
        text = bufferedReader.readLine();

        if (text.equals("RED"))
            status = Status.RED;
        else if (text.equals("BLUE"))
            status = Status.BLUE;
        else
            status = Status.NEUTRAL;

        text = bufferedReader.readLine();
        posX = (int)Double.parseDouble(text);
        text = bufferedReader.readLine();
        posY = (int)Double.parseDouble(text);
        text = bufferedReader.readLine();
        isBase = text.equals("true");
        if (isBase) setHealth(10_000);
        getImage();
        draw();
    }
}
