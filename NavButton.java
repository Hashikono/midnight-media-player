import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

import javax.swing.JButton;

public class NavButton extends JButton {
    private String normalText;
    private String expandedText;
    private Color bgColor;

    public void Expand()
    {

    }

    
    public NavButton(String normal, String expanded, String tooltip, Color bgColor)
    {
        this.normalText = normal;
        this.expandedText = expanded;
        this.bgColor = bgColor;

        // Graphics2D g2 = (Graphics2D) g.create();
        // g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
        //                     RenderingHints.VALUE_ANTIALIAS_ON);
        
        // int width = getWidth();
        // int height = getHeight();

        // Color buttonColor;
        // if (getModel().isPressed()) {
        //     buttonColor = bgColor.darker();  // Darker when pressed
        // } else if (getModel().isRollover()) {
        //     buttonColor = bgColor.brighter();  // Brighter on hover
        // } else {
        //     buttonColor = bgColor;  // Normal state
        // }

        // g2.setColor(buttonColor);
        // g2.fillRoundRect(0, 0, width - 1, height - 1, 15, 15);

        setText(normalText);
        
        // Set button properties
        setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));  // Icon font
        setPreferredSize(new Dimension(60, 40));  // Fixed size
        setToolTipText(tooltip);  // Tooltip text
        setContentAreaFilled(false);  // Don't use default background
        setBorderPainted(false);  // Don't paint default border
        setFocusPainted(false);  // Don't show focus rectangle
        setCursor(new Cursor(Cursor.HAND_CURSOR));  // Hand cursor on hover
    }
    // @Override
    //         protected void paintComponent(Graphics g) {
    //             Graphics2D g2 = (Graphics2D) g.create();
    //             g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, 
    //                                RenderingHints.VALUE_ANTIALIAS_ON);
                
    //             int width = getWidth();
    //             int height = getHeight();
                
    //             // Determine button color based on state
    //             Color buttonColor;
    //             if (getModel().isPressed()) {
    //                 buttonColor = bgColor.darker();  // Darker when pressed
    //             } else if (getModel().isRollover()) {
    //                 buttonColor = bgColor.brighter();  // Brighter on hover
    //             } else {
    //                 buttonColor = bgColor;  // Normal state
    //             }
                
    //             // Draw rounded rectangle background
    //             g2.setColor(buttonColor);
    //             g2.fillRoundRect(0, 0, width - 1, height - 1, 15, 15);
                
    //             // Draw button text (centered)
    //             g2.setColor(Color.WHITE);
    //             g2.setFont(getFont());
    //             FontMetrics fm = g2.getFontMetrics();
    //             int x = (width - fm.stringWidth(getText())) / 2;  // Center horizontally
    //             int y = (height - fm.getHeight()) / 2 + fm.getAscent();  // Center vertically
    //             g2.drawString(getText(), x, y);
                
    //             // Draw border
    //             g2.setColor(bgColor.brighter());  // Slightly brighter border
    //             g2.setStroke(new BasicStroke(1.5f));  // 1.5 pixel border
    //             g2.drawRoundRect(1, 1, width - 3, height - 3, 15, 15);
                
    //             g2.dispose();
    //         }

    
}
