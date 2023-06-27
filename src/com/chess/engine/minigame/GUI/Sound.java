package com.chess.engine.minigame.GUI;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Sound {
    Clip clip;

    public Sound() {
    }

    public void playBG(){
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("sound\\background.wav"));
            clip = AudioSystem.getClip();
            clip.open(ais);
            clip.start();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void playSE(String filename) {
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File(filename));
            Clip newClip = AudioSystem.getClip();
            newClip.open(ais);
            newClip.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
    }
}
