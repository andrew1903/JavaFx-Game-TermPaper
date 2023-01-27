package com.example.kursach.players;

import com.example.kursach.map.MapPoint;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;

import java.io.File;
import java.util.Random;

public class Authority extends Veteran {

    protected static final Image FIGHTER_IMAGE_RED = new Image(new File("src/main/java/images/Ryu_0005.png").toURI().toString());
    protected static final Image FIGHTER_IMAGE_BLUE = new Image(new File("src/main/java/images/Ryu_0055.png").toURI().toString());


    public Authority() {
        super();
    }

    public Authority(Status side) {
        super(side);
    }

    public Authority(Point2D point2D, MapPoint pointBase, String[] arrayOfNames) {
        super(arrayOfNames[new Random().nextInt(arrayOfNames.length)], pointBase.getStatus());
        speed = 1;
        fist.setDamageGive(8);
        health = random.nextInt(70) + 50;
    }

    public Authority(String name, Status side) {
        super(name, side);
        speed = 1;
        fist.setDamageGive(8);
        health = random.nextInt(70) + 50;
    }

    public Authority(Status side, MapPoint point, String name, double posX, double posY, boolean isActive) {
        super(side, point, name, posX, posY, isActive);
        speed = 1;
        fist.setDamageGive(8);
        health = random.nextInt(70) + 50;
    }

    public Authority(Status side, double x, double y, String name, double posX, double posY, boolean isActive) {
        super(side, x, y, name, posX, posY, isActive);
        speed = 2;
        fist.setDamageGive(5);
        health = random.nextInt(60) + 50;
    }

    @Override
    protected void getImage() {
        if (Status.RED.equals(this.getSide()))
            fighterImage.setImage(FIGHTER_IMAGE_RED);
        else
            fighterImage.setImage(FIGHTER_IMAGE_BLUE);
        fighterImage.setFitHeight(100);
        fighterImage.setPreserveRatio(true);
    }
}
