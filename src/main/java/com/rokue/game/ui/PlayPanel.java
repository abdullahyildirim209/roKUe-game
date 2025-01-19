package com.rokue.game.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.rokue.game.entities.Archer;
import com.rokue.game.entities.Arrow;
import com.rokue.game.entities.Character;
import com.rokue.game.entities.Entity;
import com.rokue.game.entities.Hero;
import com.rokue.game.input.Keyboard;
import com.rokue.game.map.Hall;
import com.rokue.game.utils.GameState;
import com.rokue.game.utils.SaveManager;

public class PlayPanel extends JPanel implements Runnable {
    final int scale = 2; // 1x1 pixel is shown as 4x4 pixels on screen
    final int scaledTileSize = Hall.getPixelsPerTile() * scale;

    final int screenWidth = 266 * scale;
    final int screenHeight = 360 * scale; 
    
    final int entireWidth = 400 * scale;
    
    final int xOffset = -11 * scale;
    final int yOffset = 38 * scale;

    int fps = 60;
    
    Image inventoryBacground = new ImageIcon(Hero.class.getResource("/sprites/build/build_background.png")).getImage();
    Image inventoryImage = new ImageIcon(Hero.class.getResource("/sprites/inventory.png")).getImage();
    Image heartImage = new ImageIcon(Hero.class.getResource("/sprites/heart.png")).getImage();
    
    Keyboard keyboard;
    Hero hero;
    Hall[] halls;
    int currentHallNo = 0;
    SpriteLoader spriteLoader;

    Thread gameThread;

    JButton pauseButton = new JButton("Pause");
    JButton saveButton = new JButton("Save");

    public static int tickTime = 0;

    public PlayPanel(Hall[] halls, SpriteLoader spriteLoader,int currentHallIndex,boolean isGameLoaded) {
        this.currentHallNo = currentHallIndex;
        this.halls = halls;
        this.spriteLoader = spriteLoader;

        // check if game is loaded from save or new game because depending on that we need to add hero or not
        if (isGameLoaded) {
            this.keyboard = new Keyboard();
            this.hero =halls[currentHallNo].getHero();
            this.hero.setKeyboard(keyboard);
            this.hero.setInventory(halls[currentHallNo].getHero().getInventory());
            this.hero.setHealth(halls[currentHallNo].getHero().getHealth());
            
        } else {
            this.keyboard = new Keyboard();
            this.hero = new Hero(keyboard);
            hero.randomlyPlace(halls[currentHallNo]);
        }
        
        halls[currentHallNo].runeHolder = halls[currentHallNo].getRandomProp();
        halls[currentHallNo].setTime(5 + 5 * halls[currentHallNo].getProps().size());

        this.setPreferredSize(new Dimension(entireWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyboard);
        this.setFocusable(true);
        this.setLayout(null);

        // ============= Pause Button ============
        pauseButton.addActionListener(e -> {
            keyboard.pause = !keyboard.pause;
            this.requestFocusInWindow();
        });

        pauseButton.setBounds(entireWidth - 42 * scale, 7 * scale, 30 * scale, 15 * scale);
        this.add(pauseButton);

                // ============= Save Button ============
        saveButton.addActionListener(e -> {
            // Gather the current game state
            GameState gameState = new GameState(
                halls, 
                currentHallNo, 
                hero.getHealth(), 
                tickTime
                // add any other data your GameState class needs
            );

            try {
                // For demonstration, we'll just pick a fixed filename, e.g., "mySave.sav"
                SaveManager.saveGame(gameState, "mySave.sav");
                JOptionPane.showMessageDialog(this, "Game saved successfully!");
            } catch (IOException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Error saving game: " + ex.getMessage());
            }

            // Re-focus on the panel so keyboard input continues
            this.requestFocusInWindow();
        });
        // Adjust position so it doesn't overlap with Pause
        saveButton.setBounds(entireWidth - 42 * scale, 25 * scale, 30 * scale, 15 * scale);
        this.add(saveButton);
    }

    

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
public void run() {
    double drawInterval = 1000000000 / fps;
    double delta = 0;
    long lastTime = System.nanoTime();
    long currentTime;

    long timer = 0;
    int drawCount = 0;

    while (gameThread != null) {
        halls[currentHallNo].setTotalTime(5 + 5 * halls[currentHallNo].getProps().size());
        currentTime = System.nanoTime();
        delta += (currentTime - lastTime) / drawInterval;
        timer += (currentTime - lastTime);
        lastTime = currentTime;

        while (keyboard.pause) {
            currentTime = System.nanoTime();
            lastTime = currentTime;
            System.out.print("");
        }

        if (delta >= 1) {
            PlayPanel.tickTime++;
            update();
            repaint();
            delta--;
            drawCount++;
        }

        if (timer >= 1000000000) {
            System.out.println("Time: " + halls[currentHallNo].getTime());
            if (currentHallNo == halls.length - 1 && halls[currentHallNo].isHeroExit()) {
                System.out.println("Player completed the last hall. Victory!");
                switchToGameOverPanel(true); // Player wins
                return;
            }
            
            // Check if time is up or hero's health is 0 (player loses)
            if (halls[currentHallNo].getTime() == 0 || hero.health == 0) {
                System.out.println(hero.health == 0 ? "Hero has died." : "Time has run out.");
                switchToGameOverPanel(false); // Player loses
                return;
            }
            System.out.println("FPS: " + drawCount);
            System.out.println("X: " + hero.getXPixelPosition() + " Y:" + hero.getYPixelPosition());
            System.out.println();
            drawCount = 0;
            timer = 0;
            halls[currentHallNo].setTime(halls[currentHallNo].getTime() - 1);
        }
    }
}

    void update() {
        if (halls[currentHallNo].isHeroExit()) {
            if (currentHallNo == 3) {
                System.out.println("Escaped.");
                System.out.print("Seed: ");
                System.out.println(halls[currentHallNo].RNG.getSeed());
                switchToGameOverPanel(true); // Player wins
                return;
            }
            currentHallNo++;
            spriteLoader.currentHallNo++;
            hero.randomlyPlace(halls[currentHallNo]);
            halls[currentHallNo].setTime(5 + 5 * halls[currentHallNo].getProps().size());
            halls[currentHallNo].runeHolder = halls[currentHallNo].getRandomProp();
        }
        for (Character character : halls[currentHallNo].getCharacters()) character.update();

        halls[currentHallNo].update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.drawImage(spriteLoader.hallSprites[0], 0,0, screenWidth, screenHeight, null);

        g2.setColor(new Color(0, 0, 0, 64));
        Entity entity;
        for (int y = 0; y < Hall.getTiles(); y++) {
            for (int x = 0; x < Hall.getTiles(); x++) {
                entity = halls[currentHallNo].getGrid()[x][y];
                if (entity != null) {
                    g2.drawImage(entity.getSprite(spriteLoader), entity.getXPixelPosition() * scale + xOffset, entity.getYPixelPosition() * scale + yOffset, entity.getWidth() * scaledTileSize, entity.getHeight() * scaledTileSize, null);
                    if (entity.getShadow()) {
                        g2.fillRect(entity.getShadowX() * scale + xOffset, entity.getShadowY() * scale + yOffset, entity.getShadowWidth() * scale, entity.getShadowHeight() * scale);
                    }
                }
            }
        }

        // Draw arrows
        for (Character character : halls[currentHallNo].getCharacters()) {
            if (character instanceof Archer) {
                Archer archer = (Archer) character;
                for (Arrow arrow : archer.getArrows()) {
                    int xPixel = arrow.getX() * scaledTileSize + xOffset;
                    int yPixel = arrow.getY() * scaledTileSize + yOffset;
                    Image arrowImage = spriteLoader.getArrowSprites()[arrow.getDirection()];
                    g2.drawImage(arrowImage, xPixel, yPixel, scaledTileSize, scaledTileSize, null);
                }
            }
        }
        
        // Draw Inventory Panel (right side)
        int inventoryPanelWidth = 110 * scale; // Adjust this value
        int inventoryPanelX = screenWidth + 12*scale;
        
        g2.drawImage(inventoryBacground, screenWidth, 0, entireWidth - screenWidth, screenHeight, null);
        
        g2.setColor(new Color(211, 211, 211));
        g2.drawRect(inventoryPanelX, 30*scale, inventoryPanelWidth, screenHeight-30*scale);
        
        g2.drawImage(inventoryImage, inventoryPanelX+15*scale, screenHeight/3, 80*scale, 177*scale, null);
        
        g2.drawImage(spriteLoader.enchantmentSprites[0], inventoryPanelX+31*scale, 215*scale, 16*scale, 18 * scale, null);
        g2.drawImage(spriteLoader.enchantmentSprites[1], inventoryPanelX+48*scale, 215*scale, 16*scale, 18 * scale, null);
        g2.drawImage(spriteLoader.enchantmentSprites[2], inventoryPanelX+64*scale, 215*scale, 16*scale, 18 * scale, null);
        
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.drawString(String.valueOf(halls[currentHallNo].getHero().getInventory()[1]), inventoryPanelX+35*scale, 203*scale);
        g2.drawString(String.valueOf(halls[currentHallNo].getHero().getInventory()[0]), inventoryPanelX+52*scale, 203*scale);
        g2.drawString(String.valueOf(halls[currentHallNo].getHero().getInventory()[2]), inventoryPanelX+69*scale, 203*scale);
        
        g2.setFont(new Font("Arial", Font.BOLD, 30));
        g2.drawString("Remaining Time:", inventoryPanelX+15*scale, 45*scale);
        if (halls[currentHallNo].getTime()>10) {
        	g2.setColor(Color.GREEN);	
        }
        else {
        	g2.setColor(Color.RED);
        }
        g2.drawString(halls[currentHallNo].getTime() + " s" , inventoryPanelX+15*scale, 55*scale);
        
        g2.setColor(new Color(211, 211, 211));
        g2.drawString("Life:", inventoryPanelX+15*scale, 70*scale);
        int heartCount = halls[currentHallNo].getHero().getHealth();
        if (heartCount==0) {
        	g2.setColor(Color.RED);
        	g2.drawString("DIED :(", inventoryPanelX+15*scale, 80*scale);
        }
        else {
        	for(int hc=0; hc<heartCount; hc++) {
        		g2.drawImage(heartImage, inventoryPanelX+(15+16*hc)*scale, 75*scale, 16*scale, 16*scale, null);
        	}
        }
        
        if (hero.isCloakActive()) {
        	g2.setColor(new Color(211, 211, 211));
        	g2.drawString("Cloak Time:", inventoryPanelX+15*scale, 103*scale);
        	long cloakTime = (hero.getCloakDuration() - (PlayPanel.tickTime - hero.getCloakStartTime())) / 60;
        	g2.setColor(Color.CYAN);
        	g2.drawString(cloakTime + " s", inventoryPanelX+15*scale, 113*scale);
        }
        
        String hallName = "";
        if (currentHallNo==0) hallName = "Hall Of Water";
        else if (currentHallNo==1) hallName = "Hall Of Earth";
        else if (currentHallNo==2) hallName = "Hall Of Fire";
        else hallName = "Hall of Air";
        g2.setFont(new Font("Times New Roman", Font.BOLD, 40));
        g2.setColor(new Color(211, 211, 211));
        g2.drawString(hallName, screenWidth/2-40*scale, 15*scale);
        
        g2.drawImage(spriteLoader.hallSprites[1], 0,0, screenWidth, screenHeight - 34 * scale, null);

        if (halls[currentHallNo].isRevealRuneActive()) {
            int x = halls[currentHallNo].getRevealRuneX() * scaledTileSize - 33;
            int y = halls[currentHallNo].getRevealRuneY() * scaledTileSize + 113;
            int w = 4 * scaledTileSize;
            int h = 4 * scaledTileSize;
            g2.setColor(new Color(50, 255, 50, 40)); 
            g2.fillRect(x, y, w, h);
            g2.setColor(new Color(80, 255, 120, 70)); 
            g2.setStroke(new BasicStroke(2));
            g2.drawRect(x, y, w, h);
        }

        // pause button
        g2.setColor(new Color(211, 211, 211));
        g2.fillRect(entireWidth - 42 * scale, 7 * scale, 30 * scale, 15 * scale);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Times New Roman", Font.BOLD, 12 * scale));
        g2.drawString("Pause", entireWidth - 42 * scale, 18 * scale);

        // save button
        g2.setColor(new Color(211, 211, 211));
        g2.fillRect(entireWidth - 42 * scale, 25 * scale, 30 * scale, 15 * scale);
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Times New Roman", Font.BOLD, 12 * scale));
        g2.drawString("Save", entireWidth - 39 * scale, 36 * scale);

        g2.dispose();
    }

    private void switchToGameOverPanel(boolean isWin) {
        // Stop the game thread
        gameThread = null;

        // Determine the background image based on win/lose status
        String backgroundPath = isWin ? "/sprites/gameOver/winScreen.png" : "/sprites/gameOver/loseScreen.png";

        // Use SwingUtilities to handle thread-safe operations on the GUI
        javax.swing.SwingUtilities.invokeLater(() -> {
            javax.swing.JFrame parentFrame = (javax.swing.JFrame) javax.swing.SwingUtilities.getWindowAncestor(this);

            // If the parent frame exists, make it full screen
            if (parentFrame != null) {
                // Remove all existing components
                parentFrame.getContentPane().removeAll();

                // Remove decorations for full-screen mode
                parentFrame.dispose();
                parentFrame.setUndecorated(true); // Remove window decorations
                parentFrame.setExtendedState(JFrame.MAXIMIZED_BOTH); // Maximize to full screen

                // Add the GameOverPanel
                GameOverPanel gameOverPanel = new GameOverPanel(
                    backgroundPath,
                    () -> {
                        // Return to Main Menu
                        parentFrame.dispose();
                        new MainPanel().setVisible(true); // Open MainPanel in a new frame
                    }
                );
                parentFrame.add(gameOverPanel);

                // Revalidate and repaint the frame
                parentFrame.setVisible(true);
                parentFrame.revalidate();
                parentFrame.repaint();
            }
        });
    }

    

}
