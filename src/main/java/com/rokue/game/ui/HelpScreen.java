package com.rokue.game.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

public class HelpScreen extends JDialog {

    public HelpScreen(JFrame parent) {
        super(parent, "Help & Instructions", true);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setUndecorated(true); // Make the frame border invisible

        JPanel dialogPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Load and draw background image for help screen
                Image helpBackground = new ImageIcon(SpriteLoader.class.getResource("/sprites/menu/HelpScreen.png")).getImage();
                if (helpBackground != null) {
                    g.drawImage(helpBackground, 0, 0, getWidth(), getHeight(), this);
                } else {
                    g.setColor(Color.BLACK);
                    g.fillRect(0, 0, getWidth(), getHeight());
                    g.setColor(Color.RED);
                    g.drawString("Background image not found!", 10, 20);
                }
            }
        };
        dialogPanel.setLayout(new BorderLayout(10, 10));
        dialogPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Add semi-transparent background for the text area
        JPanel textBackgroundPanel = new JPanel();
        textBackgroundPanel.setLayout(new BorderLayout());
        textBackgroundPanel.setBackground(new Color(0, 0, 0, 150)); // Semi-transparent black
        textBackgroundPanel.setOpaque(true);

        JTextArea helpTextArea = new JTextArea();
        helpTextArea.setEditable(false);
        helpTextArea.setOpaque(false); // Keep text area transparent
        helpTextArea.setFont(new Font("Serif", Font.PLAIN, 16));
        helpTextArea.setForeground(Color.WHITE);
        helpTextArea.setText(
            "How to Play\n" +
            "Navigate the Dungeon\n\n" +
            "Use the mighty arrow keys to move your hero:\n" +
            "↑: Ascend to glory!\n" +
            "↓: Descend into peril!\n" +
            "←: Sneak left like a ninja!\n" +
            "→: Sprint right with bravery!\n" +
            "Click on mysterious objects to uncover ancient runes.\n" +
            "Conquer the halls in this epic order: Earth → Air → Water → Fire.\n\n" +
            "Collect Enchantments\n" +
            "Magical doodads (aka enchantments) randomly appear to save your bacon.\n" +
            "Click to collect them faster than your enemies can blink!\n" +
            "Some enchantments work immediately, while others chill in your bag for later heroics.\n\n" +
            "Avoid Monsters (Seriously, They’re Scary)\n" +
            "Monsters spawn every 8 seconds to ruin your day. Beware!\n" +
            "- Archer Monster: Pew pew! If you're within 4 squares, dodge those arrows or wear a Cloak of Protection to go incognito.\n" +
            "- Fighter Monster: Watch out for close-range stabs! Toss a Luring Gem to send them on a wild goose chase.\n" +
            "- Wizard Monster: A tricky one! Their behavior changes based on how much time remains:\n" +
            "    * Less than 30% time left: They teleport you to a random empty location once and disappear. Stay alert!\n" +
            "    * More than 70% time left: They teleport the rune every 3 seconds, making it harder to find.\n" +
            "    * 30-70% time left: They stay where they spawn for 2 seconds and then disappear without causing trouble.\n\n" +
            "Use Items Like a Pro\n" +
            "Access your trusty bag to unleash collected enchantments:\n" +
            "- Reveal: Press R to highlight a 4x4 area where the rune might be. (It's like rune radar!)\n" +
            "- Cloak of Protection: Press P to become invisible to archers for 20 glorious seconds.\n" +
            "- Luring Gem: Press B, then aim it with A/W/S/D to distract those pesky fighters.\n\n" +
            "Game Features\n" +
            "- Lives: You get 3 lives! Lose them all, and it’s game over (cue dramatic music). Collect extra lives to keep the adventure alive!\n" +
            "- Time Limit: Beat the clock by finding runes. Pick up extra time to laugh in the face of deadlines.\n" +
            "- Pause/Resume: Need a breather? Hit the Pause button. Ready to dive back in? Hit Resume.\n" +
            "- Game Over: Lose all lives or run out of time, and the dungeon wins. Sad times.\n\n"
        );


        JScrollPane scrollPane = new JScrollPane(helpTextArea);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        textBackgroundPanel.add(scrollPane, BorderLayout.CENTER);
        dialogPanel.add(textBackgroundPanel, BorderLayout.CENTER);

        // Close button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.setOpaque(false);
        JButton closeButton = new JButton("Close");
        closeButton.setFont(new Font("Serif", Font.BOLD, 18)); // Larger font for the button
        closeButton.setOpaque(false); // Make button transparent
        closeButton.setContentAreaFilled(false);
        closeButton.setBorderPainted(false);
        closeButton.setForeground(Color.WHITE);
        closeButton.addActionListener(e -> dispose());
        buttonPanel.add(closeButton);
        

        // Adjust position slightly above the bottom and centered
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 0)); 
        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);

        setContentPane(dialogPanel);
        pack();
        setLocationRelativeTo(parent);
    }
}
