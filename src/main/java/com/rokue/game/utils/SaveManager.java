package com.rokue.game.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
// plus other classes

public class SaveManager {

    // You might keep all your saves in a "saves" folder in the working directory.
    public static final String SAVE_FOLDER = System.getProperty("user.dir") + File.separator + "saves";

    // Save the current game state to a file.
    public static void saveGame(GameState gameState, String fileName) throws IOException {
        File dir = new File(SAVE_FOLDER);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File saveFile = new File(dir, fileName);

        try (FileOutputStream fos = new FileOutputStream(saveFile);
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(gameState);
        }
    }

    // Load a previously saved game
    public static GameState loadGame(String fileName) throws IOException, ClassNotFoundException {
        File saveFile = new File(SAVE_FOLDER, fileName);
        try (FileInputStream fis = new FileInputStream(saveFile);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            return (GameState) ois.readObject();
        }
    }

    // Return list of all save files in the folder to display in "Load" menu.
    public static List<String> listSaveFiles() {
        List<String> saveFiles = new ArrayList<>();
        File dir = new File(SAVE_FOLDER);
        if (dir.exists() && dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                // Filter out anything that’s not a file or doesn’t match your naming convention
                if (file.isFile()) {
                    System.out.println(file.getName());
                    saveFiles.add(file.getName());
                }
            }
        }
        return saveFiles;
    }
}
