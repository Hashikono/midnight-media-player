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
        private JSlider mediaProgressSlider;
        private JLabel mediaNameLabel;
        private JLabel folderOriginLabel;

        //screen center
        private JButton shuffleButton;
        private JButton home_addFolderButton;
        private JButton settingsButton;
        private JLabel sectionLabel;
        private JLabel letterGroupLabel;
        private JLabel folderGroupLabel;
        private JLabel ArtistGroupLabel;
        private JLabel dateGroupLabel;
        private JLabel timeGroupLabel;
        private JList<String> playlistList;   
        private DefaultListModel<String> playlistModel;
    
    
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
        

    //---------------- Application States ---------------
    private boolean isPlaying = false;
    private boolean isShuffled = false;
    private boolean isMuted = false;
    private int currentTrackIndex = -1; //supposed to track playing track -> -1 = none


    //---------------- Appearance ----------------------------
    private static final String ICON_PATH = "icon.png";

    //change to midnight theme
    private final Color PRIMARY_COLOR = new Color(242, 75, 75);   // Midnight red color
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);  // Lighter blue
    private final Color ACCENT_COLOR = new Color(46, 204, 113);     // Green accent
    private final Color DARK_BG = new Color(33, 31, 31);            // Dark background
    private final Color LIGHT_BG = new Color(176, 162, 164);        // Light background
    private final Color TEXT_COLOR = new Color(220, 220, 220);      // Text color
    private final Color HIGHLIGHT_COLOR = new Color(155, 89, 182);  // Purple highlight


    //constructor...
    public MidnightMediaPlayer() {
        //Initialization
        setTitle("Midnight Media Player");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //icon setting
        ImageIcon icon = new ImageIcon(ICON_PATH);
        setIconImage(icon.getImage());

        //initialize other stuff...
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        //// Initialize all GUI components
        //initializeComponents();
        //
        //// Set up the layout of components
        //setupLayout();
        //
        //// Set up event handlers for components
        //setupEventListeners();
        //
        //// Initialize player state (enable/disable buttons)
        //updateButtonStates();
        //
        //// Set up custom window controls (minimize, maximize, close)
        //setupWindowControls();
    }
}