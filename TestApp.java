// Import necessary packages
import javax.swing.*;  // For Swing GUI components
import javax.swing.border.*;  // For border styling
import javax.swing.filechooser.FileNameExtensionFilter;  // For file type filtering
import java.awt.*;  // For AWT components and graphics
import java.awt.event.*;  // For event handling
import java.awt.geom.RoundRectangle2D;  // For creating rounded window corners
import java.io.File;  // For file handling
import java.util.ArrayList;  // For dynamic array list
import java.util.List;  // For List interface

// Main class extending JFrame (main application window)
public class TestApp extends JFrame {
    // ========== GUI COMPONENT DECLARATIONS ==========
    
    // Media control buttons
    private JButton playButton;      // Play media
    private JButton pauseButton;     // Pause media
    private JButton stopButton;      // Stop media
    private JButton nextButton;      // Play next track
    private JButton prevButton;      // Play previous track
    private JButton openButton;      // Open media files
    private JButton muteButton;      // Mute/unmute volume
    private JButton repeatButton;    // Toggle repeat mode
    private JButton shuffleButton;   // Toggle shuffle mode
    private JButton playlistButton;  // Show/hide playlist
    
    // Sliders for user input
    private JSlider volumeSlider;    // Control volume level (0-100)
    private JSlider progressSlider;  // Show/control playback progress
    
    // Labels for displaying information
    private JLabel currentTimeLabel; // Display current playback time
    private JLabel totalTimeLabel;   // Display total track time
    private JLabel titleLabel;       // Display track title
    private JLabel albumArtLabel;    // Display album art placeholder
    
    // Playlist components
    private JList<String> playlistList;      // List to display playlist tracks
    private DefaultListModel<String> playlistModel;  // Data model for playlist
    
    // Panels for organizing layout
    private JPanel controlPanel;     // Holds control buttons
    private JPanel displayPanel;     // Holds album art and title
    private JPanel playlistPanel;    // Holds playlist
    private JPanel mainPanel;        // Main container panel
    
    // ========== APPLICATION STATE VARIABLES ==========
    
    private boolean isPlaying = false;    // Tracks if media is currently playing
    private boolean isMuted = false;      // Tracks if audio is muted
    private boolean isRepeating = false;  // Tracks if repeat mode is enabled
    private boolean isShuffling = false;  // Tracks if shuffle mode is enabled
    private boolean playlistVisible = true; // Tracks playlist visibility
    private List<File> playlist = new ArrayList<>(); // List of media files
    private int currentTrackIndex = -1;   // Index of currently playing track (-1 = none)
    
    // ========== COLOR SCHEME ==========
    // Custom color palette for modern dark theme
    private final Color PRIMARY_COLOR = new Color(41, 128, 185);    // Main blue color
    private final Color SECONDARY_COLOR = new Color(52, 152, 219);  // Lighter blue
    private final Color ACCENT_COLOR = new Color(46, 204, 113);     // Green accent
    private final Color DARK_BG = new Color(30, 30, 35);            // Dark background
    private final Color LIGHT_BG = new Color(45, 45, 50);           // Light background
    private final Color TEXT_COLOR = new Color(220, 220, 220);      // Text color
    private final Color HIGHLIGHT_COLOR = new Color(155, 89, 182);  // Purple highlight
    
    // ========== CONSTRUCTOR ==========
    public TestApp() {
        // Set window title
        setTitle("Harmony Player");
        
        // Set default close operation (exit application when window closes)
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        // Set initial window size (width, height)
        setSize(1000, 700);
        
        // Center window on screen
        setLocationRelativeTo(null);
        
        // Remove default window decorations (title bar, borders)
        setUndecorated(true);
        
        // Create rounded corners for the window
        setShape(new RoundRectangle2D.Double(0, 0, 1000, 700, 30, 30));
        
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
    
    // ========== COMPONENT INITIALIZATION ==========
    private void initializeComponents() {
        // Create buttons with custom styling
        // Parameters: text, tooltip, background color
        playButton = createModernButton("▶", "Play", PRIMARY_COLOR);
        pauseButton = createModernButton("⏸", "Pause", PRIMARY_COLOR);
        stopButton = createModernButton("⏹", "Stop", new Color(231, 76, 60)); // Red
        nextButton = createModernButton("⏭", "Next", LIGHT_BG);
        prevButton = createModernButton("⏮", "Previous", LIGHT_BG);
        openButton = createModernButton("📂", "Open File", SECONDARY_COLOR);
        muteButton = createModernButton("🔊", "Mute", LIGHT_BG);
        repeatButton = createModernButton("🔁", "Repeat", LIGHT_BG);
        shuffleButton = createModernButton("🔀", "Shuffle", LIGHT_BG);
        playlistButton = createModernButton("≡", "Playlist", LIGHT_BG);
        
        // ========== CUSTOM VOLUME SLIDER ==========
        // Create a volume slider with custom painting
        volumeSlider = new JSlider(0, 100, 70) {  // Range: 0-100, initial: 70
            // Override paintComponent to customize appearance
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
        // Set preferred size and make background transparent
        volumeSlider.setPreferredSize(new Dimension(120, 30));
        volumeSlider.setOpaque(false);
        
        // ========== CUSTOM PROGRESS SLIDER ==========
        progressSlider = new JSlider(0, 100, 0) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                   RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                int trackHeight = 6;
                int trackY = height / 2 - trackHeight / 2;
                
                // Draw background track
                g2.setColor(new Color(50, 50, 55));
                g2.fillRoundRect(0, trackY, width, trackHeight, 
                                trackHeight, trackHeight);
                
                // Draw progress track with blue gradient
                int progressWidth = (int) (width * ((double) getValue() / getMaximum()));
                GradientPaint gradient = new GradientPaint(0, trackY, PRIMARY_COLOR, 
                                                          width, trackY, SECONDARY_COLOR);
                g2.setPaint(gradient);
                g2.fillRoundRect(0, trackY, progressWidth, trackHeight, 
                                trackHeight, trackHeight);
                
                // Draw thumb if there's progress
                if (progressWidth > 0) {
                    int thumbSize = 16;
                    int thumbX = progressWidth - thumbSize/2;
                    g2.setColor(Color.WHITE);
                    g2.fillOval(thumbX, height/2 - thumbSize/2, thumbSize, thumbSize);
                    
                    // Add inner circle for glow effect
                    g2.setColor(new Color(255, 255, 255, 100));  // Semi-transparent white
                    g2.fillOval(thumbX + 2, height/2 - thumbSize/2 + 2, 
                               thumbSize - 4, thumbSize - 4);
                }
                
                g2.dispose();
            }
        };
        progressSlider.setPreferredSize(new Dimension(700, 40));
        progressSlider.setOpaque(false);
        
        // ========== TIME LABELS ==========
        // Label for current playback time
        currentTimeLabel = new JLabel("00:00");
        currentTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        currentTimeLabel.setForeground(TEXT_COLOR);
        
        // Label for total track time
        totalTimeLabel = new JLabel("00:00");
        totalTimeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        totalTimeLabel.setForeground(TEXT_COLOR);
        
        // ========== TITLE LABEL WITH GRADIENT TEXT ==========
        titleLabel = new JLabel("Harmony Player - No Track Loaded") {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                   RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create gradient from white to light blue for text
                GradientPaint gradient = new GradientPaint(
                    0, 0, Color.WHITE,           // Start color
                    getWidth(), 0, new Color(200, 200, 255)  // End color
                );
                g2.setPaint(gradient);
                g2.setFont(getFont());
                
                // Center text in label
                FontMetrics fm = g2.getFontMetrics();
                String text = getText();
                int x = (getWidth() - fm.stringWidth(text)) / 2;  // Center horizontally
                int y = ((getHeight() - fm.getHeight()) / 2) + fm.getAscent();  // Center vertically
                
                g2.drawString(text, x, y);
                g2.dispose();
            }
        };
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        
        // ========== ALBUM ART LABEL ==========
        albumArtLabel = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                   RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                
                // Draw gradient background
                GradientPaint gradient = new GradientPaint(
                    0, 0, DARK_BG,
                    width, height, new Color(40, 40, 45)
                );
                g2.setPaint(gradient);
                g2.fillRoundRect(0, 0, width, height, 20, 20);  // Rounded corners
                
                // Draw music note icon if no track is loaded
                if (currentTrackIndex == -1) {
                    g2.setColor(new Color(100, 100, 110));
                    g2.setFont(new Font("Segoe UI Symbol", Font.PLAIN, 80));
                    FontMetrics fm = g2.getFontMetrics();
                    String note = "♪";
                    int x = (width - fm.stringWidth(note)) / 2;
                    int y = (height - fm.getHeight()) / 2 + fm.getAscent();
                    g2.drawString(note, x, y);
                }
                
                // Draw border around album art
                g2.setColor(new Color(70, 70, 75));
                g2.setStroke(new BasicStroke(2));  // 2-pixel border
                g2.drawRoundRect(0, 0, width - 1, height - 1, 20, 20);
                
                g2.dispose();
            }
        };
        albumArtLabel.setPreferredSize(new Dimension(300, 300));
        
        // ========== PLAYLIST COMPONENTS ==========
        // Create model to hold playlist data
        playlistModel = new DefaultListModel<>();
        
        // Create JList to display playlist
        playlistList = new JList<>(playlistModel);
        playlistList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  // Only select one item
        playlistList.setBackground(new Color(40, 40, 45));  // Dark background
        playlistList.setForeground(TEXT_COLOR);  // Light text
        playlistList.setFont(new Font("Segoe UI", Font.PLAIN, 12));  // Custom font
        playlistList.setBorder(new EmptyBorder(10, 10, 10, 10));  // Padding inside list
        
        // Custom cell renderer for playlist items
        playlistList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, 
                    int index, boolean isSelected, boolean cellHasFocus) {
                // Get default renderer component
                JLabel label = (JLabel) super.getListCellRendererComponent(
                    list, value, index, isSelected, cellHasFocus);
                
                // Add padding to each cell
                label.setBorder(new EmptyBorder(8, 10, 8, 10));
                
                // Set font (bold if selected)
                label.setFont(new Font("Segoe UI", isSelected ? Font.BOLD : Font.PLAIN, 13));
                
                // Custom styling based on selection state
                if (isSelected) {
                    // Highlight selected item
                    label.setBackground(new Color(60, 60, 65));
                    label.setForeground(Color.WHITE);
                    label.setBorder(new CompoundBorder(
                        new LineBorder(HIGHLIGHT_COLOR, 1),  // Purple border
                        new EmptyBorder(7, 9, 7, 9)  // Reduced padding to account for border
                    ));
                } else if (index == currentTrackIndex) {
                    // Different color for currently playing track
                    label.setBackground(new Color(50, 50, 55));
                    label.setForeground(ACCENT_COLOR);  // Green text
                } else {
                    // Normal state
                    label.setBackground(new Color(40, 40, 45));
                    label.setForeground(TEXT_COLOR);
                }
                
                // Format text with track number
                String text = "<html><div style='margin-left: 5px;'>" + 
                    "<span style='color: #888; margin-right: 15px;'>" + 
                    String.format("%02d", index + 1) + "</span>" + value + "</div></html>";
                label.setText(text);
                
                return label;
            }
        });
        
        // ========== PANEL INITIALIZATION ==========
        // Create panels with gradient backgrounds
        controlPanel = createGradientPanel(DARK_BG, new Color(35, 35, 40));
        displayPanel = createGradientPanel(new Color(25, 25, 30), DARK_BG);
        playlistPanel = createGradientPanel(new Color(35, 35, 40), new Color(40, 40, 45));
        mainPanel = createGradientPanel(DARK_BG, new Color(30, 30, 35));
    }
    
    // ========== HELPER METHOD: CREATE GRADIENT PANEL ==========
    // Creates a JPanel with a gradient background
    private JPanel createGradientPanel(Color color1, Color color2) {
        return new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                   RenderingHints.VALUE_ANTIALIAS_ON);
                
                // Create diagonal gradient
                GradientPaint gradient = new GradientPaint(
                    0, 0, color1,           // Start color at top-left
                    getWidth(), getHeight(), color2  // End color at bottom-right
                );
                g2.setPaint(gradient);
                g2.fillRect(0, 0, getWidth(), getHeight());  // Fill entire panel
            }
        };
    }
    
    // ========== HELPER METHOD: CREATE MODERN BUTTON ==========
    // Creates a custom styled button with rounded corners and hover effects
    private JButton createModernButton(String text, String tooltip, Color baseColor) {
        // Create button with custom painting
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
                                   RenderingHints.VALUE_ANTIALIAS_ON);
                
                int width = getWidth();
                int height = getHeight();
                
                // Determine button color based on state
                Color buttonColor;
                if (getModel().isPressed()) {
                    buttonColor = baseColor.darker();  // Darker when pressed
                } else if (getModel().isRollover()) {
                    buttonColor = baseColor.brighter();  // Brighter on hover
                } else {
                    buttonColor = baseColor;  // Normal state
                }
                
                // Draw rounded rectangle background
                g2.setColor(buttonColor);
                g2.fillRoundRect(0, 0, width - 1, height - 1, 15, 15);
                
                // Draw button text (centered)
                g2.setColor(Color.WHITE);
                g2.setFont(getFont());
                FontMetrics fm = g2.getFontMetrics();
                int x = (width - fm.stringWidth(getText())) / 2;  // Center horizontally
                int y = (height - fm.getHeight()) / 2 + fm.getAscent();  // Center vertically
                g2.drawString(getText(), x, y);
                
                // Draw border
                g2.setColor(baseColor.brighter());  // Slightly brighter border
                g2.setStroke(new BasicStroke(1.5f));  // 1.5 pixel border
                g2.drawRoundRect(1, 1, width - 3, height - 3, 15, 15);
                
                g2.dispose();
            }
        };
        
        // Set button properties
        button.setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));  // Icon font
        button.setPreferredSize(new Dimension(45, 40));  // Fixed size
        button.setToolTipText(tooltip);  // Tooltip text
        button.setContentAreaFilled(false);  // Don't use default background
        button.setBorderPainted(false);  // Don't paint default border
        button.setFocusPainted(false);  // Don't show focus rectangle
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));  // Hand cursor on hover
        
        return button;
    }
    
    // ========== CUSTOM WINDOW CONTROLS ==========
    // Creates custom title bar with window control buttons
    private void setupWindowControls() {
        // Create title bar panel
        JPanel titleBar = new JPanel(new BorderLayout());
        titleBar.setBackground(DARK_BG);
        titleBar.setPreferredSize(new Dimension(getWidth(), 40));  // Fixed height
        titleBar.setBorder(new MatteBorder(0, 0, 1, 0, new Color(60, 60, 65)));  // Bottom border
        
        // Window title label
        JLabel windowTitle = new JLabel("  Harmony Player");
        windowTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        windowTitle.setForeground(TEXT_COLOR);
        
        // Panel for window control buttons (minimize, maximize, close)
        JPanel controlButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        controlButtons.setOpaque(false);  // Transparent background
        
        // Create control buttons
        JButton minimizeBtn = createWindowControlButton("－", "Minimize");
        JButton maximizeBtn = createWindowControlButton("□", "Maximize");
        JButton closeBtn = createWindowControlButton("×", "Close");
        closeBtn.setBackground(new Color(231, 76, 60));  // Red close button
        
        // Add action listeners to control buttons
        minimizeBtn.addActionListener(e -> setState(JFrame.ICONIFIED));  // Minimize window
        maximizeBtn.addActionListener(e -> {
            // Toggle between normal and maximized state
            if (getExtendedState() == JFrame.MAXIMIZED_BOTH) {
                setExtendedState(JFrame.NORMAL);
            } else {
                setExtendedState(JFrame.MAXIMIZED_BOTH);
            }
        });
        closeBtn.addActionListener(e -> System.exit(0));  // Exit application
        
        // Add buttons to panel
        controlButtons.add(minimizeBtn);
        controlButtons.add(maximizeBtn);
        controlButtons.add(closeBtn);
        
        // Add components to title bar
        titleBar.add(windowTitle, BorderLayout.WEST);
        titleBar.add(controlButtons, BorderLayout.EAST);
        
        // ========== WINDOW DRAGGING LOGIC ==========
        // Make window draggable by clicking and dragging title bar
        MouseAdapter ma = new MouseAdapter() {
            private Point initialClick;  // Store initial mouse position
            
            @Override
            public void mousePressed(MouseEvent e) {
                initialClick = e.getPoint();  // Save click position
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                // Calculate new window position
                int thisX = getLocation().x;
                int thisY = getLocation().y;
                
                int xMoved = e.getX() - initialClick.x;  // Horizontal movement
                int yMoved = e.getY() - initialClick.y;  // Vertical movement
                
                int X = thisX + xMoved;  // New X position
                int Y = thisY + yMoved;  // New Y position
                
                setLocation(X, Y);  // Move window
            }
        };
        
        // Add mouse listeners to title bar for dragging
        titleBar.addMouseListener(ma);
        titleBar.addMouseMotionListener(ma);
        
        // Add title bar to top of window
        add(titleBar, BorderLayout.NORTH);
    }
    
    // ========== HELPER: CREATE WINDOW CONTROL BUTTON ==========
    private JButton createWindowControlButton(String text, String tooltip) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setPreferredSize(new Dimension(45, 30));
        button.setToolTipText(tooltip);
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(60, 60, 65));
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setOpaque(true);  // Make background visible
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(button.getBackground().brighter());  // Brighten on hover
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(new Color(60, 60, 65));  // Restore original color
            }
        });
        
        return button;
    }
    
    // ========== LAYOUT SETUP ==========
    private void setupLayout() {
        // Set main panel layout
        mainPanel.setLayout(new BorderLayout());
        
        // ========== CENTER PANEL (Album Art & Title) ==========
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setOpaque(false);  // Transparent to show gradient
        
        // Album art panel with centering
        JPanel albumPanel = new JPanel(new GridBagLayout());  // Centers content
        albumPanel.setOpaque(false);
        albumPanel.setBorder(new EmptyBorder(20, 20, 20, 20));  // Padding
        albumPanel.add(albumArtLabel);  // Add album art label
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(0, 40, 20, 40));  // Padding
        titlePanel.add(titleLabel, BorderLayout.CENTER);  // Add title label
        
        // Add components to center panel
        centerPanel.add(albumPanel, BorderLayout.CENTER);
        centerPanel.add(titlePanel, BorderLayout.SOUTH);
        
        // ========== PROGRESS PANEL ==========
        JPanel progressPanel = new JPanel(new BorderLayout(15, 0));  // 15px horizontal gap
        progressPanel.setOpaque(false);
        progressPanel.setBorder(new EmptyBorder(10, 40, 10, 40));  // Padding
        
        // Panel for time labels
        JPanel timePanel = new JPanel(new GridLayout(1, 2));  // Two columns
        timePanel.setOpaque(false);
        timePanel.add(currentTimeLabel);  // Left: current time
        timePanel.add(totalTimeLabel);    // Right: total time
        
        // Add components to progress panel
        progressPanel.add(timePanel, BorderLayout.NORTH);
        progressPanel.add(progressSlider, BorderLayout.CENTER);
        
        // ========== CONTROL PANEL (Buttons) ==========
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));  // Centered, 15px gaps
        controlPanel.setBorder(new EmptyBorder(15, 20, 20, 20));  // Padding
        
        // Add all control buttons in order
        controlPanel.add(openButton);
        controlPanel.add(prevButton);
        controlPanel.add(playButton);
        controlPanel.add(pauseButton);
        controlPanel.add(stopButton);
        controlPanel.add(nextButton);
        controlPanel.add(repeatButton);
        controlPanel.add(shuffleButton);
        
        // Volume control panel
        JPanel volumePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        volumePanel.setOpaque(false);
        volumePanel.add(new JLabel("🔈") {{  // Volume icon
            setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
            setForeground(TEXT_COLOR);
        }});
        volumePanel.add(volumeSlider);  // Volume slider
        volumePanel.add(muteButton);    // Mute button
        
        controlPanel.add(volumePanel);
        
        // ========== PLAYLIST PANEL ==========
        playlistPanel.setLayout(new BorderLayout());
        playlistPanel.setPreferredSize(new Dimension(300, 0));  // Fixed width
        playlistPanel.setBorder(new CompoundBorder(
            new MatteBorder(0, 1, 0, 0, new Color(60, 60, 65)),  // Left border
            new EmptyBorder(10, 10, 10, 10)  // Padding
        ));
        
        // Playlist title
        JLabel playlistTitle = new JLabel("PLAYLIST");
        playlistTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        playlistTitle.setForeground(new Color(180, 180, 180));
        playlistTitle.setBorder(new EmptyBorder(0, 0, 10, 0));  // Bottom margin
        
        // Scroll pane for playlist
        JScrollPane scrollPane = new JScrollPane(playlistList);
        scrollPane.setBorder(null);  // Remove default border
        scrollPane.getViewport().setBackground(new Color(40, 40, 45));  // Viewport background
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        
        // Customize scrollbar appearance
        JScrollBar verticalScrollBar = scrollPane.getVerticalScrollBar();
        verticalScrollBar.setUI(new javax.swing.plaf.basic.BasicScrollBarUI() {
            @Override
            protected void configureScrollBarColors() {
                this.thumbColor = new Color(80, 80, 85);   // Scroll thumb color
                this.trackColor = new Color(40, 40, 45);   // Scroll track color
            }
            
            // Remove arrow buttons
            @Override
            protected JButton createDecreaseButton(int orientation) {
                return createZeroButton();
            }
            
            @Override
            protected JButton createIncreaseButton(int orientation) {
                return createZeroButton();
            }
            
            private JButton createZeroButton() {
                JButton button = new JButton();
                button.setPreferredSize(new Dimension(0, 0));  // Zero size button
                button.setMinimumSize(new Dimension(0, 0));
                button.setMaximumSize(new Dimension(0, 0));
                return button;
            }
        });
        
        // Add components to playlist panel
        playlistPanel.add(playlistTitle, BorderLayout.NORTH);
        playlistPanel.add(scrollPane, BorderLayout.CENTER);
        
        // ========== ASSEMBLE MAIN PANEL ==========
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(progressPanel, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        mainPanel.add(playlistPanel, BorderLayout.EAST);
        
        // ========== SET FRAME LAYOUT ==========
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }
    
    // ========== EVENT HANDLERS SETUP ==========
    private void setupEventListeners() {
        // Play button action
        playButton.addActionListener(e -> {
            isPlaying = true;
            updateButtonStates();
            updateTitle("Playing: " + getCurrentTrackName());
            albumArtLabel.repaint();  // Redraw album art
        });
        
        // Pause button action
        pauseButton.addActionListener(e -> {
            isPlaying = false;
            updateButtonStates();
            updateTitle("Paused: " + getCurrentTrackName());
        });
        
        // Stop button action
        stopButton.addActionListener(e -> {
            isPlaying = false;
            progressSlider.setValue(0);  // Reset progress
            updateButtonStates();
            updateTitle("Stopped: " + getCurrentTrackName());
            albumArtLabel.repaint();
        });
        
        // Next button action
        nextButton.addActionListener(e -> playNextTrack());
        
        // Previous button action
        prevButton.addActionListener(e -> playPreviousTrack());
        
        // Open button action
        openButton.addActionListener(e -> openMediaFile());
        
        // Mute button action
        muteButton.addActionListener(e -> {
            isMuted = !isMuted;  // Toggle mute state
            muteButton.setText(isMuted ? "🔇" : "🔊");  // Change icon
            muteButton.setToolTipText(isMuted ? "Unmute" : "Mute");  // Update tooltip
        });
        
        // Repeat button action
        repeatButton.addActionListener(e -> {
            isRepeating = !isRepeating;  // Toggle repeat state
            repeatButton.setBackground(isRepeating ? ACCENT_COLOR : LIGHT_BG);  // Visual feedback
        });
        
        // Shuffle button action
        shuffleButton.addActionListener(e -> {
            isShuffling = !isShuffling;  // Toggle shuffle state
            shuffleButton.setBackground(isShuffling ? ACCENT_COLOR : LIGHT_BG);  // Visual feedback
        });
        
        // Playlist toggle button
        playlistButton.addActionListener(e -> {
            playlistVisible = !playlistVisible;  // Toggle visibility
            playlistPanel.setVisible(playlistVisible);  // Show/hide playlist
            revalidate();  // Update layout
        });
        
        // Playlist double-click to play
        playlistList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent evt) {
                if (evt.getClickCount() == 2) {  // Double-click
                    int index = playlistList.locationToIndex(evt.getPoint());
                    if (index >= 0) {
                        playTrack(index);  // Play selected track
                    }
                }
            }
        });
        
        // Progress slider mouse release event
        progressSlider.addMouseListener(new MouseAdapter() {
            public void mouseReleased(MouseEvent e) {
                JSlider source = (JSlider) e.getSource();
                if (!source.getValueIsAdjusting()) {  // Only when dragging stops
                    int value = source.getValue();
                    updateTimeLabels(value, 100);  // Update time display
                    progressSlider.repaint();  // Redraw slider
                }
            }
        });
        
        // Progress slider change event (for real-time updates)
        progressSlider.addChangeListener(e -> {
            if (!progressSlider.getValueIsAdjusting()) {
                progressSlider.repaint();  // Redraw while dragging
            }
        });
        
        // Volume slider change event
        volumeSlider.addChangeListener(e -> {
            if (!volumeSlider.getValueIsAdjusting()) {
                volumeSlider.repaint();  // Redraw while adjusting
            }
        });
        
        // Set up keyboard shortcuts
        setupKeyboardShortcuts();
    }
    
    // ========== KEYBOARD SHORTCUTS ==========
    private void setupKeyboardShortcuts() {
        // Get input and action maps for keyboard shortcuts
        InputMap inputMap = getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap actionMap = getRootPane().getActionMap();
        
        // Space bar: play/pause toggle
        inputMap.put(KeyStroke.getKeyStroke("SPACE"), "playPause");
        actionMap.put("playPause", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (isPlaying) {
                    pauseButton.doClick();  // Simulate pause click
                } else {
                    playButton.doClick();   // Simulate play click
                }
            }
        });
        
        // Left arrow: seek backward 5 seconds
        inputMap.put(KeyStroke.getKeyStroke("LEFT"), "seekBack");
        actionMap.put("seekBack", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int newValue = Math.max(0, progressSlider.getValue() - 5);  // Don't go below 0
                progressSlider.setValue(newValue);
                updateTimeLabels(newValue, 100);
            }
        });
        
        // Right arrow: seek forward 5 seconds
        inputMap.put(KeyStroke.getKeyStroke("RIGHT"), "seekForward");
        actionMap.put("seekForward", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                int newValue = Math.min(100, progressSlider.getValue() + 5);  // Don't exceed 100
                progressSlider.setValue(newValue);
                updateTimeLabels(newValue, 100);
            }
        });
        
        // O key: open file dialog
        inputMap.put(KeyStroke.getKeyStroke("ctrl O"), "openFile");
        actionMap.put("openFile", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                openButton.doClick();  // Simulate open button click
            }
        });
        
        // M key: mute/unmute
        inputMap.put(KeyStroke.getKeyStroke("M"), "toggleMute");
        actionMap.put("toggleMute", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                muteButton.doClick();  // Simulate mute button click
            }
        });
    }
    
    // ========== FILE MANAGEMENT METHODS ==========
    
    // Open file dialog to select media files
    private void openMediaFile() {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Select Media Files");
        fileChooser.setMultiSelectionEnabled(true);  // Allow multiple file selection
        
        // Filter for common media file types
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "Media Files", "mp3", "wav", "aiff", "au", "mp4", "avi", "mov", "wmv", "flv");
        fileChooser.setFileFilter(filter);
        
        // Show open dialog
        int result = fileChooser.showOpenDialog(this);
        
        if (result == JFileChooser.APPROVE_OPTION) {
            File[] files = fileChooser.getSelectedFiles();
            for (File file : files) {
                addToPlaylist(file);  // Add each file to playlist
            }
            
            // Auto-play first track if playlist was empty
            if (!playlist.isEmpty() && currentTrackIndex == -1) {
                playTrack(0);
            }
        }
    }
    
    // Add a file to the playlist
    private void addToPlaylist(File file) {
        playlist.add(file);  // Add to internal list
        playlistModel.addElement(file.getName());  // Add to JList model
        
        // Auto-select first item if it's the first file
        if (playlist.size() == 1) {
            playlistList.setSelectedIndex(0);
        }
    }
    
    // Play a specific track by index
    private void playTrack(int index) {
        // Validate index
        if (index >= 0 && index < playlist.size()) {
            currentTrackIndex = index;
            playlistList.setSelectedIndex(index);  // Highlight in playlist
            
            File track = playlist.get(index);
            updateTitle("Now Playing: " + track.getName());  // Update display
            
            isPlaying = true;
            updateButtonStates();  // Enable/disable buttons
            
            // Reset progress slider
            progressSlider.setValue(0);
            updateTimeLabels(0, 100);
            
            // Redraw components that might change appearance
            albumArtLabel.repaint();
            playlistList.repaint();
        }
    }
    
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
        
        playTrack(currentTrackIndex);  // Play the selected track
    }
    
    // Play previous track in playlist
    private void playPreviousTrack() {
        if (playlist.isEmpty()) return;  // Nothing to play
        
        // Calculate previous track index (wrap around to end if needed)
        currentTrackIndex = (currentTrackIndex - 1 + playlist.size()) % playlist.size();
        
        playTrack(currentTrackIndex);  // Play the selected track
    }
    
    // ========== UI UPDATE METHODS ==========
    
    // Update button enabled/disabled states based on player state
    private void updateButtonStates() {
        // Play button: enabled when not playing AND a track is loaded
        playButton.setEnabled(!isPlaying && currentTrackIndex != -1);
        
        // Pause button: enabled only when playing
        pauseButton.setEnabled(isPlaying);
        
        // Stop button: enabled when playing OR a track is loaded
        stopButton.setEnabled(isPlaying || currentTrackIndex != -1);
        
        // Next/Previous buttons: enabled only if playlist has more than 1 track
        boolean hasMultipleTracks = playlist.size() > 1;
        nextButton.setEnabled(hasMultipleTracks);
        prevButton.setEnabled(hasMultipleTracks);
    }
    
    // Update the title display
    private void updateTitle(String text) {
        titleLabel.setText(text);
        titleLabel.repaint();  // Force redraw for gradient text
    }
    
    // Update time display labels
    private void updateTimeLabels(int currentSeconds, int totalSeconds) {
        currentTimeLabel.setText(formatTime(currentSeconds));
        totalTimeLabel.setText(formatTime(totalSeconds));
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
    
    // ========== MAIN METHOD ==========
    public static void main(String[] args) {
        // Use SwingUtilities.invokeLater to ensure thread safety
        SwingUtilities.invokeLater(() -> {
            try {
                // Set the look and feel to match the operating system
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception e) {
                e.printStackTrace();  // Print error but continue execution
            }
            
            // Create and display the media player
            TestApp player = new TestApp();
            player.setVisible(true);  // Make window visible
        });
    }
}