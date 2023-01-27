package com.example.kursach;

import com.example.kursach.map.Map;
import com.example.kursach.map.MapPoint;
import com.example.kursach.players.*;
import javafx.geometry.Point2D;

import java.util.ArrayList;

public class GamePanel {

    // Screen settings
    private final static int originalTitleSize = 18; // standard 16
    private final static int scale = 3;
    private final static int maxScreenCol = 20; // standard 16
    private final static int maxScreenRow = 12;

    public final static int titleSize = originalTitleSize * scale;
    public final static int SCREEN_WIDTH = titleSize * maxScreenCol;
    public final static int SCREEN_HEIGHT = titleSize * maxScreenRow;
    public final static int MAP_WIDTH = 2448;
    public final static int MAP_HEiGHT = 2048;
    public final static double SCALE = 0.1;

    private ArrayList<FighterImpl> fighterImpls;
    private ArrayList<MapPoint> mapPoints;

    private final Map map;
    private Object temp = new Object();


    FighterImpl f1;
    Veteran v2;
    Authority a3;

    public GamePanel(Map map) {
        this.map = map;
        this.fighterImpls = map.getFighters();
        this.mapPoints = map.getMapPoints();
    }

    public void update(Map map) {
        fighterImpls = map.getFighters();
        mapPoints = map.getMapPoints();




        for (FighterImpl x: fighterImpls) {
            if (x.isAlive()) {
                if (x.isToCaptain()) {}
                else x.findTarget(this);
                x.update();
            } else temp = x;
        }
        fighterImpls.remove(temp);
        for (MapPoint x: mapPoints) {
            x.update(this);
        }

        if(fighterImpls.stream().filter(AbstractFighter::isCaptain).count() < 3) {
            for (FighterImpl x: fighterImpls) {
                if (x.isCaptain()) {
                    if (x instanceof Authority) a3 = (Authority) x;
                    else if (x instanceof Veteran) v2 = (Veteran) x;
                    else  f1 = x;
                }
            }
        } else {
            for (FighterImpl fighterImpl : fighterImpls) {
                if (!fighterImpl.isCaptain() && !fighterImpl.isToCaptain()) {
                    fighterImpl.setToCaptain(true);
                    if (fighterImpl instanceof Authority) {

                        fighterImpl.setMyTarget(a3);
                        fighterImpl.setPointTarget(new Point2D(a3.getPosX(), a3.getPosY()));
                    } else if (fighterImpl instanceof Veteran) {

                        fighterImpl.setMyTarget(v2);
                        fighterImpl.setPointTarget(new Point2D(v2.getPosX(), v2.getPosY()));
                    } else {

                        fighterImpl.setMyTarget(f1);
                        fighterImpl.setPointTarget(new Point2D(f1.getPosX(), f1.getPosY()));
                    }
                }
            }
        }
    }

    // Getters, setters.
    public ArrayList<FighterImpl> getFighters() {
        return fighterImpls;
    }

    public ArrayList<MapPoint> getPointMaps() {
        return mapPoints;
    }

    public Map getMap() {
        return map;
    }
}