package main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.net.URL;

public class Sound {
    Clip clip;
    URL[] soundURL = new URL[30];
    FloatControl fc;
    int volumeScale = 3;
    float volume;

    public Sound() {
        soundURL[0] = getClass().getResource("/sound/itemGet.wav");
        soundURL[1] = getClass().getResource("/sound/cursor.wav");
        soundURL[2] = getClass().getResource("/sound/Jingle2.wav");
        soundURL[3] = getClass().getResource("/sound/PAGE.wav");
        soundURL[4] = getClass().getResource("/sound/jingle4.wav");
        soundURL[5] = getClass().getResource("/sound/textscroll.wav");
        soundURL[6] = getClass().getResource("/sound/BookClose.wav");
        soundURL[7] = getClass().getResource("/sound/PageOpen.wav");

    }

    public void setFile(int i) {
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            fc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            checkVolume();
        }

        catch (Exception e) {
            System.out.println(e);
        }
    }

    public void play() {
        clip.start();

    }

    public void loop() {
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        clip.stop();
    }

    public void checkVolume() {
        switch (volumeScale) {
            case 0 -> volume = -80f;
            case 1 -> volume = -20f;
            case 2 -> volume = -12f;
            case 3 -> volume = -5f;
            case 4 -> volume = 1f;
            case 5 -> volume = 6f;
        }
        fc.setValue(volume);

    }
}
