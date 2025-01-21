package com.rokue.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;

public class Keyboard implements KeyListener, MouseListener, Serializable {
    private static final long serialVersionUID = 1L;

    // Existing fields
    public boolean up, down, left, right;
    public boolean use, useLock;
    public boolean r, p, b, w, a, s, d;
    public boolean pause, pauseLock;

    // === New mouse fields ===
    public boolean mouseButtonPressed;    // Tracks if the left mouse button was pressed
    private boolean mouseLock;            // Used to avoid repeated triggers from press+release

    @Override
    public void keyTyped(KeyEvent e) {} // unused but required

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
        if (code == KeyEvent.VK_R) r = true;
        if (code == KeyEvent.VK_P) p = true;
        if (code == KeyEvent.VK_B) b = true;
        if (code == KeyEvent.VK_W) w = true;
        if (code == KeyEvent.VK_A) a = true;
        if (code == KeyEvent.VK_S) s = true;
        if (code == KeyEvent.VK_D) d = true;

        if (code == KeyEvent.VK_ESCAPE) {
            if (!pauseLock) {
                pause = !pause;
                pauseLock = true;
            }
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
        if (code == KeyEvent.VK_R) r = false;
        if (code == KeyEvent.VK_P) p = false;
        if (code == KeyEvent.VK_B) b = false;
        if (code == KeyEvent.VK_W) w = false;
        if (code == KeyEvent.VK_A) a = false;
        if (code == KeyEvent.VK_S) s = false;
        if (code == KeyEvent.VK_D) d = false;

        if (code == KeyEvent.VK_ESCAPE) {
            pauseLock = false;
        }
    }

    // === Implement MouseListener to detect clicks ===
    @Override
    public void mouseClicked(MouseEvent e) {
        // Not strictly needed if we rely on mousePressed
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1 && !mouseLock) {
            mouseButtonPressed = true;
            mouseLock = true;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            mouseLock = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {}
    @Override
    public void mouseExited(MouseEvent e) {}
}
