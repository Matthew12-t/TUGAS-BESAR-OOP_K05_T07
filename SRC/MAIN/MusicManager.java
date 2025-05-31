package SRC.MAIN;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;


public class MusicManager {
    private Clip musicClip;
    private boolean isPlaying = true;
    private String currentMusicPath;
    
   public void playMusic(String musicPath) {
        try {
            
            stopMusic();
            
            
            File musicFile = new File(musicPath);
            if (!musicFile.exists()) {
                System.err.println("Music file not found: " + musicPath);
                return;
            }
            
            System.out.println("Attempting to load music file: " + musicFile.getAbsolutePath());
            
            
            AudioInputStream audioStream;
            try {
                audioStream = AudioSystem.getAudioInputStream(musicFile);
                System.out.println("Audio stream loaded successfully");
            } catch (UnsupportedAudioFileException e) {
                System.err.println("Unsupported audio format. Please ensure the file is in WAV format.");
                System.err.println("Music file: " + musicPath);
                e.printStackTrace();
                return;
            }
              
            AudioFormat format = audioStream.getFormat();
            System.out.println("Audio format: " + format);
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            
            if (!AudioSystem.isLineSupported(info)) {
                System.err.println("Audio line not supported for format: " + format);
                audioStream.close();
                return;
            }
            
            System.out.println("Creating audio clip...");
            musicClip = (Clip) AudioSystem.getLine(info);
            musicClip.open(audioStream);
            
            
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            
            
            musicClip.start();
            isPlaying = true;
            currentMusicPath = musicPath;
            
            System.out.println("Music started successfully: " + musicPath);
            System.out.println("Music clip status - Running: " + musicClip.isRunning() + ", Open: " + musicClip.isOpen());
            
            
            audioStream.close();
            
        } catch (LineUnavailableException | IOException e) {
            System.err.println("Error playing music: " + e.getMessage());
            e.printStackTrace();
        }
    }
    

    public void stopMusic() {
        if (musicClip != null && isPlaying) {
            musicClip.stop();
            musicClip.close();
            musicClip = null;
            isPlaying = false;
            System.out.println("Music stopped");
        }
    }
    

    public void pauseMusic() {
        if (musicClip != null && isPlaying) {
            musicClip.stop();
            isPlaying = false;
            System.out.println("Music paused");
        }
    }
    

    public void resumeMusic() {
        if (musicClip != null && !isPlaying) {
            musicClip.start();
            isPlaying = true;
            System.out.println("Music resumed");
        }
    }
    

    public void setVolume(float volume) {
        if (musicClip != null) {
            if (volume < 0.0f) volume = 0.0f;
            if (volume > 1.0f) volume = 1.0f;
            
            FloatControl volumeControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            volumeControl.setValue(dB);
        }
    }
    
    public boolean isPlaying() {
        return isPlaying && musicClip != null && musicClip.isRunning();
    }
    

    public String getCurrentMusicPath() {
        return currentMusicPath;
    }
    

    public void dispose() {
        stopMusic();
    }
}
