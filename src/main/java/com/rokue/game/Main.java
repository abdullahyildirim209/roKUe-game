package com.rokue.game;

import javax.swing.JFrame;

import com.rokue.game.map.Hall;
import com.rokue.game.ui.PlayPanel;

public class Main {
    public static void main(String[] args) {
        System.out.println("Move sprites folder to here: " + System.getProperty("user.dir"));

        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("RoKUe");

        Hall hall = new Hall();
        for (int i = 0; i < 5; i++) {
            hall.placeRandomCrate();
        }

        PlayPanel playPanel = new PlayPanel(hall);
        window.add(playPanel);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        playPanel.startGameThread();
    }
}