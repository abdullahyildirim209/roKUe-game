package game;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

    boolean up, down, left, right;
    boolean use, useLock;
    boolean helpPressed;

    @Override
    public void keyTyped(KeyEvent e) {} // unused but required due to implemented interface

    @Override
    public void keyPressed(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_UP) {
            up = true;
        }
        if (code == KeyEvent.VK_DOWN) {
            down = true;
        }
        if (code == KeyEvent.VK_LEFT) {
            left = true;
        }
        if (code == KeyEvent.VK_RIGHT) {
            right = true;
        }
        if (code == KeyEvent.VK_C) {
            if (!useLock) {
                use = true;
                useLock = true;
            }
        }
        if (code == KeyEvent.VK_H) {
        helpPressed = true;
        left = false;
        right = false;
        up = false;
        down = false;
    }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int code = e.getKeyCode();

        if (code == KeyEvent.VK_UP) {
            up = false;
        }
        if (code == KeyEvent.VK_DOWN) {
            down = false;
        }
        if (code == KeyEvent.VK_LEFT) {
            left = false;
        }
        if (code == KeyEvent.VK_RIGHT) {
            right = false;
        }
        if (code == KeyEvent.VK_C) {
            useLock = false;
        }
        if (code == KeyEvent.VK_H) {
            helpPressed = false;
        }
    }
}
