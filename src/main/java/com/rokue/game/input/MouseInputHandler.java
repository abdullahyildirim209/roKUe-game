package com.rokue.game.input;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPanel;

import com.rokue.game.entities.OtherObjects;
import com.rokue.game.entities.Prop;
import com.rokue.game.map.Hall;
import com.rokue.game.ui.BuildModeDesigner;

public class MouseInputHandler extends MouseAdapter {
    private final Hall[][] halls;
    private final int hallsPerRow;
    private final int scaledTileSize;
    private final int hallSpacing;
    private final int offset = 2;
    private final JPanel gridPanel; // Reference to the grid panel
    private final BuildModeDesigner buildModeDesigner; // Add a reference to BuildModeDesigner


    private String selectedAction = "Place";
    
    private String selectedObject = "Crate";

    public MouseInputHandler(Hall[][] halls, int hallsPerRow, int scaledTileSize, int hallSpacing, JPanel gridPanel, BuildModeDesigner buildModeDesigner) {
        this.halls = halls;
        this.hallsPerRow = hallsPerRow;
        this.scaledTileSize = scaledTileSize;
        this.hallSpacing = hallSpacing;
        this.gridPanel = gridPanel; // Store reference to grid panel
        this.buildModeDesigner = buildModeDesigner;
    }

    
    @Override
    public void mouseClicked(MouseEvent e) {
        int x = e.getX(); // Raw mouse X position
        int y = e.getY(); // Raw mouse Y position
    
        // Calculate which hall and tile were clicked
        int hallX = (x - 0) / (Hall.tiles * scaledTileSize + hallSpacing); // Adjust offsets
        int hallY = (y - 0) / (Hall.tiles * scaledTileSize + hallSpacing);
    
        if (hallX >= 0 && hallX < hallsPerRow && hallY >= 0 && hallY < hallsPerRow) {
            Hall clickedHall = halls[hallY][hallX];
            int xOffset = hallX * (Hall.tiles * scaledTileSize + hallSpacing);
            int yOffset = hallY * (Hall.tiles * scaledTileSize + hallSpacing);
            int localTileX = (x - xOffset) / scaledTileSize;
            int localTileY = (y - yOffset) / scaledTileSize;
    
            // Ensure the local coordinates are valid
            if (localTileX >= 0 && localTileX < Hall.getTiles() && localTileY >= 0 && localTileY < Hall.getTiles()) {
                if ("Place".equals(buildModeDesigner.getSelectedAction())) {
                    // Check if the tile is empty before placing
                    if (clickedHall.getGrid()[localTileX][localTileY] == null) {
                        if ("Crate".equals(selectedObject)) {
                            new Prop(4).place(localTileX, localTileY, clickedHall); // Place a crate
                        } else {
                            new OtherObjects("sprites/objects/" + selectedObject + ".png")
                                .place(localTileX, localTileY, clickedHall); // Place another object
                        }
                        System.out.println("Placed object: " + selectedObject + " at (" + localTileX + ", " + localTileY + ")");
                    } else {
                        System.out.println("Tile already occupied at: (" + localTileX + ", " + localTileY + ")");
                    }
                    gridPanel.revalidate();
                    gridPanel.repaint();
    
                } else if ("Remove".equals(buildModeDesigner.getSelectedAction())) {
                    // Check if there is an object to remove
                    if (clickedHall.getGrid()[localTileX][localTileY] != null) {
                        System.out.println("Removing object at: (" + localTileX + ", " + localTileY + ")");
                        clickedHall.getGrid()[localTileX][localTileY] = null; // Clear the grid cell
    
                        // Remove corresponding prop if any
                        clickedHall.getProps().removeIf(
                            prop -> prop.getXPosition() == localTileX && prop.getYPosition() == localTileY
                        );
    
                        gridPanel.revalidate();
                        gridPanel.repaint();
                    } else {
                        System.out.println("No object to remove at: (" + localTileX + ", " + localTileY + ")");
                    }
                }
            } else {
                System.out.println("Clicked outside valid grid area.");
            }
        } else {
            System.out.println("Clicked outside hall boundaries.");
        }
    }
    
    

    


    // Setters for selected action and object
    public void setSelectedAction(String selectedAction) {
        this.selectedAction = selectedAction;
    }

    public void setSelectedObject(String selectedObject) {
        this.selectedObject = selectedObject;
    }
}
