package com.rokue.game.audio;

import java.net.URL;
import java.util.HashMap;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class SoundManager {
    private static final HashMap<String, Clip> soundClips = new HashMap<>();

    public static synchronized void loadSound(String name, String soundFilePath) {
        try {
            // Use ClassLoader to load resources from the classpath
            URL soundURL = SoundManager.class.getResource(soundFilePath);
            if (soundURL == null) {
                throw new RuntimeException("Sound file not found: " + soundFilePath);
            }

            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            soundClips.put(name, clip);
            System.out.println("Loaded sound: " + name);
        } catch (Exception e) {
            System.err.println("Failed to load sound: " + name);
            e.printStackTrace();
        }
    }

    public static synchronized void playSound(String name) {
        Clip clip = soundClips.get(name);
        if (clip != null) {
            new Thread(() -> {
                clip.setFramePosition(0); // Rewind to the beginning
                clip.start();
            }).start();
        } else {
            System.err.println("Sound not found: " + name);
        }
    }

    public static synchronized void closeAllSounds() {
        for (Clip clip : soundClips.values()) {
            clip.close();
        }
        soundClips.clear();
        System.out.println("All sounds closed and cleared.");
    }
}

