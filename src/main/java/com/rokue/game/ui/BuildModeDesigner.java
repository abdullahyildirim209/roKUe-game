package com.rokue.game.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

import com.rokue.game.entities.Entity;
import com.rokue.game.input.MouseInputHandler;
import com.rokue.game.map.Hall;
import com.rokue.game.utils.RNG;

public class BuildModeDesigner extends JPanel {
    private final Hall[][] halls;
    private final int hallsPerRow = 2;
    private final int scale = 1;
    private final int scaledTileSize = Hall.getPixelsPerTile() * scale;
    private final int hallSpacing = 50;

    private String selectedAction = "Place";
    private String selectedObject = "Crate";


    

    public BuildModeDesigner(Hall[][] halls) {
        this.halls = halls;
        this.setLayout(new BorderLayout());
    
        // Grid Panel
        JPanel gridPanel = new JPanel() {
            private final Image backgroundImg = new ImageIcon(getClass().getResource("/sprites/objects/build_background.png")).getImage();
    
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
                drawGrid(g);
            }
        };
    
        gridPanel.setPreferredSize(new Dimension(
                hallsPerRow * Hall.tiles * scaledTileSize + hallSpacing * (hallsPerRow - 1),
                hallsPerRow * Hall.tiles * scaledTileSize + hallSpacing * (hallsPerRow - 1)
        ));
    
        // Create MouseInputHandler with a reference to gridPanel
        MouseInputHandler mouseInputHandler = new MouseInputHandler(halls, 2, Hall.getPixelsPerTile(), 50, gridPanel, this);
        gridPanel.addMouseListener(mouseInputHandler);
    
        // Right Panel
        JPanel rightPanel = createRightPanel();
    
        // Add panels to the layout
        this.add(gridPanel, BorderLayout.CENTER);
        this.add(rightPanel, BorderLayout.EAST);
    }
    

    private void drawGrid(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        SpriteLoader spriteLoader = new SpriteLoader(); // Create or pass an existing SpriteLoader instance
    
        for (int row = 0; row < hallsPerRow; row++) {
            for (int col = 0; col < hallsPerRow; col++) {
                Hall hall = halls[row][col];
                int xOffset = col * (Hall.tiles * scaledTileSize + hallSpacing);
                int yOffset = row * (Hall.tiles * scaledTileSize + hallSpacing);
    
                // Draw grid lines
                g2.setColor(new Color(255, 255, 255, 50));
                for (int y = 0; y < Hall.tiles; y++) {
                    for (int x = 0; x < Hall.tiles; x++) {
                        g2.drawRect(x * scaledTileSize + xOffset, y * scaledTileSize + yOffset, scaledTileSize, scaledTileSize);
    
                        // Draw objects in the grid
                        Entity entity = hall.getGrid()[x][y];
                        if (entity != null) {
                            g2.drawImage(entity.getSprite(spriteLoader), // Pass SpriteLoader instance
                                    x * scaledTileSize + xOffset, 
                                    y * scaledTileSize + yOffset, 
                                    scaledTileSize, 
                                    scaledTileSize, 
                                    null);
                        }
                    }
                }
            }
        }
    }
    
    public String getSelectedAction() {
        return selectedAction;
    }

    public void setSelectedAction(String selectedAction) {
        this.selectedAction = selectedAction;
        System.out.println("selectedAction set to: " + selectedAction);
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel() {
            private final Image backgroundImage = new ImageIcon(getClass().getResource("/sprites/objects/build_mode_crate.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };

        rightPanel.setPreferredSize(new Dimension(200, 600));
        rightPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // "ACTIONS" Label
        gbc.gridy = 0;
        JLabel actionLabel = new JLabel("ACTIONS");
        actionLabel.setForeground(Color.WHITE);
        rightPanel.add(actionLabel, gbc);

        // Place Button
// Place Button
gbc.gridy++;
JButton placeButton = new JButton("Place");
placeButton.addActionListener(e -> {
    selectedAction = "Place";
    System.out.println("Action set to Place"); // Debug log
});
placeButton.setForeground(Color.WHITE);
placeButton.setContentAreaFilled(false);
rightPanel.add(placeButton, gbc);

// Remove Button
gbc.gridy++;
JButton removeButton = new JButton("Remove");
removeButton.addActionListener(e -> {
    this.selectedAction = "Remove";
    System.out.println("Action set to Remove"); // Debug log
});
removeButton.setForeground(Color.WHITE);
removeButton.setContentAreaFilled(false);
rightPanel.add(removeButton, gbc);

// Reset Button
gbc.gridy++;
JButton resetButton = new JButton("Reset Hall");
resetButton.addActionListener(e -> {
    resetHalls();
    System.out.println("Reset Hall triggered"); // Debug log
});
resetButton.setForeground(Color.WHITE);
resetButton.setContentAreaFilled(false);
rightPanel.add(resetButton, gbc);

// Finish Button
gbc.gridy++;
JButton finishButton = new JButton("Finish Build Mode");
finishButton.addActionListener(e -> {
    checkBuildCompletion((JFrame) SwingUtilities.getWindowAncestor(this));
    System.out.println("Finish Build Mode triggered"); // Debug log
});
finishButton.setForeground(Color.WHITE);
finishButton.setContentAreaFilled(false);
rightPanel.add(finishButton, gbc);


        // "OBJECTS" Label
        gbc.gridy++;
        JLabel objectLabel = new JLabel("OBJECTS");
        objectLabel.setForeground(Color.WHITE);
        rightPanel.add(objectLabel, gbc);

        // Crate Button
        gbc.gridy++;
        ImageIcon crateIcon = new ImageIcon(getClass().getResource("/sprites/objects/Crate.png"));
        JButton crateButton = new JButton(crateIcon);
        crateButton.addActionListener(e -> selectedObject = "Crate");
        crateButton.setContentAreaFilled(false);
        crateButton.setBorderPainted(false);
        rightPanel.add(crateButton, gbc);

        // Dynamic Object Buttons
        String[] imgNames = {"blue_flag", "green_flag", "yellow_flag", "red_flag", "earth_decorate",
                "fire_decorate", "water_decorate", "head", "ladder", "pillar"};

        for (String imgName : imgNames) {
            gbc.gridy++;
            ImageIcon objectIcon = new ImageIcon(getClass().getResource("/sprites/objects/" + imgName + ".png"));
            JButton objectButton = new JButton(objectIcon);
            objectButton.addActionListener(e -> selectedObject = imgName);
            objectButton.setOpaque(false);
            objectButton.setContentAreaFilled(false);
            objectButton.setBorderPainted(false);
            rightPanel.add(objectButton, gbc);
        }

        return rightPanel;
    }


    private void resetHalls() {
        for (Hall[] row : halls) {
            for (Hall hall : row) {
                hall.getProps().clear();
                for (int x = 1; x < Hall.tiles-1; x++) {
                    for (int y = 1; y < Hall.tiles-1; y++) {
                        hall.getGrid()[x][y] = null;
                    }
                }
            }
        }
        repaint();
    }

    private void checkBuildCompletion(JFrame parentFrame) {
        int[] requiredObjects = {6, 9, 13, 17};
        String[] hallNames = {"Water", "Earth", "Fire", "Air"};
        boolean valid = true;
        StringBuilder errorMessages = new StringBuilder("The following halls are incomplete:\n\n");

        for (int i = 0; i < hallsPerRow * hallsPerRow; i++) {
            int row = i / hallsPerRow;
            int col = i % hallsPerRow;

            Hall hall = halls[row][col];
            int currentObjectCount = hall.getObjectCount();

            if (currentObjectCount < requiredObjects[i]) {
                valid = false;
                errorMessages.append("Hall of ").append(hallNames[i])
                             .append(" - Required: ").append(requiredObjects[i])
                             .append(", Found: ").append(currentObjectCount)
                             .append("\n");
            }
        }

        if (valid) {
            JOptionPane.showMessageDialog(this, "Build mode completed successfully!", "Success", JOptionPane.INFORMATION_MESSAGE);

            // Combine halls and transition to PlayPanel
            Hall combinedHall = combineHalls();
            enterPlayMode(parentFrame, combinedHall);
        } else {
            JOptionPane.showMessageDialog(this, errorMessages.toString(), "Build Mode Incomplete", JOptionPane.ERROR_MESSAGE);
        }
    }

    private Hall combineHalls() {
        Hall combinedHall = new Hall(new RNG());
        int offsetX, offsetY;

        for (int row = 0; row < hallsPerRow; row++) {
            for (int col = 0; col < hallsPerRow; col++) {
                Hall hall = halls[row][col];
                offsetX = col * Hall.tiles;
                offsetY = row * Hall.tiles;

                for (int x = 0; x < Hall.tiles; x++) {
                    for (int y = 0; y < Hall.tiles; y++) {
                        int combinedX = x + offsetX;
                        int combinedY = y + offsetY;

                        if (hall.getGrid()[x][y] != null) {
                            combinedHall.getGrid()[combinedX][combinedY] = hall.getGrid()[x][y];
                        }
                    }
                }

                combinedHall.getProps().addAll(hall.getProps());
            }
        }

        return combinedHall;
    }

    private void enterPlayMode(JFrame parentFrame, Hall combinedHall) {
        parentFrame.getContentPane().removeAll();

        // Initialize PlayPanel with the combined hall
        PlayPanel playPanel = new PlayPanel(new Hall[]{combinedHall}, new SpriteLoader());

        // Add PlayPanel to the frame
        parentFrame.add(playPanel);
        parentFrame.revalidate();
        parentFrame.repaint();

        // Start the game loop
        playPanel.startGameThread();
    }
}
