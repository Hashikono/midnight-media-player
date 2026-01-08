// Package imports
import javax.swing.*;
import javax.swing.border.*;

import models.Media;
import models.Playlist;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.List;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class MidnightMediaPlayer extends JFrame {
    private static MidnightMediaPlayer player;
    //---------------- GUI components ---------------
    // Main screen
    // Side Panel
    private Clip currentClip;
    private AudioInputStream audioStream;
    private Timer playbackTimer;
    private JPanel sidePanel;
    private JButton homeButton; 
    private JButton musicListButton; 
    private JButton playlistButton; 
    private JButton logMenuButton; 
    private JButton mainExpandButton;
    
    // Media control panel
    private JPanel controlPanel;
    private JButton playButton;
    private JButton nextButton;
    private JButton prevButton;
    private JButton loopButton;
    private JButton shuffleButton;
    private JButton muteButton;
    private JButton fullscreenButton;
    private JSlider mediaProgressSlider;
    private JLabel mediaNameLabel;
    private JLabel folderOriginLabel;
    private JLabel currentTimeLabel;
    private JLabel totalTimeLabel;
    List<Media> allSongs;

    
    // Main content area
    private JPanel mainContentPanel;
    private JLabel sectionLabel;
    private JButton newMediaButton;
    private JButton settingsButton;
    private JPanel selectedMenuPanel;
    private JScrollPane playlistScrollPane;
    private JList<String> playlistList;   
    private DefaultListModel<String> playlistModel;
    
    // Volume control
    private JSlider volumeSlider;
    
    // Settings screen components
    private JPanel settingsPanel;
    private JButton settingsBackButton;
    private JButton manageDirectoriesButton;
    private JButton appearanceButton;
    private JButton infoButton;
    private JButton settingsExpandButton;
    private JButton folder_addFolderButton;
    private JButton multiselectFoldersButton;
    private JButton removeSelectedFolderButton;
    private JButton appearanceModeToggleButton;
    
    //---------------- Application States ---------------
    private boolean isPlaying = false;
    private boolean isShuffled = false;
    private boolean isMuted = false;
    private boolean isLooped = false;
    private boolean isFullscreen = false;
    private int currentTrackIndex = -1;

    private enum tab {Home, Media, Playlist, Log};
    private tab currentTab = tab.Home;
    
    //---------------- Appearance ----------------------------
    // Dark theme colors - based on typical midnight/dark themes
    private final Color PRIMARY_COLOR = new Color(220, 20, 60);     // Crimson red - for accents
    private final Color SECONDARY_COLOR = new Color(30, 144, 255);  // Dodger blue - for secondary elements
    private final Color ACCENT_COLOR = new Color(50, 205, 50);      // Lime green - for active states
    private final Color DARK_BG = new Color(40, 40, 40);            // Near black background
    private final Color DARKER_BG = new Color(30, 30, 30);          // Even darker for panels
    private final Color TEXT_COLOR = new Color(180, 180, 180);      // Light gray text
    private final Color HIGHLIGHT_COLOR = new Color(138, 43, 226);  // Blue violet - for hover states
    private final Color SLIDER_COLOR = new Color(70, 70, 70);       // Dark gray for slider tracks
    private final Color SLIDER_THUMB = new Color(220, 20, 60);      // Red for slider thumb
    
    // Fonts - trying to match your CSS fonts
    private Font mogaFont;
    private Font ptSansFont;
    private Font ptSerifFont;
    private Font wsParadoseFont;
    private Font wsParadoseItalicFont;
    
    // Constructor
    public MidnightMediaPlayer() {
        try {
            Database.initialize();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setTitle("Midnight Media Player");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 800));
        
        // Set application icon (placeholder)
        setIconImage(new ImageIcon("icon.png").getImage());
        
        // Initialize track index
        currentTrackIndex = -1;
        
        // Load custom fonts
        loadCustomFonts();
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
        updateButtonStates();
        // Initialize playback timer
        playbackTimer = new Timer(100, e -> updatePlaybackProgress());
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);

        switchToHomeView();
        // Initialize playback timer
        playbackTimer = new Timer(100, e -> updatePlaybackProgress());
    }
    
    private void updatePlaybackProgress() {
    if (currentClip != null && isPlaying) {
        long position = currentClip.getMicrosecondPosition();
        long duration = currentClip.getMicrosecondLength();
        
        if (duration > 0) {
            int progress = (int)((position * 100) / duration);
            mediaProgressSlider.setValue(progress);
            
            // Update time labels
            long seconds = position / 1000000;
            long minutes = seconds / 60;
            seconds = seconds % 60;
            currentTimeLabel.setText(String.format("%02d:%02d", minutes, seconds));
            
            long totalSeconds = duration / 1000000;
            long totalMinutes = totalSeconds / 60;
            totalSeconds = totalSeconds % 60;
            totalTimeLabel.setText(String.format("%02d:%02d", totalMinutes, totalSeconds));
        }
    }
}

    private void loadCustomFonts() {
        try {
            // Try to load custom fonts if they exist
            mogaFont = Font.createFont(Font.TRUETYPE_FONT, 
                new java.io.File("font/Moga.ttf")).deriveFont(Font.PLAIN, 14);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(mogaFont);
        } catch (Exception e) {
            mogaFont = new Font("Arial", Font.PLAIN, 14);
        }
        
        try {
            ptSansFont = Font.createFont(Font.TRUETYPE_FONT, 
                new java.io.File("font/PTSans-Regular.ttf")).deriveFont(Font.PLAIN, 14);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(ptSansFont);
        } catch (Exception e) {
            ptSansFont = new Font("SansSerif", Font.PLAIN, 14);
        }
        
        try {
            ptSerifFont = Font.createFont(Font.TRUETYPE_FONT, 
                new java.io.File("font/PTSerif-Regular.ttf")).deriveFont(Font.PLAIN, 14);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(ptSerifFont);
        } catch (Exception e) {
            ptSerifFont = new Font("Serif", Font.PLAIN, 14);
        }
        
        try {
            wsParadoseFont = Font.createFont(Font.TRUETYPE_FONT, 
                new java.io.File("font/Ws Paradose.ttf")).deriveFont(Font.PLAIN, 14);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(wsParadoseFont);
        } catch (Exception e) {
            wsParadoseFont = new Font("Arial", Font.PLAIN, 14);
        }
        
        try {
            wsParadoseItalicFont = Font.createFont(Font.TRUETYPE_FONT, 
                new java.io.File("font/Ws Paradose Italic.ttf")).deriveFont(Font.ITALIC, 14);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(wsParadoseItalicFont);
        } catch (Exception e) {
            wsParadoseItalicFont = new Font("Arial", Font.ITALIC, 14);
        }
    }
    
    private void initializeComponents() {
        playlistModel = new DefaultListModel<>();
        
        // Initialize side panel buttons
        homeButton = createSideButton("Home", "🏠");
        musicListButton = createSideButton("Music", "📁");
        playlistButton = createSideButton("Playlists", "🎵");
        logMenuButton = createSideButton("Logs", "📋");
        mainExpandButton = createSideButton("", "❯");
        
        // Initialize control buttons
        playButton = createControlButton("▶", "Play/Pause");
        prevButton = createControlButton("⏮", "Previous");
        nextButton = createControlButton("⏭", "Next");
        shuffleButton = createControlButton("🔀", "Shuffle");
        loopButton = createControlButton("🔁", "Loop");
        muteButton = createControlButton("🔊", "Mute/Unmute");
        fullscreenButton = createControlButton("⛶", "Fullscreen");
        
        // Media labels
        mediaNameLabel = new JLabel("No media selected");
        mediaNameLabel.setFont(ptSerifFont.deriveFont(Font.BOLD, 16));
        mediaNameLabel.setForeground(TEXT_COLOR);
        
        folderOriginLabel = new JLabel("—");
        folderOriginLabel.setFont(ptSansFont.deriveFont(Font.PLAIN, 12));
        folderOriginLabel.setForeground(Color.GRAY);
        
        currentTimeLabel = new JLabel("0:00");
        totalTimeLabel = new JLabel("0:00");
        currentTimeLabel.setForeground(TEXT_COLOR);
        totalTimeLabel.setForeground(TEXT_COLOR);
        currentTimeLabel.setFont(ptSansFont.deriveFont(Font.PLAIN, 12));
        totalTimeLabel.setFont(ptSansFont.deriveFont(Font.PLAIN, 12));
        
        // Progress slider
        mediaProgressSlider = new JSlider(0, 100, 0);
        styleSlider(mediaProgressSlider);
        
        // Volume slider
        volumeSlider = new JSlider(0, 100, 80);
        styleSlider(volumeSlider);
        volumeSlider.setPreferredSize(new Dimension(100, 20));
        
        // Main content area
        sectionLabel = new JLabel("Recently Played");
        sectionLabel.setFont(wsParadoseFont.deriveFont(Font.PLAIN, 28));
        sectionLabel.setForeground(TEXT_COLOR);
        
        newMediaButton = createStyledButton("+ Add Media", PRIMARY_COLOR);
        newMediaButton.addActionListener(e -> OpenMediaAddingMenu());
        settingsButton = createStyledButton("Settings", PRIMARY_COLOR);
            
        // Initialize settings panel components (simplified for now)
        settingsBackButton = createSideButton("BACK", "←");
        manageDirectoriesButton = createSideButton("FOLDERS", "📁");
        appearanceButton = createSideButton("APPEARANCE", "🎨");
        infoButton = createSideButton("INFO", "ℹ");
        settingsExpandButton = createSideButton("", "❯");
    }
    
    private void setupLayout() {
        // Main container with BorderLayout
        setLayout(new BorderLayout());
        getContentPane().setBackground(DARK_BG);
        
        // Create side panel
        sidePanel = new JPanel();
        sidePanel.setLayout(new BoxLayout(sidePanel, BoxLayout.Y_AXIS));
        sidePanel.setBackground(DARKER_BG);
        sidePanel.setPreferredSize(new Dimension(100, getHeight()));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add logo/header
        try {
            // Load the icon image
            ImageIcon icon = new ImageIcon("icon.png");
            // Resize if needed (adjust 40, 40 to your desired dimensions)
            Image scaledImage = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            JLabel logoLabel = new JLabel(new ImageIcon(scaledImage));
            logoLabel.setToolTipText("Midnight Media Player");
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            sidePanel.add(logoLabel);
            sidePanel.add(Box.createRigidArea(new Dimension(0, 30)));
        } catch (Exception e) {
            // If icon fails to load, skip adding anything or add text fallback
            JLabel logoLabel = new JLabel("Midnight");
            logoLabel.setFont(mogaFont.deriveFont(Font.BOLD, 20));
            logoLabel.setForeground(PRIMARY_COLOR);
            logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
            sidePanel.add(logoLabel);
            sidePanel.add(Box.createRigidArea(new Dimension(0, 30)));
        }
        
        // Add side buttons
        sidePanel.add(homeButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(musicListButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(playlistButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(logMenuButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Add expand button at bottom
        sidePanel.add(Box.createVerticalGlue());
        sidePanel.add(mainExpandButton);
        
        // Create main content panel
        mainContentPanel = new JPanel(new BorderLayout(20, 20));
        mainContentPanel.setBackground(DARK_BG);
        mainContentPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Top bar with section label and buttons
        JPanel topBar = new JPanel(new BorderLayout());
        topBar.setBackground(DARK_BG);
        topBar.setOpaque(false);
        topBar.add(sectionLabel, BorderLayout.WEST);
        
        JPanel topButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        topButtons.setBackground(DARK_BG);
        topButtons.setOpaque(false);
        topButtons.add(newMediaButton);
        topButtons.add(settingsButton);
        topBar.add(topButtons, BorderLayout.EAST);
        
        // Center content with album art and playlist
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(DARK_BG);
        GridBagConstraints gbc = new GridBagConstraints();
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 0.4;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.insets = new Insets(0, 0, 0, 20);
        
        // Playlist panel
        selectedMenuPanel = new JPanel(new BorderLayout());
        selectedMenuPanel.setBackground(DARKER_BG);
        selectedMenuPanel.setBorder(BorderFactory.createLineBorder(new Color(40, 40, 40), 1));
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        gbc.insets = new Insets(0, 20, 0, 0);
        centerPanel.add(selectedMenuPanel, gbc);
        
        // Control panel at bottom
        controlPanel = new JPanel(new BorderLayout(10, 10));
        controlPanel.setBackground(DARKER_BG);
        controlPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(40, 40, 40), 1),
            BorderFactory.createEmptyBorder(15, 20, 15, 20)
        ));
        
        // Top section of control panel (track info)
        JPanel trackInfoPanel = new JPanel(new BorderLayout());
        trackInfoPanel.setBackground(DARKER_BG);
        trackInfoPanel.setOpaque(false);
        
        JPanel titlePanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        titlePanel.setBackground(DARKER_BG);
        titlePanel.setOpaque(false);
        titlePanel.add(mediaNameLabel);
        
        JPanel folderPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        folderPanel.setBackground(DARKER_BG);
        folderPanel.setOpaque(false);
        folderPanel.add(folderOriginLabel);
        
        trackInfoPanel.add(titlePanel, BorderLayout.NORTH);
        trackInfoPanel.add(folderPanel, BorderLayout.SOUTH);
        
        // Center section of control panel (progress bar)
        JPanel progressPanel = new JPanel(new BorderLayout(10, 0));
        progressPanel.setBackground(DARKER_BG);
        progressPanel.setOpaque(false);
        
        progressPanel.add(currentTimeLabel, BorderLayout.WEST);
        progressPanel.add(mediaProgressSlider, BorderLayout.CENTER);
        progressPanel.add(totalTimeLabel, BorderLayout.EAST);
        
        // Bottom section of control panel (control buttons)
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setBackground(DARKER_BG);
        buttonPanel.setOpaque(false);
        
        buttonPanel.add(shuffleButton);
        buttonPanel.add(prevButton);
        buttonPanel.add(playButton);
        buttonPanel.add(nextButton);
        buttonPanel.add(loopButton);
        
        JPanel volumePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        volumePanel.setBackground(DARKER_BG);
        volumePanel.setOpaque(false);
        volumePanel.add(muteButton);
        volumePanel.add(volumeSlider);
        volumePanel.add(fullscreenButton);
        
        // Assemble control panel
        controlPanel.add(trackInfoPanel, BorderLayout.NORTH);
        controlPanel.add(progressPanel, BorderLayout.CENTER);
        
        JPanel bottomControlPanel = new JPanel(new BorderLayout());
        bottomControlPanel.setBackground(DARKER_BG);
        bottomControlPanel.setOpaque(false);
        bottomControlPanel.add(buttonPanel, BorderLayout.WEST);
        bottomControlPanel.add(volumePanel, BorderLayout.EAST);
        
        controlPanel.add(bottomControlPanel, BorderLayout.SOUTH);
        
        // Assemble main content panel
        mainContentPanel.add(topBar, BorderLayout.NORTH);
        mainContentPanel.add(centerPanel, BorderLayout.CENTER);
        mainContentPanel.add(controlPanel, BorderLayout.SOUTH);
        
        // Add panels to frame
        add(sidePanel, BorderLayout.WEST);
        add(mainContentPanel, BorderLayout.CENTER);
    }

    private void OpenMediaAddingMenu() {
        JDialog dialog = MediaAddingMenu.OpenMediaAddingMenu(player);
        dialog.setLocationRelativeTo(player);
        dialog.setVisible(true); // BLOCKS until dialog is closed
    }

    private void OpenPlaylistCreator() {
        JDialog dialog = PlaylistAddingMenu.OpenPlaylistCreationMenu(player);
        dialog.setLocationRelativeTo(player);
        dialog.setVisible(true); // BLOCKS until dialog is closed
    }
    
    private void setupEventListeners() {
        // Side panel buttons
        homeButton.addActionListener(e -> switchToHomeView());
        musicListButton.addActionListener(e -> switchToMediaView());
        playlistButton.addActionListener(e -> switchToPlaylistView());
        settingsButton.addActionListener(e -> switchToSettingsView());
        logMenuButton.addActionListener(e -> switchToLogsView());
        
        // Control buttons (visual feedback only)
        playButton.addActionListener(e -> togglePlayPause());
        shuffleButton.addActionListener(e -> toggleShuffle());
        loopButton.addActionListener(e -> toggleLoop());
        muteButton.addActionListener(e -> toggleMute());
        fullscreenButton.addActionListener(e -> toggleFullscreen());
    }
    
    private void updateButtonStates() {
        // Update button text based on states
        playButton.setText(isPlaying ? "⏸" : "▶");
        shuffleButton.setForeground(isShuffled ? ACCENT_COLOR : TEXT_COLOR);
        loopButton.setForeground(isLooped ? ACCENT_COLOR : TEXT_COLOR);
        muteButton.setText(isMuted ? "🔇" : "🔊");
        muteButton.setForeground(isMuted ? PRIMARY_COLOR : TEXT_COLOR);
        
        // Update control button backgrounds when active
        if (isPlaying) {
            playButton.setBackground(ACCENT_COLOR);
        } else {
            playButton.setBackground(DARKER_BG);
        }
        
        if (isShuffled) {
            shuffleButton.setBackground(HIGHLIGHT_COLOR);
        } else {
            shuffleButton.setBackground(DARKER_BG);
        }
        
        if (isLooped) {
            loopButton.setBackground(HIGHLIGHT_COLOR);
        } else {
            loopButton.setBackground(DARKER_BG);
        }
    }
    
    // UI Helper Methods
    private JButton createSideButton(String text, String icon) {
        JButton button = new JButton("<html><div style='text-align: center;'>" + icon + "<br><small>" + text + "</small></div></html>");
        button.setFont(ptSansFont.deriveFont(Font.PLAIN, 11));
        button.setForeground(TEXT_COLOR);
        button.setBackground(DARKER_BG);
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        button.setHorizontalAlignment(SwingConstants.CENTER);
        button.setVerticalAlignment(SwingConstants.CENTER);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(text);
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(40, 40, 40));
                button.setForeground(PRIMARY_COLOR);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(DARKER_BG);
                button.setForeground(TEXT_COLOR);
            }
        });
        
        return button;
    }
    
    private JButton createControlButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI Emoji", Font.PLAIN, 20));
        button.setForeground(TEXT_COLOR);
        button.setBackground(DARKER_BG);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
            BorderFactory.createEmptyBorder(8, 12, 8, 12)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(50, 50, 50));
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(DARKER_BG);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
                    BorderFactory.createEmptyBorder(8, 12, 8, 12)
                ));
            }
        });
        
        return button;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(ptSansFont.deriveFont(Font.BOLD, 14));
        button.setForeground(TEXT_COLOR);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 2),
            BorderFactory.createEmptyBorder(8, 16, 8, 16)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(bgColor.brighter().darker(), 2),
                    BorderFactory.createEmptyBorder(8, 16, 8, 16)
                ));
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
                button.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(bgColor.darker(), 2),
                    BorderFactory.createEmptyBorder(8, 16, 8, 16)
                ));
            }
        });
        
        return button;
    }
    
    private void styleSlider(JSlider slider) {
        slider.setBackground(DARKER_BG);
        slider.setForeground(SLIDER_COLOR);
        slider.setBorder(BorderFactory.createEmptyBorder(5, 0, 5, 0));
        slider.setPaintTrack(true);
        slider.setPaintTicks(false);
        slider.setPaintLabels(false);
        
        // Custom UI for rounded slider
        slider.setUI(new javax.swing.plaf.basic.BasicSliderUI(slider) {
            @Override
            public void paintThumb(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2d.setColor(SLIDER_THUMB);
                g2d.fillOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
                
                // Add a subtle border to thumb
                g2d.setColor(SLIDER_THUMB.darker());
                g2d.drawOval(thumbRect.x, thumbRect.y, thumbRect.width, thumbRect.height);
            }
            
            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Track background
                g2d.setColor(SLIDER_COLOR);
                int cy = trackRect.y + trackRect.height / 2 - 2;
                g2d.fillRoundRect(trackRect.x, cy, trackRect.width, 4, 4, 4);
                
                // Progress
                int progressX = thumbRect.x + thumbRect.width / 2;
                g2d.setColor(PRIMARY_COLOR);
                g2d.fillRoundRect(trackRect.x, cy, progressX - trackRect.x, 4, 4, 4);
                
                // Track border
                g2d.setColor(SLIDER_COLOR.darker());
                g2d.drawRoundRect(trackRect.x, cy, trackRect.width, 4, 4, 4);
            }
            
            @Override
            protected void calculateTrackRect() {
                super.calculateTrackRect();
                trackRect.y += 2;
                trackRect.height = 8;
            }
            
            @Override
            protected void calculateThumbLocation() {
                super.calculateThumbLocation();
                thumbRect.y = trackRect.y + trackRect.height / 2 - thumbRect.height / 2;
            }
        });
    }
    
    private void RecyclePlaylistSelection() {
        playlistModel = new DefaultListModel<>();

        List<Playlist> allPlaylists;
        try {
            allPlaylists = Database.getAllPlaylists();

            for(Playlist song : allPlaylists)
            {
                playlistModel.addElement(song.name);
            }
        
        playlistList = new JList<>(playlistModel);
        playlistList.setFont(ptSansFont.deriveFont(Font.PLAIN, 14));
        playlistList.setForeground(TEXT_COLOR);
        playlistList.setBackground(DARKER_BG);
        playlistList.setSelectionBackground(new Color(60, 60, 60));
        playlistList.setSelectionForeground(PRIMARY_COLOR);
        playlistList.setFixedCellHeight(40);
        playlistList.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        // Playlist selection
        playlistList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int index = playlistList.getSelectedIndex();
                if (index >= 0) {
                    currentTrackIndex = index;
                    updateNowPlaying();
                }
            }
        });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // DB connections
    private void RecycleMediaSelection() {
        playlistModel = new DefaultListModel<>();
        try {
            allSongs = Database.getAllMedia();

            for(Media song : allSongs)
            {
                playlistModel.addElement(song.name);
            }
        
            // Playlist
            playlistList = new JList<>(playlistModel);
            playlistList.setFont(ptSansFont.deriveFont(Font.PLAIN, 14));
            playlistList.setForeground(TEXT_COLOR);
            playlistList.setBackground(DARKER_BG);
            playlistList.setSelectionBackground(new Color(60, 60, 60));
            playlistList.setSelectionForeground(PRIMARY_COLOR);
            playlistList.setFixedCellHeight(40);
            playlistList.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

            // Playlist selection
            playlistList.addListSelectionListener(e -> {
                if (!e.getValueIsAdjusting()) {
                    int index = playlistList.getSelectedIndex();
                    if (index >= 0) {
                        currentTrackIndex = allSongs.get(index).id;
                        updateNowPlaying();
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // View switching methods (visual only)
    private void switchToHomeView() {
        currentTab = tab.Home;
        sectionLabel.setText("Recently Played");
        homeButton.setForeground(PRIMARY_COLOR);
        musicListButton.setForeground(TEXT_COLOR);
        playlistButton.setForeground(TEXT_COLOR);
        logMenuButton.setForeground(TEXT_COLOR);

        UpdatePerMenuUI();


        //Panel goes here


        selectedMenuPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
            "Welcome",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            ptSerifFont.deriveFont(Font.BOLD, 16),
            TEXT_COLOR
        ));
    }

    private void switchToMediaView() {
        currentTab = tab.Media;
        sectionLabel.setText("All Media");
        homeButton.setForeground(TEXT_COLOR);
        musicListButton.setForeground(PRIMARY_COLOR);
        playlistButton.setForeground(TEXT_COLOR);
        logMenuButton.setForeground(TEXT_COLOR);

        UpdatePerMenuUI();
        RecycleMediaSelection();
        
        playlistScrollPane = new JScrollPane(playlistList);
        playlistScrollPane.setBorder(null);
        playlistScrollPane.getViewport().setBackground(DARKER_BG);
        playlistScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        // Style the scrollbar
        JScrollBar verticalScrollBar = playlistScrollPane.getVerticalScrollBar();
        verticalScrollBar.setBackground(DARKER_BG);
        verticalScrollBar.setForeground(new Color(60, 60, 60));
        
        selectedMenuPanel.add(playlistScrollPane, BorderLayout.CENTER);

        selectedMenuPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
            "Media Library",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            ptSerifFont.deriveFont(Font.BOLD, 16),
            TEXT_COLOR
        ));
    }

    private void switchToPlaylistView() {
        currentTab = tab.Playlist;
        sectionLabel.setText("Playlists");
        homeButton.setForeground(TEXT_COLOR);
        musicListButton.setForeground(TEXT_COLOR);
        playlistButton.setForeground(PRIMARY_COLOR);
        logMenuButton.setForeground(TEXT_COLOR);

        UpdatePerMenuUI();
        RecyclePlaylistSelection();

        playlistScrollPane = new JScrollPane(playlistList);
        playlistScrollPane.setBorder(null);
        playlistScrollPane.getViewport().setBackground(DARKER_BG);
        playlistScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        
        // Style the scrollbar
        JScrollBar verticalScrollBar = playlistScrollPane.getVerticalScrollBar();
        verticalScrollBar.setBackground(DARKER_BG);
        verticalScrollBar.setForeground(new Color(60, 60, 60));
        
        selectedMenuPanel.add(playlistScrollPane, BorderLayout.CENTER);


        selectedMenuPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
            "Your Playlists",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            ptSerifFont.deriveFont(Font.BOLD, 16),
            TEXT_COLOR
        ));
    }
    
    private void switchToLogsView() {
        currentTab = tab.Log;
        sectionLabel.setText("Logs");
        homeButton.setForeground(TEXT_COLOR);
        musicListButton.setForeground(TEXT_COLOR);
        playlistButton.setForeground(TEXT_COLOR);
        logMenuButton.setForeground(PRIMARY_COLOR);

        UpdatePerMenuUI();


        //Panel goes here


        selectedMenuPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
            "Activity Log",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            ptSerifFont.deriveFont(Font.BOLD, 16),
            TEXT_COLOR
        ));
    }

    private void switchToSettingsView() {
        sectionLabel.setText("Settings");
        homeButton.setForeground(TEXT_COLOR);
        musicListButton.setForeground(TEXT_COLOR);
        playlistButton.setForeground(TEXT_COLOR);
        logMenuButton.setForeground(TEXT_COLOR);

        UpdatePerMenuUI();


        //Panel goes here


        selectedMenuPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(new Color(60, 60, 60), 1),
            "Configuration",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            ptSerifFont.deriveFont(Font.BOLD, 16),
            TEXT_COLOR
        ));
    }

    private void UpdatePerMenuUI()
    {
        ClearOldPanel();
        for(ActionListener command : newMediaButton.getActionListeners())
        {
            newMediaButton.removeActionListener(command);
        }
        newMediaButton.setVisible(true);

        if(currentTab == tab.Media)
        {
            newMediaButton.addActionListener(e -> OpenMediaAddingMenu());
            newMediaButton.setText("+ Add Media");
        }
        else if(currentTab == tab.Playlist)
        {
            newMediaButton.addActionListener(e -> OpenPlaylistCreator());
            newMediaButton.setText("+ New Playlist");
        }
        else
            newMediaButton.setVisible(false);
    }

    private void ClearOldPanel() {
        selectedMenuPanel.removeAll();
        selectedMenuPanel.revalidate();
        selectedMenuPanel.repaint();
    }

    // State toggle methods (visual only)
    private void togglePlayPause() {
    if (!isPlaying) {
        try {
            // Try to play any WAV file in the directory
            // File[] wavFiles = new File(".").listFiles((dir, name) -> 
            //     name.toLowerCase().endsWith(".wav"));
            
            if (currentTrackIndex >= 0) {
                String path = Database.findMediaById(currentTrackIndex).path;
                File audioFile = new File(path);

                AudioInputStream originalStream = AudioSystem.getAudioInputStream(audioFile);
                System.out.println(path);

                AudioFormat baseFormat = originalStream.getFormat();

                AudioFormat decodedFormat = new AudioFormat(
                        AudioFormat.Encoding.PCM_SIGNED,
                        baseFormat.getSampleRate(),
                        16,
                        baseFormat.getChannels(),
                        baseFormat.getChannels() * 2,
                        baseFormat.getSampleRate(),
                        false
                );

                AudioInputStream decodedStream =
                AudioSystem.getAudioInputStream(decodedFormat, originalStream);

                currentClip = AudioSystem.getClip();
                currentClip.open(decodedStream);
                currentClip.start();

                currentClip.start();
                playbackTimer.start();
                isPlaying = true;
                playButton.setText("⏸");
                mediaNameLabel.setText("Playing: " + audioFile.getName());
            } else {
                playTestTone();
            }
        } catch (Exception e) {
            System.out.println(e);
            playTestTone();
        }
        } else {
            if (currentClip != null) {
                currentClip.stop();
                playbackTimer.stop();
                isPlaying = false;
                playButton.setText("▶");
            }
        }
    }

private void playTestTone() {
    try {
        // Generate a simple beep
        AudioFormat format = new AudioFormat(44100, 16, 1, true, false);
        byte[] buffer = new byte[44100]; // 1 second
        for (int i = 0; i < buffer.length; i++) {
            buffer[i] = (byte)(Math.sin(i * 0.1) * 127);
        }
        
        DataLine.Info info = new DataLine.Info(Clip.class, format);
        currentClip = (Clip) AudioSystem.getLine(info);
        currentClip.open(format, buffer, 0, buffer.length);
        currentClip.start();
        playbackTimer.start();
        isPlaying = true;
        playButton.setText("⏸");
        mediaNameLabel.setText("Playing: Test Tone");
    } catch (Exception e) {
        mediaNameLabel.setText("Audio error: " + e.getMessage());
    }
}
    
    
        int chance = 5;
        int roll = (int)(Math.random() * 100);

        if (roll < chance) {
            triggerJumpscare();
        }
    }

@Override
public void dispose() {
    if (currentClip != null) {
        currentClip.stop();
        currentClip.close();
    }
    super.dispose();
}

    private void triggerJumpscare() {
        JWindow jumpscareWindow = new JWindow();
        jumpscareWindow.setAlwaysOnTop(true);
        jumpscareWindow.setBackground(Color.BLACK);
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        jumpscareWindow.setSize(screen);
        jumpscareWindow.setLocation(0, 0);

        JLabel imageLabel = new JLabel();
        imageLabel.setHorizontalAlignment(SwingConstants.CENTER);
        ImageIcon icon = new ImageIcon("casper_jumpscare.png");
        imageLabel.setIcon(new ImageIcon(
            icon.getImage().getScaledInstance(
                screen.width, screen.height, Image.SCALE_SMOOTH
            )
        ));

        jumpscareWindow.add(imageLabel);
        jumpscareWindow.setVisible(true);

        playScreamSound(); 

        
        new Timer(300, e -> jumpscareWindow.dispose()).start();

    }

    private void playScreamSound() {
        try {
            AudioInputStream audio =
                AudioSystem.getAudioInputStream(new File("jumpscare_sound.wav"));

            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    private void toggleShuffle() {
        isShuffled = !isShuffled;
        updateButtonStates();
    }
    
    private void toggleLoop() {
        isLooped = !isLooped;
        updateButtonStates();
    }
    
    private void toggleMute() {
        isMuted = !isMuted;
        updateButtonStates();
    }
    
    private void toggleFullscreen() {
        isFullscreen = !isFullscreen;
        if (isFullscreen) {
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            // Remove window decorations for true fullscreen
            dispose();
            setUndecorated(true);
            setVisible(true);
        } else {
            dispose();
            setUndecorated(false);
            setVisible(true);
            setExtendedState(JFrame.NORMAL);
        }
    }
    
    private void updateNowPlaying() {
        if (currentTrackIndex >= 0 && currentTrackIndex < playlistModel.size()) {
            mediaNameLabel.setText("Now Playing: " + playlistModel.get(currentTrackIndex));
            folderOriginLabel.setText("From: Midnight Playlist");
            
            // Simulate progress update
            new Timer(1000, e -> {
                if (isPlaying) {
                    int current = mediaProgressSlider.getValue();
                    if (current < 100) {
                        mediaProgressSlider.setValue(current + 1);
                        updateTimeLabels(current + 1);
                    }
                }
            }).start();
        }
    }
    
    private void updateTimeLabels(int progress) {
        int totalSeconds = 180; // 3 minutes
        int currentSeconds = (progress * totalSeconds) / 100;
        
        currentTimeLabel.setText(String.format("%d:%02d", currentSeconds / 60, currentSeconds % 60));
        totalTimeLabel.setText(String.format("%d:%02d", totalSeconds / 60, totalSeconds % 60));
    }
    

    
    // Main method to run the application
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Use system look and feel
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                
                // Set some default UI properties for better dark theme
                UIManager.put("ScrollBar.thumb", new Color(60, 60, 60));
                UIManager.put("ScrollBar.track", new Color(18, 18, 18));
                UIManager.put("ScrollBar.width", 12);
            } catch (Exception e) {
                e.printStackTrace();
            }
            player = new MidnightMediaPlayer();
        });
    }
}