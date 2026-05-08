import java.io.File;
import java.util.ArrayList;
import java.util.List;

import models.Media;
import models.Playlist;

import uk.co.caprica.vlcj.player.component.EmbeddedMediaListPlayerComponent;

import uk.co.caprica.vlcj.factory.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.base.MediaPlayer;
import uk.co.caprica.vlcj.player.base.MediaPlayerEventAdapter;

public class MusicPlayer {
    private static boolean isPaused = false;    // Tracks if media is currently playing
    private static boolean isMuted = false;      // Tracks if audio is muted
    private static boolean isRepeating = false;  // Tracks if repeat mode is enabled
    private static boolean isShuffling = false;  // Tracks if shuffle mode is enabled
    // private static boolean playlistVisible = true; // Tracks playlist visibility
    private static List<Media> currentPlaylist = new ArrayList<>(); // List of media files
    public static Media currentSong;
    public static int currentTrackIndex = -1;   // Index of currently playing track (-1 = none) Only used if not shuffling


    public static MediaPlayerFactory factory;
    private static MediaPlayer player;

    public static void initialize()
    {
        MediaVisuals.visualizer = new EmbeddedMediaListPlayerComponent();
        MediaVisuals.visualizer.mediaPlayer().audio().mute();

        factory = new MediaPlayerFactory();
        player = factory.mediaPlayers().newMediaPlayer();

        // System.out.println(player.mediaPlayerInstance());

        player.events().addMediaPlayerEventListener(new MediaPlayerEventAdapter() {
            @Override
            public void finished(MediaPlayer mediaPlayer) {
                if(currentPlaylist != null)
                    playNextTrack();
            }

            @Override
            public void error(MediaPlayer mediaPlayer) {
                System.out.println("had an error");
            }

            @Override
            public void timeChanged(MediaPlayer mediaPlayer, long newTime) {
                MediaControlBar.moveProgress((int)newTime / 1000);
            }
        });

        
    }

    public static void togglePaused()
    {
        isPaused = !isPaused;

        player.controls().setPause(isPaused);
    }


    // Play next track in playlist
    private static void playNextTrack() { //causing issues for some reason...
        if (currentPlaylist.isEmpty()) return;  // Nothing to play
        
        // Calculate next track index
        if (isShuffling) {
            // Random track for shuffle mode
            currentTrackIndex = (int) (Math.random() * currentPlaylist.size());
        } else {
            // Next track in sequence, wrap around to beginning
            currentTrackIndex = (currentPlaylist.size() + currentTrackIndex + 1) % currentPlaylist.size();
        }

        playTrack(currentPlaylist.get(currentTrackIndex));  // Play the selected track
        
    }
    
    // Play previous track in playlist
    private static void playPreviousTrack() { //I might make it remember the past however many songs played to be able to run back on o7
        if (currentPlaylist.isEmpty()) return;  // Nothing to play
        
        // Calculate previous track index (wrap around to end if needed)
        currentTrackIndex = (currentTrackIndex - 1 + currentPlaylist.size()) % currentPlaylist.size();
        
        // playTrack(currentTrackIndex);  // Play the selected track
    }

    public static void setProgress(long time)
    {
        player.controls().setTime(time);
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
        try {
            MediaControlBar.setNewSong((int)MediaFileHandler.getDuration(song.path));
            player.media().play(song.path);

            // if(MediaVisuals.visualizer.getParent() != null)
            //     MediaVisuals.visualizer.mediaPlayer().media().play(song.path);
        } catch (Exception e) {
            e.printStackTrace();
        }
        //MediaFileHander. (some integration here?)
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

    public static void detatchVisuals()
    {
        // player.videoSurface().set(null); //Doesn't work in this version unfortunately, wish it did, lol
        

    }

    public static void syncVisuals()
    {
        long currentTime = player.status().time();

        MediaVisuals.visualizer.mediaPlayer().media().play(currentSong.path);
        MediaVisuals.visualizer.mediaPlayer().controls().setTime(currentTime);
    }
    
}
