package com.rokue.game.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.rokue.game.Main;
import com.rokue.game.map.Hall;
import com.rokue.game.utils.GameState;
import com.rokue.game.utils.SaveManager;

public class MainPanel extends JFrame {

    private JPanel contentPane;

    public MainPanel() {
        setTitle("Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Make it fullscreen and resizable

        // Set content pane with a custom background image
        contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load and draw background image
                Image backgroundImage = new ImageIcon(SpriteLoader.class.getResource("/sprites/MainMenu.jpg")).getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setLayout(null); // Initial layout for dynamic resizing
        setContentPane(contentPane);

        // Create buttons
        JButton playGameButton = createTransparentButton("Play Game");
        JButton buildModeButton = createTransparentButton("Build Mode");
        JButton helpButton = createTransparentButton("Help");
        JButton loadButton = createTransparentButton("Load Game");
        // Add buttons to content pane
        contentPane.add(playGameButton);
        contentPane.add(buildModeButton);
        contentPane.add(helpButton);
        contentPane.add(loadButton);

        // Add a resize listener to adjust buttons dynamically
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();

                // Dynamically align buttons
                playGameButton.setBounds(width * 175 / 1000, height * 68 / 100, width * 10 / 100, height * 10 / 100);
                buildModeButton.setBounds(width * 47 / 100, height * 68 / 100, width * 10 / 100, height * 10 / 100);
                helpButton.setBounds(width * 76 / 100, height * 68 / 100, width * 10 / 100, height * 10 / 100);
                loadButton.setBounds(width * 47 / 100, height * 80 / 100, width * 10 / 100, height * 10 / 100);
            }
        });

        // Set button actions
        playGameButton.addActionListener(e -> {
            dispose();
            Main.startPlayMode();
        });
        buildModeButton.addActionListener(e -> {
            dispose();
            Main.startBuildMode();
        });
        helpButton.addActionListener(e -> showHelpDialog());

        loadButton.addActionListener(e -> {
            List<String> saves = SaveManager.listSaveFiles();
            dispose();
            if (saves.isEmpty()) {
        JOptionPane.showMessageDialog(this, "No save files found.");
        return;
            }
            // Let the user pick from a list
            String selectedFile = (String) JOptionPane.showInputDialog(
            this, 
            "Select a save file", 
            "Load Game", 
            JOptionPane.PLAIN_MESSAGE,
            null, 
            saves.toArray(), 
            saves.get(0)
    );

    if (selectedFile != null) {
        try {
            GameState loadedState = SaveManager.loadGame(selectedFile);

            // Then restore your game environment
            Hall[] halls = loadedState.getHalls();
            int currentHallIndex = loadedState.getCurrentHallIndex();
            int heroLives = loadedState.getHeroLives();
            long globalTime = loadedState.getGlobalTime();
            //PlayPanel.tickTime = loadedState.getTime();
            halls[currentHallIndex].setTime(loadedState.getTime());

            
            Main.startPlayMode(halls, currentHallIndex, heroLives, globalTime);

            JOptionPane.showMessageDialog(this, "Game loaded successfully!");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error loading game: " + ex.getMessage());
        }
    }
    }
        );

        // Trigger initial resize to position buttons
        revalidate();
        repaint();
        
    }
    

    private JButton createTransparentButton(String text) {
        JButton button = new JButton(text);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Serif", Font.BOLD, 22));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void showHelpDialog() {
        HelpScreen helpScreen = new HelpScreen(this);
        helpScreen.setVisible(true);
    }
}
