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
    private JPanel center;
    
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
        setUndecorated(true); //Setting to false will make it have the bar of a normal app... do we want that?
        
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
        center = new JPanel();
        center.setBackground(ColorScheme.DARK_BG);
        add(center, BorderLayout.CENTER);


        add(new MediaControlBar(), BorderLayout.SOUTH);
        add(new NavBar(), BorderLayout.WEST);
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
    
    // ========== CUSTOM WINDOW CONTROLS ==========
    // Creates custom title bar with window control buttons
    private void setupWindowControls() { //Needs a rework, thing looks ugly rn
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
        JButton minimizeBtn = createWindowControlButton("-", "Minimize");
        JButton maximizeBtn = createWindowControlButton("+", "Maximize");
        JButton closeBtn = createWindowControlButton("x", "Close");
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
        
    }
    
    // ========== FILE MANAGEMENT METHODS ==========
    
    // ========== MAIN METHOD ==========
    public static void main(String[] args) { //Base was stolen from TestApp.java, so any parts we want back should come from there
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