// import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
// import java.awt.FontMetrics;
// import java.awt.Graphics;
// import java.awt.Graphics2D;
// import java.awt.RenderingHints;

import javax.swing.JButton;
import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class NavButton extends JButton {
    private String normalText;
    private String expandedText;
    private Color bgColor;
    private Color textColor;

    public void Select()
    {

    }

    public void Unselect()
    {

    }

    public void Expand()
    {
        setPreferredSize(new Dimension(170, 40));
        setText(expandedText);
        setHorizontalAlignment(SwingConstants.LEADING);
        setBorder(new LineBorder(bgColor.brighter(), 19, true));
    }

    public void Shrink()
    {
        setPreferredSize(new Dimension(50, 40));
        setText(normalText);
        setHorizontalAlignment(SwingConstants.CENTER);
        setBorder(new LineBorder(bgColor.brighter(), 2, true)); // rounded border
    }

    
    public NavButton(String normal, String expanded, String tooltip, Color bg, Color textingColor)
    {
        this.normalText = normal;
        this.expandedText = expanded;
        this.bgColor = bg;
        this.textColor = textingColor;

        // Text Stuffs
        setText(normalText);
        setFont(new Font("Segoe UI Symbol", Font.BOLD, 14));  // Icon font
        setPreferredSize(new Dimension(50, 40));  // Fixed size
        setToolTipText(tooltip);  // Tooltip text
        setContentAreaFilled(false);  // Don't use default background

        // Color Stuffs
        setBorderPainted(false);  // Don't paint default border
        setFocusPainted(false);
        setBackground(bgColor);
        setForeground(textColor);
        setOpaque(false);
        setContentAreaFilled(true);
        setBorder(new LineBorder(bgColor.brighter(), 2, true)); // rounded border

        setCursor(new Cursor(Cursor.HAND_CURSOR));
    }

    
}
