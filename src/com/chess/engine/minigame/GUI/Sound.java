package com.chess.engine.minigame.GUI;

import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sound {
    private Clip clip;
    private FloatControl bgFc, seFc;
    private int bgVolumeScale = 50, seVolumeScale = 50;
    private float bgVolume = - 14f, seVolume = -14f;
    

    public Sound() {
    }

    public void playBG(){
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(new File("sound\\background.wav"));
            clip = AudioSystem.getClip();
            clip.open(ais);
            bgFc = (FloatControl)clip.getControl(FloatControl.Type.MASTER_GAIN);
            setBGVolume(bgVolumeScale);
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
            seFc = (FloatControl)newClip.getControl(FloatControl.Type.MASTER_GAIN);
            setSEVolume(seVolumeScale);
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
    public void setBGVolume(final int scale){
        bgVolumeScale = scale;
        bgVolume = -34f + 0.4f * scale;
        bgFc.setValue(bgVolume);
    }
    
    public void setSEVolume(final int scale){
        seVolumeScale = scale;
        seVolume = -34f + 0.4f * scale;
        seFc.setValue(seVolume);
    }

    public int getBgVolumeScale() {
        return bgVolumeScale;
    }

    public int getSeVolumeScale() {
        return seVolumeScale;
    }

    
}
