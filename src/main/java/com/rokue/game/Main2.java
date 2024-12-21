package com.rokue.game;
import javax.swing.*;
import com.rokue.game.map.Hall;
import com.rokue.game.ui.BuildModeDesigner;
import com.rokue.game.utils.RNG;

public class Main2 {
	public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Build Mode - 2x2 Halls");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

            // Initialize a 2x2 grid of halls
            Hall[][] halls = new Hall[2][2];
            for (int row = 0; row < 2; row++) {
                for (int col = 0; col < 2; col++) {
                    halls[row][col] = new Hall(new RNG());
                }
            }

            BuildModeDesigner buildModeDesigner = new BuildModeDesigner(halls);

            frame.add(buildModeDesigner);
            frame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-screen window
            frame.setVisible(true);
        });
    }

}
