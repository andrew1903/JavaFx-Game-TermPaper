package com.example.kursach.map;

import com.example.kursach.GamePanel;
import com.example.kursach.players.FighterImpl;
import com.example.kursach.players.Status;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.util.ArrayList;

import static java.lang.Math.pow;

public class Warehouse extends MapPoint{

    private ArrayList<FighterImpl> myFighterImpls;
    //    private boolean isAdmire;
    protected Group group;
    protected Rectangle rectangle;

    public Warehouse(int posX, int posY, ArrayList<FighterImpl> myFighterImpls) {
        super(posX, posY);
//        getImage();
        this.status = Status.NEUTRAL;

        this.myFighterImpls = myFighterImpls;
        rectangle = new Rectangle();
        group = new Group(rectangle);
    }

    public void getImage() {
//        try {
////            this.skin = ImageIO.read(new FileInputStream("src/main/java/images/img_2.png"));
//        } catch (IOException ex) {
//            ex.printStackTrace();
//        }
    }

    public void draw() {
        rectangle.setWidth(100);
        rectangle.setHeight(100);
        rectangle.setFill(Color.GREEN);
        group.setLayoutX(posX);
        group.setLayoutY(posY);
        rectangle.setX(posX);
        rectangle.setY(posY);
    }

    public void update(GamePanel gp) {
        capture(gp.getFighters());
    }

    public void capture(ArrayList<FighterImpl> fighterImpls) {

        for(FighterImpl x: fighterImpls) {

            if (pow(x.getPosX() - this.posX, 2) + pow(x.getPosY() - this.posY, 2)
                    <= pow(x.getFist().getDistanceToAttack(), 2) && myFighterImpls.contains(x)) {
                if (x.getHealth() < 30) {
                    x.setHealth(x.getHealth()+0.5);
                }
            }
        }
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

    public Group getGroup() {
        return group;
    }

    public void toStrings() {
        System.out.println(this.getClass().getSimpleName() + "{" +
                ", posX=" + posX +
                ", posY=" + posY +
                ", side (RED/BLUE)=" + status +
                "}");
    }
}
