package com.rokue.game.ui;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import com.rokue.game.Main;
import com.rokue.game.map.Hall;
import com.rokue.game.utils.GameState;
import com.rokue.game.utils.SaveManager;

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
                Image backgroundImage = 
                    new ImageIcon(SpriteLoader.class.getResource("/sprites/menu/MainMenuV2.png")).getImage();
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        };
        contentPane.setLayout(null); 
        setContentPane(contentPane);

        // Create buttons
        JButton playGameButton = createTransparentButton("Play Game");
        JButton buildModeButton = createTransparentButton("Build Mode");
        JButton helpButton = createTransparentButton("Help");
        JButton loadButton = createTransparentButton("Load Game");

        // Add buttons to content pane
        contentPane.add(playGameButton);
        contentPane.add(buildModeButton);
        contentPane.add(helpButton);
        contentPane.add(loadButton);

        // Add a resize listener to adjust buttons dynamically
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                int width = getWidth();
                int height = getHeight();

                // Dynamically align buttons
                playGameButton.setBounds(
                    width * 235 / 1000, height * 60 / 100, 
                    width * 10 / 100, height * 40 / 100
                );
                buildModeButton.setBounds(
                    width * 350 / 1000, height * 60 / 100, 
                    width * 10 / 100, height * 40 / 100
                );
                helpButton.setBounds(
                    width * 565 / 1000, height * 60 / 100, 
                    width * 10 / 100, height * 40 / 100
                );
                loadButton.setBounds(
                    width * 675 / 1000, height * 60 / 100, 
                    width * 10 / 100, height * 40 / 100
                );
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

        // "Load Game" button logic
        loadButton.addActionListener(e -> {
            // List available saves
            List<String> saves = SaveManager.listSaveFiles();

            // Dispose of the main menu before showing dialog 
            // so it disappears in the background
            dispose();

            // If no save files exist, show a message and return to main menu
            if (saves.isEmpty()) {
                setOptionPaneColors();
                JOptionPane.showMessageDialog(
                    this, 
                    "No save files found."
                );
                returnToMainMenu();
                return;
            }

            // Set custom background color for dialog
            setOptionPaneColors();

            // Show the input dialog to choose a save file
            String selectedFile = (String) JOptionPane.showInputDialog(
                this,
                "Select a save file",
                "Load Game",
                JOptionPane.PLAIN_MESSAGE,
                null,
                saves.toArray(),
                saves.get(0)
            );

            // If user cancels or closes the dialog (selectedFile == null),
            // go back to main menu
            if (selectedFile == null) {
                returnToMainMenu();
                return;
            }

            // Otherwise, attempt to load the selected game file
            try {
                GameState loadedState = SaveManager.loadGame(selectedFile);

                // Retrieve data from loaded game state
                Hall[] halls = loadedState.getHalls();
                int currentHallIndex = loadedState.getCurrentHallIndex();
                int heroLives = loadedState.getHeroLives();
                long globalTime = loadedState.getGlobalTime();
                int remainingTime = loadedState.getTime();

                // Launch the play mode with restored state
                Main.startPlayMode(halls, currentHallIndex, heroLives, globalTime, remainingTime);

                // Confirm successful load
                setOptionPaneColors();
                JOptionPane.showMessageDialog(
                    this,
                    "Game loaded successfully!"
                );
            } catch (Exception ex) {
                ex.printStackTrace();
                setOptionPaneColors();
                JOptionPane.showMessageDialog(
                    this,
                    "Error loading game: " + ex.getMessage()
                );
                // Return to main menu on error (optional)
                returnToMainMenu();
            }
        });

        // Trigger initial layout
        revalidate();
        repaint();
    }

    /**
     * Utility method to create a transparent-looking button.
     */
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

    /**
     * Shows a custom 'Help' dialog.
     */
    private void showHelpDialog() {
        HelpScreen helpScreen = new HelpScreen(this);
        helpScreen.setVisible(true);
    }

    /**
     * Resets to the main menu by creating a new MainPanel instance.
     */
    private void returnToMainMenu() {
        MainPanel mainPanel = new MainPanel();
        mainPanel.setVisible(true);
    }

    /**
     * Applies your desired colors to JOptionPane before showing a dialog.
     * (If you want a background image instead, you'll need a custom UI, 
     * which is more involved than just setting these properties.)
     */
    private void setOptionPaneColors() {
        UIManager.put("OptionPane.background", new Color(110, 80, 57));
        UIManager.put("Panel.background", new Color(110, 80, 57));
    }
}
