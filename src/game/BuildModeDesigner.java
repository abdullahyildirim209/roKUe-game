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
    private final int hallSpacing = 50; // Spacing between grids
    private final Image backGround = new ImageIcon(PlayPanel.class.getResource("sprites/hall.png")).getImage();
    private final int hallsPerRow = 2; // Number of halls per row
    String[][] hallLabels = {{"HallWater", "HallEarth"}, {"HallFire", "HallAir"}};
    private int offset=2;
    private JPanel gridPanel;
    public BuildModeDesigner(Hall[][] halls) {
    	
        this.halls = halls;
        this.setLayout(new BorderLayout());
        // Grid Panel
        this.gridPanel = new JPanel() {
        	private Image backgroundImg = new ImageIcon(Hero.class.getResource("sprites/images/build_background.png")).getImage();
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImg, 0, 0, getWidth(), getHeight(), this);
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
        JPanel rightPanel = new JPanel(){
            private Image backgroundImage = new ImageIcon(Hero.class.getResource("sprites/images/build_mode_crate.png")).getImage();

            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        rightPanel.setPreferredSize(new Dimension(200, Hall.tiles * scaledTileSize * hallsPerRow));
        rightPanel.setLayout(new GridBagLayout());
        //new BoxLayout(rightPanel, BoxLayout.Y_AXIS)
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.anchor = GridBagConstraints.CENTER; // Center alignment

        // Adding Components
        
        gbc.gridy = 0;
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
        
        
        gbc.gridy++;
        JLabel objectLabel = new JLabel("OBJECTS");
        objectLabel.setForeground(Color.WHITE);
        rightPanel.add(objectLabel, gbc);
        
        gbc.gridy++;
        ImageIcon crateIcon = new ImageIcon(Hero.class.getResource("sprites/objects/Crate.png"));
        JButton crateButton = new JButton(crateIcon);
        crateButton.addActionListener(e -> selectedObject = "Crate");
        crateButton.setContentAreaFilled(false);
        crateButton.setBorderPainted(false);
        rightPanel.add(crateButton, gbc);
        
        String[] imgNames = {"blue_flag", "green_flag", "yellow_flag", "red_flag", "earth_decorate",
        		"fire_decorate", "water_decorate", "head", "ladder", "pillar"};
        
        for (String imgName: imgNames) {
        	gbc.gridy++;
            ImageIcon objectIcon = new ImageIcon(Hero.class.getResource("sprites/images/"+imgName+".png"));
            JButton objectButton = new JButton(objectIcon);
            objectButton.addActionListener(e -> selectedObject = imgName);
            objectButton.setOpaque(false);
            objectButton.setContentAreaFilled(false);
            objectButton.setBorderPainted(false);
            rightPanel.add(objectButton, gbc);
        }
        
        // Add panels to the main layout
        this.add(gridPanel, BorderLayout.CENTER);
        this.add(rightPanel, BorderLayout.EAST);
    }
    /*
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
    */
    private void drawGrid(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;

        // Calculate the total grid dimensions
        int totalWidth = hallsPerRow * (Hall.tiles * scaledTileSize) + hallSpacing * (hallsPerRow - 1);
        int totalHeight = hallsPerRow * (Hall.tiles * scaledTileSize) + hallSpacing * (hallsPerRow - 1);

        // Calculate the x and y offsets to center the grid
        int xCenterOffset = (gridPanel.getWidth() - totalWidth) / 2; // Center horizontally
        int yCenterOffset = (gridPanel.getHeight() - totalHeight) / 2; // Center vertically

        for (int row = 0; row < hallsPerRow; row++) {
            for (int col = 0; col < hallsPerRow; col++) {
                // Adjust xOffset and yOffset with center offsets
                int xOffset = xCenterOffset + col * (Hall.tiles * scaledTileSize + hallSpacing);
                int yOffset = yCenterOffset + row * (Hall.tiles * scaledTileSize + hallSpacing);
                
                Image hallLabel = new ImageIcon(PlayPanel.class.getResource("sprites/"+hallLabels[row][col]+".png")).getImage();
                g2.drawImage(hallLabel, xOffset+Hall.tiles * scaledTileSize/3, yOffset+Hall.tiles * scaledTileSize, null);
                // Draw hall background
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
    
    /*
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
                    else {
                    	new OtherObjects("sprites/images/"+selectedObject+".png").place(localX, localY, hall);
                    }
                    
                } else if ("Remove".equals(selectedAction)) {
                    hall.grid[localX][localY] = null;
                    hall.entities.removeIf(entity -> entity.getXPosition() == localX && entity.getYPosition() == localY);
                }
                repaint();
            }
        }
    }
    */
    
    private void handleGridClick(MouseEvent e) {
        int x = e.getX(); // Raw mouse X position
        int y = e.getY(); // Raw mouse Y position

        // Calculate total grid size
        int totalWidth = hallsPerRow * (Hall.tiles * scaledTileSize) + hallSpacing * (hallsPerRow - 1);
        int totalHeight = hallsPerRow * (Hall.tiles * scaledTileSize) + hallSpacing * (hallsPerRow - 1);

        // Calculate center offsets
        int xCenterOffset = (gridPanel.getWidth() - totalWidth) / 2;
        int yCenterOffset = (gridPanel.getHeight() - totalHeight) / 2;

        // Adjust click positions relative to the centered grid
        int adjustedX = x - xCenterOffset;
        int adjustedY = y - yCenterOffset;

        // Calculate which hall is clicked
        int hallX = adjustedX / (Hall.tiles * scaledTileSize + hallSpacing);
        int hallY = adjustedY / (Hall.tiles * scaledTileSize + hallSpacing);

        if (hallX >= 0 && hallX < hallsPerRow && hallY >= 0 && hallY < hallsPerRow) {
            // Get offsets for the specific hall
            int xOffset = hallX * (Hall.tiles * scaledTileSize + hallSpacing);
            int yOffset = hallY * (Hall.tiles * scaledTileSize + hallSpacing);

            // Calculate local grid coordinates within the selected hall
            int localX = (adjustedX - xOffset) / scaledTileSize;
            int localY = (adjustedY - yOffset) / scaledTileSize + 2;

            Hall hall = halls[hallY][hallX];

            // Ensure localX and localY are within bounds of the hall grid
            if (localX > 0 && localX < Hall.tiles - 1 && localY > 0 + 2 && localY < Hall.tiles - 1 + 1) {
                if ("Place".equals(selectedAction)) {
                    if ("Crate".equals(selectedObject)) {
                        new Crate().place(localX, localY, hall);
                    } else {
                        new OtherObjects("sprites/images/" + selectedObject + ".png").place(localX, localY, hall);
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
        String[] hallNames= {"Water", "Earth", "Fire", "Air"};
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
                errorMessages.append("Hall of ").append(hallNames[i])
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
