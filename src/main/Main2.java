package main;

import game.Hall;
import game.PlayPanel;
import javax.swing.JFrame;

public class Main2 {
    public static void main(String[] args) {
        // project root
        System.out.println("Move sprites folder to here: " + System.getProperty("user.dir"));

        // create the window
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("RoKUe");

        // empty game
        Hall hall = new Hall();
        PlayPanel playPanel = new PlayPanel(hall);
        window.add(playPanel);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        playPanel.startGameThread(); // start game loop
    }
}
