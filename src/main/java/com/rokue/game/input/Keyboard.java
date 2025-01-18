/*package com.rokue.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class Keyboard implements KeyListener {

    public boolean up, down, left, right;
    public boolean use, useLock;
    public boolean r, p, b, w, a, s, d;
    public boolean pause, pauseLock;

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
}*/
package com.rokue.game.input;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.rokue.game.entities.Archer;
import com.rokue.game.entities.Fighter;

public class Keyboard implements KeyListener {

    private StringBuilder commandBuffer = new StringBuilder();

    public boolean up, down, left, right;
    public boolean use, useLock;
    public boolean r, p, b, w, a, s, d;
    public boolean pause, pauseLock;

    @Override
    public void keyTyped(KeyEvent e) {
        // Append the typed key to the command buffer
        handleKeyPress(e.getKeyChar());
    }

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

    public void handleKeyPress(char key) {
        // Append the pressed key to the command buffer
        commandBuffer.append(key);

        // Keep the buffer length manageable
        if (commandBuffer.length() > 8) {
            commandBuffer.deleteCharAt(0);
        }

        // Check for commands
        String command = commandBuffer.toString();
        if (command.equals("fight")) {
            Fighter.toggleAlternateAppearance();
            commandBuffer.setLength(0); // Clear the buffer after triggering
        } else if (command.equals("asdfg")) {
            Archer.toggleAlternateAppearance();
            commandBuffer.setLength(0); // Clear the buffer after triggering
        }
    }
}
