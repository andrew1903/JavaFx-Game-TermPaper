package com.example.kursach.players;

import com.example.kursach.GamePanel;
import com.example.kursach.map.Map;
import com.example.kursach.map.MapPoint;
import javafx.geometry.Point2D;
import javafx.scene.paint.Color;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.*;

import static java.lang.Math.pow;

public class FighterImpl extends AbstractFighter {

    {
        numberOfPoints++;

        fist = new Fist();
        fist.setDamageGive(0.2);
        fist.setDistanceToAttack(45);
        health = random.nextDouble(50) + 50;
    }

    public FighterImpl() {

        name = Map.names[new Random().nextInt(Map.names.length)];

        if (new Random().nextBoolean()) {
            side = Status.RED;
            setPos(Map.getBadBasePoint());
        } else {
            side = Status.BLUE;
            setPos(Map.getGoodBasePoint());
        }

        getImage();
        draw();
    }

    public FighterImpl(Status side) {
        this();
        this.side = side;
        getImage();
        draw();
    }

    public FighterImpl(String name, Status side) {
        this(side);
        this.name = name;
        getImage();
        draw();
    }

    public FighterImpl(Status side, MapPoint point, String name, double posX, double posY, boolean isActive) {

        this(name, side);
        this.posX = posX;
        this.posY = posY;
        this.isActive = isActive;
        setPos(new Point2D(point.getPosX(), point.getPosY()));
        getImage();
        draw();
    }

    public FighterImpl(Status side, double x, double y, String name, double posX, double posY, boolean isActive) {

        this(name, side);
        this.posX = posX;
        this.posY = posY;
        this.isActive = isActive;
        setPos(new Point2D(x, y));
        getImage();
        draw();
    }

    // Assign a picture to the skin.
    protected void getImage() {
        if (Status.RED.equals(side))
            fighterImage.setImage(FIGHTER_IMAGE_RED);
        else
            fighterImage.setImage(FIGHTER_IMAGE_BLUE);
        fighterImage.setFitHeight(100);
        fighterImage.setPreserveRatio(true);
    }

    // The main thread of the object update.
    public void update() {
        autoMove();

        if (health <= 0) {
            switchAlive();
            numberOfPoints--;
        }
        if (pointBase.distance(new Point2D(posX, posY)) <= fist.getDistanceToAttack() && isAlive)
                //&& myTarget instanceof MapPoint && ((MapPoint) myTarget).getStatus() == side)
            group.setVisible(false);
        else if (pointBase.distance(new Point2D(posX, posY)) > fist.getDistanceToAttack() && isAlive)
            group.setVisible(true);
    }

    // Method of automatic movement on the set purposes.
    private void autoMove() {

        if (startTime == null || !isActive) {
            startTime = System.currentTimeMillis();
        }
        long now = System.currentTimeMillis();
        long diff = now - startTime;
        long playTime = 140000;

        if (diff >= playTime) {
            diff = playTime;
        }
        double i = (double) diff / (double) playTime;

        // If two fighters are within striking distance of the fist, their movement stops and the battle begins.
        if (checkArea() == 1 || isCaptain) i = 0;

        posX = (posX + ((pointTarget.getX() - posX) * i / speed));
        posY = (posY + ((pointTarget.getY() - posY) * i / speed));

        group.setLayoutX(posX);
        group.setLayoutY(posY);
    }

    // Search for all possible goals and write them in the collection.
    public void findTarget(GamePanel gp) {
        // Search for targets among other fighters.
        for (FighterImpl x: gp.getFighters()) {
            // Record goals.
            if (x.side != side && isAlive) {
                targets.add(x);
                arrayOfOpponent.add(x);
            }
            // Delete target.
            if (!x.isAlive) {
                targets.remove(x);
            }
        }
        // Find goals among the points on the map.
        for (MapPoint x: gp.getPointMaps()) {
            // Record goals.
            if (x.getStatus() != side) targets.add(x);
            // Delete target.
            if (targets.contains(x) && x.getStatus() == side) targets.remove(x);
        }

        if (targets.size() > 0) goalCapture();
        else pointTarget = new Point2D(pointBase.getX(), pointBase.getY());
    }

    // Capture a specific goal from a collection of goals.
    private void goalCapture() {
        // Nulling the completed goal.
        if (myTarget instanceof FighterImpl && !toBase && !toCaptain) {
            if (!((FighterImpl) myTarget).isAlive) myTarget = null;
            else pointTarget = new Point2D(((FighterImpl) Objects.requireNonNull(myTarget)).posX,
                        ((FighterImpl) Objects.requireNonNull(myTarget)).posY);
        } else if (myTarget instanceof MapPoint && !toBase && !toCaptain) {
            if (((MapPoint) myTarget).getStatus().equals(side)) myTarget = null;
        }

        ArrayList<Object> arrayOfTarget
                = new ArrayList<>(targets);
        // Assign a goal.
        if (myTarget == null && arrayOfTarget.size() > 0 && !toBase && !toCaptain) {
            int index = new Random().nextInt(arrayOfTarget.size());
            myTarget = arrayOfTarget.get(index);
            arrayOfTarget.remove(index);
            // Record a point with the coordinates of the target.
            if (myTarget instanceof FighterImpl) {
                pointTarget = new Point2D(((FighterImpl) myTarget).posX, ((FighterImpl) myTarget).posY);
            } else if (myTarget instanceof MapPoint) {
                pointTarget = new Point2D(((MapPoint) myTarget).getPosX(), ((MapPoint) myTarget).getPosY());
            }
        }
    }

    // Check the distance for impact.
    private int checkArea() {

        if (!isActive) return 1;
        if (pointTarget.getX() == posX && pointTarget.getY() == posY && toCaptain) {
            group.setOnMouseClicked(event->{

            });
            return 1;
        }
        for(FighterImpl x: arrayOfOpponent) {
            if ((pow(x.getPosX() - this.getPosX(), 2) + pow(x.getPosY() - this.getPosY(), 2)
                    <= pow(getFist().getDistanceToAttack(), 2)) && x.isAlive() && !x.toCaptain && !toCaptain) {
                hitOpponent(x);
                return 1;
            }
        }
        return 0;
    }

    // Fist blow.
    private void hitOpponent(FighterImpl opponent) {
        opponent.health -= fist.getDamageGive();
        lineHealth.setEndX(health * 1.4);
        lineHealth.setEndY(-10);
        if (health < 20) lineHealth.setStroke(Color.RED);
        if (health < 60) lineHealth.setStroke(Color.YELLOW);
    }

    @Override
    public void openData(BufferedReader bufferedReader) {
        try {
            String text;

            text = bufferedReader.readLine();
            setPosX(Double.parseDouble(text));
            tempPosX = Double.parseDouble(text);
            text = bufferedReader.readLine();
            setPosY(Double.parseDouble(text));
            tempPosY = Double.parseDouble(text);
            text = bufferedReader.readLine();
            setHealth(Double.parseDouble(text));
            text = bufferedReader.readLine();
            speed = Integer.parseInt(text);
            text = bufferedReader.readLine();
            name = text;
            text = bufferedReader.readLine();
            if (text.equals("RED"))
                side = Status.RED;
            else if (text.equals("BLUE"))
                side = Status.BLUE;

            text = bufferedReader.readLine();
            fist.setDistanceToAttack(Integer.parseInt(text));
            text = bufferedReader.readLine();
            fist.setDamageGive(Double.parseDouble(text));
            text = bufferedReader.readLine();
            pointBase = new Point2D(Double.parseDouble(text), Double.parseDouble(text));
            text = bufferedReader.readLine();
            isActive = Boolean.parseBoolean(text);
            text = bufferedReader.readLine();


            getImage();
            draw();
            group.setLayoutX(posX);
            group.setLayoutY(posY);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int getNumberOfPoints() {
        return numberOfPoints;
    }

    public static void setNumberOfPoints(int numberOfShips) {
        FighterImpl.numberOfPoints = numberOfShips;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FighterImpl FighterImpl = (FighterImpl) o;
        return posX == FighterImpl.posX && posY == FighterImpl.posY &&
                speed == FighterImpl.speed && health == FighterImpl.health &&
                Objects.equals(random, FighterImpl.random) && Objects.equals(name, FighterImpl.name) &&
                Objects.equals(side, FighterImpl.side) && Objects.equals(fist, FighterImpl.fist) &&
                Objects.equals(fighterImage, FighterImpl.fighterImage);
    }


}
