//Package imports
import javax.swing.*; 
import javax.swing.border.*;  //border styling (maybe)
import javax.swing.filechooser.FileNameExtensionFilter;  //File type filtering
import java.awt.*;  //Abstract window toolkit for GUI development
import java.awt.event.*;  //AWT event handling
import java.io.File;  //File handling
import java.util.ArrayList;

//Note: extending JFrae allows for easy access to all methods...although the contructore is gonna be weird
public class MidnightMediaPlayer extends JFrame{
    //---------------- GUI components ---------------
    
    //Main screen
        //Side Panel
        private JButton homeButton; 
        private JButton musicListButton; 
        private JButton playlistButton; 
        private JButton videoListButton; 
        private JButton mainExpandButton;
        
        //media control
        private JButton playButton; //also the pause button
        private JButton nextButton;
        private JButton prevButton;
        private JButton loopButton;
        private JButton muteButton;
        private JButton fullscreenButton;

        //screen center
        private JButton shuffleButton;
        private JButton home_addFolderButton;
        private JButton settingsButton;
    
    //Settings screen
        //Side Panel
        private JButton settingsBackButton; 
        private JButton manageDirectoriesButton; 
        private JButton appearanceButton; 
        private JButton infoButton; 
        private JButton settingsExpandButton;

        //manage directories page
        private JButton folder_addFolderButton;
        private JButton multiselectFoldersButton;
        private JButton removeSelectedFolderButton;

        //appearance page
        private JButton appearanceModeToggleButton;
        


    private JSlider volumeSlider; 
    private JSlider progressSlider;

    private JLabel title;
    private JLabel albumArt;

    //Playlist 
    private JList<String> playlistList;   
    private DefaultListModel<String> playlistModel;
    
    //Panels

    //---------------- Application States ---------------

    private boolean isPlaying = false;
    private boolean isPaused = false;
    private boolean isShuffling = false;
    private boolean playlistVisible = true; //not sure if needed
    private boolean isMuted = false; //pretty sure its not needed
    private List<File> playlist = new ArrayList<>();
    private int currentTrackIndex = -1; //supposed to track playing track -> -1 = none

    //---------------- Colors ----------------------------
    //change to midnight theme


    private final Color PRIMARY_COLOR = new Color(242, 75, 75);   // Midnight red color
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);  // Lighter blue
    private final Color ACCENT_COLOR = new Color(46, 204, 113);     // Green accent
    private final Color DARK_BG = new Color(33, 31, 31);            // Dark background
    private final Color LIGHT_BG = new Color(176, 162, 164);        // Light background
    private final Color TEXT_COLOR = new Color(220, 220, 220);      // Text color
    private final Color HIGHLIGHT_COLOR = new Color(155, 89, 182);  // Purple highlight
}


public TestApp() {

        setTitle("Midnight Media Player");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setShape(new RoundRectangle2D.Double(0, 0, 1000, 700, 30, 30)); // Remove default window decorations (title bar, borders) -> maybe change later
        
        // Initialize all GUI components
        initializeComponents();
        
        // Set up the layout of components
        setupLayout();
        
        // Set up event handlers for components
        setupEventListeners();
        
        // Initialize player state (enable/disable buttons)
        updateButtonStates();
        
        // Set up custom window controls (minimize, maximize, close)
        setupWindowControls();
    }
    
private void initializeComponents() {
    playButton = createModernButton("▶︎", "Play", PRIMARY_COLOR);
    //also the pause button
    pauseButton = createModernButton("❚❚", "Pause", PRIMARY_COLOR);
    nextButton = createModernButton("⏭", "Next", LIGHT_BG);
    prevButton = createModernButton("⏮", "Previous", LIGHT_BG);
    volumeSlider = new JSlider(0, 100, 50)
        @Override
                protected void paintComponent(Graphics g) {
                    // Create Graphics2D object for advanced painting
                    Graphics2D g2 = (Graphics2D) g.create();
                    
                    // Enable anti-aliasing for smooth edges
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                    RenderingHints.VALUE_ANTIALIAS_ON);
                    
                    // Get dimensions
                    int width = getWidth();
                    int height = getHeight();
                    int trackHeight = 4;  // Height of slider track
                    int trackY = height / 2 - trackHeight / 2;  // Center vertically
                    
                    // Draw background track (gray)
                    g2.setColor(new Color(60, 60, 65));
                    g2.fillRoundRect(5, trackY, width - 10, trackHeight, 
                                    trackHeight, trackHeight);
                    
                    // Draw progress track (gradient green)
                    int progressWidth = (int) ((width - 10) * 
                        ((double) getValue() / getMaximum()));
                    GradientPaint gradient = new GradientPaint(0, trackY, ACCENT_COLOR, 
                                                            width, trackY, 
                                                            new Color(39, 174, 96));
                    g2.setPaint(gradient);
                    g2.fillRoundRect(5, trackY, progressWidth, trackHeight, 
                                    trackHeight, trackHeight);
                    
                    // Draw slider thumb (circle)
                    int thumbSize = 16;  // Diameter of thumb
                    int thumbX = 5 + progressWidth - thumbSize/2;  // Position at progress end
                    g2.setColor(Color.WHITE);
                    g2.fillOval(thumbX, height/2 - thumbSize/2, thumbSize, thumbSize);
                    
                    // Clean up Graphics2D object
                    g2.dispose();
                }
            };
    }