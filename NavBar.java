// import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
// import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
// import java.awt.Font;
// import java.awt.FontMetrics;
// import java.awt.Graphics;
// import java.awt.Graphics2D;
// import java.awt.RenderingHints;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
// import javax.swing.BoxLayout;
// import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.MatteBorder;

public class NavBar extends JPanel{
    JPanel mainButtonsBox;
    NavButton homeButton;
    NavButton mediaCollectionButton;
    NavButton playlistsButton;
    NavButton viewButton;
    NavButton logsButton;
    NavButton settingsButton;
    NavButton expandButton;

    private boolean isExpanded;
    
    private void Expand() {
        setPreferredSize(new Dimension(180, 0));

        homeButton.Expand();
        mediaCollectionButton.Expand();
        playlistsButton.Expand();
        viewButton.Expand();
        logsButton.Expand();
        settingsButton.Expand();
        expandButton.Expand();

        revalidate();
        repaint();
    }

    private void Shrink() {
        setPreferredSize(new Dimension(60, 0));

        homeButton.Shrink();
        mediaCollectionButton.Shrink();
        playlistsButton.Shrink();
        viewButton.Shrink();
        logsButton.Shrink();
        settingsButton.Shrink();
        expandButton.Shrink();

        revalidate();
        repaint();
    }

    private void Resizing()
    {
        isExpanded = !isExpanded;

        if(isExpanded)
            Expand();
        else
            Shrink();
    }

    private JPanel createRow(JComponent comp) {
        JPanel row = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 5));
        row.setOpaque(false);
        row.add(comp);
        return row;
    }

    private void SetUpMainButtonBg()
    {
        mainButtonsBox = new JPanel();
        mainButtonsBox.setOpaque(false);
        mainButtonsBox.setLayout(new BoxLayout(mainButtonsBox, BoxLayout.Y_AXIS));  // Centered, 15px gaps
    }
    
    public NavBar() {
        setPreferredSize(new Dimension(60, 0));
        setBorder(new MatteBorder(0, 0, 0, 2, new Color(60, 60, 65)));
        setBackground(ColorScheme.DARK_BG);
        setLayout(new BorderLayout());

        SetUpMainButtonBg();

        homeButton = new NavButton("⌂","⌂ Home", "Home", ColorScheme.DARK_BG.brighter(), ColorScheme.PRIMARY_COLOR);
        mediaCollectionButton = new NavButton("♫","♫ Media", "Lyrics", ColorScheme.DARK_BG.brighter(), ColorScheme.PRIMARY_COLOR);
        playlistsButton = new NavButton("📜","📜 Playlists", "Lyrics", ColorScheme.DARK_BG.brighter(), ColorScheme.PRIMARY_COLOR);
        viewButton = new NavButton("🖵","🖵 View", "View", ColorScheme.DARK_BG.brighter(), ColorScheme.PRIMARY_COLOR);
        logsButton = new NavButton("☳","☳ Logs", "Logs", ColorScheme.DARK_BG.brighter(), ColorScheme.PRIMARY_COLOR); //🪵 didn't work unfortunately
        settingsButton = new NavButton("⚙","⚙ Settings", "Settings", ColorScheme.DARK_BG.brighter(), ColorScheme.PRIMARY_COLOR);
        expandButton = new NavButton("»","« Shrink", "Expand", ColorScheme.DARK_BG.brighter(), ColorScheme.TEXT_COLOR);

        //Other random characters to keep: ⚂ ☊ ★ ☳

        mainButtonsBox.add(createRow(homeButton));
        mainButtonsBox.add(createRow(mediaCollectionButton));
        mainButtonsBox.add(createRow(playlistsButton));
        mainButtonsBox.add(createRow(viewButton));
        mainButtonsBox.add(createRow(logsButton));
        mainButtonsBox.add(createRow(settingsButton));

        add(mainButtonsBox, BorderLayout.NORTH);
        add(createRow(expandButton), BorderLayout.SOUTH);

        expandButton.addActionListener(e -> Resizing());
    }
}
