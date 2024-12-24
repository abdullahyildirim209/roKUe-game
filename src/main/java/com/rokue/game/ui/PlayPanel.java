package com.rokue.game.ui;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

import com.rokue.game.input.Keyboard;
import com.rokue.game.map.Hall;
import com.rokue.game.entities.Entity;
import com.rokue.game.entities.Hero;
import com.rokue.game.entities.Character;

public class PlayPanel extends JPanel implements Runnable {
    final int scale = 3; // 1x1 pixel is shown as 4x4 pixels on screen
    final int scaledTileSize = Hall.getPixelsPerTile() * scale;

    final int screenWidth = 266 * scale;
    final int screenHeight = 360 * scale; 

    final int xOffset = -11 * scale;
    final int yOffset = 38 * scale;

    int fps = 60;

    Keyboard keyboard = new Keyboard();
    Hero hero = new Hero(keyboard);
    Hall[] halls;
    int currentHallNo = 0;
    SpriteLoader spriteLoader;

    Thread gameThread;

    public static int tickTime = 0;

    public PlayPanel(Hall[] halls, SpriteLoader spriteLoader) {
        PlayPanel.tickTime = 0;
        this.halls = halls;
        this.spriteLoader = spriteLoader;
        hero.randomlyPlace(halls[currentHallNo]);
        halls[currentHallNo].runeHolder = halls[currentHallNo].getRandomProp();
        halls[currentHallNo].setTime(5 + 5 * halls[currentHallNo].getProps().size());

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
                if (halls[currentHallNo].getTime() == 0) {
                    System.out.println("You ran out of time.");
                    while(true);
                }
                System.out.println("FPS: " + drawCount);
                System.out.println("X: " + hero.getXPixelPosition() +" Y:" + hero.getYPixelPosition());
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
                System.out.println("You escaped.");
                while(true);
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

        g2.drawImage(spriteLoader.hallSprites[1], 0,0, screenWidth, screenHeight - 34 * scale, null);
        
        g2.setColor(Color.RED);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("Health: " + halls[currentHallNo].getHero().getHealth(), screenWidth - 100, 30);

        if (hero.isCloakActive()) {
            long remainingTime = (hero.getCloakDuration() - (PlayPanel.tickTime - hero.getCloakStartTime())) / 60;
            g2.setColor(Color.CYAN);
            g2.drawString("Cloak Time: " + remainingTime + "s", screenWidth / 2 - 73, 30);
        }

        g2.setColor(Color.GREEN);
        g2.drawString("Remaining Time: " + halls[currentHallNo].getTime() + "s" , 10, 30);

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

        g2.dispose();
    }

}
