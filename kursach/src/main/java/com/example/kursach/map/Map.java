package com.example.kursach.map;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

import com.example.kursach.GamePanel;
import com.example.kursach.players.*;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class Map {

    public static String[] names = {

            "Aspect",
            "Kraken",
            "Bender",
            "Lynch",
            "BigPapa",
            "MadDog",
            "Bowser",
            "Doyle",
            "Bruise",
            "Psycho",
            "Cannon",
            "Ranger",
            "Clink",
            "Ratchet",
            "Cobra",
            "Reaper"
    };

    static {
        Arrays.sort(names);
    }

    public static final Image MAP_IMAGE = new Image(new File("src/main/java/images/Map.png").toURI().toString());

    private static final Pane ROOT = new Pane();

    private static final Point2D BAD_BASE_POINT = new Point2D(495, 840);
    private static final Point2D GOOD_BASE_POINT = new Point2D(2020, 840);

    private final MapPoint goodBase = new MapPoint(Status.BLUE, this);
    private final MapPoint badBase = new MapPoint(Status.RED, this);

    private final ArrayList<FighterImpl> fighterImpls = new ArrayList<>();
    private final ArrayList<MapPoint> mapPoints = new ArrayList<>();
    private final List<Integer> COORDS = List.of(1350, 230, 1150, 825, 1375, 1580);

    public Map(int numberOfFighters) {

        ROOT.setMinWidth(getRootWidth());
        ROOT.setMinHeight(getRootHeight());

        Rectangle rectangle = new Rectangle(getRootWidth() + 1400, getRootHeight() + 1400);
        rectangle.setFill(new ImagePattern(MAP_IMAGE));
        ROOT.getChildren().add(rectangle);

        pointsGenerator();
        fightersGenerator(numberOfFighters);

    }

    public void fightersGenerator(int number) {
        addNewMapPoint(goodBase);
        addNewMapPoint(badBase);

        for (int i = 0; i < number; i++) {
            FighterImpl fighterImplBlue = new FighterImpl(Status.BLUE);
            FighterImpl fighterImplRed = new FighterImpl(Status.RED);
            FighterImpl veteranRed = new Veteran(Status.RED);
            FighterImpl veteranBlue = new Veteran(Status.BLUE);
            FighterImpl authorityRed = new Authority(Status.RED);
            FighterImpl authorityBlue = new Authority(Status.BLUE);

            addNewFighter(fighterImplBlue);
            addNewFighter(fighterImplRed);

            addNewFighter(veteranRed);
            addNewFighter(veteranBlue);

            addNewFighter(authorityRed);
            addNewFighter(authorityBlue);
        }
    }

    public void pointsGenerator() {

        for (int i = 0; i <= (COORDS.size() / 2) + 1; i+=2) {
            MapPoint mapPoint =
                    new MapPoint(COORDS.get(i), COORDS.get(i+1));
            addNewMapPoint(mapPoint);
        }

        try {
            Object[] mapPointsTemp = mapPoints.toArray();
            String myTempArrayOfPointMap = Arrays.toString(mapPointsTemp);
        } catch (ClassCastException ex) {
            ex.printStackTrace();
        }
    }

    public void addNewFighter(FighterImpl fighterImpl){
        fighterImpls.add(fighterImpl);
        if (fighterImpl.getSide().equals(Status.RED))
            badBase.addFighter(fighterImpl);
        else
            goodBase.addFighter(fighterImpl);
        fighterImpl.setFighterChord();
        ROOT.getChildren().add(fighterImpl.getGroup());
    }

    public void addNewInsertFighter(FighterImpl fighterImpl){
        fighterImpls.add(fighterImpl);

        fighterImpl.setFighterChord();
        ROOT.getChildren().add(fighterImpl.getGroup());
    }

    public void deleteFighter(FighterImpl fighterImpl){
        ROOT.getChildren().remove(fighterImpl.getGroup());
        fighterImpls.remove(fighterImpl);
        if (fighterImpl.getSide().equals(Status.RED))
            badBase.delFighter(fighterImpl);
        else
            goodBase.delFighter(fighterImpl);
        FighterImpl.setNumberOfPoints(FighterImpl.getNumberOfPoints() - 1);
    }

    public void addNewMapPoint(MapPoint mapPoint){
        mapPoints.add(mapPoint);
        ROOT.getChildren().add(mapPoint.getGroup());
    }

    public void deleteMapPoint(MapPoint mapPoint){
        ROOT.getChildren().remove(mapPoint.getGroup());
        mapPoints.remove(mapPoint);
        MapPoint.setNumberOfPoints(MapPoint.getNumberOfPoints() - 1);
    }

    // getters
    public static Pane getRoot() {
        return ROOT;
    }

    public static int getRootHeight() {
        return GamePanel.SCREEN_HEIGHT;
    }

    public static int getRootWidth() {
        return GamePanel.SCREEN_WIDTH;
    }

    public ArrayList<FighterImpl> getFighters() {
        return fighterImpls;
    }

    public ArrayList<MapPoint> getMapPoints() {
        return mapPoints;
    }

    public static Point2D getBadBasePoint() {
        return BAD_BASE_POINT;
    }

    public static Point2D getGoodBasePoint() {
        return GOOD_BASE_POINT;
    }

    @Override
    public String toString() {
        return "Map {" +
                "fighters=" + fighterImpls +
                ", mapPoints=" + mapPoints +
                '}';
    }
}
