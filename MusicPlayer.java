import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MusicPlayer {
    private boolean isPlaying = false;    // Tracks if media is currently playing
    private boolean isMuted = false;      // Tracks if audio is muted
    private boolean isRepeating = false;  // Tracks if repeat mode is enabled
    private boolean isShuffling = false;  // Tracks if shuffle mode is enabled
    private boolean playlistVisible = true; // Tracks playlist visibility
    private List<File> playlist = new ArrayList<>(); // List of media files
    private int currentTrackIndex = -1;   // Index of currently playing track (-1 = none)


    // Play next track in playlist
    private void playNextTrack() {
        if (playlist.isEmpty()) return;  // Nothing to play
        
        // Calculate next track index
        if (isShuffling) {
            // Random track for shuffle mode
            currentTrackIndex = (int) (Math.random() * playlist.size());
        } else {
            // Next track in sequence, wrap around to beginning
            currentTrackIndex = (currentTrackIndex + 1) % playlist.size();
        }
        
        // playTrack(currentTrackIndex);  // Play the selected track
    }
    
    // Play previous track in playlist
    private void playPreviousTrack() {
        if (playlist.isEmpty()) return;  // Nothing to play
        
        // Calculate previous track index (wrap around to end if needed)
        currentTrackIndex = (currentTrackIndex - 1 + playlist.size()) % playlist.size();
        
        // playTrack(currentTrackIndex);  // Play the selected track
    }
    
    
    // Format seconds into MM:SS format
    private String formatTime(int seconds) {
        int mins = seconds / 60;    // Calculate minutes
        int secs = seconds % 60;    // Calculate remaining seconds
        return String.format("%02d:%02d", mins, secs);  // Format as 00:00
    }
    
    // Get name of current track
    private String getCurrentTrackName() {
        if (currentTrackIndex >= 0 && currentTrackIndex < playlist.size()) {
            return playlist.get(currentTrackIndex).getName();
        }
        return "No track";  // Default text when no track is loaded
    }
    
}
