package main;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import game.Hall;
import game.PlayPanel;
import game.Prop;
import game.RNG;
import game.SpriteLoader;

public class Main {
    public static void main(String[] args) {

        // create the window
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("RoKUe");
        window.setIconImage(new ImageIcon(SpriteLoader.class.getResource("sprites/icon.png")).getImage());

        // utils
        RNG RNG = new RNG();
        SpriteLoader spriteHandler = new SpriteLoader();

        // temporary builder mode
        Hall[] halls = {new Hall(RNG), new Hall(RNG), new Hall(RNG), new Hall(RNG)};
        new Prop(2).place(7, 12, halls[0]);
        new Prop(2).randomlyPlace(halls[0]);

        new Prop(2).randomlyPlace(halls[1]);
        new Prop(2).randomlyPlace(halls[1]);
        new Prop(2).randomlyPlace(halls[1]);
        new Prop(2).randomlyPlace(halls[1]);
        new Prop(2).randomlyPlace(halls[1]);
        new Prop(2).randomlyPlace(halls[1]);

        new Prop(2).place(2, 15, halls[2]);
        new Prop(2).place(3, 15, halls[2]);
        new Prop(2).place(4, 15, halls[2]);
        new Prop(2).place(5, 15, halls[2]);
        new Prop(2).place(6, 14, halls[2]);
        new Prop(2).place(7, 13, halls[2]);
        new Prop(2).randomlyPlace(halls[2]);
        new Prop(2).randomlyPlace(halls[2]);

        new Prop(2).randomlyPlace(halls[3]);
        //
        
        PlayPanel playPanel = new PlayPanel(halls, spriteHandler);
        window.add(playPanel);

        window.pack();
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        playPanel.startGameThread(); // start game loop
    }
}
