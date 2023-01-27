package com.example.kursach;

import com.example.kursach.map.Map;
import com.example.kursach.map.MapPoint;
import com.example.kursach.players.Authority;
import com.example.kursach.players.FighterImpl;
import com.example.kursach.players.Veteran;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import java.io.*;

public class SavedGame {

    public static void serializeNow(Window stage, Map map) {
        String currentDir = System.getProperty("user.dir");

        File initDirectory = new File(currentDir);

        FileChooser fileChooser = new FileChooser();

        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(
                initDirectory);

        File file = fileChooser.showSaveDialog(stage);

        if (file != null) {

            try (FileWriter fileWriter = new FileWriter(file)){

                fileWriter.write(Integer.toString(map.getMapPoints().size() - 2));
                fileWriter.write("\n");

                map.getMapPoints().stream()
                        .filter(x -> !x.isBase()).toList()
                        .forEach(mapPoint -> mapPoint.saveData(fileWriter));

                fileWriter.write(Integer.toString(map.getFighters().size()));
                fileWriter.write("\n");
                for (FighterImpl fighterImpl : map.getFighters()) {
                    fighterImpl.saveData(fileWriter);
                }

            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public static void deserializeNow(Window stage, Map map, Main main) {
        String currentDir= System.getProperty("user.dir");
        File initDirectory = new File(currentDir);
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);
        fileChooser.setInitialDirectory(
                initDirectory);
        File file = fileChooser.showOpenDialog(stage);
        if(file != null) {

            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file))) {
                String text;
                text = bufferedReader.readLine();
                int hmany = Integer.parseInt(text);

                map.getMapPoints().stream()
                        .filter(x -> !x.isBase()).toList()
                        .forEach(x->{

                    main.removeMacroMapMacroObj(main.getAnchorPane(), x);
                    map.deleteMapPoint(x);
                });

                for (int i = 0; i < hmany; i++) {
                    MapPoint mapPoint = new MapPoint();
                    mapPoint.openData(bufferedReader);
                    map.addNewMapPoint(mapPoint);
                    main.addMacroMapObj(main.getAnchorPane(), mapPoint);
                }


                text = bufferedReader.readLine();
                hmany = Integer.parseInt(text);

                for (int i = 0; i < map.getFighters().size(); i++){

                    FighterImpl fighterImpl = map.getFighters().get(i--);
                    main.removeMiniMapMiniObj(main.getAnchorPane(), fighterImpl);
                    map.deleteFighter(fighterImpl);
                }

                FighterImpl fighterImpl = null;
                for (int i = 0; i < hmany; i++) {
                    text = bufferedReader.readLine();
                    switch (text) {
                        case "Fighter" -> {
                            fighterImpl = new FighterImpl();
                            fighterImpl.openData(bufferedReader);
                            map.addNewFighter(fighterImpl);
                            main.addMiniMapMiniObj(main.getAnchorPane(), fighterImpl);
                        }
                        case "Veteran" -> {
                            fighterImpl = new Veteran();
                            fighterImpl.openData(bufferedReader);
                            map.addNewFighter(fighterImpl);
                            main.addMiniMapMiniObj(main.getAnchorPane(), fighterImpl);
                        }
                        case "Authority" -> {
                            fighterImpl = new Authority();
                            fighterImpl.openData(bufferedReader);
                            map.addNewFighter(fighterImpl);
                            main.addMiniMapMiniObj(main.getAnchorPane(), fighterImpl);
                        }
                    }
                    fighterImpl.setPosX(fighterImpl.getTempPosX());
                    fighterImpl.setPosY(fighterImpl.getTempPosY());
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
