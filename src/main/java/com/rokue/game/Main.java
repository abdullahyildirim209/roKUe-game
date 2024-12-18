package com.rokue.game;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.rokue.game.entities.Archer;
import com.rokue.game.entities.Fighter;
import com.rokue.game.entities.Prop;
import com.rokue.game.entities.Wizard;
import com.rokue.game.map.Hall;
import com.rokue.game.ui.PlayPanel;
import com.rokue.game.ui.SpriteLoader;
import com.rokue.game.utils.RNG;


public class Main {
    public static void main(String[] args) {

        // create the window
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("RoKUe");
        window.setIconImage(new ImageIcon(SpriteLoader.class.getResource("/sprites/icon.png")).getImage());

        // utils
        RNG RNG = new RNG();
        SpriteLoader spriteHandler = new SpriteLoader();

        // temporary builder mode
        Hall[] halls = {new Hall(RNG), new Hall(RNG), new Hall(RNG), new Hall(RNG)};
        new Prop(4).place(7, 12, halls[0]);
        new Prop(4).randomlyPlace(halls[0]);
        new Wizard().randomlyPlace(halls[0]);
        new Fighter().randomlyPlace(halls[0]);

        new Prop(4).randomlyPlace(halls[1]);
        new Prop(4).randomlyPlace(halls[1]);
        new Prop(4).randomlyPlace(halls[1]);
        new Prop(4).randomlyPlace(halls[1]);
        new Prop(4).randomlyPlace(halls[1]);
        new Prop(4).randomlyPlace(halls[1]);
        new Prop(2).randomlyPlace(halls[1]);
        new Archer().randomlyPlace(halls[1]);
        new Wizard().randomlyPlace(halls[1]);
        new Fighter().randomlyPlace(halls[1]);

        new Prop(4).place(2, 15, halls[2]);
        new Prop(4).place(3, 15, halls[2]);
        new Prop(4).place(4, 15, halls[2]);
        new Prop(4).place(5, 15, halls[2]);
        new Prop(4).place(6, 14, halls[2]);
        new Prop(4).place(7, 13, halls[2]);
        new Prop(4).randomlyPlace(halls[2]);
        new Prop(4).randomlyPlace(halls[2]);

        new Prop(4).randomlyPlace(halls[3]);
        new Wizard().randomlyPlace(halls[3]);
        //
        
        PlayPanel playPanel = new PlayPanel(halls, spriteHandler);
        window.add(playPanel);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        playPanel.startGameThread(); // start game loop
    }
}
