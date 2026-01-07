// Package imports
import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.awt.event.*;

public class MidnightMediaPlayer extends JFrame {
    
    //---------------- GUI components ---------------
    // Main screen
    // Side Panel
    private JPanel sidePanel;
    private JButton homeButton; 
    private JButton musicListButton; 
    private JButton playlistButton; 
    private JButton videoListButton; 
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

    // Main content area
    private JPanel mainContentPanel;
    private JLabel sectionLabel;
    private JButton home_addFolderButton;
    private JButton settingsButton;
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
    
    //---------------- Appearance ----------------------------
    private final Color PRIMARY_COLOR = new Color(242, 75, 75);     // Midnight red
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);  // Lighter blue
    private final Color ACCENT_COLOR = new Color(46, 204, 113);     // Green accent
    private final Color DARK_BG = new Color(133, 131, 131);            // Dark background
    private final Color DARKER_BG = new Color(125, 123, 123);          // Even darker
    private final Color LIGHT_BG = new Color(60, 60, 60);           // Light background for panels
    private final Color TEXT_COLOR = new Color(242, 75, 75);      // Text color
    private final Color HIGHLIGHT_COLOR = new Color(155, 89, 182);  // Purple highlight
    private final Color SLIDER_COLOR = new Color(80, 80, 80);
    private final Color SLIDER_THUMB = new Color(242, 75, 75);
    
    // Fonts
    private Font titleFont = new Font("Moga", Font.BOLD, 24);
    private Font subtitleFont = new Font("Moga", Font.PLAIN, 16);
    private Font normalFont = new Font("Moga", Font.PLAIN, 14);
    private Font smallFont = new Font("Moga", Font.PLAIN, 12);
    
    // Constructor
    public MidnightMediaPlayer() {
        setTitle("Midnight Media Player");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1200, 800));
        
        // Set application icon (placeholder)
        setIconImage(new ImageIcon("icon.png").getImage());
        
        initializeComponents();
        setupLayout();
        setupEventListeners();
        updateButtonStates();
        
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
    
    private void initializeComponents() {
        // Initialize side panel buttons
        homeButton = createSideButton("HOME", "🏠");
        musicListButton = createSideButton("MUSIC", "🎵");
        playlistButton = createSideButton("PLAYLISTS", "📋");
        videoListButton = createSideButton("VIDEOS", "🎬");
        mainExpandButton = createSideButton("", "❯");
        
        // Initialize control buttons
        playButton = createControlButton("▶", "Play/Pause", PRIMARY_COLOR);
        prevButton = createControlButton("⏮", "Previous", TEXT_COLOR);
        nextButton = createControlButton("⏭", "Next", TEXT_COLOR);
        shuffleButton = createControlButton("🔀", "Shuffle", TEXT_COLOR);
        loopButton = createControlButton("🔁", "Loop", TEXT_COLOR);
        muteButton = createControlButton("🔊", "Mute/Unmute", TEXT_COLOR);
        fullscreenButton = createControlButton("⛶", "Fullscreen", TEXT_COLOR);
        
        // Media labels
        mediaNameLabel = new JLabel("No media selected");
        mediaNameLabel.setFont(subtitleFont);
        mediaNameLabel.setForeground(TEXT_COLOR);
        
        folderOriginLabel = new JLabel("—");
        folderOriginLabel.setFont(smallFont);
        folderOriginLabel.setForeground(SECONDARY_COLOR);
        
        currentTimeLabel = new JLabel("0:00");
        totalTimeLabel = new JLabel("0:00");
        currentTimeLabel.setForeground(TEXT_COLOR);
        totalTimeLabel.setForeground(TEXT_COLOR);
        currentTimeLabel.setFont(smallFont);
        totalTimeLabel.setFont(smallFont);
        
        // Progress slider
        mediaProgressSlider = new JSlider(0, 100, 0);
        styleSlider(mediaProgressSlider);
        
        // Volume slider
        volumeSlider = new JSlider(0, 100, 80);
        styleSlider(volumeSlider);
        volumeSlider.setPreferredSize(new Dimension(100, 20));
        
        // Main content area
        sectionLabel = new JLabel("RECENTLY PLAYED");
        sectionLabel.setFont(titleFont);
        sectionLabel.setForeground(TEXT_COLOR);
        
        home_addFolderButton = createStyledButton("+ ADD FOLDER", TEXT_COLOR);
        settingsButton = createStyledButton("SETTINGS", DARKER_BG);
        
        // Playlist
        playlistModel = new DefaultListModel<>();
        
        playlistList = new JList<>(playlistModel);
        playlistList.setFont(normalFont);
        playlistList.setForeground(TEXT_COLOR);
        playlistList.setBackground(LIGHT_BG);
        playlistList.setSelectionBackground(HIGHLIGHT_COLOR);
        playlistList.setSelectionForeground(Color.WHITE);
        playlistList.setFixedCellHeight(40);
            
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
        sidePanel.setPreferredSize(new Dimension(250, getHeight()));
        sidePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // Add logo/header
        JLabel logoLabel = new JLabel("MIDNIGHT PLAYER");
        logoLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        logoLabel.setForeground(PRIMARY_COLOR);
        logoLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        sidePanel.add(logoLabel);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 30)));
        
        // Add side buttons
        sidePanel.add(homeButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(musicListButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(videoListButton);
        sidePanel.add(Box.createRigidArea(new Dimension(0, 10)));
        sidePanel.add(playlistButton);
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
        topButtons.add(home_addFolderButton);
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
        JPanel playlistPanel = new JPanel(new BorderLayout());
        playlistPanel.setBackground(DARKER_BG);
        playlistPanel.setBorder(BorderFactory.createTitledBorder(
            BorderFactory.createLineBorder(LIGHT_BG, 1),
            "PLAYLIST",
            TitledBorder.LEFT,
            TitledBorder.TOP,
            subtitleFont,
            TEXT_COLOR
        ));
        
        playlistScrollPane = new JScrollPane(playlistList);
        playlistScrollPane.setBorder(null);
        playlistScrollPane.getViewport().setBackground(DARKER_BG);
        playlistPanel.add(playlistScrollPane, BorderLayout.CENTER);
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 0.6;
        gbc.insets = new Insets(0, 20, 0, 0);
        centerPanel.add(playlistPanel, gbc);
        
        // Control panel at bottom
        controlPanel = new JPanel(new BorderLayout(10, 10));
        controlPanel.setBackground(DARKER_BG);
        controlPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        
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
    
    private void setupEventListeners() {
        // Side panel buttons
        homeButton.addActionListener(e -> switchToHomeView());
        settingsButton.addActionListener(e -> switchToSettingsView());
        
        // Control buttons (visual feedback only)
        playButton.addActionListener(e -> togglePlayPause());
        shuffleButton.addActionListener(e -> toggleShuffle());
        loopButton.addActionListener(e -> toggleLoop());
        muteButton.addActionListener(e -> toggleMute());
        fullscreenButton.addActionListener(e -> toggleFullscreen());
        
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
    }
    
    private void updateButtonStates() {
        // Update button text based on states
        playButton.setText(isPlaying ? "⏸" : "▶");
        shuffleButton.setForeground(isShuffled ? ACCENT_COLOR : DARKER_BG);
        loopButton.setForeground(isLooped ? ACCENT_COLOR : DARKER_BG);
        muteButton.setText(isMuted ? "🔇" : "🔊");
        muteButton.setForeground(isMuted ? PRIMARY_COLOR : DARKER_BG);
    }
    
    // UI Helper Methods
    private JButton createSideButton(String text, String icon) {
        JButton button = new JButton(icon + "  " + text);
        button.setFont(normalFont);
        button.setForeground(TEXT_COLOR);
        button.setBackground(DARKER_BG);
        button.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(LIGHT_BG);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(DARKER_BG);
            }
        });
        
        return button;
    }
    
    private JButton createControlButton(String text, String tooltip, Color color) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 24));
        button.setForeground(color);
        button.setBackground(DARKER_BG);
        button.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setToolTipText(tooltip);
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(LIGHT_BG);
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(DARKER_BG);
            }
        });
        
        return button;
    }
    
    private JButton createStyledButton(String text, Color bgColor) {
        JButton button = new JButton(text);
        button.setFont(normalFont);
        button.setForeground(Color.WHITE);
        button.setBackground(bgColor);
        button.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(bgColor.darker(), 1),
            BorderFactory.createEmptyBorder(10, 20, 10, 20)
        ));
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // Hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(bgColor.brighter());
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(bgColor);
            }
        });
        
        return button;
    }
    
    private void styleSlider(JSlider slider) {
        slider.setBackground(DARKER_BG);
        slider.setForeground(SLIDER_COLOR);
        slider.setBorder(null);
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
            }
            
            @Override
            public void paintTrack(Graphics g) {
                Graphics2D g2d = (Graphics2D) g;
                g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Track background
                g2d.setColor(SLIDER_COLOR);
                int cy = trackRect.y + trackRect.height / 2 - 2;
                g2d.fillRoundRect(trackRect.x, cy, trackRect.width, 4, 2, 2);
                
                // Progress
                int progressX = thumbRect.x + thumbRect.width / 2;
                g2d.setColor(PRIMARY_COLOR);
                g2d.fillRoundRect(trackRect.x, cy, progressX - trackRect.x, 4, 2, 2);
            }
        });
    }
    
    
    // View switching methods (visual only)
    private void switchToHomeView() {
        sectionLabel.setText("RECENTLY PLAYED");
        homeButton.setForeground(PRIMARY_COLOR);
        musicListButton.setForeground(DARKER_BG);
        playlistButton.setForeground(DARKER_BG);
        videoListButton.setForeground(DARKER_BG);
    }
    
    private void switchToSettingsView() {
        sectionLabel.setText("SETTINGS");
        homeButton.setForeground(DARKER_BG);
        musicListButton.setForeground(DARKER_BG);
        playlistButton.setForeground(DARKER_BG);
        videoListButton.setForeground(DARKER_BG);
    }
    
    // State toggle methods (visual only)
    private void togglePlayPause() {
        isPlaying = !isPlaying;
        updateButtonStates();
        
        if (isPlaying) {
            mediaNameLabel.setText("Now Playing: " + playlistModel.get(currentTrackIndex >= 0 ? currentTrackIndex : 0));
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
        } else {
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
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();
            }
            new MidnightMediaPlayer();
        });
    }
}