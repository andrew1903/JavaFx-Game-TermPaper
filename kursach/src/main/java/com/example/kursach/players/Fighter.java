package com.example.kursach.players;

import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.text.Text;
import java.io.BufferedReader;
import java.io.FileWriter;

public interface Fighter {

    void setPos(Point2D point) ;

    void draw();

    void switchActivation();

    void switchAlive();

    void setFighterChord();

    void openData(BufferedReader bufferedReader);

    void moveLeft();
    void moveRight();
    void moveUp();
    void moveDown();

    void saveData(FileWriter fileWriter);

    Text getNameText();

    double getPosX();

    double getPosY();

    Status getSide();

    Fist getFist();

    boolean isAlive();

    double getHealth();

    boolean getIsActive();

    void setPosX(double posX);

    void setPosY(double posY);

    void setHealth(double health);

    Group getGroup();

    double getTempPosX();

    double getTempPosY();

    String getSimpleNameClass();

    @Override
    boolean equals(Object o);

    void toStrings();

}
