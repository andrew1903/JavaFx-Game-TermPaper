package com.example.kursach.players;

import com.example.kursach.map.MapPoint;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import java.io.*;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public abstract class AbstractFighter implements Fighter, Cloneable {

    protected static final Image FIGHTER_IMAGE_RED = new Image(new File("src/main/java/images/Ryu_0002.png").toURI().toString());
    protected static final Image FIGHTER_IMAGE_BLUE = new Image(new File("src/main/java/images/Ryu_0022.png").toURI().toString());

    protected Random random = new Random();

    protected static int numberOfPoints = 0;

    protected final Set<Object> targets = new HashSet<>();
    protected final Set<FighterImpl> arrayOfOpponent = new HashSet<>();

    protected double posX;
    protected double posY;
    protected double tempPosX;
    protected double tempPosY;
    protected double health;
    protected int speed = 6;

    protected boolean toBase = false;
    protected boolean isActive = true;
    protected boolean isAlive = true;

    protected boolean isCaptain = false;
    protected boolean toCaptain = false;
    protected Long startTime;
    protected Object myTarget;

    protected String name;
    protected Status side;
    protected Group group;
    protected Fist fist;
    protected Text nameGraph;
    protected Text captainText;
    protected Line lineHealth;
    protected Rectangle rectActive;
    protected Rectangle captainRect;

    protected ImageView fighterImage = new ImageView(FIGHTER_IMAGE_RED);

    protected Point2D pointBase;
    protected Point2D pointTarget = new Point2D(0,0);

    protected MapPoint mapPoint;


    @Override
    public void setPos(Point2D point) {
        this.setPosX(point.getX());
        this.setPosY(point.getY());
        pointBase = new Point2D(posX, posY);
    }

    @Override
    public void draw() {

        nameGraph = new Text(name);
        nameGraph.setFont(new Font("Muna", 20));
        nameGraph.setFill(Color.WHITE);

        rectActive = new Rectangle(135,100);
        rectActive.setStrokeWidth(0);
        rectActive.setStroke(Color.YELLOW);
        rectActive.setFill(Color.TRANSPARENT);

        lineHealth = new Line();
        lineHealth.setStroke(Color.GREEN);
        lineHealth.setStrokeWidth(5);

        captainRect = new Rectangle(100,30);
        captainRect.setStrokeWidth(0);
        captainRect.setStroke(Color.BLACK);
        captainRect.setFill(Color.TRANSPARENT);

        captainText = new Text();
        captainText.setFont(new Font("Muna", 20));
        captainText.setFill(Color.TRANSPARENT);

        group = new Group(fighterImage, rectActive, lineHealth, nameGraph, captainRect, captainText);
        group.setOnMouseClicked(event -> {
            switchActivation();
            AbstractFighter ab;
            try {
                ab = this.clone();
                ab.toStrings();
            } catch (CloneNotSupportedException e) {
                e.printStackTrace();
            }
//            toStrings();
        });
    }

    @Override
    public void switchActivation() {
        isActive = !isActive;
        if (isActive) {
            rectActive.setStrokeWidth(0);
            rectActive.setStroke(Color.TRANSPARENT);
        } else {
            rectActive.setStrokeWidth(3);
            rectActive.setStroke(Color.RED);
        }
    }

    @Override
    public void switchAlive() {
        isActive = false;
        isAlive = false;
        group.setVisible(false);
    }

    @Override
    public void setFighterChord() {
        fighterImage.setX(0);
        fighterImage.setY(0);

        lineHealth.setStartX(0);
        lineHealth.setStartY(-10);
        lineHealth.setEndX(health * 1.4);
        lineHealth.setEndY(-10);

        nameGraph.setX(0);
        nameGraph.setY(-30);

        rectActive.setX(0);
        rectActive.setY(0);

        captainRect.setX(0);
        captainRect.setY(-75);

        captainText.setX(0);
        captainText.setY(-50);
    }

    @Override
    public void saveData(FileWriter fileWriter) {
        try {
            fileWriter.write(getSimpleNameClass());
            fileWriter.write("\n");
            fileWriter.write(Double.toString(posX));
            fileWriter.write("\n");
            fileWriter.write(Double.toString(posY));
            fileWriter.write("\n");
            fileWriter.write(Double.toString(health));
            fileWriter.write("\n");
            fileWriter.write(Integer.toString(speed));
            fileWriter.write("\n");
            fileWriter.write(name);
            fileWriter.write("\n");
            fileWriter.write(Status.toString(side));
            fileWriter.write("\n");
            fileWriter.write(Integer.toString(fist.getDistanceToAttack()));
            fileWriter.write("\n");
            fileWriter.write(Double.toString(fist.getDamageGive()));
            fileWriter.write("\n");
            fileWriter.write(Double.toString(pointBase.getX()));
            fileWriter.write("\n");
            fileWriter.write(Double.toString(pointBase.getY()));
            fileWriter.write("\n");
            fileWriter.write( Boolean.toString(isActive));
            fileWriter.write("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setToBase() {
        toBase = !toBase;
        if (!pointTarget.equals(pointBase))
            pointTarget = pointBase;
        else if (myTarget instanceof MapPoint)
            pointTarget = new Point2D(((MapPoint) myTarget).getPosX(), ((MapPoint) myTarget).getPosY());
        startTime = System.currentTimeMillis();
    }

    public boolean isToBase() {
        return toBase;
    }

    public void setCaptain() {
        isCaptain = true;
        captainRect.setFill(Color.BLACK);
        captainText.setFill(Color.WHITE);
        if (this instanceof Authority) captainText.setText("Captain 3");
        else if (this instanceof Veteran) captainText.setText("Captain 2");
        else if (this instanceof FighterImpl) captainText.setText("Captain 1");
    }

    @Override
    public void openData(BufferedReader bufferedReader) {
    }

    @Override
    public void moveLeft() {
        posX -= speed;
    }

    @Override
    public void moveRight() {
        posX += speed;
    }

    @Override
    public void moveUp() {
        posY -= speed;
    }

    @Override
    public void moveDown() {
        posY += speed;
    }

    @Override
    public Text getNameText() {
        return new Text(nameGraph.getText());
    }

    @Override
    public double getPosX() {
        return posX;
    }

    @Override
    public double getPosY() {
        return posY;
    }

    @Override
    public Status getSide() {
        return side;
    }

    @Override
    public Fist getFist() {
        return fist;
    }

    @Override
    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public double getHealth() {
        return health;
    }

    @Override
    public boolean getIsActive() {
        return isActive;
    }

    @Override
    public void setPosX(double posX) {
        this.posX = posX;
    }

    @Override
    public void setPosY(double posY) {
        this.posY = posY;
    }

    @Override
    public void setHealth(double health) {
        this.health = health;
    }

    @Override
    public Group getGroup() {
        return group;
    }

    public double getTempPosX() {
        return tempPosX;
    }

    public double getTempPosY() {
        return tempPosY;
    }

    @Override
    public String getSimpleNameClass() {
        return this.getClass().getSimpleName();
    }

    public MapPoint getMapPoint() {
        return mapPoint;
    }


    public boolean isCaptain() {
        return isCaptain;
    }

    public Object getMyTarget() {
        return myTarget;
    }

    public void setMyTarget(Object myTarget) {
        this.myTarget = myTarget;
    }

    public Point2D getPointTarget() {
        return pointTarget;
    }

    public void setPointTarget(Point2D pointTarget) {
        this.pointTarget = pointTarget;
    }



    public boolean isToCaptain() {
        return toCaptain;
    }

    public void setToCaptain(boolean toCaptain) {
        this.toCaptain = toCaptain;
    }


    @Override
    public void toStrings() {
        System.out.println(this.getClass().getSimpleName() + "{" +
                " name=" + name +
                ", posX=" + posX +
                ", posY=" + posY +
                ", side (RED/BLUE)=" + side +
                ", speed=" + speed +
                ", health=" + health +
                ", damageGive=" + fist.getDamageGive() +
                ", distanceToAttack" + fist.getDistanceToAttack() +
                ", timerAfterFight=" + fist.getDistanceToAttack() +
                " }");
    }

    public String toStringg() {
        return this.getClass().getSimpleName() +
                " name=" + name +
                ", posX=" + posX +
                ", posY=" + posY +
                ", side (RED/BLUE)=" + side +
                ", health=" + health +
                " ";
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() +
                " name=" + name +
                ", side (RED/BLUE)=" + side +
                " ";
    }

    @Override
    public AbstractFighter clone() throws CloneNotSupportedException {

        AbstractFighter tmp = (AbstractFighter)super.clone();

        if(fist == null) tmp.fist = null;
        else {
            tmp.fist = fist;
        }

        return tmp;
    }
}
