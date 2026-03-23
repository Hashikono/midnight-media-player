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
public class App extends JFrame {
    
    // Panels for organizing layout
    private JPanel controlPanel;     // Holds control buttons
    private JPanel displayPanel;     // Holds album art and title
    // private JPanel playlistPanel;    // Holds playlist
    private JPanel mainPanel;        // Main container panel
    
    // ========== APPLICATION STATE VARIABLES ==========
    
    private boolean isPlaying = false;    // Tracks if media is currently playing
    private boolean isMuted = false;      // Tracks if audio is muted
    private boolean isRepeating = false;  // Tracks if repeat mode is enabled
    private boolean isShuffling = false;  // Tracks if shuffle mode is enabled
    private boolean playlistVisible = true; // Tracks playlist visibility
    private List<File> playlist = new ArrayList<>(); // List of media files
    private int currentTrackIndex = -1;   // Index of currently playing track (-1 = none)
    
    // ========== CONSTRUCTOR ==========
    public App() {
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
        // setShape(new RoundRectangle2D.Double(0, 0, 1000, 700, 30, 30));
        
        // Initialize all GUI components
        initializeComponents();
        
        // Set up the layout of components
        setupLayout();
        
        // Set up custom window controls (minimize, maximize, close)
        setupWindowControls();
    }
    
    // ========== COMPONENT INITIALIZATION ==========
    private void initializeComponents() {
        // Create buttons with custom styling
        
        // ========== PANEL INITIALIZATION ==========
        // Create panels with gradient backgrounds
        controlPanel = createGradientPanel(ColorScheme.DARK_BG, new Color(35, 35, 40));
        displayPanel = createGradientPanel(new Color(25, 25, 30), ColorScheme.DARK_BG);
        // playlistPanel = createGradientPanel(new Color(35, 35, 40), new Color(40, 40, 45));
        mainPanel = createGradientPanel(ColorScheme.DARK_BG, new Color(30, 30, 35));
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
        titleBar.setBackground(ColorScheme.DARK_BG);
        titleBar.setPreferredSize(new Dimension(getWidth(), 40));  // Fixed height
        titleBar.setBorder(new MatteBorder(0, 0, 1, 0, new Color(60, 60, 65)));  // Bottom border
        
        // Window title label
        JLabel windowTitle = new JLabel("  Harmony Player");
        windowTitle.setFont(new Font("Segoe UI", Font.BOLD, 14));
        windowTitle.setForeground(ColorScheme.TEXT_COLOR);
        
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
        
        // Title panel
        JPanel titlePanel = new JPanel(new BorderLayout());
        titlePanel.setOpaque(false);
        titlePanel.setBorder(new EmptyBorder(0, 40, 20, 40));  // Padding
        
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
        
        // Add components to progress panel
        progressPanel.add(timePanel, BorderLayout.NORTH);
        
        // ========== CONTROL PANEL (Buttons) ==========
        controlPanel.setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));  // Centered, 15px gaps
        controlPanel.setBorder(new EmptyBorder(15, 20, 20, 20));  // Padding
        
        // Volume control panel
        JPanel volumePanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        volumePanel.setOpaque(false);
        volumePanel.add(new JLabel("🔈") {{  // Volume icon
            setFont(new Font("Segoe UI Symbol", Font.PLAIN, 16));
            setForeground(ColorScheme.TEXT_COLOR);
        }});
        
        controlPanel.add(volumePanel);
        
        // ========== PLAYLIST PANEL ==========
        // playlistPanel.setLayout(new BorderLayout());
        // playlistPanel.setPreferredSize(new Dimension(300, 0));  // Fixed width
        // playlistPanel.setBorder(new CompoundBorder(
        //     new MatteBorder(0, 1, 0, 0, new Color(60, 60, 65)),  // Left border
        //     new EmptyBorder(10, 10, 10, 10)  // Padding
        // ));
        
        // Playlist title
        JLabel playlistTitle = new JLabel("PLAYLIST");
        playlistTitle.setFont(new Font("Segoe UI", Font.BOLD, 12));
        playlistTitle.setForeground(new Color(180, 180, 180));
        playlistTitle.setBorder(new EmptyBorder(0, 0, 10, 0));  // Bottom margin
        
        // Add components to playlist panel
        // playlistPanel.add(playlistTitle, BorderLayout.NORTH);
        
        // ========== ASSEMBLE MAIN PANEL ==========
        mainPanel.add(centerPanel, BorderLayout.CENTER);
        mainPanel.add(progressPanel, BorderLayout.NORTH);
        mainPanel.add(controlPanel, BorderLayout.SOUTH);
        // mainPanel.add(playlistPanel, BorderLayout.EAST);
        
        // ========== SET FRAME LAYOUT ==========
        setLayout(new BorderLayout());
        add(mainPanel, BorderLayout.CENTER);
    }
    
    // ========== FILE MANAGEMENT METHODS ==========
    
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
            App player = new App();
            player.setVisible(true);  // Make window visible
        });
    }
}