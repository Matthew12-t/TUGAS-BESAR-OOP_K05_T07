package SRC.MAIN;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

/**
 * MusicManager handles background music playback with looping capability
 */
public class MusicManager {
    private Clip musicClip;
    private boolean isPlaying = true;
    private String currentMusicPath;
    
    /**
     * Load and play music from file path
     * @param musicPath Path to the music file
     */    public void playMusic(String musicPath) {
        try {
            // Stop any currently playing music
            stopMusic();
            
            // Load the music file
            File musicFile = new File(musicPath);
            if (!musicFile.exists()) {
                System.err.println("Music file not found: " + musicPath);
                return;
            }
            
            System.out.println("Attempting to load music file: " + musicFile.getAbsolutePath());
            
            // Load audio stream (WAV format is supported natively)
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
              // Get audio format and create clip
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
            
            // Set to loop continuously
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            
            // Start playing
            musicClip.start();
            isPlaying = true;
            currentMusicPath = musicPath;
            
            System.out.println("Music started successfully: " + musicPath);
            System.out.println("Music clip status - Running: " + musicClip.isRunning() + ", Open: " + musicClip.isOpen());
            
            // Close the audio stream as it's no longer needed
            audioStream.close();
            
        } catch (LineUnavailableException | IOException e) {
            System.err.println("Error playing music: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Stop the currently playing music
     */
    public void stopMusic() {
        if (musicClip != null && isPlaying) {
            musicClip.stop();
            musicClip.close();
            musicClip = null;
            isPlaying = false;
            System.out.println("Music stopped");
        }
    }
    
    /**
     * Pause the music
     */
    public void pauseMusic() {
        if (musicClip != null && isPlaying) {
            musicClip.stop();
            isPlaying = false;
            System.out.println("Music paused");
        }
    }
    
    /**
     * Resume the music
     */
    public void resumeMusic() {
        if (musicClip != null && !isPlaying) {
            musicClip.start();
            isPlaying = true;
            System.out.println("Music resumed");
        }
    }
    
    /**
     * Set music volume (0.0 to 1.0)
     * @param volume Volume level
     */
    public void setVolume(float volume) {
        if (musicClip != null) {
            if (volume < 0.0f) volume = 0.0f;
            if (volume > 1.0f) volume = 1.0f;
            
            FloatControl volumeControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            float dB = (float) (Math.log(volume) / Math.log(10.0) * 20.0);
            volumeControl.setValue(dB);
        }
    }
    
    /**
     * Check if music is currently playing
     * @return true if music is playing
     */
    public boolean isPlaying() {
        return isPlaying && musicClip != null && musicClip.isRunning();
    }
    
    /**
     * Get the current music file path
     * @return current music path
     */
    public String getCurrentMusicPath() {
        return currentMusicPath;
    }
    
    /**
     * Clean up resources when MusicManager is no longer needed
     */
    public void dispose() {
        stopMusic();
    }
}
