package ui;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MusicPlayer {
    private Clip clip;

    public void playMusic(String filepath) { //เล่นเพลงจาก file ในเครื่อง
        try {
            File musicPath = new File(filepath);
            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath); //อ่านเพลงจาก file
                clip = AudioSystem.getClip();
                clip.open(audioInput); //เล่นเพลง
                clip.loop(Clip.LOOP_CONTINUOUSLY); //เล่นซ้ำไปเรื่อย
                clip.start();
            } else { //กรณีหาไม่เจอ
                System.out.println("File not found: " + filepath); 
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) { //กรณีใช้ file ผิดประเภทให้แจ้ง error 
            e.printStackTrace();
        }
    }

    public void playSoundEffect(String filepath) { //เล่นเสียงจาก file ในเครื่อง (เล่นครั้งเดียว)
        try {
            File soundPath = new File(filepath);
            if (soundPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(soundPath); //อ่านเพลงจาก file
                Clip soundClip = AudioSystem.getClip();
                soundClip.open(audioInput); //เล่นเพลง
                soundClip.start();
            } else { //กรณีหาไม่เจอ
                System.out.println("File not found: " + filepath);
            }
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) { //กรณีใช้ file ผิดประเภทให้แจ้ง error 
            e.printStackTrace();
        }
    }
}
