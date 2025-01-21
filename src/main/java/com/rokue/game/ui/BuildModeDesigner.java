package com.rokue.game.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import com.rokue.game.entities.Hero;
import com.rokue.game.entities.Prop;
import com.rokue.game.map.Hall;

public class BuildModeDesigner extends JPanel {
    private Hall[][] halls; // 2x2 grid of halls
    private String selectedAction = "Place"; // Default action
    private int objectID = 2; // Default object = Crate
    private final int scale = 1; // Adjusted scale
    private final int scaledTileSize = Hall.getPixelsPerTile() * scale;
    private final int hallSpacing = 50; // Spacing between grids
    private final Image backGround = new ImageIcon(PlayPanel.class.getResource("/sprites/build/hall.png")).getImage();
    private final int hallsPerRow = 2; // Number of halls per row
    private final String[][] hallLabels = {{"HallWater", "HallEarth"}, {"HallFire", "HallAir"}};
    private final int[] requiredObjects = {6, 9, 13, 17}; // Minimum required objects per hall
    private final String[] hallNames = {"Water", "Earth", "Fire", "Air"};
    private JLabel[] hallStatusLabels = new JLabel[4]; // Labels for hall status

    private JPanel gridPanel;
    private JPanel statusPanel;
    private boolean buildFinished = false;
    private SpriteLoader spriteHandler = new SpriteLoader();

    public BuildModeDesigner(Hall[][] halls) {
        this.halls = halls;
        this.setLayout(new BorderLayout());

        // Grid Panel
        this.gridPanel = new JPanel() {
            private final Image backgroundImg = new ImageIcon(Hero.class.getResource("/sprites/build/build_background.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
                drawGrid(g);
            }
        };

        gridPanel.setPreferredSize(new Dimension(
                hallsPerRow * Hall.getTiles() * scaledTileSize + hallSpacing * (hallsPerRow - 1),
                hallsPerRow * Hall.getTiles() * scaledTileSize + hallSpacing * (hallsPerRow - 1)));
        gridPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleGridClick(e);
                updateHallStatus(); // Update hall statuses dynamically
            }
        });

        // Right Panel
        JPanel rightPanel = createRightPanel();
        updateHallStatus();
        // Add panels to the main layout
        this.add(gridPanel, BorderLayout.CENTER);
        this.add(rightPanel, BorderLayout.EAST);
    }

    private JPanel createRightPanel() {
        JPanel rightPanel = new JPanel() {
            private final Image backgroundImage = new ImageIcon(Hero.class.getResource("/sprites/build/build_mode_crate.png")).getImage();
    
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
    
        rightPanel.setPreferredSize(new Dimension(200, Hall.getTiles() * scaledTileSize * hallsPerRow));
        rightPanel.setLayout(new GridBagLayout());
    
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER;
    
        // Add Action Buttons Section
        gbc.gridy = 0;
        gbc.weighty = 0;
        JLabel label = new JLabel("ACTIONS");
        label.setForeground(Color.WHITE);
        rightPanel.add(label, gbc);
    
        gbc.gridy++;
        JButton placeButton = new JButton("Place");
        placeButton.addActionListener(e -> selectedAction = "Place");
        placeButton.setForeground(Color.WHITE);
        placeButton.setContentAreaFilled(false);
        rightPanel.add(placeButton, gbc);
    
        gbc.gridy++;
        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> selectedAction = "Remove");
        removeButton.setForeground(Color.WHITE);
        removeButton.setContentAreaFilled(false);
        rightPanel.add(removeButton, gbc);
    
        gbc.gridy++;
        JButton resetButton = new JButton("Reset Hall");
        resetButton.addActionListener(e -> resetHalls());
        resetButton.setForeground(Color.WHITE);
        resetButton.setContentAreaFilled(false);
        rightPanel.add(resetButton, gbc);
    
        gbc.gridy++;
        JButton finishButton = new JButton("Finish Build Mode");
        finishButton.addActionListener(e -> checkBuildCompletion());
        finishButton.setForeground(Color.WHITE);
        finishButton.setContentAreaFilled(false);
        rightPanel.add(finishButton, gbc);
    
        // Add Objects Section
        gbc.gridy++;
        gbc.weighty = 0; // No extra space for objects
        JLabel objectLabel = new JLabel("OBJECTS");
        objectLabel.setForeground(Color.WHITE);
        rightPanel.add(objectLabel, gbc);
    
        int[] objectIDs = {2, 3, 4, 5, 6, 7, 8, 9, 10, 12, 12};
    
        for (Integer oID : objectIDs) {
            gbc.gridy++;
            ImageIcon objectIcon = new ImageIcon(Hero.class.getResource("/sprites/prop/" + (oID * 2) + ".png"));
            JButton objectButton = new JButton(objectIcon);
            objectButton.addActionListener(e -> objectID = oID);
            objectButton.setOpaque(false);
            objectButton.setContentAreaFilled(false);
            objectButton.setBorderPainted(false);
            rightPanel.add(objectButton, gbc);
        }
    
        // Add Hall Status Section
        gbc.gridy++;
        gbc.weighty = 0; // Push the status section to the bottom
        gbc.anchor = GridBagConstraints.SOUTH; // Align to the bottom of the panel
        JLabel statusLabel = new JLabel("HALL STATUS");
        statusLabel.setForeground(Color.WHITE);
        statusLabel.setFont(new Font("Serif", Font.BOLD, 14));
        rightPanel.add(statusLabel, gbc);
    
        for (int i = 0; i < hallsPerRow * hallsPerRow; i++) {
            gbc.gridy++;
            gbc.weighty = 0;
            JLabel hallStatusLabel = new JLabel();
            hallStatusLabel.setForeground(Color.WHITE);
            hallStatusLabel.setFont(new Font("Serif", Font.PLAIN, 12)); // Slightly smaller font for clarity
            hallStatusLabels[i] = hallStatusLabel;
            rightPanel.add(hallStatusLabel, gbc);
        }
    
        return rightPanel;
    }
    
    
    
    

    private void updateHallStatus() {
        for (int i = 0; i < hallsPerRow * hallsPerRow; i++) {
            int row = i / hallsPerRow;
            int col = i % hallsPerRow;

            Hall hall = halls[row][col];
            int currentObjectCount = hall.getProps().size();

            hallStatusLabels[i].setText(String.format("Hall %s: Required: %d, Found: %d",
                    hallNames[i], requiredObjects[i], currentObjectCount));
        }
    }

    private void handleGridClick(MouseEvent e) {
        int x = e.getX();
        int y = e.getY();

        int totalWidth = hallsPerRow * (Hall.getTiles() * scaledTileSize) + hallSpacing * (hallsPerRow - 1);
        int totalHeight = hallsPerRow * (Hall.getTiles() * scaledTileSize) + hallSpacing * (hallsPerRow - 1);

        int xCenterOffset = (gridPanel.getWidth() - totalWidth) / 2;
        int yCenterOffset = (gridPanel.getHeight() - totalHeight) / 2;

        int adjustedX = x - xCenterOffset;
        int adjustedY = y - yCenterOffset;

        int hallX = adjustedX / (Hall.getTiles() * scaledTileSize + hallSpacing);
        int hallY = adjustedY / (Hall.getTiles() * scaledTileSize + hallSpacing);

        if (hallX >= 0 && hallX < hallsPerRow && hallY >= 0 && hallY < hallsPerRow) {
            int xOffset = hallX * (Hall.getTiles() * scaledTileSize + hallSpacing);
            int yOffset = hallY * (Hall.getTiles() * scaledTileSize + hallSpacing);

            int localX = (adjustedX - xOffset) / scaledTileSize;
            int localY = (adjustedY - yOffset) / scaledTileSize + 2;

            Hall hall = halls[hallY][hallX];

            if (localX > 0 && localX < Hall.getTiles() - 1 && localY > 0 + 2 && localY < Hall.getTiles() - 1 + 1) {
                if ("Place".equals(selectedAction)) {
                    hall.getGrid()[localX][localY] = null;
                    hall.getProps().removeIf(entity -> entity.getXPosition() == localX && entity.getYPosition() == localY);
                    new Prop(objectID).place(localX, localY, hall);
                } else if ("Remove".equals(selectedAction)) {
                    hall.getGrid()[localX][localY] = null;
                    hall.getProps().removeIf(entity -> entity.getXPosition() == localX && entity.getYPosition() == localY);
                }
                
                // Update "Found" count dynamically
                int currentObjectCount = hall.getProps().size();
                hallStatusLabels[hallY * hallsPerRow + hallX].setText(
                    String.format("Hall %s: Required: %d, Found: %d",
                        hallNames[hallY * hallsPerRow + hallX], requiredObjects[hallY * hallsPerRow + hallX], currentObjectCount)
                );
    
                repaint(); // Redraw the grid and halls
            }
        }
        updateHallStatus();
    }
    

    private void resetHalls() {
        for (Hall[] row : halls) {
            for (Hall hall : row) {
                hall.getProps().clear();
                for (int x = 0; x < Hall.getTiles(); x++) {
                    for (int y = 0; y < Hall.getTiles(); y++) {
                        hall.getGrid()[x][y] = null;
                    }
                }
            }
        }
        repaint();
        updateHallStatus();
    }


    private void checkBuildCompletion() {
        boolean valid = true;
        StringBuilder errorMessages = new StringBuilder("");

        for (int i = 0; i < hallsPerRow * hallsPerRow; i++) {
            int row = i / hallsPerRow;
            int col = i % hallsPerRow;

            Hall hall = halls[row][col];
            int currentObjectCount = hall.getProps().size();

            if (currentObjectCount < requiredObjects[i]) {
                valid = false;
                errorMessages.append(String.format("\nHall %s: Required: %d, Found: %d",
                        hallNames[i], requiredObjects[i], currentObjectCount));
                errorMessages.append("<br>"); // Add line break for HTML formatting
            }
        }

        if (valid) {
            buildFinished = true;
        } else {
            // Create a custom dialog
            JDialog dialog = new JDialog((JFrame) null, "Build Mode Incomplete", true);
            dialog.setSize(400, 300);
            dialog.setLocationRelativeTo(this);

            // Custom panel with a background image
            JPanel customPanel = new JPanel() {
                private final Image background = new ImageIcon(getClass().getResource("/sprites/ErrorMessage.png")).getImage();

                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(background, 0, 0, getWidth(), getHeight(), this);
                }
            };

            customPanel.setLayout(new BorderLayout());

            // Add error messages to the custom panel
            JLabel messageLabel = new JLabel("<html>" + errorMessages.toString() + "</html>");
            messageLabel.setForeground(Color.WHITE);
            messageLabel.setFont(new Font("Serif", Font.BOLD, 16));
            messageLabel.setHorizontalAlignment(SwingConstants.CENTER);
            messageLabel.setVerticalAlignment(SwingConstants.CENTER);

            // Add text and a button to the panel
            customPanel.add(messageLabel, BorderLayout.CENTER);

            JButton closeButton = new JButton("Close");
            closeButton.setFont(new Font("Serif", Font.BOLD, 16));
            closeButton.setForeground(Color.WHITE); // Set the text color
            closeButton.setContentAreaFilled(false); // Make the button background transparent
            closeButton.setBorderPainted(false); // Remove the border
            closeButton.setFocusPainted(false); // Remove focus border highlight
            closeButton.addActionListener(e -> dialog.dispose());

            JPanel buttonPanel = new JPanel();
            buttonPanel.setOpaque(false); // Transparent background for the button panel
            buttonPanel.add(closeButton);

            customPanel.add(buttonPanel, BorderLayout.SOUTH);

            dialog.add(customPanel);
            dialog.setVisible(true);

        }
    }





    public boolean getBuildFinished() {
        return buildFinished;
    }

    public Hall[][] getHalls() {
        return halls;
    }

   private void drawGrid(Graphics g) {
    Graphics2D g2 = (Graphics2D) g;

    // Calculate the total grid dimensions
    int totalWidth = hallsPerRow * (Hall.getTiles() * scaledTileSize) + hallSpacing * (hallsPerRow - 1);
    int totalHeight = hallsPerRow * (Hall.getTiles() * scaledTileSize) + hallSpacing * (hallsPerRow - 1);

    // Calculate the x and y offsets to center the grid
    int xCenterOffset = (gridPanel.getWidth() - totalWidth) / 2; // Center horizontally
    int yCenterOffset = (gridPanel.getHeight() - totalHeight) / 2; // Center vertically

    for (int row = 0; row < hallsPerRow; row++) {
        for (int col = 0; col < hallsPerRow; col++) {
            // Adjust xOffset and yOffset with center offsets
            int xOffset = xCenterOffset + col * (Hall.getTiles() * scaledTileSize + hallSpacing);
            int yOffset = yCenterOffset + row * (Hall.getTiles() * scaledTileSize + hallSpacing);

            Image hallLabel = new ImageIcon(PlayPanel.class.getResource("/sprites/build/" + hallLabels[row][col] + ".png")).getImage();
            g2.drawImage(hallLabel, xOffset + Hall.getTiles() * scaledTileSize / 3, yOffset + Hall.getTiles() * scaledTileSize, null);

            // Draw hall background
            g2.drawImage(backGround, xOffset, yOffset,
                    Hall.getTiles() * scaledTileSize, Hall.getTiles() * scaledTileSize, null);

            // Draw grid lines
            for (int y = 0; y < Hall.getTiles(); y++) {
                for (int x = 0; x < Hall.getTiles(); x++) {
                    g2.setColor(new Color(255, 255, 255, 50));
                    g2.drawRect(x * scaledTileSize + xOffset, y * scaledTileSize + yOffset,
                            scaledTileSize, scaledTileSize);
                }
            }

            // Draw entities in the current hall
            Hall hall = halls[row][col];
            for (Prop prop : hall.getProps()) {
                g2.drawImage(prop.getSprite(spriteHandler),
                        prop.getXPixelPosition() * scale + xOffset,
                        (prop.getYPixelPosition() - Hall.getPixelsPerTile()) * scale + yOffset,
                        scaledTileSize, scaledTileSize, null);
            }

        }
    }
}

}
