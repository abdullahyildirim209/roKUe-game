package com.rokue.game.ui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.EmptyBorder;

import com.rokue.game.Main;

public class MainPanel extends JFrame {

    private JPanel contentPane;

    public MainPanel() {
        setTitle("Main Menu");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 450, 351);
        contentPane = new JPanel();
        Color mainBackgroundColor = new Color(66,40,53);
        contentPane.setBackground(mainBackgroundColor);


        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

        setContentPane(contentPane);
        contentPane.setLayout(null);

        // Title
        JLabel TitleLabel = new JLabel();
        TitleLabel.setBounds(20, 40, 400, 61);


        Icon TitleIcon = new ImageIcon(SpriteLoader.class.getResource("/sprites/other/TitleRokueGame.png")); 
        TitleLabel.setIcon(TitleIcon);
        contentPane.add(TitleLabel);

        // "Play Game" Button
        JButton startGameButton = new JButton("Play Game");
        startGameButton.setBounds(141, 100, 139, 51);


        Icon playIcon = new ImageIcon(SpriteLoader.class.getResource("/sprites/other/PlayButton.png")); 
        startGameButton.setIcon(playIcon);
        
        contentPane.add(startGameButton);
        startGameButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();                       // Close the menu
                Main.startPlayMode();           // Start immediate play
            }
        });

        // "Build Mode" Button
        JButton buildModeButton = new JButton("Build Mode");
        buildModeButton.setBounds(141, 157, 139, 51);
        Icon BuildModeIcon = new ImageIcon(SpriteLoader.class.getResource("/sprites/other/BuildButton.png")); 
        buildModeButton.setIcon(BuildModeIcon);

        contentPane.add(buildModeButton);
        buildModeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();                       // Close the menu
                Main.startBuildMode();          // Start build mode
            }
        });

        // "Help" Button
        JButton helpMenuButton = new JButton("Help");
        helpMenuButton.setBounds(140, 214, 139, 51);
        Icon HelpIcon = new ImageIcon(SpriteLoader.class.getResource("/sprites/other/HelpButton.png"));  
        helpMenuButton.setIcon(HelpIcon);
        contentPane.add(helpMenuButton);
        helpMenuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Example help action
                showHelpDialog();
            }
        });
    }
    private void showHelpDialog() {
        // Create a modal dialog owned by this MainPanel
        JDialog helpDialog = new JDialog(this, "Help & Instructions", true);
        helpDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
    
        // We'll use a BorderLayout to position text area and a close button
        JPanel dialogPanel = new JPanel(new BorderLayout(10, 10));
        dialogPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
    
        // Add a text area (within a scroll pane) for multi-line help text
        JTextArea helpTextArea = new JTextArea();
        helpTextArea.setEditable(false);
        helpTextArea.setText(
            "Welcome to RoKUe!\n\n" +
            "Instructions:\n" +
            "1. Click 'Play Game' to start playing with a default map.\n" +
            "2. Click 'Build Mode' to design or customize your own halls.\n" +
            "3. In Build Mode, you can place, remove, or reset props, then press 'Finish' to play.\n\n" +
            "Have fun!"
        );
        JScrollPane scrollPane = new JScrollPane(helpTextArea);
        dialogPanel.add(scrollPane, BorderLayout.CENTER);
    
        // Create a small panel at the bottom for a 'Close' button
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> helpDialog.dispose());
        buttonPanel.add(closeButton);
        dialogPanel.add(buttonPanel, BorderLayout.SOUTH);
    
        // Add the panel to the dialog
        helpDialog.setContentPane(dialogPanel);
    
        // Size the dialog and center it relative to MainPanel
        helpDialog.pack();
        helpDialog.setLocationRelativeTo(this);
    
        // Show it (modal)
        helpDialog.setVisible(true);
    }


}




	
		
		
