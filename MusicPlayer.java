import java.io.File;
import java.util.ArrayList;
import java.util.List;

import models.Media;
import models.Playlist;

public class MusicPlayer {
    private static boolean isPlaying = false;    // Tracks if media is currently playing
    private static boolean isMuted = false;      // Tracks if audio is muted
    private static boolean isRepeating = false;  // Tracks if repeat mode is enabled
    private static boolean isShuffling = false;  // Tracks if shuffle mode is enabled
    private static boolean playlistVisible = true; // Tracks playlist visibility
    private static List<Media> currentPlaylist; // List of media files
    public static Media currentSong;
    private static int currentTrackIndex = -1;   // Index of currently playing track (-1 = none) Only used if not shuffling


    // Play next track in playlist
    private static void playNextTrack() {
        if (currentPlaylist.isEmpty()) return;  // Nothing to play
        
        // Calculate next track index
        if (isShuffling) {
            // Random track for shuffle mode
            currentTrackIndex = (int) (Math.random() * currentPlaylist.size());
        } else {
            // Next track in sequence, wrap around to beginning
            currentTrackIndex = (currentTrackIndex + 1) % currentPlaylist.size();
            playTrack(currentPlaylist.get(currentTrackIndex));  // Play the selected track
        }
        
    }
    
    // Play previous track in playlist
    private static void playPreviousTrack() { //I might make it remember the past however many songs played to be able to run back on o7
        if (currentPlaylist.isEmpty()) return;  // Nothing to play
        
        // Calculate previous track index (wrap around to end if needed)
        currentTrackIndex = (currentTrackIndex - 1 + currentPlaylist.size()) % currentPlaylist.size();
        
        // playTrack(currentTrackIndex);  // Play the selected track
    }
    
    
    // Format seconds into MM:SS format
    private static String formatTime(int seconds) {
        int mins = seconds / 60;    // Calculate minutes
        int secs = seconds % 60;    // Calculate remaining seconds
        return String.format("%02d:%02d", mins, secs);  // Format as 00:00
    }
    
    // Get name of current track
    private static String getCurrentTrackName() {
        if (currentTrackIndex >= 0 && currentTrackIndex < currentPlaylist.size()) {
            return currentPlaylist.get(currentTrackIndex).name;
        }
        return "No track";  // Default text when no track is loaded
    }

    public static void playTrack(Media song)
    {
        currentSong = song;
    }

    public static void getPlaylist(Playlist playlist)
    {
        //Just in case
    }

    public static void getPlaylist(List<Media> playlist)
    {
        currentPlaylist = playlist;

        if(isShuffling)
            currentTrackIndex = -1;

        playNextTrack();
    }
    
}
