package com.rokue.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {
    public boolean up, down, left, right;
    public boolean b, a, d, w, s, p, q, r, z;

    @Override
    public void keyTyped(KeyEvent e) {
        // Unused but required
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> up = true;
            case KeyEvent.VK_DOWN -> down = true;
            case KeyEvent.VK_LEFT -> left = true;
            case KeyEvent.VK_RIGHT -> right = true;
            case KeyEvent.VK_B -> b = true;
            case KeyEvent.VK_A -> a = true;
            case KeyEvent.VK_D -> d = true;
            case KeyEvent.VK_W -> w = true;
            case KeyEvent.VK_S -> s = true;
            case KeyEvent.VK_P -> p = true;
            case KeyEvent.VK_Q -> q = true;
            case KeyEvent.VK_R -> r = true;
            case KeyEvent.VK_Z -> z = true;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> up = false;
            case KeyEvent.VK_DOWN -> down = false;
            case KeyEvent.VK_LEFT -> left = false;
            case KeyEvent.VK_RIGHT -> right = false;
            case KeyEvent.VK_B -> b = false;
            case KeyEvent.VK_A -> a = false;
            case KeyEvent.VK_D -> d = false;
            case KeyEvent.VK_W -> w = false;
            case KeyEvent.VK_S -> s = false;
            case KeyEvent.VK_P -> p = false;
            case KeyEvent.VK_Q -> q = false;
            case KeyEvent.VK_R -> r = false;
            case KeyEvent.VK_Z -> z = false;
        }
    }
}
