package game;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.*;

public class BuildModeDesigner extends JPanel {
    private Hall[][] halls; // 2x2 grid of halls
    private String selectedAction = "Place"; // Default action
    private String selectedObject = "Crate"; // Default object
    private final int scale = 1; // Adjusted scale
    private final int scaledTileSize = Hall.pixelsPerTile * scale;
    private final int hallSpacing = 200; // Spacing between grids
    private final Image backGround = new ImageIcon(PlayPanel.class.getResource("sprites/hall.png")).getImage();
    private final int hallsPerRow = 2; // Number of halls per row
    private int offset=2;

    public BuildModeDesigner(Hall[][] halls) {
        this.halls = halls;
        this.setLayout(new BorderLayout());

        // Grid Panel
        JPanel gridPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawGrid(g);
            }
        };
        gridPanel.setPreferredSize(new Dimension(
                hallsPerRow * Hall.tiles * scaledTileSize + hallSpacing * (hallsPerRow - 1),
                hallsPerRow * Hall.tiles * scaledTileSize + hallSpacing * (hallsPerRow - 1)));
        gridPanel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                handleGridClick(e);
            }
        });

        // Right Panel
        JPanel rightPanel = new JPanel();
        rightPanel.setPreferredSize(new Dimension(200, Hall.tiles * scaledTileSize * hallsPerRow));
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));

        JLabel label = new JLabel("Actions");
        rightPanel.add(label);

        JButton placeButton = new JButton("Place");
        placeButton.addActionListener(e -> selectedAction = "Place");
        rightPanel.add(placeButton);

        JButton removeButton = new JButton("Remove");
        removeButton.addActionListener(e -> selectedAction = "Remove");
        rightPanel.add(removeButton);

        JButton resetButton = new JButton("Reset Hall");
        resetButton.addActionListener(e -> resetHalls());
        rightPanel.add(resetButton);

        JButton finishButton = new JButton("Finish Build Mode");
        finishButton.addActionListener(e -> checkBuildCompletion());
        rightPanel.add(finishButton);

        JLabel objectLabel = new JLabel("Objects");
        rightPanel.add(objectLabel);

        JButton crateButton = new JButton("Crate");
        crateButton.addActionListener(e -> selectedObject = "Crate");
        rightPanel.add(crateButton);

        // Add panels to the main layout
        this.add(gridPanel, BorderLayout.CENTER);
        this.add(rightPanel, BorderLayout.EAST);
    }

    private void drawGrid(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        for (int row = 0; row < hallsPerRow; row++) {
            for (int col = 0; col < hallsPerRow; col++) {
                int xOffset = col * (Hall.tiles * scaledTileSize + hallSpacing);
                int yOffset = row * (Hall.tiles * scaledTileSize + hallSpacing);

                // Draw background
                g2.drawImage(backGround, xOffset, yOffset,
                        Hall.tiles * scaledTileSize, Hall.tiles * scaledTileSize, null);

                // Draw grid lines
                for (int y = 0; y < Hall.tiles; y++) {
                    for (int x = 0; x < Hall.tiles; x++) {
                        g2.setColor(new Color(255, 255, 255, 50));
                        g2.drawRect(x * scaledTileSize + xOffset, y * scaledTileSize + yOffset,
                                scaledTileSize, scaledTileSize);
                    }
                }

                // Draw entities in the current hall
                Hall hall = halls[row][col];
                for (Entity entity : hall.entities) {
                    g2.drawImage(entity.getSprite(),
                            entity.getXPixelPosition() * scale + xOffset,
                            (entity.getYPixelPosition() - Hall.pixelsPerTile) * scale + yOffset,
                            scaledTileSize, scaledTileSize, null);
                }
            }
        }
    }

    private void handleGridClick(MouseEvent e) {
        int x = e.getX(); // Raw mouse X position
        int y = e.getY(); // Raw mouse Y position
    
        // Calculate which hall is clicked
        int hallX = x / (Hall.tiles * scaledTileSize + hallSpacing);
        int hallY = y / (Hall.tiles * scaledTileSize + hallSpacing);
    
        if (hallX < hallsPerRow && hallY < hallsPerRow) {
            // Get offsets for the specific hall
            int xOffset = hallX * (Hall.tiles * scaledTileSize + hallSpacing);
            int yOffset = hallY * (Hall.tiles * scaledTileSize + hallSpacing);
    
            // Calculate local grid coordinates within the selected hall
            int localX = (x - xOffset) / scaledTileSize;
            int localY = (y - yOffset) / scaledTileSize+2;
    
            Hall hall = halls[hallY][hallX];
    
            // Ensure localX and localY are within bounds of the hall grid
            if (localX > 0 && localX < Hall.tiles - 1 && localY > 0+2 && localY < Hall.tiles - 1+1) {
                if ("Place".equals(selectedAction)) {
                    if ("Crate".equals(selectedObject)) {
                        new Crate().place(localX, localY, hall);
                    }
                } else if ("Remove".equals(selectedAction)) {
                    hall.grid[localX][localY] = null;
                    hall.entities.removeIf(entity -> entity.getXPosition() == localX && entity.getYPosition() == localY);
                }
                repaint();
            }
        }
    }
    

    private void resetHalls() {
        for (Hall[] row : halls) {
            for (Hall hall : row) {
                hall.entities.clear();
                for (int x = 0; x < Hall.tiles; x++) {
                    for (int y = 0; y < Hall.tiles; y++) {
                        hall.grid[x][y] = null;
                    }
                }
            }
        }
        repaint();
    }

    private void checkBuildCompletion() {
        // Minimum required object counts for each hall
        int[] requiredObjects = {6, 9, 13, 17};
    
        boolean valid = true; // Assume all halls are valid initially
        StringBuilder errorMessages = new StringBuilder("The following halls are incomplete:\n\n");
    
        for (int i = 0; i < hallsPerRow * hallsPerRow; i++) {
            int row = i / hallsPerRow;
            int col = i % hallsPerRow;
    
            Hall hall = halls[row][col];
            int currentObjectCount = hall.getObjectCount()-68;
    
            // Check if the current hall meets the required object count
            if (currentObjectCount < requiredObjects[i]) {
                valid = false;
                errorMessages.append("Hall ").append(i + 1)
                             .append(" - Required: ").append(requiredObjects[i])
                             .append(", Found: ").append(currentObjectCount)
                             .append("\n");
            }
        }
    
        // Show a single error message with all incomplete halls
        if (valid) {
            JOptionPane.showMessageDialog(this, "Build mode completed successfully!",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, errorMessages.toString(),
                    "Build Mode Incomplete", JOptionPane.ERROR_MESSAGE);
        }
    }
     
}
