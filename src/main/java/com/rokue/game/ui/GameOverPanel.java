package com.rokue.game.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class GameOverPanel extends JPanel {
    private Image backgroundImage;
    private JButton backToMenuButton;

    public GameOverPanel(String backgroundPath, Runnable backToMenuAction) {
        // Load background image
        backgroundImage = new ImageIcon(getClass().getResource(backgroundPath)).getImage();
        
        // Create buttons
        backToMenuButton = createStyledButton("Main Menu");

        // Add buttons to panel
        setLayout(null);
        add(backToMenuButton);

        // Add actions to buttons
        backToMenuButton.addActionListener(e -> {
            // Close the current frame and open the Main Menu
            JFrame parentFrame = (JFrame) getTopLevelAncestor();
            if (parentFrame != null) {
                parentFrame.dispose(); // Close the GameOverPanel's frame
            }
            new MainPanel().setVisible(true); // Open the MainPanel
        });

        // Add a resize listener to adjust buttons dynamically
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();

                // Dynamically align and size the button
                backToMenuButton.setBounds(width * 25 / 100, height * 60 / 100, width * 50 / 100, height * 40 / 100);
            }
        });

        // Trigger initial layout adjustment
        revalidate();
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        // Draw background image
        g2.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);

        g2.dispose();
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Serif", Font.BOLD, 36)); // Larger font size
        button.setForeground(Color.WHITE); // White text color
        button.setFocusPainted(false); // Remove focus border
        return button;
    }

    public static void showGameOverScreen(String backgroundPath) {
        // Create the frame
        JFrame gameOverFrame = new JFrame("Game Over");
        gameOverFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Make the frame full screen
        gameOverFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        gameOverFrame.setUndecorated(true); // Remove window borders for a clean full-screen view

        // Add the GameOverPanel
        GameOverPanel gameOverPanel = new GameOverPanel(backgroundPath, () -> {
            // Open the MainPanel
            new MainPanel().setVisible(true);
        });
        gameOverFrame.add(gameOverPanel);

        // Show the frame
        gameOverFrame.setVisible(true);
    }
}
