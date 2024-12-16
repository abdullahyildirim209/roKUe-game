package com.rokue.game.ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

import com.rokue.game.entities.Entity;
import com.rokue.game.entities.Hero;
import com.rokue.game.input.Keyboard;
import com.rokue.game.map.Hall;

public class PlayPanel extends JPanel implements Runnable {
    public static final int tileSize = 16;
    public static final int scale = 3;
    public static final int tileSizeScaled = tileSize * scale;
    public static final int maxTiles = 18;

    public static final int screenWidth = tileSizeScaled * maxTiles;
    public static final int screenHeight = tileSizeScaled * maxTiles;

    private final Hall hall;
    private final Hero hero;
    private final Keyboard keyboard;
    private Thread gameThread;

    private final Image background;

    public PlayPanel(Hall hall) {
        this.hall = hall;
        this.keyboard = new Keyboard();
        this.hero = new Hero(keyboard);

        this.hall.setHero(this.hero);

        this.background = new ImageIcon(getClass().getResource("/sprites/hall.png")).getImage();

        this.setPreferredSize(new Dimension(screenWidth, screenHeight)); // Pencere boyutu
        this.setBackground(Color.BLACK); // Arkaplan rengi
        this.addKeyListener(keyboard);
        this.setFocusable(true);
        hero.place(1, 1, hall);

        for (int i = 0; i < 2; i++) {
            hall.placeRandomArcher();
        }
        for (int i = 0; i < 3; i++) {
            hall.placeRandomFighter();
        }
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        while (gameThread != null) {
            update();
            repaint();
            try {
                Thread.sleep(100 / 30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void update() {
        hero.update();

        if (keyboard.b) {
            int gemX = hero.getXPosition();
            int gemY = hero.getYPosition();
            int direction = -1;

            // Kullanıcı hangi yöne Luring Gem atmak istiyor
            if (keyboard.a) {
                gemX--; // Sol
                direction = 0;
            } else if (keyboard.d) {
                gemX++; // Sağ
                direction = 1;
            } else if (keyboard.w) {
                gemY--; // Yukarı
                direction = 2;
            } else if (keyboard.s) {
                gemY++; // Aşağı
                direction = 3;
            }

            if (direction != -1 && hall.isPositionEmpty(gemX, gemY)) {
                hall.placeLuringGem(gemX, gemY, direction); // Hall'da Luring Gem yerleştir
                keyboard.b = false; // Luring Gem kullanıldıktan sonra tuşu sıfırla
            }
        }

        for (Entity entity : hall.getEntities()) {
            entity.update();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw background
        g2.drawImage(background, 0, 0, screenWidth, screenHeight, null);

        // Draw all entities (including monsters)
        for (Entity entity : hall.getEntities()) {
            g2.drawImage(
                    entity.getSprite(),
                    entity.getXPixelPosition() * scale,
                    entity.getYPixelPosition() * scale,
                    tileSizeScaled,
                    tileSizeScaled,
                    null
            );
        }

        g2.drawImage(
                hero.getSprite(),
                hero.getXPosition() * tileSizeScaled,
                hero.getYPosition() * tileSizeScaled,
                tileSizeScaled,
                tileSizeScaled,
                null
        );

        g2.setColor(Color.RED);
        g2.setFont(new Font("Arial", Font.BOLD, 20));
        g2.drawString("Health: " + hall.getHero().getHealth(), screenWidth - 150, 30);

        if (hero.isCloakActive()) {
            long remainingTime = (hero.getCloakDuration() - (System.currentTimeMillis() - hero.getCloakStartTime())) / 1000;
            g2.setColor(Color.BLUE);
            g2.drawString("Cloak Time: " + remainingTime + "s", screenWidth / 2 - 50, 30);
        }
    }
}
