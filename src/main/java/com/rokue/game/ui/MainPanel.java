package com.rokue.game.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.rokue.game.Main;

public class MainPanel extends JFrame {

    private JPanel contentPane;

    public MainPanel() {
        setTitle("Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH); // Make it fullscreen and resizable

        // Set content pane with a custom background image
        contentPane = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load and draw background image
                Image backgroundImage = new ImageIcon(SpriteLoader.class.getResource("/sprites/MainMenu.jpg")).getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setLayout(null); // Initial layout for dynamic resizing
        setContentPane(contentPane);

        // Create buttons
        JButton playGameButton = createTransparentButton("Play Game");
        JButton buildModeButton = createTransparentButton("Build Mode");
        JButton helpButton = createTransparentButton("Help");

        // Add buttons to content pane
        contentPane.add(playGameButton);
        contentPane.add(buildModeButton);
        contentPane.add(helpButton);

        // Add a resize listener to adjust buttons dynamically
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();

                // Dynamically align buttons
                playGameButton.setBounds(width * 175 / 1000, height * 68 / 100, width * 10 / 100, height * 10 / 100);
                buildModeButton.setBounds(width * 47 / 100, height * 68 / 100, width * 10 / 100, height * 10 / 100);
                helpButton.setBounds(width * 76 / 100, height * 68 / 100, width * 10 / 100, height * 10 / 100);
            }
        });

        // Set button actions
        playGameButton.addActionListener(e -> {
            dispose();
            Main.startPlayMode();
        });
        buildModeButton.addActionListener(e -> {
            dispose();
            Main.startBuildMode();
        });
        helpButton.addActionListener(e -> showHelpDialog());

        // Trigger initial resize to position buttons
        revalidate();
        repaint();
    }

    private JButton createTransparentButton(String text) {
        JButton button = new JButton(text);
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFont(new Font("Serif", Font.BOLD, 22));
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        return button;
    }

    private void showHelpDialog() {
        HelpScreen helpScreen = new HelpScreen(this);
        helpScreen.setVisible(true);
    }
}
