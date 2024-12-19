package game;

import java.awt.*;
import javax.swing.*;

public class BuildGameMode extends JFrame {
    private final int GRID_SIZE = 16; // Active placement grid
    private final Hall[] halls = new Hall[4]; // Four hall objects
    private final String[] hallNames = {"Hall of Earth", "Hall of Air", "Hall of Water", "Hall of Fire"};
    private final Image[] hallBackgrounds = {
        new ImageIcon(BuildGameMode.class.getResource("sprites/hall.png")).getImage(),
        new ImageIcon(BuildGameMode.class.getResource("sprites/hall.png")).getImage(),
        new ImageIcon(BuildGameMode.class.getResource("sprites/hall.png")).getImage(),
        new ImageIcon(BuildGameMode.class.getResource("sprites/hall.png")).getImage()
    };

    private JPanel mainPanel;
    private JPanel rightPanel;
    private boolean removeMode = false; // Toggle for removal mode
    private String selectedObject = "Crate"; // Default object for placement
    
    public BuildGameMode() {
        // Initialize halls
        for (int i = 0; i < halls.length; i++) {
            halls[i] = new Hall();
        }

        setTitle("Build Game Mode");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        
        // Main panel containing all halls
        mainPanel = new JPanel(new GridLayout(2, 2, 10, 10)); // 2x2 grid layout for four halls
        for (int i = 0; i < halls.length; i++) {
            mainPanel.add(createHallPanel(i));
        }
        add(mainPanel, BorderLayout.CENTER);

        // Right panel for tools
        setupRightPanel();
        add(rightPanel, BorderLayout.EAST);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JPanel createHallPanel(int hallIndex) {
        JPanel hallPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(hallBackgrounds[hallIndex], 0, 0, getWidth(), getHeight(), null);
            }
        };
        hallPanel.setLayout(new GridLayout(GRID_SIZE, GRID_SIZE));

        Hall currentHall = halls[hallIndex];
        for (int y = 1; y <= GRID_SIZE; y++) {
            for (int x = 1; x <= GRID_SIZE; x++) {
                JButton gridButton = new JButton();
                gridButton.setPreferredSize(new Dimension(Hall.pixelsPerTile, Hall.pixelsPerTile));
                gridButton.setOpaque(false);
                gridButton.setContentAreaFilled(false);
                gridButton.setBorderPainted(false);

                // Update button appearance based on the presence of objects
                if (currentHall.grid[x][y] != null) {
                    gridButton.setIcon(new ImageIcon(currentHall.grid[x][y].getSprite()));
                } else {
                    gridButton.setIcon(null); // Clear the icon if no object
                }

                int finalX = x, finalY = y;
                gridButton.addActionListener(e -> {
                    if (removeMode) {
                        currentHall.grid[finalX][finalY] = null;
                    } else {
                        Entity object = createEntity(selectedObject);
                        currentHall.grid[finalX][finalY] = object;
                    }
                    refreshHallPanel(hallPanel, currentHall);
                });

                hallPanel.add(gridButton);
            }
        }

        hallPanel.setBorder(BorderFactory.createTitledBorder(hallNames[hallIndex]));
        return hallPanel;
    }

    private void setupRightPanel() {
        rightPanel = new JPanel();
        rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
        rightPanel.setPreferredSize(new Dimension(200, getHeight()));
        rightPanel.setBorder(BorderFactory.createTitledBorder("Tools"));
    
        // Add object selection buttons
        JLabel toolsLabel = new JLabel("Select Object:");
        toolsLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        rightPanel.add(toolsLabel);
    
        JButton crateButton = new JButton("Crate");
        crateButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        crateButton.addActionListener(e -> {
            selectedObject = "Crate";
            removeMode = false;
        });
        rightPanel.add(crateButton);
    
        // Add remove mode toggle button
        JButton removeButton = new JButton("Remove Mode");
        removeButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        removeButton.addActionListener(e -> removeMode = true);
        rightPanel.add(removeButton);
    
        // Add a separator
        rightPanel.add(Box.createVerticalStrut(10));
        rightPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
    
        // Add Reset Hall button
        JButton resetButton = new JButton("Reset Hall");
        resetButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        resetButton.addActionListener(e -> resetCurrentHall());
        rightPanel.add(resetButton);
    
        // Add Finish button
        JButton finishButton = new JButton("Finish Build Mode");
        finishButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        finishButton.addActionListener(e -> validateAndFinishBuildMode());
        rightPanel.add(finishButton);
    }
    private void validateAndFinishBuildMode() {
        // Minimum object requirements for each hall
        int[] minObjectsPerHall = {6, 9, 13, 17}; // Corresponding to Earth, Air, Water, Fire
    
        for (int i = 0; i < halls.length; i++) {
            // Subtract 68 (border tiles) from the object count
            int validObjectCount = halls[i].getObjectCount() - 68;
    
            if (validObjectCount < minObjectsPerHall[i]) {
                // Show error message if a hall does not meet the requirement
                JOptionPane.showMessageDialog(this,
                        "The " + hallNames[i] + " does not have enough objects.\n" +
                        "It needs at least " + minObjectsPerHall[i] + " objects in the inner grid.\n" +
                        "Current valid objects: " + validObjectCount,
                        "Build Mode Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }
        }
    
        // All halls meet the requirements
        JOptionPane.showMessageDialog(this,
                "All halls are valid! Exiting build mode.",
                "Build Mode Complete",
                JOptionPane.INFORMATION_MESSAGE);
    
        // Proceed to the next part of the game (e.g., Play Mode or closing the window)
        System.out.println("Build Mode finished successfully. Proceeding to Play Mode...");
        this.dispose(); // Close the build mode window
    }
    

    private void resetCurrentHall() {
        // Resetting all halls 
        for(int selectedIndex=0; selectedIndex<4;selectedIndex++){
            halls[selectedIndex] = new Hall();
            refreshHallPanel(mainPanel.getComponent(selectedIndex), halls[selectedIndex]);
        }
        
        
    }

    private void refreshHallPanel(Component hallComponent, Hall currentHall) {
        JPanel hallPanel = (JPanel) hallComponent;
        hallPanel.removeAll();

        for (int y = 1; y <= GRID_SIZE; y++) {
            for (int x = 1; x <= GRID_SIZE; x++) {
                JButton gridButton = new JButton();
                gridButton.setPreferredSize(new Dimension(Hall.pixelsPerTile, Hall.pixelsPerTile));
                gridButton.setOpaque(false);
                gridButton.setContentAreaFilled(false);
                gridButton.setBorderPainted(false);

                // Update button appearance based on the presence of objects
                if (currentHall.grid[x][y] != null) {
                    gridButton.setIcon(new ImageIcon(currentHall.grid[x][y].getSprite()));
                } else {
                    gridButton.setIcon(null); // Clear the icon if no object
                }

                int finalX = x, finalY = y;
                gridButton.addActionListener(e -> {
                    if (removeMode) {
                        currentHall.grid[finalX][finalY] = null;
                    } else {
                        Entity object = createEntity(selectedObject);
                        currentHall.grid[finalX][finalY] = object;
                    }
                    refreshHallPanel(hallPanel, currentHall);
                });

                hallPanel.add(gridButton);
            }
        }

        hallPanel.revalidate();
        hallPanel.repaint();
    }

    private Entity createEntity(String objectType) {
        // Factory for creating objects
        switch (objectType) {
            case "Crate":
                return new Crate();
            // Add more objects as needed
            default:
                return null;
        }
    }

    

    public static void main(String[] args) {
        SwingUtilities.invokeLater(BuildGameMode::new);
    }
}
