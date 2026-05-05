import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
// import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
// import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import models.Media;
import models.Playlist;

import java.util.ArrayList;
import java.util.List;

public class MediaVisuals extends JPanel {

    public static Playlist heldPlaylist = null;

    public MediaVisuals()
    {
        setSize(1000, 10);
        setLayout(new BorderLayout(10, 7));
        setBorder(new EmptyBorder(12, 12, 12, 20));
        setBackground(ColorScheme.DARK_BG);

        
    }
}
