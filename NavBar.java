import java.awt.BasicStroke;
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
import javax.swing.border.MatteBorder;

public class NavBar extends JPanel{
    JButton homeButton;
    JButton expandButton;
    
    private void Expand() {
        setPreferredSize(new Dimension(180, 0));
        revalidate();
        repaint();
    }

    private void Shrink() {
        setPreferredSize(new Dimension(60, 0));
    }
    
    public NavBar() {
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
        setPreferredSize(new Dimension(60, 0));
        setBorder(new MatteBorder(0, 0, 0, 2, new Color(60, 60, 65)));
        setBackground(ColorScheme.DARK_BG);

        setLayout(new FlowLayout(FlowLayout.CENTER, 15, 15));  // Centered, 15px gaps

        homeButton = new NavButton("🔀","🔀 Home", "Home", ColorScheme.LIGHT_BG);
        expandButton = new NavButton(">>","<< Shrink", "Expand", Color.WHITE);

        add(homeButton);
        add(expandButton);

        homeButton.addActionListener(e -> Expand());
    }
}
