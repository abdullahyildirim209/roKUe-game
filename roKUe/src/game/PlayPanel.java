package game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class PlayPanel extends JPanel implements Runnable {
    final int scale = 3; // 1x1 pixel is shown as 4x4 pixels on screen
    final int scaledTileSize = Hall.pixelsPerTile * scale;

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

    public PlayPanel(Hall[] halls, SpriteLoader spriteLoader) {
        this.halls = halls;
        this.spriteLoader = spriteLoader;
        hero.randomlyPlace(halls[currentHallNo]);
        halls[currentHallNo].runeHolder = halls[currentHallNo].getRandomProp();
        halls[currentHallNo].time = 5 + 5 * halls[currentHallNo].props.size();

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
                System.out.println("Time: " + halls[currentHallNo].time);
                if (halls[currentHallNo].time == 0) {
                    System.out.println("You ran out of time.");
                    while(true);
                }
                System.out.println("FPS: " + drawCount);
                System.out.println("X: " + hero.getXPixelPosition() +" Y:" + hero.getYPixelPosition());
                System.out.println();
                drawCount = 0;
                timer = 0;
                halls[currentHallNo].time--;
            }
        }
    }

    void update() {
        if (halls[currentHallNo].heroExit) {
            if (currentHallNo == 3) {
                System.out.println("You escaped.");
                while(true);
            }
            currentHallNo++;
            spriteLoader.currentHallNo++;
            hero.randomlyPlace(halls[currentHallNo]);
            halls[currentHallNo].time = 5 + 5 * halls[currentHallNo].props.size();
            halls[currentHallNo].runeHolder = halls[currentHallNo].getRandomProp();
        }
        for (Character character : halls[currentHallNo].characters) character.update();
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;

        g2.drawImage(spriteLoader.hallSprites[0], 0,0, screenWidth, screenHeight, null);

        g2.setColor(new Color(0, 0, 0, 64));
        Entity entity;
        for (int y = 0; y < Hall.tiles; y++) {
            for (int x = 0; x < Hall.tiles; x++) {
                entity = halls[currentHallNo].grid[x][y];
                if (entity != null) {
                    g2.drawImage(entity.getSprite(spriteLoader), entity.getXPixelPosition() * scale + xOffset, entity.getYPixelPosition() * scale + yOffset, entity.getWidth() * scaledTileSize, entity.getHeight() * scaledTileSize, null);
                    if (entity.shadow) {
                        g2.fillRect(entity.getShadowX() * scale + xOffset, entity.getShadowY() * scale + yOffset, entity.getShadowWidth() * scale, entity.getShadowHeight() * scale);
                    }
                }
            }
        }

        g2.drawImage(spriteLoader.hallSprites[1], 0,0, screenWidth, screenHeight - 34 * scale, null);

        g2.dispose();
    }

}
