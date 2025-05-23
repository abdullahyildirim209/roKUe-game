package com.rokue.game;



import java.awt.Color;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import com.rokue.game.audio.SoundManager;
import com.rokue.game.entities.Prop;
import com.rokue.game.map.Hall;
import com.rokue.game.ui.BuildModeDesigner;
import com.rokue.game.ui.MainPanel;
import com.rokue.game.ui.PlayPanel;
import com.rokue.game.ui.SpriteLoader;
import com.rokue.game.utils.RNG;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            MainPanel mainPanel = new MainPanel();
            mainPanel.setIconImage(new ImageIcon(SpriteLoader.class.getResource("/sprites/icon.png")).getImage());
            mainPanel.setVisible(true);
        });
    }

    public static void startBuildMode() {
        // Create our sprite loader, RNG, etc.
        SpriteLoader spriteHandler = new SpriteLoader();
        RNG rng = new RNG();
        
        // Create the build-mode window
        JFrame buildWindow = new JFrame();
        buildWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        buildWindow.setResizable(false);
        buildWindow.setTitle("RoKUe-Build");
        buildWindow.setIconImage(new ImageIcon(SpriteLoader.class.getResource("/sprites/icon.png")).getImage());
        
        // Initialize a 2x2 grid of halls
        Hall[][] halls = new Hall[2][2];
        for (int row = 0; row < 2; row++) {
            for (int col = 0; col < 2; col++) {
                halls[row][col] = new Hall(rng);
            }
        }
        
        // Create your BuildModeDesigner and show it
        BuildModeDesigner buildModeDesigner = new BuildModeDesigner(halls);
        buildWindow.add(buildModeDesigner);
        buildWindow.setExtendedState(JFrame.MAXIMIZED_BOTH); // Full-screen window
        buildWindow.setVisible(true);
        
        // Poll for when build mode is finished. Then close and start Play.
        Timer checkBuildFinishedTimer = new Timer(100, e -> {
            if (buildModeDesigner.getBuildFinished()) {
                ((Timer) e.getSource()).stop(); // Stop checking
                buildWindow.dispose(); // Close build window

                // Transition to play window
                Hall[] playHalls = new Hall[4];
                int index = 0;
                for (int r = 0; r < 2; r++) {
                    for (int c = 0; c < 2; c++) {
                        playHalls[index++] = halls[r][c];
                    }
                }
                

                // Start "Play" with those halls
                int currentHallIndex = 0;
                boolean isGameLoaded = false;
                startPlayMode(playHalls, spriteHandler,currentHallIndex,isGameLoaded,0);
            }
        });
        checkBuildFinishedTimer.start();
    }

    /**
     * Start immediate Play Mode (bypassing Build Mode).
     */
    public static void startPlayMode() {
        // If you want a simple layout or want to load from a file, do it here.
        SpriteLoader spriteHandler = new SpriteLoader();
        RNG rng = new RNG();
        
        // // Example: same 2x2 approach
        // Hall[][] hallsGrid = new Hall[2][2];
        // Hall[] halls = new Hall[4];
        // int index = 0;
        // for (int r = 0; r < 2; r++) {
        //     for (int c = 0; c < 2; c++) {
        //         hallsGrid[r][c] = new Hall(rng);
        //         halls[index++] = hallsGrid[r][c];
        //     }
        // }

        Hall[] halls = new Hall[4];
        for (int i = 0; i < 4; i++) {
            halls[i] = new Hall(rng);
            for (int j = 0; j < ((i + 1) * 4 + 1 + rng.nextInt(4)); j++) {
                int[] IDs = {2, 2, 2, 2, 10, 10, 11, 11, 12, 12, 12, 12}; // crate and pillar more likely because they look good
                new Prop(IDs[rng.nextInt(IDs.length)]).randomlyPlace(halls[i]);
            }
        }


        
        // Create the play window with these halls
        int currentHallIndex = 0;
        boolean isGameLoaded = false;
        startPlayMode(halls, spriteHandler, currentHallIndex, isGameLoaded,0);
    }

    /**
     * Utility method used by both startBuildMode() and startPlayMode() to
     * actually open the "Play" window with a known set of halls and an existing spriteHandler.
     */
    private static void startPlayMode(Hall[] halls, SpriteLoader spriteHandler,int currentHallIndex, boolean isGameLoaded, int remainingTime) {
        SoundManager.loadSound("fighterAttack", "/sprites/wav/fighterHit.wav");
        SoundManager.loadSound("doorOpen", "/sprites/wav/kapi acilma sesi.wav");
        SoundManager.loadSound("wizard", "/sprites/wav/wizard.wav");
        SoundManager.loadSound("archer", "/sprites/wav/okcunun sesi.wav");
        SoundManager.loadSound("heroHit", "/sprites/wav/hasar alma sesi.wav");
        SoundManager.loadSound("itemCollected", "/sprites/wav/collecting item.wav");
        SoundManager.loadSound("fail", "/sprites/wav/fail-144746.wav");
        SoundManager.loadSound("back", "/sprites/wav/Background.wav");

        System.out.println("All sounds loaded successfully.");
        SoundManager.playSound("back");

        JFrame playWindow = new JFrame();
        playWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        playWindow.setResizable(false);
        playWindow.setTitle("RoKUe-Play");
        playWindow.setIconImage(new ImageIcon(SpriteLoader.class.getResource("/sprites/icon.png")).getImage());
        //playWindow.setLayout(new BorderLayout());
        playWindow.setLayout(new GridBagLayout());
        playWindow.setUndecorated(true);
        playWindow.getContentPane().setBackground(new Color(66, 40, 53));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.NONE;

        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd = ge.getDefaultScreenDevice();
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //int screenHeight = gd.getDisplayMode().getHeight();
        int scale = screenSize.height / 360;
        playWindow.setSize(screenSize.width, screenSize.height);

        PlayPanel playPanel = new PlayPanel(halls, spriteHandler,currentHallIndex, isGameLoaded, remainingTime, scale);
        playWindow.add(playPanel, gbc);
        playWindow.setLocationRelativeTo(null);
        playWindow.setVisible(true);
        //gd.setFullScreenWindow(playWindow);

        // Start the game loop
        playPanel.startGameThread();
    }

    public static void startPlayMode(Hall[] halls, int currentHallIndex, int heroLives, long globalTime, int remainingTime) {
        // When you are loading from a save, this method is used
        // If you want a simple layout or want to load from a file, do it here.
        SpriteLoader spriteHandler = new SpriteLoader();
        RNG rng = new RNG();



        // Create the play window with these halls
        boolean isGameLoaded = true;
        startPlayMode(halls, spriteHandler,currentHallIndex,isGameLoaded,remainingTime);
    }

    public static void showMainMenu() {
        MainPanel mainPanel = new MainPanel();
        mainPanel.setVisible(true);
    }
}