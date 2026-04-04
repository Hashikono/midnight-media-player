import java.awt.BasicStroke;
// import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
// import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;

public class MediaControlBar extends JPanel{
    JButton playButton;
    JButton nextButton;
    JButton prevButton;
    JButton repeatButton;
    JButton shuffleButton;
    
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
    
    
    public MediaControlBar() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(0, 70));
        setBorder(new MatteBorder(2, 0, 0, 0, new Color(60, 60, 65)));
        setBackground(ColorScheme.DARK_BG);

        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));  // Centered, 15px gaps

        shuffleButton = createModernButton("🔀", "Shuffle", ColorScheme.LIGHT_BG);
        playButton = createModernButton("▶", "Play", ColorScheme.PRIMARY_COLOR);
        nextButton = createModernButton("⏭", "Next", ColorScheme.LIGHT_BG);
        prevButton = createModernButton("⏮", "Previous", ColorScheme.LIGHT_BG);
        repeatButton = createModernButton("🔁", "Repeat", ColorScheme.LIGHT_BG);

        add(shuffleButton);
        add(prevButton);
        add(playButton);
        add(nextButton);
        add(repeatButton);
    }
}
