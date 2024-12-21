package com.rokue.game;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.Timer;

import com.rokue.game.map.Hall;
import com.rokue.game.ui.BuildModeDesigner;
import com.rokue.game.ui.PlayPanel;
import com.rokue.game.ui.SpriteLoader;
import com.rokue.game.utils.RNG;


public class Main {
    public static void main(String[] args) {
    	SpriteLoader spriteHandler = new SpriteLoader();
    	RNG RNG = new RNG();
    	
        // create the window
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("RoKUe-Build");
        window.setIconImage(new ImageIcon(SpriteLoader.class.getResource("/sprites/icon.png")).getImage());
        
        // Initialize a 2x2 grid of halls
        Hall[][] halls = new Hall[2][2];
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                halls[row][col] = new Hall(RNG);
            }
        }
        
        BuildModeDesigner buildModeDesigner = new BuildModeDesigner(halls);

        window.add(buildModeDesigner);
        window.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-screen window
        window.setVisible(true);
        
        Timer checkBuildFinishedTimer = new Timer(100, e -> {
            if (buildModeDesigner.getBuildFinished()) {
                ((Timer) e.getSource()).stop(); // Stop the timer
                window.dispose(); // Close the build window

                // Transition to the play window
                JFrame playWindow = new JFrame();
                playWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                playWindow.setResizable(false);
                playWindow.setTitle("RoKUe-Play");
                playWindow.setIconImage(new ImageIcon(SpriteLoader.class.getResource("/sprites/icon.png")).getImage());

                Hall[] playHalls = new Hall[4];
                int index = 0;
                for (int r = 0; r < 2; r++) {
                    for (int c = 0; c < 2; c++) {
                        playHalls[index++] = buildModeDesigner.getHalls()[r][c];
                    }
                }

                PlayPanel playPanel = new PlayPanel(playHalls, spriteHandler);
                playWindow.add(playPanel);

                playWindow.pack();
                playWindow.setLocationRelativeTo(null);
                playWindow.setVisible(true);

                playPanel.startGameThread(); // Start game loop
            }
        });
        
        checkBuildFinishedTimer.start();
        
    }
    
}
