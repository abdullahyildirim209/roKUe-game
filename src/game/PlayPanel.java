package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

public class PlayPanel extends JPanel implements Runnable {
    final int scale = 3; // 1x1 pixel is shown as 4x4 pixels on screen
    final int scaledTileSize = Hall.pixelsPerTile * scale;

    final int screenWidth = 266 * scale;
    final int screenHeight = 292 * scale; 

    final int xOffset = -11 * scale;
    final int yOffset = 4 * scale;

    final Image backGround = new ImageIcon(PlayPanel.class.getResource("sprites/hall.png")).getImage();

    final int fps = 60;

    Keyboard keyboard = new Keyboard();
    Hero hero = new Hero(keyboard);
    Hall hall;

    Thread gameThread;

    public PlayPanel(Hall hall) {
        this.hall = hall;

        // builder mode will do this (temporary)
        new Crate().place(2, 15, hall);
        new Crate().place(3, 15, hall);
        new Crate().place(4, 15, hall);
        new Crate().place(5, 15, hall);
        new Crate().place(6, 14, hall);
        new Crate().place(7, 13, hall);
        new Crate().place(7, 12, hall);
        new Crate().randomlyPlace(hall);
        new Crate().randomlyPlace(hall);

        hero.randomlyPlace(hall);
        //Crate crate2 = new Crate();
        //crate2.randomlyPlace(hall);

        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyboard);
        this.setFocusable(true);
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
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if (delta >= 1) {
                update();
                repaint();
                delta--;
                drawCount++;
            }

            if (timer >= 1000000000) {
                System.out.println("FPS: " + drawCount);
                System.out.println("X: " + hero.getXPixelPosition() +" Y:" + hero.getYPixelPosition());
                System.out.println();
                drawCount = 0;
                timer = 0;
                
            }
        }
    }

    void update() {
        for (Entity entity : hall.entities) entity.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.drawImage(backGround, 0,0, screenWidth, screenHeight, null);

        g2.setColor(new Color(0, 0, 0, 128));
        g2.fillRect(hero.getXPosition() * scaledTileSize + xOffset, hero.getYPosition() * scaledTileSize + yOffset, scaledTileSize, scaledTileSize);

        g2.setColor(new Color(0, 255, 0, 128));
        int[] heroObservedTilePosition = hero.getObservedTilePosition();
        g2.fillRect(heroObservedTilePosition[0] * scaledTileSize + xOffset, heroObservedTilePosition[1] * scaledTileSize + yOffset, scaledTileSize, scaledTileSize);

        Entity entity;
        for (int y = 0; y < Hall.tiles; y++) {
            for (int x = 0; x < Hall.tiles; x++) {
                entity = hall.grid[x][y];
                if (entity != null) {
                    g2.drawImage(entity.getSprite(), entity.getXPixelPosition() * scale + xOffset, entity.getYPixelPosition() * scale + yOffset, entity.getWidth() * scaledTileSize, entity.getHeight() * scaledTileSize, null);
                }
            }
        }

        g2.dispose();
    }

}
